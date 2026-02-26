package kr.co.pionnet.scdev4.bot.domain.restaurant.entity;

import kr.co.pionnet.scdev4.bot.domain.restaurant.dto.MenuResponseDto;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum MenuEnum {
	MENU1("menu01", "사미식당"), 
	MENU2("menu02", "서브웨이"), 
	MENU3("menu03", "다온참치"),
	MENU4("menu04", "삼미당"), 
	MENU5("menu05", "봉추찜닭"),
	MENU6("menu06", "오봉집"), 
	MENU7("menu07", "고반식당"), 
	MENU8("menu08", "남다른감자탕"),
	MENU9("menu09", "맘스푸드"),
	MENU10("menu10", "등초마라탕"),
	MENU11("menu11", "닭묵"), 
	MENU12("menu12", "센몬텐"), 
	MENU13("menu13", "수작카츠"), 
	MENU14("menu14", "가온가마순대국"), 
	MENU15("menu15", "정담"),
	MENU16("menu16", "한우마당"),
	MENU17("menu17", "명동찌개마을"), 
	MENU18("menu18", "노브랜드버거"), 
	MENU19("menu19", "어식한상"), 
	MENU20("menu20", "종로계림닭도리탕"), 
	MENU21("menu21", "이태리부대찌개"),
	MENU22("menu22", "담방담방"), 
	MENU23("menu23", "제주담돌"),
	MENU24("menu24", "담솥"), 
	MENU25("menu25", "홍박식당"), 
	MENU26("menu26", "해촌샤브칼국수"), 
	MENU27("menu27", "이차이"),
	MENU28("menu28", "노아식당"), 
	MENU29("menu29", "육전국밥"), 
	MENU30("menu30", "두찜"), 
	MENU31("menu31", "요쇼쿠"), 
	MENU32("menu32", "버거킹"),
	MENU33("menu33", "신의한국수"),
	MENU34("menu34", "금고깃집"),
	MENU35("menu35", "직화장인"),
	MENU36("menu36", "청년다방"),
	MENU37("menu37", "마곡사위식당"),
	MENU38("menu38", "피자 배달"),
	MENU39("menu39", "카파이카이"),
	MENU40("menu40", "곤드레밥상"),
	MENU41("menu41", "미숯랭"),
	MENU42("menu42", "바스버거"),
	MENU43("menu43", "란콰이펑누들"),
	MENU44("menu44", "또보겠지 떡볶이"),
	MENU45("menu45", "보영만두"),
	MENU46("menu46", "블리스버거"),
	MENU47("menu47", "우동키노야"),
	MENU48("menu48", "경아식당"),
	MENU49("menu49", "코코이찌방야"),
	MENU50("menu50", "해탄"),
	MENU51("menu51", "넴넴"),
	MENU52("menu52", "BBQ"),
	MENU53("menu53", "오뼈집"),
	MENU54("menu54", "유가네 닭갈비"),
	MENU55("menu55", "일산칼국수"),
	MENU56("menu56", "일일미미"),
	MENU57("menu57", "옥딤섬"),
    MENU58("menu58", "델리케밥"),
    MENU59("menu59", "아우어루꼴라"),
    MENU60("menu60", "라화방마라탕"),
    MENU61("menu61", "타이반쩜"),
    MENU62("menu62", "하양옥"),
    MENU63("menu63", "규카츠정"),
    MENU64("menu64", "다이닝원"),
    MENU65("menu65", "스시풀"),
    MENU66("menu66", "멘츠루"),
    MENU67("menu67", "노아"),
    MENU68("menu68", "오토김밥 배달"),
    MENU69("menu69", "고메스퀘어"),
    MENU70("menu70", "쏭타이치앙마이"),
    MENU71("menu73", "잭슨피자 배달"),
    MENU72("menu74", "아웃백"),
    MENU73("menu75", "푸라닭"),
	MENU999("menu999", "분식 배달");

	private final String code;
	private final String name;

	MenuEnum(String code, String name) {
		this.code = code;
		this.name = name;
	}

	public String getCode() {
		return this.code;
	}

	public String getName() {
		return this.name;
	}

	public static boolean isCodeExists(String s) {
		for (MenuEnum menu : values()) {
			if (menu.getCode().equals(s)) {
				return true;
			}
		}
		return false;
	}

	public static String getNameByCode(String s) {
		for (MenuEnum menu : MenuEnum.values()) {
			if (menu.getCode().equals(s)) {
				return menu.getName();
			}
		}
		return null;
	}

	public static List<MenuResponseDto> convertToMenuResponseDtoList() {
		return Arrays.stream(MenuEnum.values()).map(menu -> MenuResponseDto.builder()
                       .code(menu.getCode())
                       .name(menu.getName())
                       .build()).collect(Collectors.toList());
	}
}
