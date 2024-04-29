package jpabook.jpashop.api;

import jakarta.validation.Valid;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.service.MemberService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

//package의 분리 - template engine과 api용
//공통 예외 처리와 같은 부분에 대한 차이점
//app을 만드는 경우 api 통신을 위한 컨트롤러
//@RestController = @ResponseBody(data를 바로 json으로 보냄) + @Controller
@RestController @RequiredArgsConstructor
public class MemberApiController {
    private final MemberService memberService;

    //회원 등록하는 api
    @PostMapping("/api/v1/members")
    public CreateMemberResponse saveMemberV1(@RequestBody @Valid Member member){
        //@RequestBody : json으로 온 body를 member로 바꿈(mapping 함)
        //id는 자동 생성되므로 name, address만 있으면 됨
        //entity에 대한 제약조건이 없는 경우 null 값도 member 안에 들어가버림
        //Member entity안에서 @NotEmpty 설정 가능

        //entity와 api가 분리되지 않는 문제
        //다른 api의 경우에는 필요없는 속성일 수 있음
        //entity에 화면 validation logic이 들어감

        Long id = memberService.join(member);
        return new CreateMemberResponse(id);
    }


    @PostMapping("/api/v2/members")
    public CreateMemberResponse saveMemberV2(@RequestBody @Valid CreateMemberRequest request){
        //데이터 전송을 위한 별도의 객체 활용 : DTO 생성
        
        Member member = new Member();
        member.setName(request.getName());

        Long id = memberService.join(member);
        return new CreateMemberResponse(id);
    }

    @Data
    static class CreateMemberRequest{
        private String name;
    }

    @Data
    static class CreateMemberResponse{
        private Long id;

        public CreateMemberResponse(Long id){
            this.id = id;
        }
    }




}
