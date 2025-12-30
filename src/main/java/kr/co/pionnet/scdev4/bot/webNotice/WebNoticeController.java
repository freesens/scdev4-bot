package kr.co.pionnet.scdev4.bot.webNotice;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("notice")
public class WebNoticeController {
    private final WebNoticeService webNoticeService;

    @GetMapping("/lunchMenuCheck")
    public ModelAndView lunchMenuCheck() {
        try {
            LunchMenuData viewData = webNoticeService.getLunchMenuData();

            ModelAndView mav = new ModelAndView("lunchMenuCheck");
            mav.addObject("recommMenu", viewData.getRecommendedMenu());
            mav.addObject("menuList", viewData.getMenuList());

            return mav;
        } catch (IllegalStateException e) {
            ModelAndView mav = new ModelAndView("lunchMenuError");
            mav.addObject("message", NoticeMessage.clientMessage.ALREADY_CLICKED);

            return mav;
        }
    }
}
