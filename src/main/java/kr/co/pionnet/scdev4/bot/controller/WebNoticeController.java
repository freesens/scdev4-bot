package kr.co.pionnet.scdev4.bot.controller;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.io.PrintWriter;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("notice")
public class WebNoticeController {

    private final WebNoticeService webNoticeService;

    @GetMapping("/lunchMenuCheck")
    public ModelAndView lunchMenuCheck() {
        try {
            return webNoticeService.openLunchMenuPage();
        } catch (IllegalStateException e) {
            ModelAndView mav = new ModelAndView("lunchMenuError");
            mav.addObject("message", e.getMessage());
            return mav;
        }
    }

    @GetMapping("/lunchMenuChecked")
    public void lunchMenuChecked(HttpServletResponse response) throws Exception {
        JSONObject resultJson = new JSONObject();

        try {
            resultJson.put("resultMessage", webNoticeService.confirmLunchMenuVisited());
        } catch (Exception e) {
            resultJson.put("result", "Fail");
            resultJson.put("failMessage", e.getMessage());
        }

        flushData(response, new StringBuffer(resultJson.toString()));
    }

    @PostMapping("/lunchMenuChecked")
    public void lunchMenuCheckYN(
            @RequestParam(required = false) String menuCode,
            @RequestParam(required = false) String menuName,
            HttpServletResponse response
    ) throws Exception {

        JSONObject resultJson = new JSONObject();

        try {
            String message = webNoticeService.rejectLunchMenu(menuCode, menuName);
            resultJson.put("resultMessage", message);
        } catch (Exception e) {
            resultJson.put("result", "Fail");
            resultJson.put("failMessage", e.getMessage());
        }

        flushData(response, new StringBuffer(resultJson.toString()));
    }

    private void flushData(final HttpServletResponse response, final StringBuffer result) throws Exception {
        response.setContentType("application/json; charset=UTF-8");

        PrintWriter out = response.getWriter();
        out.write(result.toString());
        out.flush();
    }
}
