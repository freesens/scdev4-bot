package kr.co.pionnet.scdev4.bot.domain.restaurant.entity;

public enum LunchMentEnum {
	LUNCH0("점심몬한테 일을 뺐겼어.\n유일하게 남은 역할이니 식사하러 가시죠~"),
	LUNCH1("할 일도 없는데, 식사하러 가시죠!"),
	LUNCH2("점심시간~~\n식사하러 가시죠!"),
	LUNCH3("점심시간입니다~!\n식사하러 가시죠 :)"),
	LUNCH4("벌써 점심시간이라니!\n점심 드시고 하시죠!!"),
	LUNCH5("원래도 놀고 있었겠지만,\n하시던 일 잠시 내려놓고!\n점심 드시러 가시죠!!"),
	LUNCH6("점심시간인데..오늘의 메뉴는 무엇???");
	
	private final String value;
	
	LunchMentEnum(String value) {
		this.value = value;
	}
	
	public String getValue() {
        return value;
    }
}
