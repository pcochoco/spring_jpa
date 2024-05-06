package jpabook.jpashop.api;

import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderItem;
import jpabook.jpashop.domain.repository.OrderRepository;
import jpabook.jpashop.domain.repository.OrderSearch;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

//일대다 관계인 컬렉션 - order 기준으로 orderItems와 items
@RestController
@RequiredArgsConstructor
public class OrderApiController {
    private final OrderRepository orderRepository;

    //엔티티 직접 노출 -> api와 entity 간 유연성 떨어짐
    //hibernate 활용 entity json으로 생성 + 양방향 관계에 대해 한쪽에 JsonIgnore 추가
    public List<Order> ordersV1(){
        List<Order> all = orderRepository.findAllByString(new OrderSearch());
        for (Order order : all){
            order.getMember().getName(); //LAZY 강제 초기화
            order.getDelivery().getAddress();
            List<OrderItem> orderItems = order.getOrderItems();
            orderItems.stream().forEach(o -> o.getItem().getName()); //LAZY 강제 초기화
        }
        return all;
    }


}
