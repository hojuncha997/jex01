//servlet-context.xml 대신 사용하는 클래스이다.


package org.zerock.config;

import java.io.IOException;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.io.FileSystemResource;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

@EnableWebMvc
@ComponentScan(basePackages= {"org.zerock.controller", "org.zerock.exception"})
public class ServletConfig implements WebMvcConfigurer {
	
	@Override
	public void configureViewResolvers(ViewResolverRegistry registry) {
		
		InternalResourceViewResolver bean = new InternalResourceViewResolver();
			bean.setViewClass(JstlView.class);
			bean.setPrefix("/WEB-INF/views/");
			bean.setSuffix(".jsp");
			registry.viewResolver(bean);
	}
	
	
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/resources/**").addResourceLocations("/resources");
	}
	
	
	@Bean(name = "multipartResolver")
	public CommonsMultipartResolver getResolver() throws IOException {
		CommonsMultipartResolver resolver = new CommonsMultipartResolver();
		
		// 10MB (MaxUploadSize = 한 번의 Request로 전달될 수 있는 최대의 크기)
		resolver.setMaxUploadSize(1024 * 1024 * 10);
		
		// 2MB (MaxUploadSizePerFile = 하나의 파일이 가질 수 있는 최대의 크기)
		resolver.setMaxUploadSizePerFile(1024 * 1024 * 2);
		
		//1MB (MaxInMemorySize = 메모리상에서 유지하는 최대의 크기. 이 크기 이상의 데이터는 uploadTempDir에 임시 파일 형태로 보관됨)
		resolver.setMaxInMemorySize(1024 * 1024);
		
		//temp upload (MaxInMemorySize의 크기를 넘어섰을 때 보관.)
		resolver.setUploadTempDir(new FileSystemResource("C:\\upload\\tmp"));
		
		// defaultEncoding은 업로드 하는 파일의 이름이 한글일 경우 깨지는 문제를 처리
		resolver.setDefaultEncoding("UTF-8");
		
		return resolver;
		
		
	}
	
}










