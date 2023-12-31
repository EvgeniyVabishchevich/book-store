package com.andersen.controllers.servlet;

import com.andersen.annotations.Get;
import com.andersen.controllers.RequestController;
import com.andersen.enums.RequestSortKey;
import com.andersen.models.Request;
import com.andersen.services.RequestService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.List;

public class RequestControllerServlet implements RequestController {

    private final RequestService requestService;

    public RequestControllerServlet(RequestService requestService) {
        this.requestService = requestService;
    }

    @Override
    @Get("/requests/list")
    public List<Request> getAllSorted(HttpServletRequest request, HttpServletResponse response) {
        RequestSortKey sortKey = RequestSortKey.valueOf(request.getParameter("sort").toUpperCase());

        return requestService.getAllSorted(sortKey);
    }
}
