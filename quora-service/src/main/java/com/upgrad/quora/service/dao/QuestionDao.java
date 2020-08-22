package com.upgrad.quora.service.dao;

import com.upgrad.quora.service.entity.QuestionEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class QuestionDao {

    @PersistenceContext
    EntityManager entityManager;

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
