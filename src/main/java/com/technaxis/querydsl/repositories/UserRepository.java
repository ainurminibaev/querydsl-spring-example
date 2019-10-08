package com.technaxis.querydsl.repositories;

import com.technaxis.querydsl.model.User;
import com.technaxis.querydsl.repositories.custom.UserCustomRepository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * 28.10.18
 *
 * @author Dinar Rafikov
 * @version 1.0
 */
public interface UserRepository extends JpaRepository<User, Long>, UserCustomRepository {
	Optional<User> findByEmailIgnoreCase(String email);
}
