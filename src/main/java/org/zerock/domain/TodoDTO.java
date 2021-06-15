package org.zerock.domain;

import java.util.Date;

import lombok.Data;

@Data
public class TodoDTO {

	private String title;
	private Date dueDate;
	
	//dueDate 변수의 타입이 java.util.Date 타입이다. 만약 사용자가 '2018-01-01"과 같이 들어오는 데이터를 변환하고자 할 떄
	// 문제가 발생하게 된다. 이러한 문제의 간단한 해결책은 @InitBinder를 이용하는 것이다.
	
}
