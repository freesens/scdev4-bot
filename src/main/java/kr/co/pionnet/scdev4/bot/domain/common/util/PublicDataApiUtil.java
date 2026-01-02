package kr.co.pionnet.scdev4.bot.domain.common.util;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.StringReader;
import java.net.URI;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Slf4j
@Component
@RequiredArgsConstructor
public class PublicDataApiUtil {
    @Value("${public-data.api.key}")
    private String publicDataApiKey;

    private final RestTemplate restTemplate;

    private static final String HOLIDAY_API_URL = "http://apis.data.go.kr/B090041/openapi/service/SpcdeInfoService/getHoliDeInfo";

    public boolean isHoliday() {
        boolean result = false;

        LocalDate now = LocalDate.now();
        String solYear = String.valueOf(now.getYear());
        String solMonth = now.format(DateTimeFormatter.ofPattern("MM"));
        String todayYyyyMMdd = now.format(DateTimeFormatter.ofPattern("yyyyMMdd"));

        try {
            URI uri = UriComponentsBuilder.fromUriString(HOLIDAY_API_URL)
                    .queryParam("serviceKey", publicDataApiKey)
                    .queryParam("solYear", solYear)
                    .queryParam("solMonth", solMonth)
                    .queryParam("_type", "xml")
                    .build(true)
                    .toUri();

            String response = restTemplate.getForObject(uri, String.class);

            if (response != null && !response.isEmpty()) {
                result = parseHolidayXml(response, todayYyyyMMdd);
            } else {
                log.error("Fail to get holiday data from public data api.");
            }
        } catch (Exception e) {
            log.error("Fail to get holiday data from public data api.", e);
        }

        return result;
    }

    private boolean parseHolidayXml(String xmlString, String targetDate) throws Exception {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(new InputSource(new StringReader(xmlString)));

        NodeList itemList = doc.getElementsByTagName("item");
        if (itemList != null && itemList.getLength() > 0) {
            for (int i = 0; i < itemList.getLength(); i++) {
                Node node = itemList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    if (targetDate.equals(getXmlTagValue("locdate", element))) {
                        return true;
                    }
                }
            }
        }
        return false;
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
