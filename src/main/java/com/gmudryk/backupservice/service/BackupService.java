package com.gmudryk.backupservice.service;

import com.gmudryk.backupservice.entity.BackupInfo;
import com.gmudryk.backupservice.entity.BackupStatus;
import com.gmudryk.backupservice.entity.BackupUser;
import exception.BackupServiceApplicationException;

import java.util.List;

public interface BackupService {

    BackupInfo createBackupInfo() throws BackupServiceApplicationException;

    BackupInfo updateBackupInfo(BackupInfo backupInfo, BackupStatus backupStatus) throws BackupServiceApplicationException;

    void createUsersBackup(List<BackupUser> backupUsers, Long backupId) throws BackupServiceApplicationException;

    String exportBackup(Long backupId) throws BackupServiceApplicationException;

    List<BackupInfo> listBackups() throws BackupServiceApplicationException;
}
