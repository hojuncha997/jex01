package org.zerock.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;

import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.zerock.domain.SampleDTO;
import org.zerock.domain.SampleDTOList;
import org.zerock.domain.TodoDTO;

import lombok.extern.log4j.Log4j;


@Controller
@RequestMapping("/sample/*") //@GetMapping()과 같은 기능. 현재 클래스의 모든 메서드들의 기본적인 URL 경로가 된다.
@Log4j
public class SampleController {
	
	/*
	  
	 파라미터의 수집을 다른 용어로는 binding이라고 한다. 변환이 가능한 데이터는 자동으로 변환되지만 경우에 따라서는
	 파라미터를 수동으로 변환해서 처리해야 하는 경우도 있다. 예를 들어 화면에서 '2018-01-01'과 같이 문자열로 전달된
	 데이터를 java.util.Date 타입으로 변환하는 작업이 그러하다. 스프링 Controller에서는 파라미터를 바인딩할 때
	 호출되는 @InitBinder를 이용해서 이러한 변환을 처리할 수 있다.
	  
	  
	  
	 */
	
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		binder.registerCustomEditor(java.util.Date.class, new CustomDateEditor(dateFormat, false));
	}
	
	

	@RequestMapping("")
	public void basic() {
		log.info("basic.....");
	}
	
	//RequestMapping은 GET, POST 방식 모두를 지원해야 하는 경우에 배열로 처리해서 지정할 수 있다.
	//일반적인 경우에는 GET, POST 방식만을 사용하지만 최근에는 PUT, DELETE 방식 등도 점점 사용하고 있다.
	@RequestMapping(value = "/basic", method = {RequestMethod.GET, RequestMethod.POST})
	public void basicGet() {
		log.info("basic get.................");
		
	}
	
	//GetMapping은 오직 GET방식에만 사용할 수 있으므로 편하지만 기능이 제한적이다.
	@GetMapping("/basicOnlyGet")
	public void basicGet2() {
		log.info("basic get Only get..........");
	}
	
	@GetMapping("/ex01")
	public String ex01(SampleDTO dto) { //SampleDTO를 파라미터로 사용하게 되면 자동으로 setter메소드가 동작하면서 파라미터를 수집한다.
		
		log.info("" + dto);
		
		return "ex01";
	}
	
	/*
	 * 
	 * Controller가 파라미터를 수집하는 방식은 파라미터 타입에 따라 자동으로 변환하는 방식을 사용한다.
	 * 예를 들어 SampleDTO에는 int 타입으로 선언된 age가 자동으로 숫자로 변환된다.
	 * 
	 * 만일 기본 자료형이나 문자열 등을 이용한다면 파라미터의 타입만을 맞게 선언해 주는 방식을 사용할 수 있다.
	 * 
	 * @RequestParam은 파라미터로 사용된 변수의 이름과 전달되는 파라미터의 이름이 다른 경우에 사용된다.
	 * (아래의 예제의 경우 변수명과 파라미터의 이름이 동일하기 때문에 같이 나온다.)
	 * 
	 * */
	
	@GetMapping("/ex02")
	public String ex02(@RequestParam("name") String name, @RequestParam("age") int age) {
		log.info("name: " + name);
		log.info("age: " + age);
		
		return "ex02";
	}
	
	
	
	/*p132 
	 * 
	 * 리스트
	 * 
	동일한 이름의 파라미터가 여러 개 전달되는 경우에는 ArrayList<> 등을 사용해서 처리할 수 있다.
	스프링은 파라미터의 타입을 보고 객체를 생성하므로 파라미터의 타입은 List<>와 같이 인터페이스 타입이 아닌
	실제적인 클래스 타입으로 지정한다.
	
	아래 코드의 경우 'ids'라는 이름의 파라미터가 여러 개 전달되더라도 ArrayList<String>이 생성되어
	자동으로 수집된다.
	
	http://localhost:8080/sample/ex02List?ids=111&ids=222&ids=333
	INFO : org.zerock.controller.SampleController - ids: [111, 222, 333]
	
	
	*/
	
	@GetMapping("/ex02List")
	public String ex02List(@RequestParam("ids")ArrayList<String> ids) {
		log.info("ids: " + ids);
		return "ex02List";
	}
	
	
	//	 배열도 마찬가지
	//	http://localhost:8080/sample/ex02Array?ids=111&ids=222&ids=333
	//	INFO : org.zerock.controller.SampleController - array ids: [111, 222, 333]

	@GetMapping("/ex02Array")
	public String ex02Array(@RequestParam("ids") String[] ids) {
		
		log.info("array ids: "+ Arrays.toString(ids));
		return "ex02Array";
	}
	
	/*
	 * 객체 리스트.
	 * 
	 * 만일 전달하는 데이터가 SampleDTO와 같이 객체타입이고 여러 개를 처리해야 한다면.
	 * 아래와 같이 SampleList 타입을 파라미터로 사용하는 메서드를 작성한다.
	 * 
	 * 파라미터는 '[인덱스]'와 같은 형식으로 전달해서 처리할 수 있다.
	 * 
	 *	http://localhost:8080/sample/ex02Bean?list%5B0%5D.name=aaa&list%5B1%5D.name=bbb&list%5B2%5D.name=ccc
	 *	이렇게 호출하면
	 *
 	 *  INFO : org.zerock.controller.SampleController - list DTOs: SampleDTOList(list=[SampleDTO(name=aaa, age=0), SampleDTO(name=bbb, age=0), SampleDTO(name=ccc, age=0)])
	 *
	 * 이렇게 여러 개의 SampleDTO 객체를 생성한다.
	 *
	 */
	
	@GetMapping("/ex02Bean")
	public String ex02Bean (SampleDTOList list) {
		log.info("list DTOs: "+ list);
		
		return "ex02Bean";
	}
	
	
	/*
	 아래와 같이 설정했다면
	  
	 @InitBinder
	public void initBinder(WebDataBinder binder) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		binder.registerCustomEditor(java.util.Date.class, new CustomDateEditor(dateFormat, false));
	} 
	 
	 * 
	 * http://localhost:8080/sample/ex03?title=test&dueDate=2018-01-01
	 * 위의 주소로 호출했을 때 서버에서는 정상적으로 파라미터를 수집해서 처리한다.
	 * 
	 * INFO : org.zerock.controller.SampleController - todo: TodoDTO(title=test, dueDate=Mon Jan 01 00:00:00 KST 2018)
	 * 
	 * 반면 @InitBinder 처리가 되지 않는다면 브라우저에서는 400 에러가 발생한다.(400은 요청 syntax에러다).
	 * 하지만 날짜가 정상적으로 처리되어도 jsp페이지가 없다면 404 에러를 발생시킨다.
	 * 
	 * */
	@GetMapping("/ex03")
	public String ex03(TodoDTO todo) {
		log.info("todo: " + todo);
		return "ex03";
	}
	/*
	하지만 InitBinder를 사용하지 않고 파라미터로 사용되는 인스턴스 변수에 @DateTimeFormat을 적용해도 변환이 가능하다.
	 * @DateTimeFormat을 이용하는 경우에는 @InitBinder는 필요하지 않다. p138 참고
	 * 
	 * package org.zerock.domain;
	 * import java.util.Date;
	 * import org.springframework.format.annotation.DateTimeFormat;
	 * import lombok.Data;
	 * 
	 * @Data
	 * public class TodoDTO{
	 * 	private String title;
	 * 
	 * @DateTimeFormat(pattern = "yyyy/mm/dd")
	 * private Date dueDate;
	 * }
	 * 
	 * 
	 * 
	 */
	
	
	
	
	/*
	 
	  Controller의 메서드를 작성할 때는 특별하게 Model이라는 타입을 파라미터로 지정할 수 있다.
	  Model 객체는 JSP에 컨트롤러에서 생성된 데이터를 담아서 전달하는 역할을 하는 존재이다.
	  따라서 우리는 Model을 이용해서 JSP와 같은 view로 전달해야 하는 데이터를 담아 보낼 수 있다.
	  
	  메서드의 파라미터에 Model타입이 지정된 겨우에는 스프링은 특별하게 Model 타입의 객체를 만들어서 메서드에 주입한다. 
	  ex)
	  
	  public String home(Model model) {
	  	model.addAttribute("serverTime", new java.util.Date());
	  	return "home";
	  }
	  
	  메서드의 파라미터를 Model 타입으로 선언하게 되면 자동으로 스프링MVC에서 Model 타입의 객체를 만들어주기 때문에
	  개발자의 입장에서는 필요한 데이터를 담아 주는 작업만으로 모든 작업이 완료된다.
	  
	  Model을 사용해야 하는 경우는 주로 Controller에 전달된 데이터(파라미터)를 이용해서 추가적인 데이터를 가져와야 하는 상황이다.
	  예를 들어,
	  
	  - 라스트 페이지 번호를 파라미터로 전달받고, 실제 데이터를 View로 전달해야 하는 경우
	  - 파라미터들에 대한 처리 후 결과를 전달해야 하는 경우
	  
	 */


	
	
	/*
	 
	  @ModelAttribute 어노테이션
	  
	  웹페이지의 구조는 Request에 전달된 데이터를 가지고 필요하다면 추가적인 데이터를 생성해서 화면으로 전달하는 방식으로 동작한다.
	  Model의 경우는 '파라미터로 전달된 데이터'는 존재하지 않지만 화면에 필요한 데이터를 전달하기 위해 사용한다.
	  예를 들어 페이지 번호는 파라미터로 전달되지만, 결과 데이터를 전달하려면 Model에 담아서 전달한다.
	  
	   스프링 MVC의 컨트롤러는 기본적으로 Java Beans 규칙에 맞는 객체는 다시 화면으로 객체를 전달한다.
	   좁은 의미에서 Java Beans의 규칙은 단순히 생성자가 없거나 빈 생성자를 가져야 하며, getter/setter를 가진 클래스의
	   객체들을 의미한다. 앞의 예제에서 파라미터로 사용된 SampleDTO의 경우는 Java Bean의 규칙에 맞기 때문에 자동으로 다시
	   화면까지 전달된다. 전달될 떄에는 클래스명의 앞글자는 소문자로 처리된다.
	   
	   반면에 기본 자료형의 경우는 파라미터로 선언하더라도 기본적으로 화면까지 전달되지 않는다.
	  
	  
	 */
	
	
	@GetMapping("/ex04")
	public String ex04(SampleDTO dto, @ModelAttribute("page") int page) {
		log.info("dto: "+ dto);
		log.info("page: "+ page);
		
		return "/sample/ex04";
	}
//	
	
	
	/*
		  
	  @ModelAttribute는 강제로 전달받은 파라미터를 Model에 담아서 전달하도록 할 때 필요한 어노테이션이다.
	  @ModelAttribute가 걸린 파라미터는 타입에 관계 없이 무조건 Model에 담아서 전달되므로,
	  파라미터로 전달된 데이터를 다시 화면에서 사용해야 할 경우 유용하게 사용된다.
	  
	  @ModelAttribute가 붙은 파라미터는 화면까지 전달되므로 브라우저를 통해 호출하면 ${page}가 출력되는 것을 확인할 수 있다.
	  (기본 자료형에  @ModelAttribute를 적용할 경우에는 @ModelAttribute("page")와 같이 값(value)를 지정하자)
	  
	  
	  --
	  
	  RedirectAttributes
	  Model 타입과 더불어서 스프링 MVC가 자동으로 전달해 주는 타입 중에서는 RedirectAttribute 타입이 존재한다.
	  RedirectAttribute는 일회성으로 데이터를 전달하는 용도로 사용된다.
	  
	  RedirctAttributes는 Model과 같이 파라미터로 선언해서 사용하고, addFlashAttribute(이름, 값) 메서드를
	  이용해서 화면에 한 번만 사용되고 다음에는 사용되지 않는 데이터를 전달하기 위해 사용한다.
	
	*/
	
	
	
	
	
	
	
	
	
	/*
	 Controller의  return type
	 
	  Controller의 메서드가 사용할 수 있는 리턴 타입은 주로 다음과 같다.
	  
	  - String : jsp를 이용하는 경우에는 jsp 파일의 경로와 파일 이름을 나타내기 위해 사용한다.
	  - void : 호출하는 URL과 동일한 이름의 jsp를 의미한다.
	  - VO, DTO 타입 : 주로 JSON 타입의 데이터를 만들어서 반환하는 용도로 사용한다.
	  - ResponseEntity : response 할 때 Http 헤더 정보와 내용을 가공하는 용도로 사용 
	  - Model, ModelAndView: Model로 데이터를 반환하거나 화면까지 같이 지정하는 경우(근래에는 사용X)
	  - HttpHeaders: 응답에 내용 없이 http 헤더 메시지만 전달하는 용도로 사용
	 
	 
	
	 
	
	 
	 */
	
	/*
	 1.Void 타입
	 
	 메서드의 리턴 타입을 Void로 지정하는 경우 일반적인 경우에는 해당 URL의 경로를 그대로 jsp파일의 이름으로 사용하게 된다.
	*/
	
	@GetMapping("/ex05")  //localhost:8080/sample/ex05를 요청하면 ex05.jsp를 호출하게 된다.
	public void ex05() {
		log.info("/ex05.........");
	}
	
	
	/*
	
	 2.String 타입
	 
	 void 타입과 더불어 가장 많이 사용한다. String 타입은 상황에 따라 다른 화면을 보여줄 필요가 있을 경우에 사용한다.
	 예를 들면 (if ~ else와 같은 처리가 필요한 상황)
	 일반적으로 String 타입은 현재 프로젝트의 경우 JSP 파일의 이름을 의미한다.
	 프로젝트 생성 시 기본으로 만들어진 HomeController의 home메소드를 보면 String을 반환 타입으로 사용한는 것을 볼 수 있다.
	 
	 ' return "home"; '
	 
	 home() 메서드는 'home'이라는 문자열을 리턴했기 때문에 경로는 '/WEB-INF/views/home.jsp' 경로가 된다.
	 
	 String타입에는 다음과 같은 특별한 키워드를 붙여서 사용할 수 있다.
	 
	 - redirect: 리다이렉트 방식으로 처리하는 경우
	 - forward: 포워드 방식으로 처리하는 경우
	 
	*/
	
	
	
	/*

	 3.객체 타입!!
	 
	Controller의 메서드 리턴 타입을 VO(Value Object)나 DTO(Data Transfer Object) 타입 등
	복합적인 데이터가 들어간 객체 타입으로 지정할 수 있는데, 이 경우는 주로 JSON 데이터를 만들어 내는 용도로 사용된다.
	우선 이를 위해서는 jackson-databind 라이브러리를 pom.xml에 추가한다.
	 
	 	<dependency>
        	<groupId>com.fasterxml.jackson.core</groupId>
        	<artifactId>jackson-databind</artifactId>
        	<version>2.9.4</version>
        </dependency>
	  
	 */
	
	@GetMapping("/ex06")
	public @ResponseBody SampleDTO ex06() {
		log.info("/ex06........");
		
		SampleDTO dto = new SampleDTO();
		dto.setAge(10);
		dto.setName("홍길동");
		
		return dto;
	
	/*
	 개발자 도구를 통해서 살펴보면 서버에서 전송하는 MIME(Multipurpose Internet Mail Extensions) 타입을 보면
	   'application/json'으로 처리되는 것을 볼 수 있다.만일 jackson-databind 라이브러리가 포함되지 않았다면
	   500에러를 만나게 된다.
	 */
	}
		
	
	
	
	
	/*
	4.ResponseEntity 타입
	
	웹을 다루다 보면 HTTP 프로토콜의 헤더를 다루는 경우도 종종 있다. 스프링 MVC의 사상은 HttpServletRequest나
	HttpServletResponse를 직접 핸들링하지 않아도 이런 작업이 가능하도록 작성되었기 때문에, 이러한 처리를 위해
	ResponseEntity를 통해 원하는 헤더 정보나 데이터를 전달할 수 있다.
	
	ResponseEntity는 HttpHeaders 객체를 같이 전달할 수 있고, 이를 통해서 원하는 HTTP 헤더 메시지를
	가공하는 것이 가능하다. ex07()의 경우 브라우저에는 JSON 타입이라는 헤더 메시지와 200 OK라는 상태 코드를 전송한다.
	
	*/
	
	@GetMapping("/ex07")
	public ResponseEntity<String> ex07() {
		log.info("/ex07.........");
		
		// {"name" : "홍길동"}
		String msg = "{\"name\": \"홍길동\"}";
		
		HttpHeaders header = new HttpHeaders();
		header.add("Content-Type", "application/json;charset=UTF-8");
		
		return new ResponseEntity<>(msg, header, HttpStatus.OK);
		
	}
	
	
	
	/*
	 파일 업로드 처리!!!
	 
	 Controller의 많은 작업은 스프링MVC를 통해서 처리하기 때문에 개발자는 자신이 해야하는 역할에만 집중해서
	 코드를 작성할 수 있다. 그러나 조금 신경 써야 하는 부분이 있다면 파일을 업로드 하는 부분에 대한 처리일 것이다.
	 파일 업로드를 하기 위해서는 전달되는 파일 데이터를 분석해야 하는데, 이를 위해서 Servlet 3.0 전까지는
	 commons의 파일 업로드를 이용하거나 cos.jar 등을 이용해서 처리를 해 왔다.
	 
	 Servlet 3.0 이후 (Tomcat 7.0)에는 기본적으로 업로드 되는 파일을 처리할 수 있는 기능이 추가되어 있으므로
	 더 이상 추가적인 라이브러리가 필요하지 않다.
	 
	 아쉬운 점은 Spring Legacy Project로 생성되는 프로젝트의 경우 Servlet 2.5를 기준으로 생성되기 때문에
	 3.0 이후에 지원되는 설정을 사용하기 어렵다는 점이다. 우선 여기서는 일반적으로 많이 사용하는
	 commons-fileupload를 이용한다.
	 
	 pom에 라이브러리 추가
	 <dependency>
        	<groupId>commons-fileupload</groupId>
        	<artifactId>commons-fileupload</artifactId>
        	<version>1.3.3</version>
        </dependency>
	 
	 */
	
	@GetMapping("/exUpload")
	public void exUpload() {
		log.info("/exUpload.....");
		
	//	http://localhost:8080/sample/exUpload
	}
	
	@PostMapping("/exUploadPost")
	public void exUploadPost(ArrayList<MultipartFile> files) {
		
		files.forEach(file -> {
			log.info("--------------------------------------");
			log.info("name: " + file.getOriginalFilename());
			log.info("size: " + file.getSize());
			
			/*
			INFO : org.zerock.controller.SampleController - --------------------------------------
			INFO : org.zerock.controller.SampleController - name: 스프링.txt
			INFO : org.zerock.controller.SampleController - size: 562
			INFO : org.zerock.controller.SampleController - --------------------------------------
			INFO : org.zerock.controller.SampleController - name: centos서버구축 - 복사본.txt
			INFO : org.zerock.controller.SampleController - size: 7619
			INFO : org.zerock.controller.SampleController - --------------------------------------
			INFO : org.zerock.controller.SampleController - name: schema.sql
			INFO : org.zerock.controller.SampleController - size: 1214
			INFO : org.zerock.controller.SampleController - --------------------------------------
			INFO : org.zerock.controller.SampleController - name: 
			INFO : org.zerock.controller.SampleController - size: 0
			INFO : org.zerock.controller.SampleController - --------------------------------------
			INFO : org.zerock.controller.SampleController - name: 
			INFO : org.zerock.controller.SampleController - size: 0
			  
			 */
			
		});
	}
	
	
	
	
	
	
	
	
	
}
