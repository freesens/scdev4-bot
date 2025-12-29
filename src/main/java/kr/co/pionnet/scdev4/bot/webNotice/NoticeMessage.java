package kr.co.pionnet.scdev4.bot.webNotice;

public final class NoticeMessage {
    public interface clientMessage {
        String ALREADY_CLICKED = "이미 클릭되었습니다!!!";
        String LUNCH_VISITED = "넵^^ 오늘 나온 메뉴는 드셨군요 ^^";
        String LUNCH_REJECTED = "넵^^ 오늘 나온 메뉴는 다음기회에 ^^";
    }

    public interface LogMessage {
        String NOT_ALLOWED_LUNCH = "접근할 수 없는 상태입니다.";
    }
}