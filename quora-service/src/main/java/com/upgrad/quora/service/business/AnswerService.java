package com.upgrad.quora.service.business;

import com.upgrad.quora.service.dao.AnswerDao;
import com.upgrad.quora.service.dao.UserDao;
import com.upgrad.quora.service.entity.AnswerEntity;
import com.upgrad.quora.service.entity.UserAuthTokenEntity;
import com.upgrad.quora.service.exception.AnswerNotFoundException;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.InvalidQuestionException;
import java.util.List;
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
    public AnswerEntity createAnswer(AnswerEntity answerEntity){
        AnswerEntity answer = answerDao.createAnswer(answerEntity);
        return answer;
    }


    public UserAuthTokenEntity getUserAuthToken(final String accesstoken) throws AuthorizationFailedException {
        UserAuthTokenEntity userAuthToken = answerDao.getUserAuthToken(accesstoken);
        if(userAuthToken == null)
        {
            throw new AuthorizationFailedException("ATHR-001","User has not signed in");
        }
        final ZonedDateTime signOutUserTime = userAuthToken.getLogoutAt();

        if(signOutUserTime!=null && userAuthToken!=null)
        {
            throw new AuthorizationFailedException("ATHR-002","User is signed out.Sign in first to get user details");
        }

        return userAuthToken;
    }

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

    public List<AnswerEntity> getAllAnswersToQuestion(Integer questionId) throws InvalidQuestionException {
        List<AnswerEntity> allAnswersToQuestion = answerDao.getAllAnswersToQuestion(questionId);
        if(allAnswersToQuestion == null){
            throw new InvalidQuestionException("QUES-001", "The question with entered uuid whose details are to be seen does not exist");
        }

        return allAnswersToQuestion;
    }

    public AnswerEntity getAnswerByUUID(final String questionUUID) throws AnswerNotFoundException {
        AnswerEntity answerEntity = answerDao.getAnswerByUUID(questionUUID);
        if(answerEntity == null){
            throw new AnswerNotFoundException("ANS-001","Entered answer uuid does not exist");
        }
        return answerEntity;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteAnswer(AnswerEntity answerEntity,UserAuthTokenEntity userAuthTokenEntity) throws AuthorizationFailedException{
        if ((answerEntity.getUser().getId() == userAuthTokenEntity.getUser().getId()) || userAuthTokenEntity.getUser().getRole().equals("admin") ) {
            answerDao.deleteAnswer(answerEntity);
        }else {
            throw new AuthorizationFailedException("ATHR-003","Only the answer owner or admin can delete the answer");
        }
    }
}

