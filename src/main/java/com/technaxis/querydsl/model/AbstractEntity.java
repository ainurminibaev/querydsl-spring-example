package com.technaxis.querydsl.model;

import com.google.common.base.Objects;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@Data
@MappedSuperclass
@NoArgsConstructor
@AllArgsConstructor
public abstract class AbstractEntity {

	public static final String ID_COLUMN_NAME = "id";
	public static final String GENERATOR = "seq_generator";

	@Id
	@Column(name = ID_COLUMN_NAME)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = GENERATOR)
	private Long id;

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		AbstractEntity that = (AbstractEntity) o;
		return Objects.equal(id, that.id);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(id);
	}
}


