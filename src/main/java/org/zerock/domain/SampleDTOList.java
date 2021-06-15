package org.zerock.domain;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

/*
 * 객체 리스트
 * 
 * 만일 전달하는 데이터가 SampleDTO와 같이 객체 타입이고 여러 개를 처리해야 한다면,
 * 예를 들어 SampleDTO를 여러 개 전달 받아서 처리하고 싶다면 아래와 같이
 * 
 * SampleDTO의 리스트를 포함하는 SampleDTOList 클래스를 설계한다.
 * */


@Data
public class SampleDTOList {
	private List<SampleDTO> list;
	
	public SampleDTOList() {
		list = new ArrayList<>();
	}

}
