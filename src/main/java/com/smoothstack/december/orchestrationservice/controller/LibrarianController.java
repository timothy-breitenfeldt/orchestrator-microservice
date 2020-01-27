package com.smoothstack.december.orchestrationservice.controller;

import java.util.Arrays;

import javax.validation.constraints.Min;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.smoothstack.december.orchestrationservice.entity.Book;
import com.smoothstack.december.orchestrationservice.entity.BookCopy;
import com.smoothstack.december.orchestrationservice.entity.LibraryBranch;

@RestController
@RequestMapping("/lms/librarian")
public class LibrarianController {

    @Autowired
    private RestTemplate restTemplate;

    private static final String baseUrl = "http://localhost:8081/lms/librarian";
    private static final Logger logger = LogManager.getLogger(LibrarianController.class);

    @PostMapping("/book-copies")
    public BookCopy createBookCopy(@RequestBody BookCopy bookCopy) {
        logger.debug("request: {}", bookCopy.toString());
        BookCopy response = this.restTemplate.postForObject(baseUrl + "/book-copies", bookCopy, BookCopy.class);
        logger.debug("response: {}", bookCopy);
        return response;
    }

    @PostMapping("/books")
    public Book createBook(@RequestBody Book book) {
        logger.debug("request: {}", book.toString());
        Book response = this.restTemplate.postForObject(baseUrl + "/books", book, Book.class);
        logger.debug("response: {}", response.toString());
        return response;
    }

    @GetMapping("/books")
    public Book[] getBooks() {
        Book[] books = this.restTemplate.getForObject(baseUrl + "/books", Book[].class);
        logger.debug("response: {}", Arrays.deepToString(books));
        return books;
    }

    @GetMapping("/branches")
    public LibraryBranch[] getLibraryBranches() {
        LibraryBranch[] branches = this.restTemplate.getForObject(baseUrl + "/branches", LibraryBranch[].class);
        logger.debug("response: {}", Arrays.deepToString(branches));
        return branches;
    }

    @GetMapping("/book-copies/branches/{branchId}")
    public BookCopy[] getBookCopies(@PathVariable Long branchId) {
        logger.debug("request: branchId={}", branchId);
        BookCopy[] bookCopies = this.restTemplate.getForObject(baseUrl + "/book-copies/branches/" + branchId,
                BookCopy[].class);
        logger.debug("response: {}", Arrays.deepToString(bookCopies));
        return bookCopies;
    }

    @PutMapping("/book-copies/books/{bookId}/branches/{branchId}")
    public void updateBookCopy(@PathVariable Long bookId, @PathVariable Long branchId, @RequestBody BookCopy bookCopy) {
        logger.debug("request: bookId={}, branchId={},  body: {}", bookId, branchId, bookCopy.toString());
        this.restTemplate.put(baseUrl + "/book-copies/books/" + bookId + "/branches/" + branchId, bookCopy);
    }

    @PutMapping("/branches/{branchId}")
    public void updateLibraryBranch(@PathVariable Long branchId, @RequestBody LibraryBranch branch) {
        logger.debug("request: branchId={}, body=", branchId, branch.toString());
        this.restTemplate.put(baseUrl + "/branches/" + branchId, branch);
    }

    @DeleteMapping("/book-copies/books/{bookId}/branches/{branchId}")
    public ResponseEntity<BookCopy> deleteBookCopy(@PathVariable @Min(1) Long bookId,
            @PathVariable @Min(1) Long branchId) {
        logger.debug("request: bookId=[], branchId={}", bookId, branchId);
        this.restTemplate.delete(baseUrl + "/book-copies/books/" + bookId + "/branches/" + branchId);
        return new ResponseEntity<BookCopy>(HttpStatus.NO_CONTENT);
    }

}
