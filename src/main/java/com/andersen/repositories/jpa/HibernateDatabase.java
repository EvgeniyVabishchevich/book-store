package com.andersen.repositories.jpa;

import com.andersen.models.Book;
import com.andersen.models.Order;
import com.andersen.models.Request;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.util.Properties;

public class HibernateDatabase {
    private final SessionFactory sessionFactory;
    public HibernateDatabase(Properties properties) {
        Configuration config = new Configuration();
        config.setProperties(properties);

        config.addAnnotatedClass(Book.class);
        config.addAnnotatedClass(Order.class);
        config.addAnnotatedClass(Request.class);

        sessionFactory = config.buildSessionFactory();
    }

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }
}
