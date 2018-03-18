package com.gmudryk.backupservice.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.gmudryk.backupservice.jackson.CustomJsonDateDeserializer;
import com.gmudryk.backupservice.jackson.CustomJsonDateSerializer;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Data
@Builder
@Entity
@ApiModel
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "backup_info")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BackupInfo {
    @Id
    @GeneratedValue
    @JsonProperty("backupId")
    private Long id;

    @JsonSerialize(using = CustomJsonDateSerializer.class)
    @JsonDeserialize(using = CustomJsonDateDeserializer.class)
    private Date date;

    private BackupStatus status;
}
