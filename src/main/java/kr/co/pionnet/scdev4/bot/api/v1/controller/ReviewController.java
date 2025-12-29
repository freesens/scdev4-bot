package kr.co.pionnet.scdev4.bot.api.v1.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import kr.co.pionnet.scdev4.bot.domain.common.dto.v1.ResponseV1;
import kr.co.pionnet.scdev4.bot.domain.common.entity.ApiResultEnum;
import kr.co.pionnet.scdev4.bot.domain.review.service.GithubWebhookService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "리뷰봇", description = "코드리뷰 관련 메시지를 전송하는 API Section")
@RequestMapping("api/v1/review")
@RequiredArgsConstructor
public class ReviewController {

    private final GithubWebhookService githubWebhookService;

    @Operation(summary = "github Webhooks", description = "github에서 전송되는 코드 변경사항을 파싱하여, 리뷰 관련 채널로 메시지를 전송합니다.")
    @PostMapping("/github-webhook")
    public ResponseV1<String> lunchMenu(final HttpServletRequest request) throws Exception {
        return ResponseV1.<String>builder(ApiResultEnum.SUCCESS.getCode(), ApiResultEnum.SUCCESS.getMessage())
                         .data(githubWebhookService.parseToMessage(request))
                         .build();
    }
}
