package jpabook.jpashop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//http://127.0.0.0:8080
//localhost:8080

//build.gradle에 devtools -> recompile을 통해 창 새로고침만으로 변화를 볼 수 있음(다시 빌드 안하는 방법)
@SpringBootApplication
public class JpashopApplication {

	public static void main(String[] args) {

		//lombok 활용 예시
		Hello hello = new Hello();
		hello.setDate("hello");
		String data = hello.getDate();

		SpringApplication.run(JpashopApplication.class, args);
	}

}
