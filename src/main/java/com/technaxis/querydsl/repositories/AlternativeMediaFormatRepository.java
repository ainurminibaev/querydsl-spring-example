package com.technaxis.querydsl.repositories;

import com.technaxis.querydsl.model.AlternativeMediaFormat;
import com.technaxis.querydsl.model.enums.MediaFormat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Created by Vitaly on 27.02.2018.
 */
@Repository
public interface AlternativeMediaFormatRepository extends JpaRepository<AlternativeMediaFormat, Long> {

    Optional<AlternativeMediaFormat> findByOriginPathAndMediaFormat(String originPath, MediaFormat mediaFormat);

    List<AlternativeMediaFormat> findByOriginPathInAndMediaFormat(List<String> originPaths, MediaFormat mediaFormat);

    List<AlternativeMediaFormat> findByOriginPathIn(List<String> originPaths);

    List<AlternativeMediaFormat> findByOriginPath(String originPath);
}
