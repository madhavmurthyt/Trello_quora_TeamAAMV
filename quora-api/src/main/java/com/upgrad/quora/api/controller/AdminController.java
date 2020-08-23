package com.upgrad.quora.api.controller;

import com.upgrad.quora.api.model.UserDeleteResponse;
import com.upgrad.quora.service.business.AdminService;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping
public class AdminController {
    @Autowired
    private AdminService adminService;

    @RequestMapping(method = RequestMethod.DELETE, path = "/admin/user/{userId}")
    public ResponseEntity<UserDeleteResponse> deleteUser(@PathVariable("userId") final String userUuId, @RequestHeader("authorization") final String authorization) throws AuthorizationFailedException, UserNotFoundException {
        final UserEntity userEntity = adminService.deleteUser(userUuId, authorization);
        UserDeleteResponse userDeleteResponse = new UserDeleteResponse().id(userUuId).status("USER SUCCESSFULLY DELETED");
        return new ResponseEntity<UserDeleteResponse>(userDeleteResponse, HttpStatus.OK);
    }

}
