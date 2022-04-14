package com.pado.SpringBootrealpractice.domain;

import lombok.Getter;

import javax.persistence.Embeddable;

@Embeddable // 어딘가에 내장 (나중에 찾아보기)
@Getter
public class Address { // 값타입은 변경 불가하게 설계해야한다.

    private String city;
    private String street;
    private String zipcode;
    // jpa 스펙상 기본 생성자 넣어줘야한다. 상속할 일은 없으니 protected로 써놓자.
    protected Address(){

    }
    // 생성할 때만 값 세팅 후 변경 못하도록 처리해야한다.
    public Address(String city, String street, String zipcode) {
        this.city = city;
        this.street = street;
        this.zipcode = zipcode;
    }
}
