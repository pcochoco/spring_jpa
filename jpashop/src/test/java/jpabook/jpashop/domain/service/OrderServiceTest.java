package jpabook.jpashop.domain.service;

import jakarta.persistence.EntityManager;
import jpabook.jpashop.Exception.NotEnoughNewStockException;
import jpabook.jpashop.domain.*;
import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.repository.OrderRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class OrderServiceTest {

    @Autowired EntityManager em;
    @Autowired OrderService orderService;
    @Autowired OrderRepository orderRepository;

    @Test
    public void 상품주문() throws Exception{
        //given
        Member member = new Member();
        member.setName("member1");
        member.setAddress(new Address("seoul", "street", "011"));
        em.persist(member);

        Book book = new Book();
        book.setName("jpa");
        book.setPrice(10000);
        book.setStockQuantity(10);
        em.persist(book);

        int orderCount = 2;

        //when
        Long orderId = orderService.order(member.getId(), book.getId(), orderCount);

        //then
        Order getOrder = orderRepository.findOne(orderId);

        assertEquals("ORDER status", OrderStatus.ORDER, getOrder.getStatus());
        assertEquals("주문 상품 종류 수", 1, getOrder.getOrderItems().size());
        assertEquals("주문 가격", 10000*orderCount, getOrder.getTotalPrice());
        assertEquals("재고", 8, book.getStockQuantity());
    }

    @Test(expected = NotEnoughNewStockException.class)
    public void notEnoughStock() throws Exception{
        Member member = createMember();
        Item item = createBook();

        int orderCount = 1;

        orderService.order(member.getId(), item.getId(), orderCount);

        fail("not enough stock");

    }

    @Test
    public void order_cancel() throws Exception{
        Member member = createMember();
        Book item = createBook();

        int orderCount = 2;
        Long orderId = orderService.order(member.getId(), item.getId(), orderCount);

        orderService.cancelOrder(orderId);

        Order getOrder = orderRepository.findOne(orderId);

        assertEquals("CANCEL", OrderStatus.CANCEL, getOrder.getStatus());
        assertEquals("increase by cancel", 10, item.getStockQuantity());
    }

    //기본 데이터 셋
    private Book createBook(){
        Book book = new Book();
        book.setName("jpa");
        book.setPrice(10000);
        book.setStockQuantity(10);
        em.persist(book);
        return book;
    }

    private Member createMember(){
        Member member = new Member();
        member.setName("member1");
        member.setAddress(new Address("seoul", "street", "123"));
        em.persist(member);
        return member;
    }
}
