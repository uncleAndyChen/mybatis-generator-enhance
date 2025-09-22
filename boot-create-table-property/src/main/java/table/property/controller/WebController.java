package table.property.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class WebController {
    @RequestMapping(value = {"/home", "/index"})
    public String home() {
        return "indexCustom";
    }
}
