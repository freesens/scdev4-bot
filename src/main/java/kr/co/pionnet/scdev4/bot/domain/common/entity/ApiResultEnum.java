package kr.co.pionnet.scdev4.bot.domain.common.entity;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ApiResultEnum {
    SUCCESS("0000", "성공"),
    UNKNOWN("9999", "알수없는 오류가 발생했습니다.");

    private final String code;
    private final String message;

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
