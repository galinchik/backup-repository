package com.gmudryk.backupservice.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.gmudryk.backupservice.jackson.CustomJsonDateDeserializer;
import com.gmudryk.backupservice.jackson.CustomJsonDateSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TodoItemServerUser {

    private Long id;

    @NonNull
    @JsonProperty("username")
    private String userName;

    private List<Todo> todos;

    private String email;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Todo {
        @NonNull
        private Long id;

        @NonNull
        private String subject;

        @NonNull
        @JsonSerialize(using = CustomJsonDateSerializer.class)
        @JsonDeserialize(using = CustomJsonDateDeserializer.class)
        private Date dueDate;

        @NonNull
        private Boolean done;
    }

}