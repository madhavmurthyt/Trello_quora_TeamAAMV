package com.upgrad.quora.service.dao;

import com.upgrad.quora.service.entity.QuestionEntity;
import com.upgrad.quora.service.entity.UserEntity;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;

public class QuestionDao {
    @PersistenceContext
    EntityManager entityManager;

    public QuestionEntity createQuestion(QuestionEntity questionEntity){
        entityManager.persist(questionEntity);
        return questionEntity;
    }
    public List<QuestionEntity> getAllQuestion() {
        try {
            return entityManager.createNamedQuery("allQuestions", QuestionEntity.class).getResultList();
        } catch (NoResultException nre) {

        }
    }

    public List<QuestionEntity> getAllQuestionsByUser(long userId) {
        return entityManager.createNamedQuery("allQuestionsByUser", QuestionEntity.class)
                .setParameter("user_id", userId).getResultList();
    }

    public QuestionEntity deleteQuestion(QuestionEntity questionEntity) {
        entityManager.remove(questionEntity);
        return questionEntity;
     }
     public QuestionEntity getQuestionByUUID(String uuid) {
        try {
            return entityManager.createNamedQuery("questionByUuid", QuestionEntity.class).setParameter(
                    "uuid",
                    uuid).getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
}
