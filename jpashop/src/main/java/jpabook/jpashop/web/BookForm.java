package jpabook.jpashop.web;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
//데이터를 입력받는 폼을 나타냄
//데이터 전달과 유효성 검사를 위한 용도
//DTO : 웹 계층에서만 사용
public class BookForm {
    private Long id;
    private String name;
    private int price;
    private int stockQuantity; //for measuring left overs
    private String author;
    private String isbn;

}
