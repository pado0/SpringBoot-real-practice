package com.pado.SpringBootrealpractice.repository;

import com.pado.SpringBootrealpractice.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceUnit;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class MemberRepository {

    //@PersistenceContext // jpa 표준 어노테이션
    //@Autowired // 스프링 부트가 잇어 persistenceContext를 requiredargcons-로 바꿀 수 있다.
    private final EntityManager em; // 스프링이 엔티티 매니저를 자동으로 인젝션 해줌

    // 엔티티 매니저도 생성자 인젝션 해줄 수 있음
    /*public MemberRepository(EntityManager em) {
        this.em = em;
    }*/

    public void save(Member member) {
        em.persist(member); // persist:영속성 객체에 반영, db에 들어간 시점이 아니라도 키를 제너레이트 해줌. 키는 pk로 설정됨
    }

    public Member findOne(Long id) {
        return em.find(Member.class, id);
    }

    public List<Member> findAll(){
       return em.createQuery("select m from Member m", Member.class) // jpql과 sql에 가장 다른점은, sql은 테이블 대상으로 쿼리를 짜지만, jpql은 객체 대상으로 쿼리를 한다.
               .getResultList();
    }

    public List<Member> findByName(String name) {
        return em.createQuery("select m from Member m where m.name = :name", Member.class)
                .setParameter("name", name)
                .getResultList();
    }

}
