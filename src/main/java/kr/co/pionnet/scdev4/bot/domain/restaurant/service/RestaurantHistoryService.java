package kr.co.pionnet.scdev4.bot.domain.restaurant.service;

import kr.co.pionnet.scdev4.bot.domain.restaurant.dao.RestaurantMapper;
import kr.co.pionnet.scdev4.bot.domain.restaurant.vo.RestaurantHistory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Slf4j
@Service
public class RestaurantHistoryService {

	private final RestaurantMapper restaurantMapper;

	@Autowired
	public RestaurantHistoryService(RestaurantMapper restaurantMapper) {
		this.restaurantMapper = restaurantMapper;
	}


	public List<RestaurantHistory> selectRestaurantHistory() {
		
		return restaurantMapper.searchResturantHistory();
	}
	
	public void insertRestaurantHistory(RestaurantHistory restaurantHistory) {
		
		restaurantMapper.insertRestaurantHistory(restaurantHistory);
	}

	public void updateRestaurantVisitYN(RestaurantHistory restaurantHistory)  {
//		Validator.throwIfEmpty(restaurantHistory.getVisitDt(), "업데이트 대상 방문일자 정보가 존재하지 않습니다.");
//		if(!DateUtil.isValid(restaurantHistory.getVisitDt(), "yyyy-MM-dd")) {
//			throw new ValidationException("업데이트 대상 방문일 날짜형식이 올바르지 않습니다.");
//		}
//		if(!YumConst.RESULT_YES.equals(restaurantHistory.getVisitYN())) {
//			restaurantHistory.setVisitYN(YumConst.RESULT_NO);
//		}

		restaurantMapper.updateRestaurantVisitYN(restaurantHistory);
	}
	
	public RestaurantHistory getRestaurantVisitToday() {

		return restaurantMapper.getRestaurantVisitToday();
	}
	
	public void updateRestaurantName(RestaurantHistory restaurantHistory) throws IOException {
		//Validator.throwIfEmpty(restaurantHistory.getVisitDt(), "업데이트 대상 방문일자 정보가 존재하지 않습니다.");
//		if(!DateUtil.isValid(restaurantHistory.getVisitDt(), "yyyy-MM-dd")) {
//			throw new ValidationException("업데이트 대상 방문일 날짜형식이 올바르지 않습니다.");
//		}

		restaurantMapper.updateRestaurantName(restaurantHistory);
	}
}
