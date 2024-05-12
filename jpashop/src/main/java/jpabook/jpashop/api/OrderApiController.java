package jpabook.jpashop.api;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderItem;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.domain.repository.OrderRepository;
import jpabook.jpashop.domain.repository.OrderSearch;
import jpabook.jpashop.repository.order.query.OrderQueryDto;
import jpabook.jpashop.repository.order.query.OrderQueryRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

import static java.util.stream.Collectors.toList;

//일대다 관계인 컬렉션 - order 기준으로 orderItems와 items
@RestController
@RequiredArgsConstructor
public class OrderApiController {
    private final OrderRepository orderRepository;

    //엔티티 직접 노출 -> api와 entity 간 유연성 떨어짐
    //hibernate 활용 entity json으로 생성 + 양방향 관계에 대해 한쪽에 JsonIgnore 추가
    //order 1, member address orderItem item N번 => 너무 많은 sql 문 실행
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

    //entity -> dto(order dto, orderItem dto)
    /*
      지연로딩은 영속성 컨텍스트에 있으면 있는 엔티티를 사용하고 없으면 sql을 실행
      -> 같은 영속성 컨텍스트에서 이미 로딩한 회원 엔티티를 추가로 조회하면 sql을 실행하지 않음

     */
    public List<OrderDto> ordersV2(){
        List<Order> orders = orderRepository.findAllByString(new OrderSearch());
        List<OrderDto> result = orders.stream()
                .map(o -> new OrderDto(o))
                .collect(toList());
        return result;
    }

    @Data
    static class OrderDto{
        private Long orderId;
        private String name;
        private LocalDateTime orderDate;
        private OrderStatus orderStatus;
        private Address address;
        private List<OrderItemDto> orderItems;
        public OrderDto(Order order){
            orderId = order.getId();
            name = order.getMember().getName();
            orderDate = order.getOrderDate();
            orderStatus = order.getStatus();
            orderItems = order.getOrderItems().stream()
                    .map(orderItem -> new OrderItemDto(orderItem))
                    .collect(toList());
        }
    }

    @Data
    static class OrderItemDto{
        private String itemName;
        private int orderPrice;
        private int count;

        public OrderItemDto(OrderItem orderItem){
            itemName = orderItem.getItem().getName();
            orderPrice = orderItem.getOrderPrice();
            count = orderItem.getCount();
        }
    }

    //entity -> dto + fetch join
    @GetMapping("/api/v3/orders")

    public List<OrderDto> ordersV3() {
        List<Order> orders = orderRepository.findAllWithItem();
        List<OrderDto> result = orders.stream()
                .map(o -> new OrderDto(o))
                .collect(toList());
        return result;
    }
    /*
      fetch join으로 sql 1번 실행, distinct로 중복 거름
      컬렉션 페치 조인은 1개만 사용 가능

      [문제] 페이징 불가
      일대다 조인이 발생하면 데이터가 예측할 수 없이 증가
      -> 일대다에서 일을 기준으로 페이징하는 것이 목적(데이터는 다를 기준으로 생성)
        order이 아닌 orderItem이 기준이 되는 것
      -> 하이버네이트가 경고로그를 남기고 모든 데이터를 db에서 읽어와 페이징

      [한계 돌파] 페이징 + 컬렉션 엔티티 함께 조회
      - ToOne 관계를 모두 fetch join : row 수를 증가시키지 않으므로 영향 안줌
      - 컬렉션은 지연 로딩으로 조회
        hibernate.default_batch_fetch_size(global), @BatchSize(for individual entity) 적용
        -> 설정한 size만큼 in 쿼리로 조회
     */

    //paging + collection 조회
    //1 + N -> 1 + 1 으로 쿼리 호출 수 감소
    @GetMapping("/api/v3.1/orders")
    public List<OrderDto> ordersV3_page(@RequestParam(value = "offset", defaultValue = "0") int offset,
                                        @RequestParam(value = "limit", defaultValue = "100") int limit){
        List<Order> orders = orderRepository.findAllWithMemberDelivery(offset, limit);
        List<OrderDto> result = orders.stream()
                .map(o -> new OrderDto(o))
                .collect(toList());
        return result;
    }

    private final OrderQueryRepository orderQueryRepository;
    @GetMapping("/api/v4/orders")
    public List<OrderQueryDto> ordersV4(){
        return orderQueryRepository.findOrderQueryDtos();

    }

    //jpa dto 직접 조회 : 쿼리 2번 호출
    @GetMapping("/api/v5/orders")
    public List<OrderQueryDto> ordersV5(){
        return orderQueryRepository.findAllByDto_optimization();
    }


}
