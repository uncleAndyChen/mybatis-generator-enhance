package table.property.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
class WelcomeController {
	@RequestMapping("/welcome")
	public String welcome() {
		return "welcome";
	}
}
