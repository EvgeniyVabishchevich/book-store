package com.andersen.controllers.servlet;

import com.andersen.annotations.Get;
import com.andersen.annotations.Post;
import com.andersen.annotations.Put;
import com.andersen.controllers.OrderController;
import com.andersen.controllers.servlet.jsonBodies.JsonOrder;
import com.andersen.enums.OrderSortKey;
import com.andersen.models.Order;
import com.andersen.models.Request;
import com.andersen.services.impl.BookServiceImpl;
import com.andersen.services.impl.OrderServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Singleton
public class OrderControllerServlet implements OrderController {
    private final BookServiceImpl bookService;
    private final OrderServiceImpl orderService;
    private final ObjectMapper objectMapper;

    @Inject
    public OrderControllerServlet(BookServiceImpl bookService, OrderServiceImpl orderService, ObjectMapper objectMapper) {
        this.bookService = bookService;
        this.orderService = orderService;
        this.objectMapper = objectMapper;
    }

    @Override
    @Get("/orders/list")
    public List<Order> getAllSorted(HttpServletRequest request, HttpServletResponse response) {
        String sortKey = request.getParameter("sort");

        OrderSortKey orderSortKey = OrderSortKey.valueOf(sortKey.toUpperCase());

        return orderService.getAllSorted(orderSortKey);
    }

    @Override
    @Post("/orders/add")
    public void addOrder(HttpServletRequest request, HttpServletResponse response) {
        try {
            JsonOrder jsonOrder = objectMapper.readValue(request.getInputStream(), JsonOrder.class);

            List<Request> requests = jsonOrder.getRequests().stream()
                    .map(jsonRequest -> new Request(jsonOrder.getClientId(), bookService.findById(jsonRequest.getBookId()),
                            jsonRequest.getAmount()))
                    .toList();

            Order order = new Order(jsonOrder.getClientId(), requests);

            orderService.add(order);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    @Put("/orders/complete")
    public void complete(HttpServletRequest request, HttpServletResponse response) {
        try {
            Long id = objectMapper.readValue(request.getInputStream(), Long.class);

            orderService.complete(id);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    @Get("/orders/income")
    public int getIncomeBySpecifiedPeriod(HttpServletRequest request, HttpServletResponse response) {
        LocalDateTime from = LocalDateTime.parse(request.getParameter("from"));
        LocalDateTime to = LocalDateTime.parse(request.getParameter("to"));

        return orderService.getIncomeBySpecifiedPeriod(from, to);
    }

    @Override
    @Put("/orders/cancel")
    public void cancel(HttpServletRequest request, HttpServletResponse response) {
        try {
            Long id = objectMapper.readValue(request.getInputStream(), Long.class);

            orderService.cancel(id);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
