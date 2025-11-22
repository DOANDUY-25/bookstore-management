package com.bookshop.bookshop.repository;

import com.bookshop.bookshop.entity.OrderGift;
import com.bookshop.bookshop.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderGiftRepository extends JpaRepository<OrderGift, Integer> {
    
    /**
     * Find all gift orders for a specific user, sorted by creation date descending
     * @param user the user to find gift orders for
     * @return list of gift orders sorted by creation date (newest first)
     */
    List<OrderGift> findByUserOrderByCreateAtDesc(User user);
    
    /**
     * Find gift orders by status
     * @param status the gift order status to filter by
     * @return list of gift orders with the specified status
     */
    List<OrderGift> findByStatus(String status);
}
