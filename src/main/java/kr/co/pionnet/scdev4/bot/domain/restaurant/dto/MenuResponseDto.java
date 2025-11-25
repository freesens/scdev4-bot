package kr.co.pionnet.scdev4.bot.domain.restaurant.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class MenuResponseDto {
    private String code;
    private String name;
}
