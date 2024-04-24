package jpabook.jpashop.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Member {
    @Id @GeneratedValue //pk 자동 생성
    @Column(name = "member_id")
    private Long id;

    //@NotEmpty //api data 보낼 때를 위한 용도
    //depends on whether api needs the annotation or not
    //entity를 통해 api 스펙이 변경될 수 있는 문제점
    //DTO for api needed
    private String name;

    @Embedded
    private Address address;

    @OneToMany(mappedBy = "member")
    //order field의 member
    //mappedBy : 매핑된 거울(조회용)
    private List<Order> orders = new ArrayList<>();

    /*밑의 코드보다 위를 권장
    필드 내에서 컬렉션 초기화
    public Member(){
        orders = new ArrayList<>();
    }
    hibernate이 entity를 persist하는 순간
    내장 컬렉션으로 변경됨에 따라 최대한 변경이 없도록 해야함

     */
}
