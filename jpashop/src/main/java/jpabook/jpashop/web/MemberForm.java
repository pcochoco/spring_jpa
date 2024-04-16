package jpabook.jpashop.web;

import jakarta.validation.constraints.NotEmpty; //gradle에 직접 implementation 입력해서 NotEmpty 활용
import lombok.Getter;
import lombok.Setter;
@Getter @Setter
public class MemberForm {
    @NotEmpty(message = "Member name must be identified")
    private String name;
    private String city;
    private String street;
    private String zipcode;

}
