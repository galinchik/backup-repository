package com.gmudryk.backupservice.helper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.gmudryk.backupservice.entity.BackupUser;
import com.gmudryk.backupservice.entity.TodoItemServerUser;
import org.assertj.core.util.Lists;

import java.util.List;

public class BackupHelper {

    public static List<BackupUser> convertUsersToBackupUsers(List<TodoItemServerUser> users) {

        List<BackupUser> backupUsers = Lists.newArrayList();
        for (TodoItemServerUser user : users) {
            List<TodoItemServerUser.Todo> todos = user.getTodos();
            for (TodoItemServerUser.Todo todo : todos) {
                BackupUser backupUser = new BackupUser();
                backupUser.setUserName(user.getUserName());
                backupUser.setSubject(todo.getSubject());
                backupUser.setTodoItemId(todo.getId());
                backupUser.setDone(todo.getDone());
                backupUser.setDueDate(todo.getDueDate());
                backupUsers.add(backupUser);
            }
        }

        return backupUsers;
    }

    public static String convertUsersBackupToCsvFormat(List<BackupUser> backupUsers) throws JsonProcessingException {

        CsvMapper mapper = new CsvMapper();
        CsvSchema schema = mapper.schemaFor(BackupUser.class).withHeader();
        return mapper.writer(schema).writeValueAsString(backupUsers);

    }
}
