package com.technaxis.querydsl.repositories.custom;

import com.technaxis.querydsl.dto.forms.UsersSearchForm;
import com.technaxis.querydsl.model.User;
import org.springframework.data.domain.Page;

/**
 * @author Dmitry Sadchikov
 */
public interface UserCustomRepository {

    Page<User> getUsersPage(UsersSearchForm form);
}
