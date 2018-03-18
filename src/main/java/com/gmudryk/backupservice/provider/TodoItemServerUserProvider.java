package com.gmudryk.backupservice.provider;

import com.gmudryk.backupservice.entity.TodoItemServerUser;
import exception.TodoItemServerApplicationException;

import java.util.List;

public interface TodoItemServerUserProvider {

    List<TodoItemServerUser> getUsers() throws TodoItemServerApplicationException;
}
