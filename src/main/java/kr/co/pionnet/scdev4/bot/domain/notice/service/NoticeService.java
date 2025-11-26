package kr.co.pionnet.scdev4.bot.domain.notice.service;

import kr.co.pionnet.scdev4.bot.domain.common.constant.BotConst;
import kr.co.pionnet.scdev4.bot.domain.common.entity.ApiResultEnum;
import kr.co.pionnet.scdev4.bot.domain.common.util.PublicDataApiUtil;
import kr.co.pionnet.scdev4.bot.domain.common.util.TelegramUtil;
import kr.co.pionnet.scdev4.bot.domain.notice.entity.BarEnum;
import kr.co.pionnet.scdev4.bot.domain.restaurant.entity.LunchMentEnum;
import kr.co.pionnet.scdev4.bot.domain.restaurant.entity.MemberEnum;
import kr.co.pionnet.scdev4.bot.domain.restaurant.entity.MenuEnum;
import kr.co.pionnet.scdev4.bot.domain.restaurant.service.RestaurantHistoryService;
import kr.co.pionnet.scdev4.bot.domain.restaurant.vo.RestaurantHistory;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Random;

@Slf4j
@Service
public class NoticeService {
    @Value("${telegram.bot-token.notice}")
    private String BOT_TOKEN_NOTICE;
    @Value("${telegram.chat-id.dev4-employee}")
    private String CHAT_ID_SCDEV4;
    @Value("${telegram.chat-id.dev4-all}")
    private String CHAT_ID_SCDEV4_ALL;

    private final TelegramUtil telegramUtil;
    private final PublicDataApiUtil publicDataApiUtil;
    private final RestaurantHistoryService restaurantHistoryService;

    @Autowired
    public NoticeService(
        TelegramUtil telegramUtil,
        PublicDataApiUtil publicDataApiUtil,
        RestaurantHistoryService restaurantHistoryService
    ) {
        this.telegramUtil = telegramUtil;
        this.publicDataApiUtil = publicDataApiUtil;
        this.restaurantHistoryService = restaurantHistoryService;
    }

    public String lunchMenu() throws Exception {

        StringBuffer result = new StringBuffer();
        JSONObject resultJson = new JSONObject();

        try {
            String botToken = BOT_TOKEN_NOTICE;
            String chatId = CHAT_ID_SCDEV4;

            Random rand = new Random();
            String menu = "";
            String firstMember = "";
            String secondMember;
            int randomNo;
            int count = 0;

            randomNo = rand.nextInt(LunchMentEnum.values().length);

            boolean isRepeat = true;
            boolean isExist;

            Date now = new Date();
            for (int i = 0; i <= now.getDay(); i++) {
                randomNo = rand.nextInt(rand.nextInt(now.getDay()) + 1);
            }

            List<RestaurantHistory> restaurantHistoryList = restaurantHistoryService.selectRestaurantHistory();

            while (isRepeat && count < 100) {
                count++;
                randomNo = rand.nextInt(MenuEnum.values().length);

                menu = MenuEnum.values()[randomNo].getName();

                isExist = false;
                for (int i = 0; i < restaurantHistoryList.size(); i++) {
                    if (BotConst.RESULT_YES.equals(restaurantHistoryList.get(i).getVisitYN())
                        && restaurantHistoryList.get(i).getRestNm().equals(menu)) {
                        isExist = true;
                        break;
                    }
                }

                if (!isExist) {
                    isRepeat = false;
                }
            }

            isRepeat = true;
            count = 0;

            while (isRepeat && count < 100) {
                count++;
                randomNo = rand.nextInt(MemberEnum.values().length);
                firstMember = MemberEnum.values()[randomNo].getValue();

                isExist = false;
                for (RestaurantHistory history : restaurantHistoryList) {
                    if (history.getMemNm().equals(firstMember)) {
                        isExist = true;
                        break;
                    }
                }

                if (!isExist) {
                    isRepeat = false;
                }
            }

            int magicNo2;
            do {
                magicNo2 = rand.nextInt(MemberEnum.values().length);
            } while (randomNo == magicNo2);
            secondMember = MemberEnum.values()[magicNo2].getValue();

            result.append("오늘의 추천식당 : ").append(menu);
            result.append("\r\n마음에 안 드시면 ").append(firstMember).append("님이 정해주세요 !");
            result.append("\r\n부재시 ").append(secondMember).append("님이 정해주세요 !");

            RestaurantHistory restaurantHistory = RestaurantHistory.builder()
                                                                   .restNm(menu)
                                                                   .memNm(firstMember)
                                                                   .build();

            if (log.isDebugEnabled()) {
                log.debug(result.toString());
            }

            if (!publicDataApiUtil.isHoliday()) {
                telegramUtil.sendMessage(result.toString(), botToken, chatId);
                restaurantHistoryService.insertRestaurantHistory(restaurantHistory);
            }

            resultJson.put("result", "Success");
        } finally {
            result.setLength(0);
            result.append(resultJson.toString());
        }

        return result.toString();
    }

    public String lunchTime() throws Exception {

        StringBuffer result = new StringBuffer();
        JSONObject resultJson = new JSONObject();

        try {
            String botToken = BOT_TOKEN_NOTICE;
            String chatId = CHAT_ID_SCDEV4_ALL;

            Random rand = new Random();
            int value;

            value = rand.nextInt(LunchMentEnum.values().length);
            result.append(LunchMentEnum.values()[value].getValue());

            if (!publicDataApiUtil.isHoliday()) {
                telegramUtil.sendMessage(result.toString(), botToken, chatId);
            }

            resultJson.put("result", "Success");
        } finally {
            result.setLength(0);
            result.append(resultJson.toString());
        }

        return result.toString();
    }

    public String lunchMenuCheck() throws Exception {

        StringBuffer result = new StringBuffer();

        try {
            String botToken = BOT_TOKEN_NOTICE;
            String chatId = CHAT_ID_SCDEV4;

            if (!publicDataApiUtil.isHoliday()) {
                RestaurantHistory visitTodayInfo = restaurantHistoryService.getRestaurantVisitToday();
                if (visitTodayInfo != null) {
                    result.append(visitTodayInfo.getMemNm()).append("님").append(" 오늘 ")
                          .append(visitTodayInfo.getRestNm()).append(" 방문 하셨나요?\n\n");
                    result.append("추천메뉴를 드셨다면 아래 링크를 클릭 해주세요\n").append("https://shorturl.at/zyGBK")
                          .append("\n\n추천메뉴를 안드셨으면 아래 링크를 클릭 해주세요\n").append("https://shorturl.at/abIIr");
                    telegramUtil.sendMessage(result.toString(), botToken, chatId);
                }
            }
        } finally {
            result.setLength(0);
        }

        return ApiResultEnum.SUCCESS.getMessage();
    }

    public String dinnerTime() throws Exception {
        StringBuffer result = new StringBuffer();
        JSONObject resultJson = new JSONObject();

        try {
            String botToken = BOT_TOKEN_NOTICE;
            String chatId = CHAT_ID_SCDEV4_ALL;

            String bar;
            Random rand = new Random();
            int value = rand.nextInt(5);

            bar = BarEnum.values()[value].getValue();

            result.append(bar).append("\r\n").append(bar)
                  .append("\r\n\r\n저녁 식사하실 분만 말씀해주세요.\n미리 말씀 안 해주시면 법카는 집에 갑니다.\r\n\r\n").append(bar).append("\r\n")
                  .append(bar);

            if (log.isDebugEnabled()) {
                log.debug(result.toString());
            }

            if (!publicDataApiUtil.isHoliday()) {
                telegramUtil.sendMessage(result.toString(), botToken, chatId);
            }

            resultJson.put("result", "Success");
        } finally {
            result.setLength(0);
            result.append(resultJson.toString());
        }

        return result.toString();
    }

    public String checkAccount() throws Exception {
        StringBuffer result = new StringBuffer();
        JSONObject resultJson = new JSONObject();

        try {
            String botToken = BOT_TOKEN_NOTICE;
            String chatId = CHAT_ID_SCDEV4_ALL;

            result.append("마당 계정 잠기거나 삭제되지 않도록 한번씩 접속 해주세요.");

            if (log.isDebugEnabled()) {
                log.debug(result.toString());
            }

            if (!publicDataApiUtil.isHoliday()) {
                telegramUtil.sendMessage(result.toString(), botToken, chatId);
            }

            resultJson.put("result", "Success");
        } finally {
            result.setLength(0);
            result.append(resultJson.toString());
        }

        return result.toString();
    }
}
