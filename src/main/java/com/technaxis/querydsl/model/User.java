package com.technaxis.querydsl.model;

import com.querydsl.core.types.dsl.ComparableExpressionBase;
import com.technaxis.querydsl.model.enums.sorting.ISortType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.util.Arrays;
import java.util.List;

/**
 * 28.10.18
 *
 * @author Dinar Rafikov
 * @version 1.0
 */
@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Table(name = "users")
@SequenceGenerator(name = AbstractEntity.GENERATOR, sequenceName = "users_seq", allocationSize = 10)
public class User extends AbstractAuditableDeletableEntity {

	public static final String EMAIL = "email";
	public static final String NAME = "name";
	public static final String PHONE = "phone";
	public static final String ROLE = "role";

	@Column(name = EMAIL)
	private String email;

	private String passwordHash;

	@Column(name = PHONE)
	private String phone;

	@Column(name = NAME)
	private String name;

	@Builder.Default
	private Boolean phoneConfirmed = Boolean.FALSE;

	@Column(name = ROLE)
	@Enumerated(EnumType.STRING)
	private Role role;

	@Builder.Default
	private Integer bonuses = 0;

	@Column(unique = true)
	private String promoCode;

	public enum Role {
		NOT_CONFIRMED,
		CLIENT,
		ADMIN,
		SUPER_ADMIN;

		public static final List<Role> ADMIN_ROLES = Arrays.asList(ADMIN, SUPER_ADMIN);
	}

	public enum SortType implements ISortType {

		ID(QUser.user.id),
		BONUSES(QUser.user.bonuses),
		EMAIL(QUser.user.email),
		NAME(QUser.user.name),
		PHONE(QUser.user.phone),
		ROLE(QUser.user.role);

		@Getter
		private ComparableExpressionBase[] expressions;

		SortType(ComparableExpressionBase... expressions) {
			this.expressions = expressions;
		}
	}
}
