package com.upgrad.quora.service.dao;

import com.upgrad.quora.service.entity.AnswerEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class AnswerDao {


    @PersistenceContext
    private EntityManager entityManager;


    public AnswerEntity update(final AnswerEntity answerEntity) {
        return entityManager.merge(answerEntity);
    }

    public AnswerEntity getAnswer(final String uuId) {
        try {
            return entityManager.createNamedQuery("answerById", AnswerEntity.class).setParameter("uuid", uuId).getSingleResult();
        }catch (NoResultException nre) {
            return null;
        }
    }


    public List<AnswerEntity> getAnswersByQuestion(final int questionId) {
        try {
             return entityManager.createNamedQuery("answerByQuestion", AnswerEntity.class).setParameter("question_id", questionId).getResultList();
        }catch (NoResultException nre) {
            return null;
        }
    }

}