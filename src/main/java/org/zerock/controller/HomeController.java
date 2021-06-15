package org.zerock.controller;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Controller는 개발자가 작성하는 클래스로, 여기에는 실제 Request를 처리하는 로직을 작성한다.
 * 이 때 View에 전달하는 데이터는 주로 Model이라는 객체에 담아서 전달한다.
 * Controller는 다양한 타입의 결과를 반환하는데, 이에 대한 처리는 ViewResolver를 이용하게 된다.
 * 
 * 스프링 MVC를 이용하는 경우 작성되는 Controller는 다음과 같은 특징이 있다.
 *  - HttpServletRequest, HttpServletResponse를 거의 사용할 필요 없이 필요기능 구현
 *  - GET, POST 방식 등 전송 방식에 대한 처리를 어노테이션으로 처리 가능
 *  - 상속/인터페이스 방식 대신 어노테이션만으로도 필요한 설정 가능.
 * 
 * Handles requests for the application home page.
 */
@Controller
public class HomeController {
	
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
	
	/**
	 * Simply selects the home view to render by returning its name.
	 */
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(Locale locale, Model model) {
		logger.info("Welcome home! The client locale is {}.", locale);
		
//Locale(로케일은) 프로그램을 언어와 국가에 최적화하기 위해서 사용하는 "지역/언어"정보다.
//프로그램은 유저(보통 시스템 관리자)가 설정한 locale에 따라서, 입/출력 인코딩을 적용해서 메시지를 출력한다.
		
		Date date = new Date();
		DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, locale);
		
		String formattedDate = dateFormat.format(date);
		
		model.addAttribute("serverTime", formattedDate );
		
		return "home";
	}
	
}
