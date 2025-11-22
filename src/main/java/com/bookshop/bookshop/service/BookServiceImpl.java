package com.bookshop.bookshop.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bookshop.bookshop.dto.BookDTO;
import com.bookshop.bookshop.entity.Book;
import com.bookshop.bookshop.exception.ResourceNotFoundException;
import com.bookshop.bookshop.repository.BookRepository;

@Service
@Transactional
public class BookServiceImpl implements BookService {

	private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(BookServiceImpl.class);
	private final BookRepository bookRepository;

	public BookServiceImpl(BookRepository bookRepository) {
		this.bookRepository = bookRepository;
	}

	@Override
	@Transactional(readOnly = true)
	public List<Book> getAllBooks() {
		log.debug("Fetching all books");
		return bookRepository.findAll();
	}
	
	@Override
	@Transactional(readOnly = true)
	public List<Book> getAllBooksSorted(String sortBy, String sortOrder) {
		log.debug("Fetching all books sorted by: {} {}", sortBy, sortOrder);
		List<Book> books = bookRepository.findAll();
		return sortBooks(books, sortBy, sortOrder);
	}

	@Override
	@Transactional(readOnly = true)
	public Book getBookById(Integer bookId) {
		log.debug("Fetching book by ID: {}", bookId);
		return bookRepository.findById(bookId).orElseThrow(() -> new ResourceNotFoundException("Book", "id", bookId));
	}

	@Override
	@Transactional(readOnly = true)
	public List<Book> searchBooks(String keyword) {
		log.debug("Searching books with keyword: {}", keyword);
		return bookRepository.findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCase(keyword, keyword);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Book> getBooksByCategory(String category) {
		log.debug("Fetching books by category: {}", category);
		return bookRepository.findByCategory(category);
	}
	
	@Override
	@Transactional(readOnly = true)
	public List<Book> getBooksByCategorySorted(String category, String sortBy, String sortOrder) {
		log.debug("Fetching books by category: {} sorted by: {} {}", category, sortBy, sortOrder);
		List<Book> books = bookRepository.findByCategory(category);
		return sortBooks(books, sortBy, sortOrder);
	}
	
	private List<Book> sortBooks(List<Book> books, String sortBy, String sortOrder) {
		if (sortBy == null || sortBy.isEmpty()) {
			return books;
		}
		
		boolean ascending = "asc".equalsIgnoreCase(sortOrder);
		
		books.sort((b1, b2) -> {
			int comparison = 0;
			switch (sortBy.toLowerCase()) {
				case "title":
					comparison = b1.getTitle().compareToIgnoreCase(b2.getTitle());
					break;
				case "price":
					BigDecimal price1 = b1.getPrice() != null ? b1.getPrice() : BigDecimal.ZERO;
					BigDecimal price2 = b2.getPrice() != null ? b2.getPrice() : BigDecimal.ZERO;
					comparison = price1.compareTo(price2);
					break;
				case "author":
					comparison = b1.getAuthor().compareToIgnoreCase(b2.getAuthor());
					break;
				case "rating":
					BigDecimal rating1 = b1.getRating() != null ? b1.getRating() : BigDecimal.ZERO;
					BigDecimal rating2 = b2.getRating() != null ? b2.getRating() : BigDecimal.ZERO;
					comparison = rating2.compareTo(rating1); // Higher rating first
					break;
				default:
					comparison = 0;
			}
			return ascending ? comparison : -comparison;
		});
		
		return books;
	}

	@Override
	public Book createBook(BookDTO dto) {
		log.info("Creating new book: {}", dto.getTitle());

		Book book = new Book();
		book.setTitle(dto.getTitle());
		book.setAuthor(dto.getAuthor());
		book.setCategory(dto.getCategory());
		book.setPrice(dto.getPrice());
		book.setStock(dto.getStock());
		book.setDescription(dto.getDescription());
		book.setCoverImageURL(dto.getCoverImageURL());
		book.setRating(dto.getRating());

		Book savedBook = bookRepository.save(book);
		log.info("Book created successfully with ID: {}", savedBook.getBookId());

		return savedBook;
	}

	@Override
	public Book updateBook(Integer bookId, BookDTO dto) {
		log.info("Updating book: {}", bookId);

		Book book = bookRepository.findById(bookId)
				.orElseThrow(() -> new ResourceNotFoundException("Book", "id", bookId));

		book.setTitle(dto.getTitle());
		book.setAuthor(dto.getAuthor());
		book.setCategory(dto.getCategory());
		book.setPrice(dto.getPrice());
		book.setStock(dto.getStock());
		book.setDescription(dto.getDescription());
		book.setCoverImageURL(dto.getCoverImageURL());
		book.setRating(dto.getRating());

		Book updatedBook = bookRepository.save(book);
		log.info("Book updated successfully: {}", updatedBook.getBookId());

		return updatedBook;
	}

	@Override
	public void deleteBook(Integer bookId) {
		log.info("Deleting book: {}", bookId);

		Book book = bookRepository.findById(bookId)
				.orElseThrow(() -> new ResourceNotFoundException("Book", "id", bookId));

		bookRepository.delete(book);
		log.info("Book deleted successfully: {}", bookId);
	}

	@Override
	@Transactional(readOnly = true)
	public boolean checkStockAvailability(Integer bookId, Integer quantity) {
		Book book = bookRepository.findById(bookId)
				.orElseThrow(() -> new ResourceNotFoundException("Book", "id", bookId));

		return book.getStock() >= quantity;
	}
}
