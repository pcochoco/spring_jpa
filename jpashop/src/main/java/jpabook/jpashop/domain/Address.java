package jpabook.jpashop.domain;

import jakarta.persistence.Embeddable;
import lombok.Getter;

@Getter //수정할 수 없음
//값 자체는 변경되지 않아야하므로
@Embeddable
public class Address {
    private String city;
    private String street;
    private String zipcode;

    //jpa가 기본적으로 사용하는 기술에 따라 기본 생성자 필요
    protected Address(){
    }

    public Address(String city, String street, String zipcode){
        this.city = city;
        this.street = street;
        this.zipcode = zipcode;
    }
}
