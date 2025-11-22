package com.bookshop.bookshop.service;

import java.util.List;

import com.bookshop.bookshop.dto.BookDTO;
import com.bookshop.bookshop.entity.Book;

public interface BookService {

	List<Book> getAllBooks();
	
	List<Book> getAllBooksSorted(String sortBy, String sortOrder);

	Book getBookById(Integer id);

	List<Book> searchBooks(String keyword);

	List<Book> getBooksByCategory(String category);
	
	List<Book> getBooksByCategorySorted(String category, String sortBy, String sortOrder);

	Book createBook(BookDTO dto);

	Book updateBook(Integer bookId, BookDTO dto);

	void deleteBook(Integer bookId);

	boolean checkStockAvailability(Integer bookId, Integer quantity);
}
