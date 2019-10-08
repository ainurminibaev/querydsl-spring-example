package com.technaxis.querydsl.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.MappedSuperclass;

/**
 * @author Dmitry Sadchikov
 */
@Data
@EqualsAndHashCode(callSuper = true)
@MappedSuperclass
public class AbstractAuditableDeletableEntity extends AbstractAuditableEntity {
    private Boolean deleted = Boolean.FALSE;
}
