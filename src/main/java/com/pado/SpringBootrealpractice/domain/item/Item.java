package com.pado.SpringBootrealpractice.domain.item;

import com.pado.SpringBootrealpractice.domain.Category;
<<<<<<< HEAD
import com.pado.SpringBootrealpractice.exception.NotEnoughStockException;
=======
import com.pado.SpringBootrealpractice.exception.NoEnoughStockException;
>>>>>>> order_service
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "dtype")
@Getter
@Setter
public abstract class Item {
    @Id
    @GeneratedValue
    @Column(name = "item_id")
    private Long id;

    private String Name;
    private int price;
    private int stockQuantity; // 이 데이터를 가진 곳에서 엔티티 비즈니스 로직을 설계

    @ManyToMany(mappedBy = "items")
    private List<Category> categories = new ArrayList<>();

<<<<<<< HEAD
    //==비즈니스 로직 ==// 재고 수량 증가
    // 엔티티에 필요한 값 조정이 있으면 그 엔티티 클래스 내에서 해결하는게 가장 객체지향적임. 세터로 밖에서 어찌저찌해서 넣는건 너무 별로
    public void addStock(int quantity) {
=======
    //비즈니스 로직
    public void addStock(int quantity){
>>>>>>> order_service
        this.stockQuantity += quantity;
    }

    public void removeStock(int quantity) {
        int restStock = this.stockQuantity - quantity;
<<<<<<< HEAD
        if(restStock < 0){
            throw new NotEnoughStockException("need more stock");
=======
        if (restStock < 0) {
            throw new NoEnoughStockException("need more stock");
>>>>>>> order_service
        }
        this.stockQuantity = restStock;
    }

}
