package jpabook.jpashop.api;

import jakarta.validation.Valid;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.service.MemberService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

//package의 분리 - template engine과 api용
//공통 예외 처리와 같은 부분에 대한 차이점
//app을 만드는 경우 api 통신을 위한 컨트롤러
//@RestController = @ResponseBody(data를 바로 json으로 보냄) + @Controller
@RestController @RequiredArgsConstructor
public class MemberApiController {
    private final MemberService memberService;

    //Member 조회 api
    //entity를 직접 노출하게 되면 entity 자체 정보 또한 외부로 공유되는 문제점
    //entity에서 무시할 정보를 @JsonIgnore로 빼줄 수 있음
    //PostMapping 함수와 마찬가지로 api와 entity의 분리 필요 문제 발생
    //array 형태로 반환 -> 다른 추가 정보를 넣고자 할 시 json 형식 파괴해야함 : 유연성 개선 필요
    @GetMapping("/api/v1/members")
    public List<Member> membersV1(){
        return memberService.findMembers();
    }

    //data 배열 안의 값
    //entity 직접 노출 없이 api 스펙에 맞는 dto를 만들어 사용할 것
    @GetMapping("/api/v2/members")
    public Result memberV2(){
        List<Member> findMembers = memberService.findMembers();
        List<MemberDto> collect = findMembers.stream()
                .map(m -> new MemberDto(m.getName()))
                .collect(Collectors.toList());
        return new Result(collect.size(), collect); //data 개수, data 함께 반환 가능
    }

    @Data
    @AllArgsConstructor
    static class Result<T>{
        private int count;
        private T data;
    }

    @Data
    @AllArgsConstructor
    static class MemberDto{
        private String name;
    }

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

    //Member 값 수정용 -> put 활용
    //postman put send -> updateMemberV2 -> update : entity 변경, transaction 끝나고 commit되는 시점에서 jpa의 변경감지 실행
    //update 완료 후 transaction 끝남 -> 정상 작동 이후의 쿼리를 가져와 response로 담음
    @PutMapping("/api/v2/members/{id}")
    public UpdateMemberResponse updateMemberV2(
            @PathVariable("id") Long id,
            @RequestBody @Valid UpdateMemberRequest request){

        memberService.update(id, request.getName());
        Member findMember = memberService.findOne(id);
        return new UpdateMemberResponse(findMember.getId(), findMember.getName());
    }

    @Data
    static class UpdateMemberRequest{
        String name;
    }

    @Data
    @AllArgsConstructor
    //바깥에서 따로 쓸 클래스는 아니기 때문에 controller 안에서 만들어줌
    static class UpdateMemberResponse{
        Long id;
        String name;
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
