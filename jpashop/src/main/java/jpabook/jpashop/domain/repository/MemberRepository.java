package jpabook.jpashop.domain.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jpabook.jpashop.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository //spring bean으로 자동 관리
@RequiredArgsConstructor
public class MemberRepository {
    @PersistenceContext //@Autowired도 jpa에서 가능
    private final EntityManager em;

    public void save(Member member){
        em.persist(member);
    }
    public Member findOne(Long id){//Member을 찾아 반환
        return em.find(Member.class, id); //type, pk
    }

    public List<Member> findAll() {
        return em.createQuery("select m from Member m", Member.class)
                .getResultList();
    }

    public List<Member> findByName(String name){
        return em.createQuery("select m from Member m where m.name = :name",Member.class)
                .setParameter("name", name)
                .getResultList();
    }
}

