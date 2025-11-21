package kr.co.pionnet.scdev4.bot.domain.common.util;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.net.ssl.HttpsURLConnection;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class PublicDataApiUtil {
    @Value("${public-data.api.key}")
    private String publicDataApiKey;

    public boolean isHoliday() {
        boolean result = false;

        Date now = new Date();
        String today = DateFormatUtils.format(now, "yyyyMMdd");
        String url = "http://apis.data.go.kr/B090041/openapi/service/SpcdeInfoService/getRestDeInfo?serviceKey=" + publicDataApiKey
            + "&solYear=" + DateFormatUtils.format(now, "yyyy") + "&solMonth=" + DateFormatUtils.format(now, "MM");

        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(url);

            NodeList itemList = doc.getElementsByTagName("item");
            if (itemList != null && itemList.getLength() > 0) {
                for (int i = 0; i < itemList.getLength(); i++) {
                    Node node = itemList.item(i);
                    if (node.getNodeType() == Node.ELEMENT_NODE) {
                        Element element = (Element) node;
                        if (today.equals(getXmlTagValue("locdate", element))) {
                            result = true;
                            break;
                        }
                    }
                }
            }
        } catch (Exception e) {
        }

        return result;
    }

    private String getXmlTagValue(final String tag, final Element element) {
        NodeList nodeList = element.getElementsByTagName(tag).item(0).getChildNodes();
        Node nValue = (Node) nodeList.item(0);
        if (nValue == null) {
            return null;
        }
        return nValue.getNodeValue();
    }
}
