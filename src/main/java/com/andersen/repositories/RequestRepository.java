package com.andersen.repositories;

import com.andersen.enums.RequestSortKey;
import com.andersen.models.Request;

import java.util.List;

public interface RequestRepository {
    void save(Request request);

    List<Request> getAllSorted(RequestSortKey sortKey);
    List<Request> findAllByOrderId(Long orderId);
    List<Request> findAllByBookId(Long bookId);
}
