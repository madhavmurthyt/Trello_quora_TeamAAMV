package com.upgrad.quora.service.business;

import com.upgrad.quora.service.dao.AnswerDao;
import com.upgrad.quora.service.dao.UserDao;
import com.upgrad.quora.service.entity.AnswerEntity;
import com.upgrad.quora.service.entity.UserAuthTokenEntity;
import com.upgrad.quora.service.exception.AnswerNotFoundException;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;

@Service
public class AnswerService {

    @Autowired
    AnswerDao answerDao;

    @Autowired
    UserDao userDao;

    @Transactional(propagation = Propagation.REQUIRED)
    public AnswerEntity updateAnswer(final String uuId,final String content, final String authorization) throws AuthorizationFailedException, AnswerNotFoundException {


        UserAuthTokenEntity userAuthTokenEntity = userDao.getUserAuthToken(authorization);
        if (userAuthTokenEntity == null) throw new AuthorizationFailedException("ATHR-001", "User has not signed in");
        if (userAuthTokenEntity.getLogoutAt()!=null) throw new AuthorizationFailedException("ATHR-002","User is signed out. Sign in first to edit an answer");

        if(!userAuthTokenEntity.getUser().getUuid().equalsIgnoreCase(answerDao.getAnswer(uuId).getUser().getUuid()))  throw new AuthorizationFailedException("ATHR-003", "Only the answer owner can edit the answer");
        if(answerDao.getAnswer(uuId) == null) throw new AnswerNotFoundException("ANS-001","Entered answer uuid does not exist");

        AnswerEntity answerEntity = new AnswerEntity();
        answerEntity.setId(answerDao.getAnswer(uuId).getId());
        answerEntity.setUuid(uuId);
        answerEntity.setAns(content);
        answerEntity.setDate(ZonedDateTime.now());
        answerEntity.setUser(userAuthTokenEntity.getUser());
        answerEntity.setQuestion(answerDao.getAnswer(uuId).getQuestion());
        return answerDao.update(answerEntity);

    }
}

