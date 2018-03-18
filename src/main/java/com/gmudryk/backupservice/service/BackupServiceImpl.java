package com.gmudryk.backupservice.service;

import com.gmudryk.backupservice.entity.BackupInfo;
import com.gmudryk.backupservice.entity.BackupStatus;
import com.gmudryk.backupservice.entity.BackupUser;
import com.gmudryk.backupservice.helper.BackupHelper;
import com.gmudryk.backupservice.repository.BackupRepository;
import com.gmudryk.backupservice.repository.UserRepository;
import exception.BackupServiceApplicationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

/**
 * Provides saving, updating, searching of backup data and users data
 */
@Service
@Transactional
public class BackupServiceImpl implements BackupService {

    private BackupRepository backupRepository;

    private UserRepository userRepository;

    @Autowired
    public BackupServiceImpl(BackupRepository backupRepository, UserRepository userRepository) {
        this.backupRepository = backupRepository;
        this.userRepository = userRepository;
    }

    @Override
    public BackupInfo createBackupInfo() throws BackupServiceApplicationException {
        BackupInfo backupInfo = new BackupInfo();
        backupInfo.setDate(new Date());
        backupInfo.setStatus(BackupStatus.IN_PROGRESS);
        return backupRepository.save(backupInfo);
    }

    @Override
    public void createUsersBackup(List<BackupUser> backupUsers, Long backupId) throws BackupServiceApplicationException {

        try {

            backupUsers.forEach(backupUser -> backupUser.setBackupId(backupId));
            userRepository.saveAll(backupUsers);
        } catch (Exception e) {
            throw new BackupServiceApplicationException("Cannot save user's data", e, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @Override
    public BackupInfo updateBackupInfo(BackupInfo backupInfo, BackupStatus backupStatus) throws BackupServiceApplicationException {

        try {
            backupInfo.setStatus(backupStatus);
            return backupRepository.save(backupInfo);
        } catch (Exception e) {
            throw new BackupServiceApplicationException("Cannot save user's data", e, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @Override
    public String exportBackup(Long backupId) throws BackupServiceApplicationException {
        List<BackupUser> usersBackup;
        try {
            usersBackup = userRepository.findAllByBackupId(backupId);
            return BackupHelper.convertUsersBackupToCsvFormat(usersBackup);

        } catch (Exception e) {
            throw new BackupServiceApplicationException("Cannot export user's backup", e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public List<BackupInfo> listBackups() throws BackupServiceApplicationException {

        try {
            return backupRepository.findAll();
        } catch (Exception e) {
            throw new BackupServiceApplicationException("Cannot list backup info", e, HttpStatus.INTERNAL_SERVER_ERROR);

        }
    }
}
