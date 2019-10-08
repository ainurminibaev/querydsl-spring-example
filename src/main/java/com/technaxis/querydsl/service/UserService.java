package com.technaxis.querydsl.service;

import com.technaxis.querydsl.dto.forms.UsersSearchForm;
import com.technaxis.querydsl.exceptions.NotFoundException;
import com.technaxis.querydsl.model.User;
import com.technaxis.querydsl.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

/**
 * 29.10.18
 *
 * @author Dinar Rafikov
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
public class UserService {

	private final UserRepository userRepository;

	public User get(Long userId) {
		return userRepository.findById(userId)
				.orElseThrow(() -> new NotFoundException("User with this id not found"));
	}

	public Page<User> search(UsersSearchForm form) {
		return userRepository.getUsersPage(form);
	}
}
