package com.bookshop.bookshop.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.bookshop.bookshop.dto.BookDTO;
import com.bookshop.bookshop.entity.Book;
import com.bookshop.bookshop.service.BookService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/admin/books")
public class AdminBookController {

	private final BookService bookService;

	public AdminBookController(BookService bookService) {
		this.bookService = bookService;
	}

	@GetMapping
	public String listBooks(Model model) {
		List<Book> books = bookService.getAllBooks();
		model.addAttribute("books", books);
		// TODO: Create admin/books/list.html template or use REST API
		return "redirect:/index.html"; // Temporary redirect
	}

	@GetMapping("/create")
	public String showCreateForm(Model model) {
		model.addAttribute("book", new BookDTO());
		// TODO: Create admin/books/create.html template
		return "redirect:/index.html"; // Temporary redirect
	}

	@PostMapping("/create")
	public String createBook(@Valid @ModelAttribute("book") BookDTO dto, BindingResult result,
			RedirectAttributes redirectAttributes) {
		if (result.hasErrors()) {
			return "redirect:/index.html"; // Temporary redirect
		}

		try {
			bookService.createBook(dto);
			redirectAttributes.addFlashAttribute("success", "Book created successfully");
			return "redirect:/index.html"; // Temporary redirect
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("error", e.getMessage());
			return "redirect:/index.html"; // Temporary redirect
		}
	}

	@GetMapping("/{id}/edit")
	public String showEditForm(@PathVariable Integer id, Model model) {
		Book book = bookService.getBookById(id);
		BookDTO dto = new BookDTO();
		dto.setTitle(book.getTitle());
		dto.setAuthor(book.getAuthor());
		dto.setCategory(book.getCategory());
		dto.setPrice(book.getPrice());
		dto.setStock(book.getStock());
		dto.setDescription(book.getDescription());
		dto.setCoverImageURL(book.getCoverImageURL());
		dto.setRating(book.getRating());

		model.addAttribute("book", dto);
		model.addAttribute("bookId", id);
		// TODO: Create admin/books/edit.html template
		return "redirect:/index.html"; // Temporary redirect
	}

	@PostMapping("/{id}/edit")
	public String updateBook(@PathVariable Integer id, @Valid @ModelAttribute("book") BookDTO dto, BindingResult result,
			RedirectAttributes redirectAttributes, Model model) {
		if (result.hasErrors()) {
			model.addAttribute("bookId", id);
			return "redirect:/index.html"; // Temporary redirect
		}

		try {
			bookService.updateBook(id, dto);
			redirectAttributes.addFlashAttribute("success", "Book updated successfully");
			return "redirect:/index.html"; // Temporary redirect
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("error", e.getMessage());
			return "redirect:/index.html"; // Temporary redirect
		}
	}

	@PostMapping("/{id}/delete")
	public String deleteBook(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
		try {
			bookService.deleteBook(id);
			redirectAttributes.addFlashAttribute("success", "Book deleted successfully");
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("error", e.getMessage());
		}
		return "redirect:/index.html"; // Temporary redirect
	}
}
