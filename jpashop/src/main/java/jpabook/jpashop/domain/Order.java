package jpabook.jpashop.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;
@Entity
@Table(name = "orders") //없으면 관례로 Order이 되어버림
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
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

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL) //OrderItem의 order에 의한 매핑(조회용)
    //기본이 LAZY
    private List<OrderItem> orderItems = new ArrayList<>();
    //entity의 기본 persist는 각자 이루어짐
    //persist(orderItemA)
    //persist(orderItemB)
    //.. persist(order)을
    //함께 해주고, delete 시에도 함께 지워줌


    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "delivery_id")
    private Delivery delivery; //마찬가지로 fk


    private LocalDateTime orderDate; //주문시간
    //hibernate의 지원에 따라 Date date 선언 생략

    @Enumerated(EnumType.STRING)
    private OrderStatus status; //주문상태 : order, cancel

    //연관관계 편의 메서드 : 양방향인 경우의 세팅
    public void setMember(Member member){
        this.member = member;
        member.getOrders().add(this);
    }

    public void addOrderItem(OrderItem orderItem){
        orderItems.add(orderItem);
        orderItem.setOrder(this);
    }

    public void setDelivery(Delivery delivery){
        this.delivery = delivery;
        delivery.setOrder(this);

    }
    public static void main(String[] args){
        Member member = new Member();
        Order order = new Order();

        order.setMember(member);
    }
    /*
    public void setMember(Member member){
        this.member = member;
    }
    public static void main(String[] args){
        Member member = new Member();
        Order order = new Order();
        member.getOrders().add(order);
        order.setMmeber(member);
    }

     */

    public static Order createOrder(Member member, Delivery delivery, OrderItem... orderItems){
        Order order = new Order();
        order.setMember(member);
        order.setDelivery(delivery);
        for (OrderItem orderItem : orderItems) {
            order.addOrderItem(orderItem);
        }
        order.setStatus(OrderStatus.ORDER);
        order.setOrderDate(LocalDateTime.now());
        return order;
    }

    //비즈니스 로직
    //주문 취소
    public void cancel(){
        if (delivery.getStatus() == DeliveryStatus.COMP){
            throw new IllegalStateException("이미 배송완료된 상품은 취소가 불가능합니다");
        }
        this.setStatus(OrderStatus.CANCEL);
        for (OrderItem orderItem : orderItems){
            //this.orderItems에서 this 생략
            //문법 로직을 보면서 강조하거나 이름이 같은 경우
            orderItem.cancel();
        }
    }

    //조회 로직
    //전체 주문 가격 조회
    public int getTotalPrice() {
        int totalPrice = 0;
        for (OrderItem orderItem : orderItems) {
            totalPrice += orderItem.getTotalPrice();
        }
        return totalPrice;
    }

}
