package com.pado.SpringBootrealpractice.service;

import com.pado.SpringBootrealpractice.domain.item.Book;
import com.pado.SpringBootrealpractice.domain.item.Item;
import com.pado.SpringBootrealpractice.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


// 위임만 하는 클래스. 컨트롤러에서 리포지토리에 바로 접근해도 되지 않을까하는 ..
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemService {
    private final ItemRepository itemRepository;

    @Transactional
    public void saveItem(Item item) {
        itemRepository.save(item);
    }


    @Transactional // 값이 세팅이 되고, 함수가 종료되면 transactional 어노테이션에 의해 commit이 된다. 이 다음 변경감지를 한다. 바뀐 값은 업데이트 쿼리를 넘겨
    public void updateItem(Long itemId, String name, int price, int stockQuantity){ //id가 명확히 전달되면 트랜젝션 안에서 저장소 조회한 뒤 영속성으로 값 변경 가
        Item findItem = itemRepository.findOne(itemId); //얘는 영속상테이므로, 업데이트 될 값을 여기에 업데이트 해주면 자동으로 db에 jpa가 쿼리짜서 넣어 > 변경 감지
        findItem.setPrice(price);
        findItem.setName(name);
        findItem.setStockQuantity(stockQuantity);
    }

    public List<Item> findItems(){
        return itemRepository.findAll();
    }

    public Item findOne(Long itemId) {
        return itemRepository.findOne(itemId);
    }

}
