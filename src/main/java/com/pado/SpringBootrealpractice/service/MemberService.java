package com.pado.SpringBootrealpractice.service;

import com.pado.SpringBootrealpractice.domain.Member;
import com.pado.SpringBootrealpractice.repository.MemberRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true) // 이렇게 읽기만 하는 곳에서 readOnly 설정을 하면 더 최적화됨
//@RequiredArgsConstructor -> 파이널이 붙은 필드를 대상으로 생성자 대신 만들어줌
public class MemberService {

    // 변경할 일 없으므로 final. 컴파일 시점에 체크가 가능한 이점
    private final MemberRepository memberRepository;

    //@Autowired -> 생성자 하나일 땐 자동으로 autowired 됨
    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    /**
     * 회원가입
     */
    @Transactional //쓰기가 있는 곳에서는 readOnly false가 되도록
    public Long join(Member member){
        // 중복체크
        validateDuplicateMember(member);
        memberRepository.save(member);
        return member.getId();
    }

    private void validateDuplicateMember(Member member) {
        //중복 조회 // Exception
        // 이렇게 해도 문제가 있음. was가 여러개 떳을 때 db에 member A가 동시에 insert하고 둘 다 통과하게 될 수 있음.
        // 실무에서는 멀티쓰레드 상황을 고려해서 최후의 수단으로 member db를 unique 제약조건으로 걸어두는게 좋음
        List<Member> findMembers = memberRepository.findByName(member.getName());
        if(!findMembers.isEmpty()){
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }

    }

    // 회원 전체 조회
    public List<Member> fineMembers(){
        return memberRepository.findAll();
    }

    // 회원 하나 조회
    public Member findOne(Long memberId){
        return memberRepository.findOne(memberId);

    }

}
