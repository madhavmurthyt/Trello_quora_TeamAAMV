package com.upgrad.quora.service.dao;

<<<<<<< HEAD
=======
<<<<<<< HEAD
import com.upgrad.quora.entity.UserAuthEntity;
import com.upgrad.quora.entity.UserEntity;
import org.springframework.stereotype.Repository;
=======
>>>>>>> c406c8e5e5919d5ea8095fe3ce2b9138e055c3aa
import com.upgrad.quora.service.entity.UserAuthTokenEntity;
import com.upgrad.quora.service.entity.UserEntity;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

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

    public UserEntity getUser(final String userUuId) {
        try {
            return entityManager.createNamedQuery("userById", UserEntity.class).setParameter("uuid", userUuId).getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }

    public UserAuthTokenEntity getUserAuthToken(final String accesstoken) {
        try {
            return entityManager.createNamedQuery("userAuthTokenByAccessToken", UserAuthTokenEntity.class).setParameter("accessToken", accesstoken).getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }

    public boolean deleteUser(final String userUuId) {
        try {
            entityManager.joinTransaction();
            entityManager.createQuery("delete from UserEntity d where d.uuid = :uuid").setParameter("uuid", userUuId).executeUpdate();
            return true;
        } catch (NullPointerException nulle) {
            return false;
        }
    }

}
