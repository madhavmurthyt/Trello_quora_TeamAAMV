package com.upgrad.quora.service.business;

import com.upgrad.quora.entity.QuestionEntity;
import com.upgrad.quora.entity.UserAuthEntity;
import com.upgrad.quora.entity.UserEntity;
import com.upgrad.quora.service.dao.QuestionDao;
import com.upgrad.quora.service.dao.UserDao;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuestionService {
    @Autowired
    QuestionDao questionDao;

    @Autowired
    UserDao userDao;

    public List<QuestionEntity> getAllQuestionsByUser(String accessToken, String userId)
            throws AuthorizationFailedException, UserNotFoundException {
        UserAuthEntity userAuth = userDao.getUserAuth(accessToken);
        if (userAuth == null) {
            throw new AuthorizationFailedException("ATHR-001", "User has not signed in");
        } else if (userAuth.getLogoutAt() != null) {
            throw new AuthorizationFailedException("ATHR-002", "User is signed out.Sign in first to get "
                    + "all questions posted by a specific user");
        }
        UserEntity userEntity = userDao.getUserByUUID(userId);
        if (userEntity == null) {
            throw new UserNotFoundException("USR-001", "User with entered uuid whose question details "
                    + "are to be seen does not exist");
        }
        return questionDao.getAllQuestionsByUser(userEntity.getId());
    }


}
