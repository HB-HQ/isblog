package com.hb.isblog.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HuajiController {
    @GetMapping("/huaji")
    public String about() {
        return "huaji";
    }
}
