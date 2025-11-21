package kr.co.pionnet.scdev4.bot.domain.restaurant.vo;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RestaurantHistory {
	private long seq;
	private String memNm;
	private String restNm;
	private String visitDt;
	private String visitYN;
	private long rtrNum;
	private long memNum;
	private String createDt;
	private String updateDt;
}
