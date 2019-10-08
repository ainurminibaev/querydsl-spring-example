package com.technaxis.querydsl.controller;

import com.technaxis.querydsl.dto.PageableListDto;
import com.technaxis.querydsl.dto.Response;
import com.technaxis.querydsl.dto.UserDto;
import com.technaxis.querydsl.dto.forms.UsersSearchForm;
import com.technaxis.querydsl.mappers.PageableListMapper;
import com.technaxis.querydsl.mappers.UserMapper;
import com.technaxis.querydsl.model.User;
import com.technaxis.querydsl.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Dmitry Sadchikov
 */
@Slf4j
@Api(tags = {"Common.Users"})
@RestController
@RequestMapping(UserController.ROOT_URL)
@RequiredArgsConstructor
public class UserController {

    public static final String ROOT_URL = "/v1/users";
    private static final String SEARCH_URL = "/search";

    private final UserService userService;
    private final UserMapper userMapper;
    private final PageableListMapper<User, UserDto> pageableListMapper;

    @ApiOperation(value = "Search users")
    @PostMapping(SEARCH_URL)
    public Response<PageableListDto<UserDto>> search(@RequestBody final UsersSearchForm form) {
        return new Response<>(pageableListMapper.apply(userService.search(form), userMapper::apply));
    }
}
