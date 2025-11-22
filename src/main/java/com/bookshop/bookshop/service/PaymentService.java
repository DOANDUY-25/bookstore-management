package com.bookshop.bookshop.service;

import com.bookshop.bookshop.dto.PaymentDTO;
import com.bookshop.bookshop.entity.Payment;

public interface PaymentService {
    
    Payment processOrderPayment(Integer orderId, PaymentDTO dto);
    
    Payment processGiftOrderPayment(Integer orderGiftId, PaymentDTO dto);
    
    Payment getPaymentByOrder(Integer orderId);
    
    Payment getPaymentByGiftOrder(Integer orderGiftId);
    
    Payment updatePaymentStatus(Integer paymentId, String status);
}
