package kr.co.pionnet.scdev4.bot.webNotice;

import kr.co.pionnet.scdev4.bot.api.v1.common.exception.LunchMenuActionNotAllowedException;
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

import java.util.Date;

@Slf4j
@Service
@RequiredArgsConstructor
public class WebNoticeService {
    private final TelegramUtil telegramUtil;
    private final PublicDataApiUtil publicDataApiUtil;
    private final RestaurantHistoryService restaurantHistoryService;

    public LunchMenuData getLunchMenuData() {
        RestaurantHistory visitTodayInfo = restaurantHistoryService.getRestaurantVisitToday();

        if (isLunchMenuActionNotAllowed(visitTodayInfo)) {
            throw new LunchMenuActionNotAllowedException(NoticeMessage.LogMessage.NOT_ALLOWED_LUNCH);
        }

        return new LunchMenuData(
                visitTodayInfo.getRestNm(),
                MenuEnum.convertToMenuResponseDtoList()
        );
    }

    public String confirmLunchMenuVisited() throws Exception {
        if (isLunchMenuActionNotAllowed(restaurantHistoryService.getRestaurantVisitToday())) {
            throw new LunchMenuActionNotAllowedException(NoticeMessage.LogMessage.NOT_ALLOWED_LUNCH);
        }

        restaurantHistoryService.updateRestaurantVisitYN(
                RestaurantHistory.builder()
                        .visitDt(DateFormatUtils.format(new Date(), "yyyy-MM-dd"))
                        .visitYN(BotConst.RESULT_YES)
                        .build()
        );

        telegramUtil.sendMessage(NoticeMessage.clientMessage.LUNCH_VISITED);

        return NoticeMessage.clientMessage.LUNCH_VISITED;
    }

    public String rejectLunchMenu(String menuCode, String newMenuName) throws Exception {
        if (isLunchMenuActionNotAllowed(restaurantHistoryService.getRestaurantVisitToday())) {
            throw new LunchMenuActionNotAllowedException(NoticeMessage.LogMessage.NOT_ALLOWED_LUNCH);
        }

        String today = DateFormatUtils.format(new Date(), "yyyy-MM-dd");

        if (StringUtils.isNotBlank(menuCode) && MenuEnum.isCodeExists(menuCode)) {
            restaurantHistoryService.updateRestaurantName(
                    RestaurantHistory.builder()
                            .visitDt(today)
                            .restNm(MenuEnum.getNameByCode(menuCode))
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

        telegramUtil.sendMessage(NoticeMessage.clientMessage.LUNCH_REJECTED);

        return NoticeMessage.clientMessage.LUNCH_REJECTED;
    }

    private boolean isLunchMenuActionNotAllowed(RestaurantHistory visitTodayInfo) {
        return visitTodayInfo == null
                || !visitTodayInfo.getCreateDt().equals(visitTodayInfo.getUpdateDt())
                || publicDataApiUtil.isHoliday();
    }
}