package com.andersen.repositories.jdbc;

import com.andersen.enums.RequestSortKey;
import com.andersen.models.Request;
import com.andersen.repositories.RequestRepository;
import com.google.inject.Inject;

import java.util.List;

public class JdbcRequestRepository implements RequestRepository {

    private final Database database;

    @Inject
    public JdbcRequestRepository(Database database) {
        this.database = database;
    }

    @Override
    public void save(Request request) {

    }

    @Override
    public List<Request> getAllSorted(RequestSortKey sortKey) {
        return null;
    }

    @Override
    public void changeRequestStatus(Long id, Request.RequestStatus status) {

    }
}
