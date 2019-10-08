package com.technaxis.querydsl.repositories;

import com.technaxis.querydsl.model.AbstractAuditableDeletableEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.Optional;

/**
 * @author Dmitry Sadchikov
 */
@NoRepositoryBean
public interface JpaDeletableRepository<E extends AbstractAuditableDeletableEntity, ID> extends JpaRepository<E, ID> {

    boolean existsByIdAndDeletedFalse(ID id);

    Optional<E> findByIdAndDeletedFalse(ID id);
}
