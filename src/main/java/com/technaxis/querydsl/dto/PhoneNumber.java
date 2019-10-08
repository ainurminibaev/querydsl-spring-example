package com.technaxis.querydsl.dto;

import com.technaxis.querydsl.utils.AppConstants;
import com.technaxis.querydsl.utils.HttpResponseStatus;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.annotation.Nullable;
import javax.validation.constraints.Pattern;

/**
 * gordeevnm@gmail.com
 * 9/24/18
 */
@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PhoneNumber {

    @Pattern(regexp = AppConstants.VALID_PHONE_REGEX, message = HttpResponseStatus.INVALID_PARAM)
    private String phone;

    public String getPhone() {
        if (phone == null) {
            return null;
        }
        return cleanUp(phone);
    }

    @Nullable
    public static String cleanUp(String phone) {
        if (phone == null) {
            return null;
        }
        if (phone.startsWith("8")) {
            return phone.replaceFirst("8", "+7");
        } else if (phone.startsWith("7")) {
            return phone.replaceFirst("7", "+7");
        }
        return phone;
    }

    @Override
    public String toString() {
        return getPhone();
    }
}
