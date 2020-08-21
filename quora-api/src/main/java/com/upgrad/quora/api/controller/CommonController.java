package com.upgrad.quora.api.controller;

import com.upgrad.quora.service.business.UserService;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.api.model.UserDetailsResponse;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
public class CommonController {

  @Autowired
  private UserService userService;

    @RequestMapping(method = RequestMethod.GET, path = "/userprofile/{userId}")
    public ResponseEntity<UserDetailsResponse> getUser(@PathVariable("userId") final String userId, @RequestHeader("authorization") final String authorization) throws AuthorizationFailedException, UserNotFoundException {
        final UserEntity userEntity = userService.getUser(userId,authorization);
        UserDetailsResponse userDetailsResponse = new UserDetailsResponse().firstName(userEntity.getFirstName())
                .lastName(userEntity.getLastName()).userName(userEntity.getUserName()).emailAddress(userEntity.getEmail())
                .contactNumber(userEntity.getContactNumber()).aboutMe(userEntity.getAboutMe()).dob(userEntity.getDob()).country(userEntity.getCountry());
        return new ResponseEntity<UserDetailsResponse>(userDetailsResponse, HttpStatus.OK);
    }

}
