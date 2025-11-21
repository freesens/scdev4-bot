package kr.co.pionnet.scdev4.bot.domain.restaurant.entity;

public enum MemberEnum {
	NAME1("파민"),
	NAME2("자연"),
	NAME3("돌+I"),
	NAME4("레몬"),
	NAME5("제육"),
	NAME6("위키"),
	NAME7("제로"),
	NAME8("비움"),
	NAME9("소팔"),
	NAME10("쓸모"),
	NAME11("오이");

	private final String value;
	
	MemberEnum(String value) {
		this.value = value;
	}
	
	public String getValue() {
        return value;
    }
}
