package com.technaxis.querydsl.model;

import com.technaxis.querydsl.model.enums.MediaFormat;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * Created by Vitaly on 27.02.2018.
 */
@Entity
@Table(name = "alternative_media_formats")
@SequenceGenerator(name = AbstractEntity.GENERATOR, sequenceName = "alternative_media_formats_seq", allocationSize = 10)
public class AlternativeMediaFormat extends AbstractAuditableEntity {

    @Column(name = "origin_path")
    private String originPath;

    @Column(name = "converted_path")
    private String convertedPath;

    @Column(name = "media_format")
    @Enumerated(EnumType.STRING)
    private MediaFormat mediaFormat;

    public String getOriginPath() {
        return originPath;
    }

    public void setOriginPath(String originPath) {
        this.originPath = originPath;
    }

    public String getConvertedPath() {
        return convertedPath;
    }

    public void setConvertedPath(String convertedPath) {
        this.convertedPath = convertedPath;
    }

    public MediaFormat getMediaFormat() {
        return mediaFormat;
    }

    public void setMediaFormat(MediaFormat mediaFormat) {
        this.mediaFormat = mediaFormat;
    }
}
