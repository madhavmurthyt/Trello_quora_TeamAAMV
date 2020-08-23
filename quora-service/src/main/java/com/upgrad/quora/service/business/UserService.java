package com.upgrad.quora.service.business;


import com.upgrad.quora.service.dao.UserDao;
import com.upgrad.quora.service.entity.UserAuthTokenEntity;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    UserDao userDao;

    // public UserEntity getImage(final String Uuid, final String authorization) throws AuthorizationFailedException, UserNotFoundException {

    public UserEntity getUser(final String userUuId, final String authorization) throws AuthorizationFailedException, UserNotFoundException {
        UserEntity userEntity = userDao.getUser(userUuId);

        if (userEntity == null) throw new UserNotFoundException("USR-001", "User with entered uuid does not exist");

        UserAuthTokenEntity userAuthTokenEntity = userDao.getUserAuthToken(authorization);
        if (userAuthTokenEntity == null) throw new AuthorizationFailedException("ATHR-001", "User not Signed in");

        if (userAuthTokenEntity.getLogoutAt() != null)
            throw new AuthorizationFailedException("ATHR-002", "User is signed out. Sign in first to get user details");

        return userDao.getUser(userUuId);
    }

}
