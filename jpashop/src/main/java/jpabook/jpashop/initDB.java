package jpabook.jpashop;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jpabook.jpashop.domain.*;
import jpabook.jpashop.domain.item.Book;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

//api 활용을 위한 data setting
@Component //spring container에 등록 후 component scan
@RequiredArgsConstructor //final, non null 변수에 대한 생성자 주입
public class initDB {
    private final InitService initService;

    @PostConstruct
    public void init(){ //spring life cycle 때문에 모든 코드를 이 안으로 적을 수는 없음
        initService.dbInit1();
        initService.dbInit2();
    }

    @Component
    @Transactional
    @RequiredArgsConstructor
    static class InitService{
        private final EntityManager em;

        public void dbInit1(){
            //기능의 중복에 따라 em 활용, 객체 create 함수로 분리 git
            Member member = createMember("userA","seoul","1","1111");
            em.persist(member);

            Book book1 = createBook("jpa1",10000,1000);
            em.persist(book1);

            Book book2 = createBook("jpa2", 20000, 100);
            em.persist(book2);

            OrderItem orderItem1 = OrderItem.createOrderItem(book1, 10000, 1000);
            OrderItem orderItem2 = OrderItem.createOrderItem(book2, 20000, 100);
            Order order = Order.createOrder(member, createDelivery(member), orderItem1, orderItem2);
            em.persist(order);
        }

        public void dbInit2(){
            Member member = createMember("userB", "uijeongbu", "2", "2222");
            em.persist(member);

            Book book1 = createBook("spring1", 10000, 1000);
            em.persist(book1);

            Book book2 = createBook("spring2", 20000, 100);
            em.persist(book2);

            OrderItem orderItem1 = OrderItem.createOrderItem(book1, 10000, 1000);
            OrderItem orderItem2 = OrderItem.createOrderItem(book2, 20000, 100);
            Order order = Order.createOrder(member, createDelivery(member), orderItem1, orderItem2);
            em.persist(order);
        }

        private Member createMember(String name, String city, String street, String zipcode){
            Member member = new Member();
            member.setName(name);
            member.setAddress(new Address(city, street, zipcode)); //주소 자체가 Embeddable class이므로 생성
            return member;
        }

        private Book createBook(String name, int price, int stockQuantity){
            Book book = new Book();
            book.setName(name);
            book.setPrice(price);
            book.setStockQuantity(stockQuantity);
            return book;
        }

        private Delivery createDelivery(Member member){
            Delivery delivery = new Delivery();
            delivery.setAddress(member.getAddress());
            return delivery;
        }
    }



}
