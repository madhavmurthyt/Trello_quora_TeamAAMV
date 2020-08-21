package com.upgrad.quora.api.controller;

import com.upgrad.quora.api.model.SigninResponse;
import com.upgrad.quora.api.model.SignoutResponse;
import com.upgrad.quora.api.model.SignupUserRequest;
import com.upgrad.quora.api.model.SignupUserResponse;
import com.upgrad.quora.service.business.SignOutBusinessService;
import com.upgrad.quora.service.business.SigninBusinessService;
import com.upgrad.quora.service.business.SignupBusinessService;
import com.upgrad.quora.service.entity.UserAuthTokenEntity;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.AuthenticationFailedException;
import com.upgrad.quora.service.exception.SignOutRestrictedException;
import com.upgrad.quora.service.exception.SignUpConfictException;
import com.upgrad.quora.service.exception.SignUpRestrictedException;
import java.util.Base64;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class UserController {
  @Autowired
  private SignupBusinessService signupBusinessService;

  @Autowired
  private SigninBusinessService signinBusinessService;

  @Autowired
  private SignOutBusinessService signOutBusinessService;

  @RequestMapping(method = RequestMethod.POST, path = "/user/signup", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public ResponseEntity<SignupUserResponse> signUp(final SignupUserRequest signupUserRequest) throws SignUpRestrictedException, SignUpConfictException {

    final UserEntity userEntity = new UserEntity();
    userEntity.setUuid(UUID.randomUUID().toString());
    userEntity.setFirstName(signupUserRequest.getFirstName());
    userEntity.setLastName(signupUserRequest.getLastName());
    userEntity.setUserName(signupUserRequest.getUserName());
    userEntity.setEmail(signupUserRequest.getEmailAddress());
    userEntity.setPassword(signupUserRequest.getPassword());
    userEntity.setCountry(signupUserRequest.getCountry());
    userEntity.setAboutMe(signupUserRequest.getAboutMe());
    userEntity.setDob(signupUserRequest.getDob());
    userEntity.setContactNumber(signupUserRequest.getContactNumber());
    userEntity.setRole("nonAdmin");
    final UserEntity createdUserEntity = signupBusinessService.signup(userEntity);

    // we need to convert the user entity obeject to signup response object
    // as the user will get status and its id
    SignupUserResponse userResponse = new SignupUserResponse().id(createdUserEntity.getUuid()).status("USER SUCCESSFULLY REGISTERED");
    return new ResponseEntity<SignupUserResponse>(userResponse, HttpStatus.CREATED);
  }

  @RequestMapping(method= RequestMethod.POST, path="/user/signin", consumes= MediaType.APPLICATION_JSON_UTF8_VALUE, produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
  public ResponseEntity<SigninResponse> signIn(@RequestHeader("authorization") final String authorization) throws AuthenticationFailedException {
    byte[] decode = Base64.getDecoder().decode(authorization.split("Basic ")[1]);
    String decodedText = new String(decode);
    String[] decodedArray = decodedText.split(":");
    UserAuthTokenEntity userAuthToken = signinBusinessService.authenticate(decodedArray[0],decodedArray[1]);
    UserEntity user = userAuthToken.getUser();
    SigninResponse signinResponse= new SigninResponse().id(user.getUuid()).message("SIGNED IN SUCCESSFULLY");
    HttpHeaders headers = new HttpHeaders();
    // to send the auth token as header as it can not go in payload
    headers.add("access-token", userAuthToken.getAccessToken());
    return new ResponseEntity<SigninResponse>(signinResponse,headers, HttpStatus.OK);
  }
  @RequestMapping(method= RequestMethod.POST, path="/user/signout", produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
  public ResponseEntity<SignoutResponse> signOut(@RequestHeader("authorization") final String authorization) throws SignOutRestrictedException {
    String [] bearerToken = authorization.split("Bearer ");
    UserEntity signoutUser = signOutBusinessService.signOut(bearerToken[1]);
    SignoutResponse signoutResponse = new SignoutResponse().id(signoutUser.getUuid()).message("SIGNED OUT SUCCESSFULLY");
    return new ResponseEntity<SignoutResponse>(signoutResponse, HttpStatus.OK);
  }
}