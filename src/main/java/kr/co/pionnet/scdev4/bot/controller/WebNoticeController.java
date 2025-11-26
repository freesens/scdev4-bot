package kr.co.pionnet.scdev4.bot.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.io.PrintWriter;
import java.util.Date;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("notice")
public class WebNoticeController {

    TelegramUtil telegramUtil;
    PublicDataApiUtil publicDataApiUtil;
    RestaurantHistoryService restaurantHistoryService;

    @Autowired
    public WebNoticeController(
        TelegramUtil telegramUtil,
        PublicDataApiUtil publicDataApiUtil,
        RestaurantHistoryService restaurantHistoryService
    ) {
        this.telegramUtil = telegramUtil;
        this.publicDataApiUtil = publicDataApiUtil;
        this.restaurantHistoryService = restaurantHistoryService;
    }

    @GetMapping("/lunchMenuCheck")
    public ModelAndView lunchMenuCheck(final HttpServletResponse response) throws Exception {
        ModelAndView mav = new ModelAndView("lunchMenuCheck");

        StringBuffer result = new StringBuffer();
        JSONObject resultJson = new JSONObject();

        RestaurantHistory visitTodayInfo = restaurantHistoryService.getRestaurantVisitToday();

        try {
            if (visitTodayInfo != null
                && visitTodayInfo.getCreateDt().equals(visitTodayInfo.getUpdateDt())
                && !publicDataApiUtil.isHoliday()) {

                mav.addObject("recommMenu", visitTodayInfo.getRestNm());
                mav.addObject("menuList", MenuEnum.convertToMenuResponseDtoList());
            } else {
                result.append("이미 클릭되었습니다!!!");
                resultJson.put("resultMessage", result.toString());
                flushData(response, result);
                return null;
            }
        } catch (Exception e) {
            resultJson.put("result", "Fail");
            resultJson.put("failMessage", e.getMessage());

            result.setLength(0);
            result.append(resultJson.toString());
            flushData(response, result);
            return null;
        }

        return mav;
    }

    @GetMapping("/lunchMenuChecked")
    public void lunchMenuChecked(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        StringBuffer result = new StringBuffer();
        JSONObject resultJson = new JSONObject();

        RestaurantHistory visitTodayInfo = restaurantHistoryService.getRestaurantVisitToday();

        try {
            String date = DateFormatUtils.format(new Date(),  "yyyy-MM-dd");

            if (visitTodayInfo != null
                && visitTodayInfo.getCreateDt().equals(visitTodayInfo.getUpdateDt())
                && !publicDataApiUtil.isHoliday()) {

                restaurantHistoryService.updateRestaurantVisitYN(RestaurantHistory.builder()
                                                                                  .visitDt(date)
                                                                                  .visitYN(BotConst.RESULT_YES)
                                                                                  .build());

                result.append("넵^^ 오늘 나온 메뉴는 드셨군요 ^^");
                telegramUtil.sendMessage(result.toString());
            } else {
                resultJson.put("resultMessage", "이미 클릭되었습니다!!!");
            }
            resultJson.put("resultMessage", result.toString());
        } catch (Exception e) {
            resultJson.put("result", "Fail");
            resultJson.put("failMessage", e.getMessage());

        }

        result.setLength(0);
        result.append(resultJson.toString());
        flushData(response, result);
    }

    @PostMapping("/lunchMenuChecked")
    public void lunchMenuCheckYN(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        StringBuffer result = new StringBuffer();
        JSONObject resultJson = new JSONObject();

        RestaurantHistory visitTodayInfo = restaurantHistoryService.getRestaurantVisitToday();

        try {
            if (visitTodayInfo != null
                && visitTodayInfo.getCreateDt().equals(visitTodayInfo.getUpdateDt())
                && !publicDataApiUtil.isHoliday()) {

                String date = DateFormatUtils.format(new Date(),  "yyyy-MM-dd");
                String menuCode = request.getParameter("menuCode");
                String newMenuName = request.getParameter("menuName");

                if (menuCode != null && newMenuName != null) {
                    if (!StringUtils.EMPTY.equals(menuCode)) {
                        if(MenuEnum.isCodeExists(menuCode)) {
                            String choiceMenu = MenuEnum.getNameByCode(menuCode);

                            restaurantHistoryService.updateRestaurantName(RestaurantHistory.builder()
                                                                                           .visitDt(date)
                                                                                           .restNm(choiceMenu)
                                                                                           .build());
                        }
                    } else {
                        restaurantHistoryService.updateRestaurantVisitYN(RestaurantHistory.builder()
                                                                                          .visitDt(date)
                                                                                          .visitYN(BotConst.RESULT_NO)
                                                                                          .build());

                        if (!StringUtils.EMPTY.equals(newMenuName)) {
                            if (log.isDebugEnabled()) {
                                log.debug("inputNewMenuName : " + newMenuName.toString());
                            }
                        }
                    }

                    result.append("넵^^ 오늘 나온 메뉴는 다음기회에 ^^");
                    resultJson.put("resultMessage", "넵^^ 오늘 나온 메뉴는 다음기회에 ^^");

                    telegramUtil.sendMessage(result.toString());
                }
            } else {
                resultJson.put("resultMessage", "이미 클릭되었습니다!!!");
            }
            resultJson.put("resultMessage", result.toString());
        } catch (Exception e) {
            resultJson.put("result", "Fail");
            resultJson.put("failMessage", e.getMessage());

        }

        result.setLength(0);
        result.append(resultJson.toString());
        flushData(response, result);
    }

    private void flushData(final HttpServletResponse response, final StringBuffer result) throws Exception {
        response.setContentType("application/json; charset=UTF-8");

        PrintWriter out = response.getWriter();
        out.write(result.toString());
        out.flush();
    }
}
