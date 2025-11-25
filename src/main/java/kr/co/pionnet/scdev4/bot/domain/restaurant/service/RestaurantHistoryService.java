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

		restaurantMapper.updateRestaurantVisitYN(restaurantHistory);
	}
	
	public RestaurantHistory getRestaurantVisitToday() {

		return restaurantMapper.getRestaurantVisitToday();
	}
	
	public void updateRestaurantName(RestaurantHistory restaurantHistory) throws IOException {

		restaurantMapper.updateRestaurantName(restaurantHistory);
	}
}
