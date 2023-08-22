package com.andersen.repositories.jpa;

import com.andersen.enums.BookSortKey;
import com.andersen.models.Book;
import com.andersen.repositories.BookRepository;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class JpaBookRepository implements BookRepository {
    private final HibernateDatabase hibernateDatabase;

    public JpaBookRepository(HibernateDatabase hibernateDatabase) {
        this.hibernateDatabase = hibernateDatabase;
    }

    @Override
    public void save(Book book) {
        try (Session session = hibernateDatabase.getSessionFactory().openSession()) {

            Transaction transaction = session.beginTransaction();

            session.merge(book);

            transaction.commit();
        }
    }

    @Override
    public Book findById(Long bookId) {
        try (Session session = hibernateDatabase.getSessionFactory().openSession()) {

            return session.get(Book.class, bookId);
        }
    }

    @Override
    public List<Book> getAllSorted(BookSortKey sortKey) {
        try (Session session = hibernateDatabase.getSessionFactory().openSession()) {

            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<Book> criteriaQuery = criteriaBuilder.createQuery(Book.class);

            Root<Book> from = criteriaQuery.from(Book.class);

            switch (sortKey) {
                case NAME -> criteriaQuery.orderBy(criteriaBuilder.asc(from.get("name")));
                case PRICE -> criteriaQuery.orderBy(criteriaBuilder.asc(from.get("price")));
                case STATUS -> criteriaQuery.orderBy(criteriaBuilder.asc(from.get("status")));
            }

            return session.createQuery(criteriaQuery).getResultList();
        }
    }
}
