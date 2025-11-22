package com.bookshop.bookshop.exception;

/**
 * Exception thrown when requested quantity exceeds available stock
 */
public class InsufficientStockException extends BookshopException {
    
    private Integer bookId;
    private Integer requestedQuantity;
    private Integer availableStock;
    
    public InsufficientStockException(String message) {
        super(message);
    }
    
    public InsufficientStockException(Integer bookId, Integer requestedQuantity, Integer availableStock) {
        super(String.format("Insufficient stock for book ID %d. Requested: %d, Available: %d", 
                bookId, requestedQuantity, availableStock));
        this.bookId = bookId;
        this.requestedQuantity = requestedQuantity;
        this.availableStock = availableStock;
    }
    
    public Integer getBookId() {
        return bookId;
    }
    
    public Integer getRequestedQuantity() {
        return requestedQuantity;
    }
    
    public Integer getAvailableStock() {
        return availableStock;
    }
}
