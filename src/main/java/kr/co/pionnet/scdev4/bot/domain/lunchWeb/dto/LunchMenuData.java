package kr.co.pionnet.scdev4.bot.domain.lunchWeb.dto;

import kr.co.pionnet.scdev4.bot.domain.restaurant.dto.MenuResponseDto;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class LunchMenuData{
    private final String recommendedMenu;
    private final List<MenuResponseDto> menuList;
}
