package com.pado.SpringBootrealpractice.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Member {
    @Id
    @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    private String name;

    @Embedded
    private Address address;

    @OneToMany(mappedBy = "member") // order 테이블에 있는 멤버 필드에 의해 맵핑되는 것. 내가 맵핑 된 거울일 뿐이야라는 뜻. 여기서 변경해도 반영되지 않음.
    private List<Order> orders = new ArrayList<>(); // 컬렉션은 필드에서 초기화하는게 좋다. 하이버네이트 내장 컬렉션으로 자동 변경이된다. best practice. 컬렉션은 가급적 변경하지 말기.
}
