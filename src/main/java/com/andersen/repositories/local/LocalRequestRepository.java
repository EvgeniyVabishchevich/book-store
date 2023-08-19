package com.andersen.repositories.local;

import com.andersen.config.model.ConfigModel;
import com.andersen.enums.RequestSortKey;
import com.andersen.models.Request;
import com.andersen.repositories.RequestRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import java.io.File;
import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@Singleton
public class LocalRequestRepository implements RequestRepository {
    private final ObjectMapper objectMapper;
    private final ConfigModel configModel;

    @Inject
    public LocalRequestRepository(ObjectMapper objectMapper, ConfigModel configModel) {
        this.objectMapper = objectMapper;
        this.configModel = configModel;
    }

    @Override
    public void save(Request request) {
        List<Request> requests = getAllSorted(RequestSortKey.NATURAL);

        requests.add(request);

        save(requests);
    }

    @Override
    public List<Request> getAllSorted(RequestSortKey sortKey) {
        try {
            List<Request> requests = objectMapper.readValue(new File(configModel.savePath()), AppState.class).getRequests();

            switch (sortKey) {
                case PRICE ->
                        requests.sort(Comparator.comparing(request -> request.getBook().getPrice() * request.getAmount()));
                case NAME -> requests.sort(Comparator.comparing(request -> request.getBook().getName()));
            }

            return requests;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Request> findAllByOrderId(Long orderId) {
        //TODO make
        return null;
    }

    @Override
    public List<Request> findAllByBookId(Long bookId) {
        // TODO make
        return null;
    }

    public void save(List<Request> requests) {
        try {
            AppState appState = objectMapper.readValue(new File(configModel.savePath()), AppState.class);

            appState.synchronizeRequests(requests);

            objectMapper.writeValue(new File(configModel.savePath()), appState);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
