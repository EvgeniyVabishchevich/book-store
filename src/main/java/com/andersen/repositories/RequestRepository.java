package com.andersen.repositories;

import com.andersen.enums.RequestSortKey;
import com.andersen.models.Request;

import java.util.List;

public interface RequestRepository {
    void save(Request request);

    List<Request> getAllSorted(RequestSortKey sortKey);

    void changeRequestStatus(Long id, Request.RequestStatus status);
}
