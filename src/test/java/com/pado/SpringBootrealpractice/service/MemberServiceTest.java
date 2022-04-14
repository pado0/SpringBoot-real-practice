package com.pado.SpringBootrealpractice.service;

import com.pado.SpringBootrealpractice.domain.Member;
import com.pado.SpringBootrealpractice.repository.MemberRepository;
import com.pado.SpringBootrealpractice.service.MemberService;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional // 스프링에선 트랜젠션 붙인걸로 @테스트시 롤백이 됨
public class MemberServiceTest {

    @Autowired MemberService memberService;
    @Autowired MemberRepository memberRepository;
    @Autowired EntityManager em;


    //회원 가입 예제
    @Test
    @Rollback(value = false)
    public void join() throws Exception{
        //given
        Member member = new Member();
        member.setName("kim");

        //when
        Long saveId = memberService.join(member);

        //then
        //em.flush(); //flush: 영속성 컨텐츠를 db에 반영하는 것. 롤백하거나 플러시가 안되면 insert가 안됨
        Assertions.assertEquals(member, memberRepository.findOne(saveId));

    }

    // 중복 회원 예제
    @Test
    public void 중복회원() throws Exception{

        //given
        Member member1 = new Member();
        Member member2 = new Member();

        member1.setName("kim");
        member2.setName("kim");

        //when
        memberService.join(member1);
        try {
            memberService.join(member2);
        } catch (IllegalStateException e) {
            return;
        }
            //then

        //Assertions.fail("이미 존재하는 회원입니다.");
    }

}
