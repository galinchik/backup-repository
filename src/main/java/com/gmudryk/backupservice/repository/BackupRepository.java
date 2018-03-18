package com.gmudryk.backupservice.repository;

import com.gmudryk.backupservice.entity.BackupInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;

@Transactional
public interface BackupRepository extends JpaRepository<BackupInfo, Integer> {

}
