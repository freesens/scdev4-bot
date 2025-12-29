package kr.co.pionnet.scdev4.bot.api.v1.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.pionnet.scdev4.bot.domain.common.dto.v1.ResponseV1;
import kr.co.pionnet.scdev4.bot.domain.common.entity.ApiResultEnum;
import kr.co.pionnet.scdev4.bot.domain.notice.service.NoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "알림봇", description = "텔레그램 채널로 메시지를 전송하는 API Section")
@RequestMapping("api/v1/notice")
@RequiredArgsConstructor
public class NoticeController {

    private final NoticeService noticeService;

    @Operation(summary = "점심 메뉴 추천", description = "오늘의 점심 메뉴 및 담당자 정/부를 추천 해줍니다.")
    @GetMapping("/lunch/menu")
    public ResponseV1<String> lunchMenu() throws Exception {
        return ResponseV1.<String>builder(ApiResultEnum.SUCCESS.getCode(), ApiResultEnum.SUCCESS.getMessage())
                         .data(noticeService.lunchMenu())
                         .build();
    }

    @Operation(summary = "점심 시간 알림", description = "점심 식사 시간을 알려줍니다.")
    @GetMapping("/lunch/time")
    public ResponseV1<String> lunchTime() throws Exception {
        return ResponseV1.<String>builder(ApiResultEnum.SUCCESS.getCode(), ApiResultEnum.SUCCESS.getMessage())
                         .data(noticeService.lunchTime())
                         .build();
    }

    @Operation(summary = "점심 추천식당 방문여부 확인요청", description = "점심식사 장소가 어디었는지 확인 요청하는 메시지를 전송합니다.")
    @GetMapping("/lunch/menu-check")
    public ResponseV1<String> lunchMenuCheck() throws Exception {
        return ResponseV1.<String>builder(ApiResultEnum.SUCCESS.getCode(), ApiResultEnum.SUCCESS.getMessage())
                         .data(noticeService.lunchMenuCheck())
                         .build();
    }

    @Operation(summary = "저녁 식사여부 알림", description = "저녁 식사 대상자가 있는지 확인하는 메시지를 보내줍니다.")
    @GetMapping("/dinner/time")
    public ResponseV1<String> dinnerTime() throws Exception {
        return ResponseV1.<String>builder(ApiResultEnum.SUCCESS.getCode(), ApiResultEnum.SUCCESS.getMessage())
                         .data(noticeService.dinnerTime())
                         .build();
    }

    @Operation(summary = "계정 잠금방지 알림", description = "고객사 인프라 접근용 계정이 잠기지 않도록 연장 안내 메시지를 보내쥡니다.")
    @GetMapping("/check-account")
    public ResponseV1<String> checkAccount() throws Exception {
        return ResponseV1.<String>builder(ApiResultEnum.SUCCESS.getCode(), ApiResultEnum.SUCCESS.getMessage())
                         .data(noticeService.checkAccount())
                         .build();
    }
}
