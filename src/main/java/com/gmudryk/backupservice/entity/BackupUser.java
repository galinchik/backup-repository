package com.gmudryk.backupservice.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.gmudryk.backupservice.jackson.CustomJsonDateSerializer;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data
@Entity
@ApiModel
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user_data")
public class BackupUser implements Serializable {
    @Id
    @GeneratedValue
    @JsonIgnore
    private Long id;

    @Column(name = "BackupId")
    @JsonIgnore
    private Long backupId;

    @Column(name = "Username")
    private String userName;

    @Column(name = "TodoItemId")
    private Long todoItemId;

    @Column(name = "Subject")
    private String subject;

    @Column(name = "DueDate")
    @JsonSerialize(using = CustomJsonDateSerializer.class)
    private Date dueDate;

    @Column(name = "Done")
    private Boolean done;

}
