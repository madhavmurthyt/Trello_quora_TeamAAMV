package com.upgrad.quora.service.dao;

import com.upgrad.quora.entity.QuestionEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
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

}
