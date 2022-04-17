package com.pado.SpringBootrealpractice.repository;

import com.pado.SpringBootrealpractice.domain.item.Item;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ItemRepository {
    private final EntityManager em;

    // item은 jpa에 저장하기 전까지 id값이 없음. 완전 새로 생성한 객체.
    public void save(Item item){
        if (item.getId() == null) {
            em.persist(item); //id가 없으면 신규 등록
        } else { // 수정할 때가 null이 아닌 경 > em.merge는 병합을 쓴거. 변경감지가 spring에서는 best practice.
            em.merge(item); //이미 db에 등록된걸 가져오면 업데이트하는것임.
        }
    }

    public Item findOne(Long id) {
        return em.find(Item.class, id);
    }

    public List<Item> findAll(){
        return em.createQuery("select i from Item i", Item.class).getResultList();
    }
}
