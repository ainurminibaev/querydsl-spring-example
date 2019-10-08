package com.technaxis.querydsl.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@Setter
@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
public abstract class AbstractAuditableEntity extends AbstractEntity {
	@CreatedDate
	@Type(type = "java.time.LocalDateTime")
	private LocalDateTime createdDateTime;

	@LastModifiedDate
	@Type(type = "java.time.LocalDateTime")
	private LocalDateTime lastModifiedDateTime;

	public AbstractAuditableEntity(Long id, LocalDateTime createdDateTime, LocalDateTime lastModifiedDateTime) {
		super(id);
		this.createdDateTime = createdDateTime;
		this.lastModifiedDateTime = lastModifiedDateTime;
	}
}

