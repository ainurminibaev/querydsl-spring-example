package com.technaxis.querydsl.repositories.custom.helpers;

import java.util.List;

import com.google.common.collect.Lists;
import com.querydsl.core.types.Predicate;
import com.technaxis.querydsl.dto.forms.UsersSearchForm;
import com.technaxis.querydsl.model.QUser;
import com.technaxis.querydsl.utils.StringUtils;

public class UserRepositoryHelper extends RepositoryHelper {

    public static Predicate[] getPredicates(UsersSearchForm form) {
        QUser user = QUser.user;
        List<Predicate> predicates = Lists.newArrayList();
        if (form.getRole() != null) {
            predicates.add(user.role.eq(form.getRole()));
        }
        String formattedQuery = form.getFormattedQuery();
        if (StringUtils.isNotBlank(formattedQuery)) {
            predicates.add(
                    user.name.lower().like(formattedQuery)
                            .or(user.id.like(formattedQuery))
            );
        }
        return predicates.toArray(new Predicate[0]);
    }
}
