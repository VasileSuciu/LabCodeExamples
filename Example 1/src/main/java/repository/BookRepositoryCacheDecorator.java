package repository;

import model.Book;

import java.util.List;

/**
 * Created by Alex on 07/03/2017.
 */
public class BookRepositoryCacheDecorator extends BookRepositoryDecorator {

    private Cache<Book> cache;

    public BookRepositoryCacheDecorator(BookRepository bookRepository, Cache<Book> cache) {
        super(bookRepository);
        this.cache = cache;
    }

    @Override
    public List<Book> findAll() {
        if (cache.hasResult()) {
            return cache.load();
        }
        List<Book> books = decoratedRepository.findAll();
        cache.save(books);
        return books;
    }

    @Override
    public Book findById(Long id) {
        return decoratedRepository.findById(id);
    }

    @Override
    public boolean save(Book book) {
        cache.invalidateCache();
        return decoratedRepository.save(book);
    }

    @Override
    public void deleteAll() {
        cache.invalidateCache();
        decoratedRepository.deleteAll();
    }
}
