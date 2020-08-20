package com.upgrad.quora.service.business;


import com.upgrad.quora.service.dao.QuestionDao;
import com.upgrad.quora.service.dao.UserDao;
import com.upgrad.quora.service.entity.QuestionEntity;
import com.upgrad.quora.service.entity.UserAuthTokenEntity;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
public class QuestionService {

    @Autowired
    private UserDao userDao;
    @Autowired
    private QuestionDao questionDao;

    @Transactional(propagation = Propagation.REQUIRED)
    public QuestionEntity createQuestion(QuestionEntity questionEntity, final String authorization) throws AuthorizationFailedException, UserNotFoundException {
        UserAuthTokenEntity userAuthTokenEntity = userDao.getUserAuthToken(authorization);

        if (userAuthTokenEntity == null) throw new AuthorizationFailedException("ATHR-001", "User has not signed in");
        //if(!userAuthTokenEntity.getUser().getRole().equalsIgnoreCase("admin")) throw new AuthorizationFailedException("ATHR-003", "Unauthorized Access, Entered user is not an admin");
        if (userAuthTokenEntity.getLogoutAt() != null) throw new AuthorizationFailedException("ATHR-002", "User is signed out");
        questionDao.createQuestion(questionEntity);

        questionEntity.setUser(userAuthTokenEntity.getUser());
        return questionDao.createQuestion(questionEntity);

    }

    public List<QuestionEntity> getAllQuestion(final String authorization)
        throws AuthorizationFailedException, UserNotFoundException{
        UserAuthTokenEntity userAuthTokenEntity = userDao.getUserAuthToken(authorization);

        if (userAuthTokenEntity == null) throw new AuthorizationFailedException("ATHR-001", "User has not signed in");
        //if(!userAuthTokenEntity.getUser().getRole().equalsIgnoreCase("admin")) throw new AuthorizationFailedException("ATHR-003", "Unauthorized Access, Entered user is not an admin");
        if (userAuthTokenEntity.getLogoutAt() != null) throw new AuthorizationFailedException("ATHR-002", "User is signed out");
        return questionDao.getAllQuestion();

    }
}
