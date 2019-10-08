package com.technaxis.querydsl.repositories;

import com.technaxis.querydsl.model.FileItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Vitaly on 13.02.2018.
 */
@Repository
public interface FileItemRepository extends JpaRepository<FileItem, Long> {

    @Query("Select f.url from FileItem f where f.id=:fileItemId")
    String getFileUrlById(@Param("fileItemId") Long fileItemId);

    @Modifying
    void deleteByIdIn(List<Long> fileItemIds);
}
