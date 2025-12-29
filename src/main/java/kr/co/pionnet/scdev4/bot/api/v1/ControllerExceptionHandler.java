package kr.co.pionnet.scdev4.bot.api.v1;

import kr.co.pionnet.scdev4.bot.webNotice.NoticeMessage;
import kr.co.pionnet.scdev4.bot.domain.common.dto.v1.ResponseV1;
import kr.co.pionnet.scdev4.bot.domain.common.entity.ApiResultEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class ControllerExceptionHandler {
    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<Map<String, Object>> handleException(IllegalStateException e) {
        log.error(e.getMessage(), e);

        Map<String, Object> result = new HashMap<>();
        result.put("result", "Fail");
        result.put("failMessage", NoticeMessage.clientMessage.ALREADY_CLICKED);

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(result);
    }

    @ExceptionHandler(value = {Exception.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseV1<?> unknownException(Exception e) {
        log.error(e.getMessage(), e);
        return ResponseV1.builder(ApiResultEnum.UNKNOWN.getCode(), ApiResultEnum.UNKNOWN.getMessage()).build();
    }
}
