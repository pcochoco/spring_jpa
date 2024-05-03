package jpabook.jpashop.api;

import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.repository.OrderRepository;
import jpabook.jpashop.domain.repository.OrderSearch;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

//주문, 배송정보, 회원 조회
//XToOne 관계
//hibernate 사용 -> dto를 사용하는 게 더 적절
@RestController
@RequiredArgsConstructor
public class OrderSimpleApiController {
    private final OrderRepository orderRepository;

    //양방향 관계에 걸리는 컬럼은 모두 JsonIgnore을 해줘야함
    //fetch type이 LAZY인 경우
    //db에서 값을 가져오지 않음
    //hibernate이 proxy 활용 (가짜 객체를 넣어놓고 객체에 실제로 손을 댈때 db에 쿼리를 날려 값을 가져옴)
    @GetMapping("/api/v1/simple-orders")
    public List<Order> ordersV1(){
        List<Order> all = orderRepository.findAllByString(new OrderSearch());
        for(Order order : all){
            //LAZY 강제 초기화
            order.getMember().getName();
            order.getDelivery().getAddress();
        }
        return all;
    }


}
