package jpabook.jpashop.api;

import jpabook.jpashop.domain.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

//일대다 관계인 컬렉션 - order 기준으로 orderItems와 items
@RestController
@RequiredArgsConstructor
public class OrderApiController {
    private final OrderRepository orderRepository;


}
