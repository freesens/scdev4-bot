package kr.co.pionnet.scdev4.bot.domain.common.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Component
public class TelegramUtil {

    @Value("${telegram.bot-token.notice}")
    private String DEFAULT_BOT_TOKEN_NOTICE;

    @Value("${telegram.chat-id.dev4-employee}")
    private String DEFAULT_CHAT_ID_SCDEV4;

    public void sendMessage(String message) throws Exception {
        sendMessage(message, DEFAULT_BOT_TOKEN_NOTICE, DEFAULT_CHAT_ID_SCDEV4);
    }

    public void sendMessage(final String message, String botToken, String chatId) throws Exception {
        String params = "chat_id=" + URLEncoder.encode(chatId, StandardCharsets.UTF_8)
            + "&link_preview_options=" + URLEncoder.encode("{\"is_disabled\":true}", StandardCharsets.UTF_8)
            + "&text=" + URLEncoder.encode(message, StandardCharsets.UTF_8);

        callApi(botToken, "sendMessage", params, 3000);
    }

    public void sendAnimation(final String animation, final String caption, String botToken, String chatId) throws Exception {
        String params = "chat_id=" + URLEncoder.encode(chatId, StandardCharsets.UTF_8)
            + "&animation=" + URLEncoder.encode(animation, StandardCharsets.UTF_8);
        if (caption != null && !caption.isEmpty()) {
            params += "&caption=" + URLEncoder.encode(caption, StandardCharsets.UTF_8);
        }

        callApi(botToken, "sendAnimation", params, 10000);
    }

    private void callApi(String botToken, String method, String params, int readTimeout) throws Exception {
        String sendUrl = "https://api.telegram.org/bot" + botToken + "/" + method;

        HttpsURLConnection conn = null;
        InputStreamReader is = null;
        BufferedReader br = null;
        try {
            System.setProperty("https.protocols", "TLSv1.2");
            URL url = new URL(sendUrl);
            conn = (HttpsURLConnection) url.openConnection();
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestProperty("Cache-Control", "no-cache");
            conn.setRequestMethod("POST");
            conn.setConnectTimeout(1000);
            conn.setReadTimeout(readTimeout);
            conn.setDoOutput(true);
            conn.setDoInput(true);

            DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
            wr.write(params.getBytes(StandardCharsets.UTF_8));
            wr.flush();
            wr.close();

            int responseCode = conn.getResponseCode();
            boolean success = responseCode == HttpStatus.OK.value()
                || responseCode == HttpStatus.CREATED.value();

            is = new InputStreamReader(success ? conn.getInputStream() : conn.getErrorStream(), StandardCharsets.UTF_8);
            br = new BufferedReader(is, 1024);

            StringBuffer sb = new StringBuffer();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            if (!success) {
                throw new IllegalStateException("Telegram " + method + " failed (HTTP " + responseCode + "): " + sb);
            }
        } finally {
            if (is != null)
                is.close();
            if (br != null)
                br.close();
            if (conn != null)
                conn.disconnect();
        }
    }
}
