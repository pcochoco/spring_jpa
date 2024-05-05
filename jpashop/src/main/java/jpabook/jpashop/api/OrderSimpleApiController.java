package jpabook.jpashop.api;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.domain.repository.OrderRepository;
import jpabook.jpashop.domain.repository.OrderSearch;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

import static java.util.stream.Collectors.toList;

//주문, 배송정보, 회원 조회
//지연로딩과 관련한 조회 성능 최적화
@RestController
@RequiredArgsConstructor
public class OrderSimpleApiController {
    private final OrderRepository orderRepository;


    //eager이 아닌 lazy loading을 사용함에 따라 조회 기능을 개발할 때 생기는 문제점
    //eager의 경우 관련 객체를 모두 끌어오고 lazy는 해당 객체가 필요한 시점까지 끌어오기를 지연함
    //Order은 Member, Delivery와 XToOne관계 => LAZY
    //default = lazy, 필요 시 fetch join 사용
    //엔티티 노출 시 양방향 관계에 걸리는 경우 모두 JsonIgnore을 해줘야함
    //실제로 db에서 값을 가져오지 않음 -> 조회한 주체가 되는 개체(order) 이외 객체를 가져오지 않는 것
    //jackson library는 proxy에 대해 알지 못함

    //hibernate이 proxy 활용 (가짜 객체를 넣어놓고 객체에 실제로 손을 댈때 db에 쿼리를 날려 값을 가져옴)

    //직접 엔티티를 활용하는 조회
    @GetMapping("/api/v1/simple-orders")//사용하지 말 것
    public List<Order> ordersV1(){
        List<Order> all = orderRepository.findAllByString(new OrderSearch());
        for(Order order : all){
            //LAZY 강제 초기화
            order.getMember().getName(); //get method으로 LAZY의 강제 초기화
            order.getDelivery().getAddress(); //마찬가지
        }
        return all;
    }

    //직접 엔티티를 조회하지 않는 방법 : dto를 둠
    //orderRepostiory에서 가져온 list -> SimpleOrderDto의 list로 옮겨주어
    //map으로 개별적인 원소들을 불러와 출력

    //N + 1의 성능 문제 (조회 수가 많아질 수록 쿼리 수도 많아짐)
    //N : Member, N : Delivery, 1 : Order
    @GetMapping("/api/v2/simple-orders")
    public List<SimpleOrderDto> ordersV2(){
        List<Order> orders = orderRepository.findAllByString(new OrderSearch());
        List<SimpleOrderDto> result = orders.stream()
                .map(o -> new SimpleOrderDto(o))
                .collect(toList());
        return result;
    }

    //dto - 변수 선언 + 생성자의 변수 초기화
    @Data
    static class SimpleOrderDto{
        private Long orderId;
        private String name;
        private LocalDateTime orderDate;
        private OrderStatus orderStatus;
        private Address address;

        public SimpleOrderDto(Order order) { //dto에서는 entity를 받아도 됨(상대적으로 덜 중요한 부분)
            orderId = order.getId();
            name = order.getMember().getName(); //LAZY 초기화
            orderDate = order.getOrderDate();
            orderStatus = order.getStatus();
            address = order.getDelivery().getAddress();
        }
    }

    //엔티티를 dto로 변환 + fetch join 활용



}
