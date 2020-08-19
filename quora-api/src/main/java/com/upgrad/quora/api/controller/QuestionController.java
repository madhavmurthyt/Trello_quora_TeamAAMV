package com.upgrad.quora.api.controller;

import com.upgrad.quora.entity.QuestionEntity;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.UserNotFoundException;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class QuestionController {

    @RequestMapping(method = RequestMethod.GET, path="question/all/{userId}",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity getAllQuestionsByUser(
            @PathVariable("userId") String userId,
            @RequestHeader("authorization") String authorization)
            throws AuthorizationFailedException, UserNotFoundException {
String accessToken=AuthTokenParser.parseAuthToken(authorization){
    List<QuestionEntity> questionEntityList=questionService.getAllQuestionByUser(accessToken,userId)
        }

        }


}
