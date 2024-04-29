package jpabook.jpashop.domain.service;


import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
@Service
@Transactional(readOnly = true) //트랜잭션 안에서 데이터 변경
//springframework의 Transactional을 import해야 readOnly 사용 가능

@RequiredArgsConstructor//final field만 가지고 생성
//생성자 주입 활용

public class MemberService {
    @Autowired
    private final MemberRepository memberRepository;

    //가입
    @Transactional
    public Long join(Member member){
        validateDuplicateMember(member);
       memberRepository.save(member);
       return member.getId();
    }
    //db insert를 둘이 동시에 접근하면 같은 이름으로 등록가능



    private void validateDuplicateMember(Member member){
        //중복에 대한 예외처리
        List<Member> findMembers = memberRepository.findByName(member.getName());
        if(!findMembers.isEmpty()){
            throw new IllegalStateException("이미 존재하는 회원입니다");
        }
    }


    //회원 전체 조회
    //@Transactional(readOnly = true)
    //db에 리소스를 너무 많이 쓰지 않고 실행하도록 해줌
    //읽기에는 가급적인 readOnly = true
    //데이터 변경이 안됨
    public List<Member> findMembers(){
        return memberRepository.findAll();
    }

    public Member findOne(Long memberId){
        return memberRepository.findOne(memberId);
    }

    @Transactional
    //이 함수에서 Member을 return하게 되면 쿼리와 업데이트를 분리하지 않게 됨
    //엔티티를 바꾸는 기능 + 조회 기능을 함께 가지게 됨
    public void update(Long id, String name) {
        Member member = memberRepository.findOne(id);
        member.setName(name);
    }
}
