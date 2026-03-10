package kr.co.pionnet.scdev4.bot.domain.common.exception.custom;

public class LunchMenuActionNotAllowedException extends RuntimeException {
    public LunchMenuActionNotAllowedException(String message) {
        super(message);
    }
}
