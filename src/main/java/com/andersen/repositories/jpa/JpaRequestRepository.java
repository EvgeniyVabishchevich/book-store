package com.andersen.repositories.jpa;

import com.andersen.enums.RequestSortKey;
import com.andersen.models.Request;
import com.andersen.repositories.RequestRepository;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.ParameterExpression;
import jakarta.persistence.criteria.Root;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;
import java.util.Objects;

public class JpaRequestRepository implements RequestRepository {
    private final HibernateDatabase hibernateDatabase;

    public JpaRequestRepository(HibernateDatabase hibernateDatabase) {
        this.hibernateDatabase = hibernateDatabase;
    }

    @Override
    public void save(Request request) {
        try (Session session = hibernateDatabase.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();

            if (Objects.isNull(session.find(Request.class, request.getId()))) {
                session.persist(request);
            } else {
                session.merge(request);
            }

            transaction.commit();
        }
    }

    @Override
    public List<Request> getAllSorted(RequestSortKey sortKey) {
        try (Session session = hibernateDatabase.getSessionFactory().openSession()) {

            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<Request> criteriaQuery = criteriaBuilder.createQuery(Request.class);

            Root<Request> from = criteriaQuery.from(Request.class);

            switch (sortKey) {
                case NAME -> criteriaQuery.orderBy(criteriaBuilder.asc(from.get("book").get("name")));
                case PRICE -> criteriaQuery.orderBy(criteriaBuilder.asc(criteriaBuilder.prod(from.get("amount"),
                        from.get("book").get("price"))));
            }

            return session.createQuery(criteriaQuery).getResultList();
        }
    }

    @Override
    public List<Request> findAllByOrderId(Long orderId) {
        try (Session session = hibernateDatabase.getSessionFactory().openSession()) {
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<Request> criteriaQuery = criteriaBuilder.createQuery(Request.class);
            Root<Request> root = criteriaQuery.from(Request.class);

            ParameterExpression<Long> param = criteriaBuilder.parameter(Long.class);
            criteriaQuery.where(criteriaBuilder.equal(root.get("orderId"), param));

            TypedQuery<Request> query = session.createQuery(criteriaQuery);
            query.setParameter(param, orderId);

            return query.getResultList();
        }
    }

    @Override
    public List<Request> findAllByBookId(Long bookId) {
        try (Session session = hibernateDatabase.getSessionFactory().openSession()) {
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<Request> criteriaQuery = criteriaBuilder.createQuery(Request.class);
            Root<Request> root = criteriaQuery.from(Request.class);

            ParameterExpression<Long> param = criteriaBuilder.parameter(Long.class);
            criteriaQuery.where(criteriaBuilder.equal(root.get("book").get("id"), param));

            TypedQuery<Request> query = session.createQuery(criteriaQuery);
            query.setParameter(param, bookId);

            return query.getResultList();
        }
    }

}
