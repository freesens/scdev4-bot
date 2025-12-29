package kr.co.pionnet.scdev4.bot.controller;

import kr.co.pionnet.scdev4.bot.domain.common.constant.BotConst;
import kr.co.pionnet.scdev4.bot.domain.common.util.PublicDataApiUtil;
import kr.co.pionnet.scdev4.bot.domain.common.util.TelegramUtil;
import kr.co.pionnet.scdev4.bot.domain.restaurant.entity.MenuEnum;
import kr.co.pionnet.scdev4.bot.domain.restaurant.service.RestaurantHistoryService;
import kr.co.pionnet.scdev4.bot.domain.restaurant.vo.RestaurantHistory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import java.util.Date;

@Slf4j
@Service
@RequiredArgsConstructor
public class WebNoticeService {
    private final TelegramUtil telegramUtil;
    private final PublicDataApiUtil publicDataApiUtil;
    private final RestaurantHistoryService restaurantHistoryService;

    public ModelAndView openLunchMenuPage() {
        RestaurantHistory visitTodayInfo =
                restaurantHistoryService.getRestaurantVisitToday();

        if (visitTodayInfo != null
                && visitTodayInfo.getCreateDt().equals(visitTodayInfo.getUpdateDt())
                && !publicDataApiUtil.isHoliday()) {

            ModelAndView mav = new ModelAndView("lunchMenuCheck");
            mav.addObject("recommMenu", visitTodayInfo.getRestNm());
            mav.addObject("menuList", MenuEnum.convertToMenuResponseDtoList());
            return mav;
        }

        throw new IllegalStateException(NoticeMessage.ALREADY_CLICKED);
    }

    public String confirmLunchMenuVisited() throws Exception {
        RestaurantHistory visitTodayInfo =
                restaurantHistoryService.getRestaurantVisitToday();

        if (visitTodayInfo != null
                && visitTodayInfo.getCreateDt().equals(visitTodayInfo.getUpdateDt())
                && !publicDataApiUtil.isHoliday()) {

            String today = DateFormatUtils.format(new Date(), "yyyy-MM-dd");

            restaurantHistoryService.updateRestaurantVisitYN(
                    RestaurantHistory.builder()
                            .visitDt(today)
                            .visitYN(BotConst.RESULT_YES)
                            .build()
            );

            String message = NoticeMessage.LUNCH_VISITED;
            telegramUtil.sendMessage(message);

            return message;
        }

        throw new IllegalStateException(NoticeMessage.ALREADY_CLICKED);
    }

    public String rejectLunchMenu(String menuCode, String newMenuName) throws Exception {
        RestaurantHistory visitTodayInfo =
                restaurantHistoryService.getRestaurantVisitToday();

        if (visitTodayInfo != null
                && visitTodayInfo.getCreateDt().equals(visitTodayInfo.getUpdateDt())
                && !publicDataApiUtil.isHoliday()) {

            String today = DateFormatUtils.format(new Date(), "yyyy-MM-dd");

            if (StringUtils.isNotBlank(menuCode)
                    && MenuEnum.isCodeExists(menuCode)) {

                String choiceMenu = MenuEnum.getNameByCode(menuCode);

                restaurantHistoryService.updateRestaurantName(
                        RestaurantHistory.builder()
                                .visitDt(today)
                                .restNm(choiceMenu)
                                .build()
                );

            } else {
                restaurantHistoryService.updateRestaurantVisitYN(
                        RestaurantHistory.builder()
                                .visitDt(today)
                                .visitYN(BotConst.RESULT_NO)
                                .build()
                );

                if (StringUtils.isNotBlank(newMenuName) && log.isDebugEnabled()) {
                    log.debug("inputNewMenuName : {}", newMenuName);
                }
            }

            String message = NoticeMessage.LUNCH_REJECTED;
            telegramUtil.sendMessage(message);
            return message;
        }

        throw new IllegalStateException(NoticeMessage.ALREADY_CLICKED);
    }
}