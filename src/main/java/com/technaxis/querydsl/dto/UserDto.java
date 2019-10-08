package com.technaxis.querydsl.dto;

import com.technaxis.querydsl.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Dmitry Sadchikov
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private Long id;
    private String email;
    private String phone;
    private String name;
    private Integer bonuses;
    private String promoCode;
    private User.Role role;
}
