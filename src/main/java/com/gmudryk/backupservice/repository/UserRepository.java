package com.gmudryk.backupservice.repository;

import com.gmudryk.backupservice.entity.BackupUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
public interface UserRepository extends JpaRepository<BackupUser, Integer> {

    @Query("SELECT user FROM BackupUser user WHERE user.backupId = ?1")
    List<BackupUser>    findAllByBackupId(Long backupId);

}
