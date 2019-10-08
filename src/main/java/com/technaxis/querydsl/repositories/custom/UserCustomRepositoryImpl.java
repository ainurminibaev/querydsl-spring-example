package com.technaxis.querydsl.repositories.custom;

import com.querydsl.jpa.impl.JPAQuery;
import com.technaxis.querydsl.dto.forms.UsersSearchForm;
import com.technaxis.querydsl.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

/**
 * @author Dmitry Sadchikov
 */
@Repository
@RequiredArgsConstructor
public class UserCustomRepositoryImpl implements UserCustomRepository {

    private final EntityManager entityManager;

    @Override
    public Page<User> getUsersPage(final UsersSearchForm form) {
        // TODO: 26/09/2019 implementation using QueryDSL

        return Page.empty();
    }
}
