package com.upgrad.quora.service.business;

import com.upgrad.quora.service.dao.UserDao;
import com.upgrad.quora.service.entity.UserAuthTokenEntity;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.SignOutRestrictedException;
import java.time.ZonedDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SignOutBusinessService {


  @Autowired
  private UserDao userDao;

  @Transactional(propagation = Propagation.REQUIRED)
  public UserEntity signOut(final String accessToken) throws SignOutRestrictedException {
    UserAuthTokenEntity userAuthToken = userDao.checkToken(accessToken);
    UserEntity signoutUser = null;
    if (userAuthToken == null){
      throw new SignOutRestrictedException("SGR-001","User is not Signed in.");
    }
    else if (userAuthToken!= null && userAuthToken.getAccessToken().equals(accessToken)) {
      final ZonedDateTime now = ZonedDateTime.now();
      userAuthToken.setLogoutAt(now);
      signoutUser = userAuthToken.getUser();
      userDao.updateUserAuthToken(userAuthToken);
    }
    return signoutUser;
  }
  }
