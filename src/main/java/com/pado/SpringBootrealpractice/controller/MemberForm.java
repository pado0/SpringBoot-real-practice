package com.pado.SpringBootrealpractice.controller;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
public class MemberForm { // 폼 객체 , 회원가입 폼을 받기 위한 객체

    @NotEmpty(message = "회원이름은 필수입니다.") // javax validation을 통해 필수 값 체크
    private String name;

    private String city;
    private String street;
    private String zipcode;
}
