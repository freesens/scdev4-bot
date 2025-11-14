package kr.co.pionnet.scdev4.bot.api.v1.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Test", description = "Test API Section")
@RequestMapping("api/v1/tests")
public class TestController {

    @GetMapping("/test")
    public void test() {

    }
}
