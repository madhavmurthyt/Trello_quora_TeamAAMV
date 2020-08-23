package com.upgrad.quora.service.business;


import com.upgrad.quora.service.dao.UserDao;
import com.upgrad.quora.service.entity.UserAuthTokenEntity;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


@Service
public class AdminService {

    @Autowired
    private UserDao userDao;

    @Transactional(propagation = Propagation.REQUIRED)
    public UserEntity deleteUser(final String userUuId, final String authorization) throws AuthorizationFailedException, UserNotFoundException {
        UserAuthTokenEntity userAuthTokenEntity = userDao.getUserAuthToken(authorization);

        if (userAuthTokenEntity == null) throw new AuthorizationFailedException("ATHR-001", "User has not signed in");
        if (!userAuthTokenEntity.getUser().getRole().equalsIgnoreCase("admin"))
            throw new AuthorizationFailedException("ATHR-003", "Unauthorized Access, Entered user is not an admin");
        if (userAuthTokenEntity.getLogoutAt() != null)
            throw new AuthorizationFailedException("ATHR-002", "User is signed out");

        UserEntity userEntity = userDao.getUser(userUuId);
        if (userEntity == null) throw new UserNotFoundException("USR-001", "User with entered uuid does not exist");
        userDao.deleteUser(userUuId);

        return userDao.getUser(userUuId);

    }
}


