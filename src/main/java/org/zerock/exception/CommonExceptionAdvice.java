package org.zerock.exception;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import lombok.extern.log4j.Log4j;

@ControllerAdvice //<== 해당 객체가 스프링 컨트롤러에서 발생하는 예외를 처리하는 존재임을 명시
@Log4j
public class CommonExceptionAdvice { 

	
	 // @ExceptionHandler: 해당 메서드가 ()안에 들어가는 예외 타입을 처리한다는 것을 의미. 이 어노테이션의 속성으로는 Exception클래스 타입을 지정 가능.
	 // 아래와 같은 경우  Exception.class를 지정하였으므로 모든 예외에 대한 처리가 except()만을 이용해서 처리할 수 있다.
	@ExceptionHandler(Exception.class)
	public String except(Exception ex, Model model) { 
		
		log.error("Exception ....." + ex.getMessage());
		model.addAttribute("exception", ex);
		log.error(model);
		return "error_page";
	}
}


/* 만일 특정한 타입의 예외를 달고 싶다면 Exception.class 대신에 구체적인 예외의 클래스를 지정해야 한다.
   JSP 화면에서도 구체적인 메시지를 보고 싶다면 Model을 이용해서 전달하는 것이 좋다.

   원래는 servlet-context.xml에 설정을 해주지만, 이 책으로는 자바 설정을 이용해서 구현하므로 
	org.zerock.exception을 추가한다. 그리고 ServletConfig 클래스를 수정한다.
	
	
	@EnableWebMvc
	@ComponentScan(basePackages= {"org.zerock.controller", "org.zerock.exception"})
	public class ServletConfig implements WebMvcConfigurer {
	
	
*/


