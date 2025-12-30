package kr.co.pionnet.scdev4.bot.api.v1.common.exception;

public class LunchMenuActionNotAllowedException extends RuntimeException {
    public LunchMenuActionNotAllowedException(String message) {
        super(message);
    }
}
