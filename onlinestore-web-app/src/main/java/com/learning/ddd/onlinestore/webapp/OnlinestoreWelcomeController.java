package com.learning.ddd.onlinestore.webapp;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class OnlinestoreWelcomeController {
	
	private static final String WELCOME_JSP_NAME = "welcome";
	
	public OnlinestoreWelcomeController() {
	}
	
    @GetMapping("/welcome")
    public String welcome(){
        return WELCOME_JSP_NAME;
    }
    
    
}
