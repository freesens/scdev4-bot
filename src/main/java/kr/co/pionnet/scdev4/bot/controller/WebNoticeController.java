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
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.io.PrintWriter;

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

    @GetMapping("/lunchMenuCheckYN")
    public ModelAndView lunchMenuCheckYN(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        ModelAndView mav = new ModelAndView("lunchMenuCheckYN");

        StringBuffer result = new StringBuffer();
        JSONObject resultJson = new JSONObject();

        String visitYN = BotConst.RESULT_YES.equals(request.getParameter("visitYN")) ? BotConst.RESULT_YES : BotConst.RESULT_NO;
        String menuCheckYN = visitYN;
        JSONArray menuList = new JSONArray();

        RestaurantHistory visitTodayInfo = restaurantHistoryService.getRestaurantVisitToday();

        try {
            // TODO
            String date = (request.getParameter("date"));

            if (visitTodayInfo.getCreateDt().equals(visitTodayInfo.getUpdateDt()) && !publicDataApiUtil.isHoliday()) {
                if (BotConst.RESULT_YES.equals(visitYN)) {
                    restaurantHistoryService.updateRestaurantVisitYN(RestaurantHistory.builder()
                                                                                      .visitDt(date)
                                                                                      .visitYN(visitYN)
                                                                                      .build());

                    result.append("넵^^ 오늘 나온 메뉴는 드셨군요 ^^");
                    telegramUtil.sendMessage(result.toString());

                    return mav;
                }

                for (MenuEnum menu : MenuEnum.values()) {
                    JSONObject menuItem = new JSONObject();
                    menuItem.put("name", menu.getName());
                    menuItem.put("code", menu.getCode());

                    menuList.put(menuItem);
                }

                // 확인 버튼 클릭 시
                String menuCode = request.getParameter("menuCode");
                String newMenuName = request.getParameter("menuName");

                if (menuCode != null && newMenuName != null) {
                    if (!"".equals(menuCode)) {
                        if(MenuEnum.isCodeExists(menuCode)) {
                            String choiceMenu = MenuEnum.getNameByCode(menuCode);

                            restaurantHistoryService.updateRestaurantName(RestaurantHistory.builder()
                                                                                           .visitDt(date)
                                                                                           .visitYN(choiceMenu)
                                                                                           .build());
                        } else {
                            return mav;
                        }
                    } else {
                        restaurantHistoryService.updateRestaurantVisitYN(RestaurantHistory.builder()
                                                                                          .visitDt(date)
                                                                                          .visitYN(visitYN)
                                                                                          .build());

                        if (!"".equals(newMenuName)) {
                            if (log.isDebugEnabled()) {
                                log.debug("inputNewMenuName : " + newMenuName.toString());
                            }
                        }
                    }

                    result.append("넵^^ 오늘 나온 메뉴는 다음기회에 ^^");
                    menuCheckYN = BotConst.RESULT_YES;
                    telegramUtil.sendMessage(result.toString());
                }
            } else {
                menuCheckYN = BotConst.RESULT_YES;
                result.append("이미 클릭되었습니다!!!");
            }
            resultJson.put("resultMessage", result.toString());
        } catch (Exception e) {
            resultJson.put("result", "Fail");
            resultJson.put("failMessage", e.getMessage());

            result.setLength(0);
            result.append(resultJson.toString());
            flushData(response, result);
        } finally {
            request.setAttribute("menuCheckYN", menuCheckYN);
            request.setAttribute("recommMenu", visitTodayInfo.getRestNm());
            request.setAttribute("menuList", menuList);
        }

        return mav;
    }

    private void flushData(final HttpServletResponse response, final StringBuffer result) throws Exception {
        response.setContentType("application/json; charset=UTF-8");

        PrintWriter out = response.getWriter();
        out.write(result.toString());
        out.flush();
    }
}
