package com.bookshop.bookshop.repository;

import com.bookshop.bookshop.entity.Order;
import com.bookshop.bookshop.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {
    
    /**
     * Find all orders for a specific user, sorted by order date descending
     * @param user the user to find orders for
     * @return list of orders sorted by order date (newest first)
     */
    List<Order> findByUserOrderByOrderDateDesc(User user);
    
    /**
     * Find orders by status
     * @param status the order status to filter by
     * @return list of orders with the specified status
     */
    List<Order> findByStatus(String status);
}
