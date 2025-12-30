package kr.co.pionnet.scdev4.bot;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.pionnet.scdev4.bot.domain.common.dto.v1.ResponseV1;
import kr.co.pionnet.scdev4.bot.domain.common.entity.ApiResultEnum;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
@Tag(name = "Test", description = "Test API Section")
@RequestMapping("api/v1/test")
public class TestController {

    @Operation(summary = "서버 시간", description = "알려준다!!  현재 서버 시간.")
    @GetMapping("/today")
    public ResponseV1<String> test() {
        return ResponseV1.<String>builder(ApiResultEnum.SUCCESS.getCode(), ApiResultEnum.SUCCESS.getMessage())
                         .data(DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"))
                         .build();
    }
}
