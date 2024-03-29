package jpabook.jpashop.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

//상속관계 전략의 지정

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
//모든 내용을 한 테이블로
@DiscriminatorColumn(name = "dtype")
@Getter @Setter
public abstract class Item {

    @Id @GeneratedValue
    @Column(name = "item_id")
    private Long id;

    private String name;

    private int price;

    private int stockQuantity;

    private List<Cateogries> categories = new ArrayList<>();
}
