package jpabook.jpashop.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

//상속관계 전략의 지정

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
//모든 내용을 한 테이블로
@DiscriminatorColumn(name = "dtype")
//@DiscriminatorValue("M")과 같이 활용
@Getter @Setter
//기본적으로 Getter은 조회용 -> 열어두고 Setter은 안두는 편
//변경용 비즈니스 메서드를 제공해야함 -> 변경 지점이 명확하도록
public abstract class Item {

    //실시간 트래픽 <-> pk 사용(정확성)
    @Id @GeneratedValue
    @Column(name = "item_id")
    //객체의 경우는 객체 타입과 id를 통해 구별 가능하지만
    //테이블의 경우에는 좀 더 명확한 이름이 필요
    private Long id;

    private String name;

    private int price;

    private int stockQuantity;

    @ManyToMany(mappedBy = "items")
    private List<Category> categories = new ArrayList<>();
}
