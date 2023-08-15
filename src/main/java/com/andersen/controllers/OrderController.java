package com.andersen.controllers;

import com.andersen.models.Order;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.List;

public interface OrderController {
    List<Order> getAllSorted(HttpServletRequest request, HttpServletResponse response);

    void addOrder(HttpServletRequest request, HttpServletResponse response);

    void complete(HttpServletRequest request, HttpServletResponse response);

    int getIncomeBySpecifiedPeriod(HttpServletRequest request, HttpServletResponse response);

    void cancel(HttpServletRequest request, HttpServletResponse response);
}
