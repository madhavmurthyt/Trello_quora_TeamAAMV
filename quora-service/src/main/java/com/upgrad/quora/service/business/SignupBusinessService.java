package com.upgrad.quora.service.business;

import com.upgrad.quora.service.dao.UserDao;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.SignUpConfictException;
import com.upgrad.quora.service.exception.SignUpRestrictedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SignupBusinessService {
    @Autowired
    private PasswordCryptographyProvider cryptographyProvider;

    @Autowired
    private UserDao userDao;

    @Transactional(propagation = Propagation.REQUIRED)
    public UserEntity signup(UserEntity userEntity) throws SignUpRestrictedException, SignUpConfictException {
        UserEntity checkedUsername = userDao.checkUserName(userEntity.getUserName());
        UserEntity checkedUseremail = userDao.checkUserEmail(userEntity.getEmail());
        if (checkedUsername != null) {
            throw new SignUpConfictException("SGR-001", "Try any other Username, this Username has already been taken. "); //Dhruv
        }
        if (checkedUseremail != null) {
            throw new SignUpConfictException("SGR-002", "This user has already been registered, try with any other emailId. ");
        }
        String password = userEntity.getPassword();
        String[] encryptedText = cryptographyProvider.encrypt(userEntity.getPassword());
        userEntity.setSalt(encryptedText[0]);
        userEntity.setPassword(encryptedText[1]);
        return userDao.createUser(userEntity);
    }
}