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

    public QuestionEntity createQuestion(QuestionEntity questionEntity) {
        entityManager.persist(questionEntity);
        return questionEntity;
    }

    public List<QuestionEntity> getAllQuestion() {
        try {
            return entityManager.createNamedQuery("allQuestions", QuestionEntity.class).getResultList();
        } catch (NoResultException nre) {
            return null;
        }
    }

    public QuestionEntity getQuestion(final String uuId) {
        try {
            return entityManager.createNamedQuery("questionById", QuestionEntity.class).setParameter("uuid", uuId).getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }

    public QuestionEntity update(final QuestionEntity questionEntity) {
        return entityManager.merge(questionEntity);
    }

}
