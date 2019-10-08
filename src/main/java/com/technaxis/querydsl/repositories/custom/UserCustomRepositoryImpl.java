package com.technaxis.querydsl.repositories.custom;

import javax.persistence.EntityManager;

import com.querydsl.jpa.impl.JPAQuery;
import com.technaxis.querydsl.dto.forms.UsersSearchForm;
import com.technaxis.querydsl.model.QUser;
import com.technaxis.querydsl.model.User;
import com.technaxis.querydsl.repositories.custom.helpers.UserRepositoryHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Repository;

/**
 * @author Dmitry Sadchikov
 */
@Repository
@RequiredArgsConstructor
public class UserCustomRepositoryImpl implements UserCustomRepository {

    private final EntityManager entityManager;

    @Override
    public Page<User> getUsersPage(final UsersSearchForm form) {
        final var query = new JPAQuery<User>(entityManager)
                .from(QUser.user)
                .where(UserRepositoryHelper.getPredicates(form))
                .orderBy(UserRepositoryHelper.getOrders(form.getSortType(), form.getOrder()))
                .limit(form.getLimit())
                .offset(form.getPage());
        return UserRepositoryHelper.pageBy(query, form);
    }
}
