package jpabook.jpashop.repository.order.query;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class OrderQueryRepository {
    private final EntityManager em;
    //V4 method에서 사용
    public List<OrderQueryDto> findOrderQueryDtos(){
        //ToOne 코드 모두 한번에 조회
        List<OrderQueryDto> result = findOrders();

        //루프를 돌면서 컬렉션 추가(추가 쿼리 실행)
        result.forEach(o -> {
            List<OrderItemQueryDto> orderItems = findOrderItems(o.getOrderId());
            o.setOrderItems(orderItems);
        });
        return result;
    }

    //1 : N 관계 제외 조회 = ToOne 관계 -> join해도 data row 수 증가 x
    private List<OrderQueryDto> findOrders(){
        return em.createQuery(
                "select new jpabook.jpashop.repository.order.query.OrderQueryDto(o.id, m.name, o.orderDate, o.status, d.address)" +
                        "from Order o" +
                        "join o.member m" +
                        "join o.delivery d", OrderQueryDto.class)
                .getResultList();
    }

    //1 : N 관계 조회 = ToMany 관계 -> join하면 row 수 증가
    private List<OrderItemQueryDto> findOrderItems(Long orderId){
        return em.createQuery(
                "select new jpabook.jpashop.repository.order.query.OrderQueryDto(o.id, m.name, o.orderDate, o.status, d.address" +
                        "from OrderItem oi" +
                        "join oi.item i" +
                        "where oi.order.id = : orderId", OrderItemQueryDto.class)
                .setParameter("orderId", orderId)
                .getResultList();
    }

    //V5 method에서 사용 -> map 사용 성능 향상 O(1)
    public List<OrderQueryDto> findAllByDto_optimization() {
        List<OrderQueryDto> result = findOrders(); //ToOne 관계는 한번에 조회

        //orderItem collection을 map 한방에 조회
        Map<Long, List<OrderItemQueryDto>> orderItemMap = findOrderItemMap(toOrderIds(result));

        //루프를 돌면서 collection 추가
        result.forEach(o -> o.setOrderItems(orderItemMap.get(o.getOrderId())));
        //findOrders로 order을 찾은 것처럼 result에 orderItem의 정보도 넣는 것

        return result;
    }

    private Map<Long, List<OrderItemQueryDto>> findOrderItemMap(List<Long> orderIds) {
        List<OrderItemQueryDto> orderItems = em.createQuery(
                "select new jpabook.jpashop.repository.order.query.OrderItemQueryDto(oi.order.id, i.name, oi.orderPrice, oi.count" +
                        "from OrderItem oi" +
                        "join oi.item i" +
                        "where oi.order.id in orderIds", OrderItemQueryDto.class) //in query 사용
                .setParameter("orderIds",orderIds)
                .getResultList();
        return orderItems.stream()
                .collect(Collectors.groupingBy(OrderItemQueryDto::getOrderId));
    }

    private List<Long> toOrderIds(List<OrderQueryDto> result) {
        return result.stream()
                .map(o -> o.getOrderId())
                .collect(Collectors.toList());
    }


}
