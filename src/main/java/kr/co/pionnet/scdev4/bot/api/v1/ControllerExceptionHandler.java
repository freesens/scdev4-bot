package kr.co.pionnet.scdev4.bot.api.v1;

import kr.co.pionnet.scdev4.bot.api.v1.common.exception.LunchMenuActionNotAllowedException;
import kr.co.pionnet.scdev4.bot.domain.common.dto.v1.ResponseV1;
import kr.co.pionnet.scdev4.bot.domain.common.entity.ApiResultEnum;
import kr.co.pionnet.scdev4.bot.webNotice.NoticeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.RestClientException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class ControllerExceptionHandler {
    @ExceptionHandler(LunchMenuActionNotAllowedException.class)
    public ResponseEntity<Map<String, Object>> handleException(LunchMenuActionNotAllowedException e) {
        log.warn(e.getMessage(), e);

        Map<String, Object> result = new HashMap<>();
        result.put("result", "Fail");
        result.put("failMessage", NoticeMessage.clientMessage.ALREADY_CLICKED);

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(result);
    }

    @ExceptionHandler({
            MethodArgumentTypeMismatchException.class,
            HttpMessageNotReadableException.class
    })
    public ResponseV1<?> handleBadRequest(Exception e) {

        log.warn("Invalid request: {}", e.getMessage());

        return ResponseV1.builder(ApiResultEnum.INVALID_REQUEST.getCode(), ApiResultEnum.INVALID_REQUEST.getMessage()).build();
    }

    @ExceptionHandler({
            DataAccessException.class,
            RestClientException.class,
            NullPointerException.class
    })
    public ResponseV1<?> handleSystemError(Exception e) {
        log.error("System error", e);

        return ResponseV1.builder(ApiResultEnum.SERVER_ERROR.getCode(), ApiResultEnum.SERVER_ERROR.getMessage()).build();
    }

    @ExceptionHandler(value = {Exception.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseV1<?> unknownException(Exception e) {
        log.error(e.getMessage(), e);

        return ResponseV1.builder(ApiResultEnum.UNKNOWN.getCode(), ApiResultEnum.UNKNOWN.getMessage()).build();
    }
}
