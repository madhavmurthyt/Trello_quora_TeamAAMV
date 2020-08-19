package com.upgrad.quora.service.dao;

import com.upgrad.quora.entity.UserAuthEntity;
import com.upgrad.quora.entity.UserEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

@Repository
public class UserDao {
    @PersistenceContext
    EntityManager entityManager;

    public UserAuthEntity getUserAuth(String accessToken) {
        try {
            return entityManager.createNamedQuery("userAuthTokenByAccessToken", UserAuthEntity.class)
                    .setParameter("accessToken", accessToken).getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
    public UserEntity getUserByUUID(String uuid) {
        try {
            return entityManager.createNamedQuery("userByUuid", UserEntity.class).setParameter(
                    "uuid", uuid).getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
}
