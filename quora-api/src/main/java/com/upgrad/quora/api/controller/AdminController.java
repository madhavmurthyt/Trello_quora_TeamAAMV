package com.upgrad.quora.api.controller;

import com.upgrad.quora.service.business.AdminService;
import com.upgrad.quora.service.business.UserService;
import com.upgrad.quora.service.dao.UserDao;
import com.upgrad.quora.service.entity.UserAuthTokenEntity;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.upgrad.quora.api.model.UserDeleteResponse;


@RestController
@RequestMapping
public class AdminController {
    @Autowired
    private AdminService adminService;

    @RequestMapping(method = RequestMethod.DELETE, path = "/admin/user/{userId}")
    public ResponseEntity<UserDeleteResponse> deleteUser(final String userUuId, @RequestHeader("authorization")  final String authorization) throws AuthorizationFailedException, UserNotFoundException {
        final UserEntity userEntity = adminService.deleteUser(userUuId,authorization);
        UserDeleteResponse userDeleteResponse = new UserDeleteResponse().id(userUuId).status("USER SUCCESSFULLY DELETED");
        return new ResponseEntity<UserDeleteResponse>(userDeleteResponse, HttpStatus.OK);
    }

}
