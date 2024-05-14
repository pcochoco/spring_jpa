package jpabook.jpashop.domain.repository;

import jpabook.jpashop.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/*spring data jpa 적용 예시
JpaRepository interface를 통해 crud 기능 제공
구현체는 spring data jpa application 실행 시점에 주입
해당 method의 ex) select m from Member m where m.name = :name

 */
public interface JpaMemberRepository extends JpaRepository<Member, Long> {
    List<Member> findByName(String name);
}
