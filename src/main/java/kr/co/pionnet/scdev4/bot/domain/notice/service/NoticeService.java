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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
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

    public String lunchMenu() {
        JSONObject resultJson = new JSONObject();

        try {
            if (!publicDataApiUtil.isHoliday()) {
                List<RestaurantHistory> restaurantHistoryList = restaurantHistoryService.selectRestaurantHistory();

                Set<String> visitedRestaurants = restaurantHistoryList.stream()
                        .filter(h -> BotConst.RESULT_YES.equals(h.getVisitYN()))
                        .map(RestaurantHistory::getRestNm)
                        .collect(Collectors.toSet());

                List<MenuEnum> availableMenus = Arrays.stream(MenuEnum.values())
                        .filter(menu -> !visitedRestaurants.contains(menu.getName()))
                        .collect(Collectors.toCollection(ArrayList::new));

                if (availableMenus.isEmpty()) {
                    availableMenus = new ArrayList<>(Arrays.asList(MenuEnum.values()));
                }

                Collections.shuffle(availableMenus);
                String selectedMenu = availableMenus.getFirst().getName();

                Set<String> recentMembers = restaurantHistoryList.stream()
                        .map(RestaurantHistory::getMemNm)
                        .collect(Collectors.toSet());

                List<MemberEnum> availableMembers = Arrays.stream(MemberEnum.values())
                        .filter(m -> !recentMembers.contains(m.getValue()))
                        .collect(Collectors.toCollection(ArrayList::new));

                if (availableMembers.isEmpty()) {
                    availableMembers = new ArrayList<>(Arrays.asList(MemberEnum.values()));
                }

                Collections.shuffle(availableMembers);
                String firstMember = availableMembers.getFirst().getValue();
                String secondMember = availableMembers.get(1).getValue();

                String message = String.format(
                        "오늘의 추천식당 : %s\r\n마음에 안 드시면 %s님이 정해주세요 !\r\n부재시 %s님이 정해주세요 !",
                        selectedMenu, firstMember, secondMember
                );

                RestaurantHistory newHistory = RestaurantHistory.builder()
                        .restNm(selectedMenu)
                        .memNm(firstMember)
                        .build();
                restaurantHistoryService.insertRestaurantHistory(newHistory);

                telegramUtil.sendMessage(message, BOT_TOKEN_NOTICE, CHAT_ID_SCDEV4);

                resultJson.put("result", "Success");
            }
        } catch (Exception e) {
            log.error("lunchMenu Error", e);
            resultJson.put("result", "Error");
            resultJson.put("message", e.getMessage());
        }

        return resultJson.toString();
    }

    public String lunchTime() throws Exception {

        StringBuilder result = new StringBuilder();
        JSONObject resultJson = new JSONObject();

        try {
            int value;

            value = new Random().nextInt(LunchMentEnum.values().length);
            result.append(LunchMentEnum.values()[value].getValue());

            if (!publicDataApiUtil.isHoliday()) {
                telegramUtil.sendMessage(result.toString(), BOT_TOKEN_NOTICE, CHAT_ID_SCDEV4_ALL);
            }

            resultJson.put("result", "Success");
        } finally {
            result.setLength(0);
            result.append(resultJson);
        }

        return result.toString();
    }

    public String lunchMenuCheck() throws Exception {

        StringBuilder result = new StringBuilder();

        try {
            if (!publicDataApiUtil.isHoliday()) {
                RestaurantHistory visitTodayInfo = restaurantHistoryService.getRestaurantVisitToday();
                if (visitTodayInfo != null) {
                    result.append(visitTodayInfo.getMemNm()).append("님").append(" 오늘 ")
                          .append(visitTodayInfo.getRestNm()).append(" 방문 하셨나요?\n\n");
                    result.append("추천메뉴를 드셨다면 아래 링크를 클릭 해주세요\n").append("https://shorturl.at/zyGBK")
                          .append("\n\n추천메뉴를 안드셨으면 아래 링크를 클릭 해주세요\n").append("https://shorturl.at/abIIr");
                    telegramUtil.sendMessage(result.toString(), BOT_TOKEN_NOTICE, CHAT_ID_SCDEV4);
                }
            }
        } finally {
            result.setLength(0);
        }

        return ApiResultEnum.SUCCESS.getMessage();
    }

    public String dinnerTime() throws Exception {
        StringBuilder result = new StringBuilder();
        JSONObject resultJson = new JSONObject();

        try {
            String bar;
            int value = new Random().nextInt(5);

            bar = BarEnum.values()[value].getValue();

            result.append(bar).append("\r\n").append(bar)
                  .append("\r\n\r\n저녁 식사하실 분만 말씀해주세요.\n미리 말씀 안 해주시면 법카는 집에 갑니다.\r\n\r\n").append(bar).append("\r\n")
                  .append(bar);

            if (log.isDebugEnabled()) {
                log.debug(result.toString());
            }

            if (!publicDataApiUtil.isHoliday()) {
                telegramUtil.sendMessage(result.toString(), BOT_TOKEN_NOTICE, CHAT_ID_SCDEV4_ALL);
            }

            resultJson.put("result", "Success");
        } finally {
            result.setLength(0);
            result.append(resultJson);
        }

        return result.toString();
    }

    public String checkAccount() throws Exception {
        StringBuilder result = new StringBuilder();
        JSONObject resultJson = new JSONObject();

        try {
            result.append("마당 계정 잠기거나 삭제되지 않도록 한번씩 접속 해주세요.");

            if (log.isDebugEnabled()) {
                log.debug(result.toString());
            }

            if (!publicDataApiUtil.isHoliday()) {
                telegramUtil.sendMessage(result.toString(), BOT_TOKEN_NOTICE, CHAT_ID_SCDEV4_ALL);
            }

            resultJson.put("result", "Success");
        } finally {
            result.setLength(0);
            result.append(resultJson);
        }

        return result.toString();
    }
}
