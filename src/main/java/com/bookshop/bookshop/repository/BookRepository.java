package com.bookshop.bookshop.repository;

import com.bookshop.bookshop.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Integer> {
    
    /**
     * Search books by title or author containing the keyword (case-insensitive)
     * @param title the title keyword to search for
     * @param author the author keyword to search for
     * @return list of books matching the criteria
     */
    List<Book> findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCase(String title, String author);
    
    /**
     * Find books by category
     * @param category the category to filter by
     * @return list of books in the specified category
     */
    List<Book> findByCategory(String category);
    
    /**
     * Find books with stock greater than specified value
     * @param stock the minimum stock value
     * @return list of books with stock greater than the specified value
     */
    List<Book> findByStockGreaterThan(Integer stock);
}
