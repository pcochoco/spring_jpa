package jpabook.jpashop.domain;

import jakarta.persistence.Embeddable;
import lombok.Getter;

@Getter //수정할 수 없음
@Embeddable
public class Address {
    private String city;
    private String street;
    private String zipcode;
}
