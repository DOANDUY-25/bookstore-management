package com.bookshop.bookshop.repository;

import com.bookshop.bookshop.entity.Order;
import com.bookshop.bookshop.entity.OrderGift;
import com.bookshop.bookshop.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Integer> {
    
    /**
     * Find payment by order
     * @param order the order to find payment for
     * @return Optional containing the payment if found
     */
    Optional<Payment> findByOrder(Order order);
    
    /**
     * Find payment by gift order
     * @param orderGift the gift order to find payment for
     * @return Optional containing the payment if found
     */
    Optional<Payment> findByOrderGift(OrderGift orderGift);
}
