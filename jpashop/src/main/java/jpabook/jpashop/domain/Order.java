package jpabook.jpashop.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;
@Entity
@Table(name = "orders") //없으면 관례로 Order이 되어버림
@Getter @Setter
public class Order {
    @Id @GeneratedValue
    @Column(name = "order_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)//Order과 Member은 다대 일 관계
    //fk : 주문한 회원에 대한 매핑
    //연관관계의 주인이 되는 쪽 = 값을 변경하는 쪽
    //연관된 쿼리는 함께 fetch하지 않음
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "order") //OrderItem의 order에 의한 매핑(조회용)
    //기본이 LAZY
    private List<OrderItem> orderItems = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "delivery_id")
    private Delivery delivery; //마찬가지로 fk


    private LocalDateTime orderDate; //주문시간
    //hibernate의 지원에 따라 Date date 선언 생략

    private OrderStatus status; //주문상태 : order, cancel

}
