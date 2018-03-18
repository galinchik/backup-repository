package com.gmudryk.backupservice.provider;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gmudryk.backupservice.entity.TodoItemServerUser;
import exception.TodoItemServerApplicationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URI;
import java.util.List;

/**
 * Provides users data from TodoItemServer
 */
@Service
public class TodoItemServerUserProviderImpl implements TodoItemServerUserProvider {

    @Value("${todo.item.server.user.data.url}")
    private URI getUserDataUrl;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public List<TodoItemServerUser> getUsers() throws TodoItemServerApplicationException {

        String result;

        try {
            result = restTemplate.getForObject(getUserDataUrl, String.class);
        } catch (RestClientException e) {
            throw new TodoItemServerApplicationException("Cannot get user backup data by url = " + getUserDataUrl, e, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        try {
            return objectMapper.readValue(result, new TypeReference<List<TodoItemServerUser>>() {
            });
        } catch (IOException e) {
            throw new TodoItemServerApplicationException("Cannot parse user backup data", e, HttpStatus.BAD_REQUEST);
        }

    }
}
