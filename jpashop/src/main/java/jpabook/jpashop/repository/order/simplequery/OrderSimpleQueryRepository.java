package jpabook.jpashop.repository.order.simplequery;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
//ordersV4의 전용 repository
public class OrderSimpleQueryRepository {
    private final EntityManager em;
    public List<OrderSimpleQueryDto> findOrderDtos(){
        return em.createQuery(
                //v3와 달리 직접 select 으로 지정 => 직접 원하는 데이터를 선택 : 애플리케이션 네트워크 용량 최적화(미비)
                "select new jpabook.jpashop.repository.order.simplequery.OrderSimpleQueryDto(o.id, m.name, o.orderDate, o.status, d.address)"
                + "from Order o"+
                "join o.member m" + "join o.delivery d", OrderSimpleQueryDto.class)
                .getResultList();

    }
}
