package com.gmudryk.backupservice.controller;

import com.gmudryk.backupservice.entity.BackupInfo;
import com.gmudryk.backupservice.entity.BackupStatus;
import com.gmudryk.backupservice.entity.BackupUser;
import com.gmudryk.backupservice.entity.TodoItemServerUser;
import com.gmudryk.backupservice.helper.BackupHelper;
import com.gmudryk.backupservice.provider.TodoItemServerUserProvider;
import com.gmudryk.backupservice.service.BackupService;
import exception.BackupServiceApplicationException;
import exception.TodoItemServerApplicationException;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * The service provides a REST API to backup user data provided by remote TodoItemServer.
 */
@Slf4j
@RestController
public class BackupController {

    private BackupService backupService;

    private TodoItemServerUserProvider userProvider;

    private ExecutorService executorService;

    @PostConstruct
    public void init() {
        this.executorService = Executors.newSingleThreadExecutor();
    }

    @PreDestroy
    public void destroy() {
        this.executorService.shutdown();
    }

    @Autowired
    public BackupController(BackupService backupService, TodoItemServerUserProvider userProvider) {
        this.backupService = backupService;
        this.userProvider = userProvider;
    }

    @ApiOperation(value = "Initiate a backup of remote user data from TodoItemServer", notes = "Also return backupId if backup was successful", response = Long.class)
    @RequestMapping(value = "/backups", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity createBackup() throws BackupServiceApplicationException {

        BackupInfo backupInfo;
        backupInfo = backupService.createBackupInfo();

        List<TodoItemServerUser> users;

        try {
            users = userProvider.getUsers();
        } catch (TodoItemServerApplicationException e) {
            backupService.updateBackupInfo(backupInfo, BackupStatus.FAILED);
            return new ResponseEntity<>(e.getHttpStatus());
        }

        if (users.isEmpty()) {
            log.warn("There is no data to backup");
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }

        List<BackupUser> backupUsers = BackupHelper.convertUsersToBackupUsers(users);
        backupService.createUsersBackup(backupUsers, backupInfo.getId());

        backupService.updateBackupInfo(backupInfo, BackupStatus.DONE);
        return ResponseEntity.ok(BackupInfo.builder().id(backupInfo.getId()).build());

    }

    @ApiOperation(value = "List all backup witch contains an information about backups", response = List.class)
    @RequestMapping(value = "/backups", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity listBackups() throws BackupServiceApplicationException {

        List<BackupInfo> backupInfos = backupService.listBackups();
        return ResponseEntity.ok(backupInfos);

    }

    @ApiOperation(value = "Export users data in csv format by backupId", response = List.class)
    @RequestMapping(value = "/exports/{backupId}", method = RequestMethod.GET, produces = "application/csv")
    public ResponseEntity listUsers(@PathVariable("backupId") Long backupId) throws BackupServiceApplicationException {

        String exportedBackup = backupService.exportBackup(backupId);
        return ResponseEntity.ok(exportedBackup);

    }

    @ExceptionHandler(BackupServiceApplicationException.class)
    public ResponseEntity<Object> handleException(BackupServiceApplicationException ex) {
        log.error(ex.getLocalizedMessage(), ex.getHttpStatus());
        return new ResponseEntity<>(ex.getHttpStatus());
    }

}
