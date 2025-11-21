package kr.co.pionnet.scdev4.bot.domain.restaurant.dao;

import kr.co.pionnet.scdev4.bot.domain.restaurant.vo.RestaurantHistory;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface RestaurantMapper {
	List<RestaurantHistory> searchResturantHistory();
	void insertRestaurantHistory(RestaurantHistory restaurantHistory);
	void updateRestaurantVisitYN(RestaurantHistory restaurantHistory);
	RestaurantHistory getRestaurantVisitToday();
	void updateRestaurantName(RestaurantHistory restaurantHistory);
}
