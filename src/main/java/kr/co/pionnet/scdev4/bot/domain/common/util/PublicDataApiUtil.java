package kr.co.pionnet.scdev4.bot.domain.common.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.util.Date;

@Slf4j
@Component
public class PublicDataApiUtil {
    @Value("${public-data.api.key}")
    private String publicDataApiKey;

    private static final String HOLIDAY_API_URL = "http://apis.data.go.kr/B090041/openapi/service/SpcdeInfoService/getHoliDeInfo";

    public boolean isHoliday() {
        boolean result = false;

        Date now = new Date();
        String today = DateFormatUtils.format(now, "yyyyMMdd");
        String url = HOLIDAY_API_URL
                + "?serviceKey=" + publicDataApiKey
                + "&solYear=" + today.substring(0, 4)
                + "&solMonth=" + today.substring(4, 6);

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
            log.error("Fail to get holiday data from public data api.", e);
        }

        return result;
    }

    private String getXmlTagValue(final String tag, final Element element) {
        NodeList nodeList = element.getElementsByTagName(tag).item(0).getChildNodes();
        Node nValue = nodeList.item(0);

        if (nValue == null) {
            return null;
        }

        return nValue.getNodeValue();
    }
}
