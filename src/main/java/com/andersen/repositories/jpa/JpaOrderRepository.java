package com.andersen.repositories.jpa;

import com.andersen.enums.OrderSortKey;
import com.andersen.models.Order;
import com.andersen.repositories.OrderRepository;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class JpaOrderRepository implements OrderRepository {
    private final HibernateDatabase hibernateDatabase;

    public JpaOrderRepository(HibernateDatabase hibernateDatabase) {
        this.hibernateDatabase = hibernateDatabase;
    }

    @Override
    public void save(Order order) {
        try (Session session = hibernateDatabase.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();

            if (Objects.isNull(session.find(Order.class, order.getId()))) {
                session.persist(order);
            } else {
                session.merge(order);
            }

            transaction.commit();
        }
    }

    @Override
    public List<Order> getAllSorted(OrderSortKey sortKey) {
        try (Session session = hibernateDatabase.getSessionFactory().openSession()) {

            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<Order> criteriaQuery = criteriaBuilder.createQuery(Order.class);

            Root<Order> from = criteriaQuery.from(Order.class);

            switch (sortKey) {
                case DATE -> criteriaQuery.orderBy(criteriaBuilder.asc(from.get("completionDate")));
                case PRICE -> criteriaQuery.orderBy(criteriaBuilder.asc(from.get("price")));
                case STATUS -> criteriaQuery.orderBy(criteriaBuilder.asc(from.get("status")));
            }

            return session.createQuery(criteriaQuery).getResultList();
        }
    }

    @Override
    public Optional<Order> findById(Long id) {
        try (Session session = hibernateDatabase.getSessionFactory().openSession()) {

            return Optional.of(session.get(Order.class, id));
        }
    }

    @Override
    public void remove(Long id) {
        try (Session session = hibernateDatabase.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();

            Order order = session.get(Order.class, id);
            session.remove(order);

            transaction.commit();
        }
    }
}
