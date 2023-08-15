package com.andersen.controllers;

import com.andersen.models.Request;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.List;

public interface RequestController {
    List<Request> getAllSorted(HttpServletRequest request, HttpServletResponse response);
}
