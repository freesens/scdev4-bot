package kr.co.pionnet.scdev4.bot.domain.common.util;

import org.apache.commons.lang3.StringUtils;
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
        String sendUrl = "https://api.telegram.org/bot" + botToken + "/sendMessage?chat_id=" + chatId;
        sendUrl += "&link_preview_options=" + URLEncoder.encode("{\"is_disabled\":true}", StandardCharsets.UTF_8);

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
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(1000);
            conn.setReadTimeout(3000);
            conn.setDoOutput(true);
            conn.setDoInput(true);

            String params = "text=" + StringUtils.toEncodedString(message.getBytes(), StandardCharsets.UTF_8);
            DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
            wr.write(params.getBytes(StandardCharsets.UTF_8));
            wr.flush();
            wr.close();

            if (conn.getResponseCode() != HttpStatus.OK.value()
                && conn.getResponseCode() != HttpStatus.CREATED.value()) {
            }

            is = new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8);
            br = new BufferedReader(is, 1024);

            StringBuffer sb = new StringBuffer();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
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
