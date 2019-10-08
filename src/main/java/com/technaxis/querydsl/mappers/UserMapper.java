package com.technaxis.querydsl.mappers;

import com.technaxis.querydsl.dto.UserDto;
import com.technaxis.querydsl.model.User;
import org.springframework.stereotype.Component;

import javax.annotation.Nullable;

/**
 * @author Dmitry Sadchikov
 */
@Component
public class UserMapper extends JsonMapper<User, UserDto> {

    @Nullable
    @Override
    public UserDto apply(@Nullable User user) {
        if (user == null) {
            return null;
        }
        return UserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .role(user.getRole())
                .bonuses(user.getBonuses())
                .promoCode(user.getPromoCode())
                .build();
    }
}
