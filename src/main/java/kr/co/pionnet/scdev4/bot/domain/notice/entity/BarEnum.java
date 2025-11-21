package kr.co.pionnet.scdev4.bot.domain.notice.entity;

public enum BarEnum {
	BAR1("◎◎◎◎◎◎◎◎◎◎◎◎◎◎◎◎◎◎◎◎◎◎◎◎◎◎◎◎◎◎◎"),
	BAR2("▩▩▩▩▩▩▩▩▩▩▩▩▩▩▩▩▩▩▩▩▩▩▩▩▩▩▩▩▩▩▩▩"),
	BAR3("◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆"),
	BAR4("▥▥▥▥▥▥▥▥▥▥▥▥▥▥▥▥▥▥▥▥▥▥▥▥▥▥▥▥▥▥▥▥"),
	BAR5("▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒");

	private final String value;
	
	BarEnum(String value) {
		this.value = value;
	}
	
	public String getValue() {
        return value;
    }
}
