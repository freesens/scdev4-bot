package kr.co.pionnet.scdev4.bot.domain.lunchWeb.controller;

import kr.co.pionnet.scdev4.bot.domain.common.dto.v1.ResponseV1;
import kr.co.pionnet.scdev4.bot.domain.common.entity.ApiResultEnum;
import kr.co.pionnet.scdev4.bot.domain.lunchWeb.service.WebNoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("notice")
public class WebNoticeRestController {
    private final WebNoticeService webNoticeService;

    @GetMapping("/lunchMenuChecked")
    public ResponseV1<?> lunchMenuChecked() throws Exception {
        return ResponseV1.builder(
                ApiResultEnum.SUCCESS.getCode(),
                webNoticeService.confirmLunchMenuVisited())
                .build();
    }

    @PostMapping("/lunchMenuChecked")
    public ResponseV1<?> lunchMenuCheckYN(
            @RequestParam(required = false) String menuCode,
            @RequestParam(required = false) String menuName
    ) throws Exception {
        return ResponseV1.builder(
                ApiResultEnum.SUCCESS.getCode(),
                webNoticeService.rejectLunchMenu(menuCode, menuName))
                .build();
    }
}