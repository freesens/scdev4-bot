package kr.co.pionnet.scdev4.bot.domain.common.dto.v1;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.pionnet.scdev4.bot.domain.common.entity.ApiResultEnum;
import lombok.Builder;
import lombok.Getter;

@Builder(builderMethodName = "innerBuilder")
@Getter
public class ResponseV1<T> {
    @Builder.Default
    @Schema(description = "api response code", example = "0000")
    private String code = ApiResultEnum.SUCCESS.getCode();

    @Builder.Default
    @Schema(description = "api response message", example = "success")
    private String message = ApiResultEnum.SUCCESS.getMessage();

    @Schema(description = "api response data")
    private T data;

    public static <T> ResponseV1Builder<T> builder(String code, String message) {
        ResponseV1Builder<T> innerBuilder = innerBuilder();

        return innerBuilder
            .code(code)
            .message(message);
    }
}
