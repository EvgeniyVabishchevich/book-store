package com.andersen.services;

import com.andersen.enums.RequestSortKey;
import com.andersen.models.Request;

import java.util.List;

public interface RequestService {
    List<Request> getAllSorted(RequestSortKey requestSortKey);
}
