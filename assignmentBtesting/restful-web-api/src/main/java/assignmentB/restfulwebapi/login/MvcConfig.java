//package assignmentB.restfulwebapi.login;
//
//import org.springframework.context.annotation.ComponentScan;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//
//@Configuration
//@ComponentScan("org.springframework.security.samples.mvc")
//public class MvcConfig implements WebMvcConfigurer {
//
//	public void addViewControllers(ViewControllerRegistry registry) {
//		registry.addViewController("/polls").setViewName("polls.html");
//		registry.addViewController("/").setViewName("home");
//		registry.addViewController("/login").setViewName("login");
//		registry.addViewController("/register").setViewName("register");
//	}
//	
//	@PostMapping("/login")
//	public void login() {
//		
//	}
//
//}