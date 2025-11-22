package com.bookshop.bookshop.repository;

import com.bookshop.bookshop.entity.OrderGift;
import com.bookshop.bookshop.entity.OrderGiftDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderGiftDetailRepository extends JpaRepository<OrderGiftDetail, Integer> {
    
    /**
     * Find all gift order details for a specific gift order
     * @param orderGift the gift order to find details for
     * @return list of gift order details
     */
    List<OrderGiftDetail> findByOrderGift(OrderGift orderGift);
}
