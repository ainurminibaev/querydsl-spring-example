package com.technaxis.querydsl.dto.forms;

import com.querydsl.core.types.Order;
import com.technaxis.querydsl.model.User;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Dmitry Sadchikov
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class UsersSearchForm extends SearchablePageableForm {
    private User.Role role;
    private User.SortType sortType = User.SortType.ROLE;
    private Order order = Order.ASC;
    // TODO: 26/09/2019 other fields
}
