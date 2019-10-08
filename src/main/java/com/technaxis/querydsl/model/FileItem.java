package com.technaxis.querydsl.model;

import com.technaxis.querydsl.model.enums.FileItemType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * Created by Vitaly on 13.02.2018.
 */
@Data
@Accessors(chain = true)
@Entity
@Table(name = "file_items")
@EqualsAndHashCode(callSuper = true)
@SequenceGenerator(name = AbstractEntity.GENERATOR, sequenceName = "file_items_seq", allocationSize = 10)
public class FileItem extends AbstractAuditableEntity {

    @Column(columnDefinition = "TEXT")
    private String name;

    @Column(columnDefinition = "TEXT")
    private String url;

    @Enumerated(EnumType.STRING)
    @Column(name = "file_item_type")
    private FileItemType fileItemType;

    @Column(name = "item_order")
    private Integer order;

    @Column(name = "file_size")
    private Long fileSize;

    private Long ownerId;

    @Column(name = "visible_for_all")
    private Boolean visibleForAll = Boolean.FALSE;

    public Boolean getVisibleForAll() {
        if (visibleForAll == null) {
            visibleForAll = false;
        }
        return visibleForAll;
    }
}
