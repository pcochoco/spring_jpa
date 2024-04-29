package jpabook.jpashop.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Delivery {
    @Id @GeneratedValue
    @Column(name = "delivery_id")
    private Long id;

    @OneToOne(mappedBy = "delivery", fetch = FetchType.LAZY)
    private Order order;

    @Embedded //@Embeddable한 address를 쓰는 것
    private Address address;

    @Enumerated(EnumType.STRING)
    //default는 ORDINAL인데 값을 추가(변경)할 때 같이 변경되는 문제
    private DeliveryStatus status; //READY, COMP

}
