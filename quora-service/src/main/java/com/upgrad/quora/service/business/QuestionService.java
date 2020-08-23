package com.upgrad.quora.service.business;

import com.upgrad.quora.service.dao.QuestionDao;
import com.upgrad.quora.service.dao.UserDao;
import com.upgrad.quora.service.entity.QuestionEntity;
import com.upgrad.quora.service.entity.UserAuthTokenEntity;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.InvalidQuestionException;
import com.upgrad.quora.service.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.List;

@Service
public class QuestionService {
    @Autowired
    QuestionDao questionDao;

    @Autowired
    UserDao userDao;

    public List<QuestionEntity> getAllQuestionsByUser(String userId, String accessToken)
            throws AuthorizationFailedException, UserNotFoundException {
        UserAuthTokenEntity userAuth = userDao.getUserAuthToken(accessToken);
        if (userAuth == null) {
            throw new AuthorizationFailedException("ATHR-001", "User has not signed in");
        } else if (userAuth.getLogoutAt() != null) {
            throw new AuthorizationFailedException("ATHR-002", "User is signed out.Sign in first to get "
                    + "all questions posted by a specific user");
        }
        UserEntity userEntity = userDao.getUser(userId);
        if (userEntity == null) {
            throw new UserNotFoundException("USR-001", "User with entered uuid whose question details "
                    + "are to be seen does not exist");
        }
        return questionDao.getAllQuestionsByUser(userEntity.getId());
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public QuestionEntity deleteQuestion(String uuid, String accessToken)
            throws AuthorizationFailedException, InvalidQuestionException {
        UserAuthTokenEntity userAuth = userDao.getUserAuthToken(accessToken);
        if (userAuth == null) {
            throw new AuthorizationFailedException("ATHR-001", "User has not signed in");
        } else if (userAuth.getLogoutAt() != null) {
            throw new AuthorizationFailedException("ATHR-002", "User is signed out.Sign in first to "
                    + "delete a question");
        }
        QuestionEntity question = questionDao.getQuestionByUUID(uuid);
        if (question == null) {
            throw new InvalidQuestionException("QUES-001", "Entered question uuid does not exist");
        }
        UserEntity authUser = userAuth.getUser();
        if (authUser.getRole().equals("nonadmin") && !authUser.getUserName()
                .equals(question.getUser().getUserName())) {
            throw new AuthorizationFailedException("ATHR-003", "Only the question owner or admin can "
                    + "delete the question");
        }
        QuestionEntity deletedQuestion = questionDao.deleteQuestion(question);
        return deletedQuestion;
    }

    public QuestionEntity getQuestionByUUID(final String questionUUID) throws InvalidQuestionException {
        QuestionEntity questionEntity = questionDao.getQuestionByUUID(questionUUID);
        if (questionEntity == null) {
            throw new InvalidQuestionException("QUES-001", "The question entered is invalid");
        }
        return questionEntity;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public QuestionEntity createQuestion(QuestionEntity questionEntity, final String authorization) throws AuthorizationFailedException, UserNotFoundException {
        UserAuthTokenEntity userAuthTokenEntity = userDao.getUserAuthToken(authorization);
        if (userAuthTokenEntity == null) throw new AuthorizationFailedException("ATHR-001", "User has not signed in");
        //if(!userAuthTokenEntity.getUser().getRole().equalsIgnoreCase("admin")) throw new AuthorizationFailedException("ATHR-003", "Unauthorized Access, Entered user is not an admin");
        if (userAuthTokenEntity.getLogoutAt() != null)
            throw new AuthorizationFailedException("ATHR-002", "User is signed out");

        questionEntity.setUser(userAuthTokenEntity.getUser());
        return questionDao.createQuestion(questionEntity);
    }

    public List<QuestionEntity> getAllQuestion(final String authorization)
            throws AuthorizationFailedException {
        UserAuthTokenEntity userAuthTokenEntity = userDao.getUserAuthToken(authorization);

        if (userAuthTokenEntity == null) throw new AuthorizationFailedException("ATHR-001", "User has not signed in");
        if (userAuthTokenEntity.getLogoutAt() != null)
            throw new AuthorizationFailedException("ATHR-002", "User is signed out");
        return questionDao.getAllQuestion();

    }

    @Transactional(propagation = Propagation.REQUIRED)
    public QuestionEntity updateQuestion(final String uuId, final String content, final String authorization) throws AuthorizationFailedException, InvalidQuestionException {
        UserAuthTokenEntity userAuthTokenEntity = userDao.getUserAuthToken(authorization);
        if (userAuthTokenEntity == null) throw new AuthorizationFailedException("ATHR-001", "User has not signed in");
        if (userAuthTokenEntity.getLogoutAt() != null)
            throw new AuthorizationFailedException("ATHR-002", "User is signed out. Sign in first to edit an question");

        if (questionDao.getQuestion(uuId) == null)
            throw new InvalidQuestionException("ANS-001", "Entered question uuid does not exist");
        if (!userAuthTokenEntity.getUser().getUuid().equalsIgnoreCase(questionDao.getQuestion(uuId).getUser().getUuid()))
            throw new AuthorizationFailedException("ATHR-003", "Only the question owner can edit the question");

        QuestionEntity questionEntity = new QuestionEntity();
        questionEntity.setId(questionDao.getQuestion(uuId).getId());
        questionEntity.setUuid(uuId);
        questionEntity.setContent(content);
        questionEntity.setCreateDate(ZonedDateTime.now());
        questionEntity.setUser(userAuthTokenEntity.getUser());

        return questionDao.update(questionEntity);

    }

}
