package com.pado.SpringBootrealpractice.service;

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

    public List<Item> findItems(){
        return itemRepository.findAll();
    }

    public Item findOne(Long itemId) {
        return itemRepository.fineOne(itemId);
    }

}
