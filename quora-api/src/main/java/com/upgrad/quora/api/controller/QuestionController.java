package com.upgrad.quora.api.controller;

import com.upgrad.quora.api.model.QuestionDeleteResponse;
import com.upgrad.quora.service.business.QuestionService;
import com.upgrad.quora.api.model.QuestionDetailsResponse;
import com.upgrad.quora.service.entity.QuestionEntity;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class QuestionController {

    @Autowired
    private QuestionService questionService;

    @RequestMapping(method = RequestMethod.GET, path="question/all/{userId}",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<List> getAllQuestionsByUser( @PathVariable("userId") String userId,  @RequestHeader("authorization") String authorization)  throws AuthorizationFailedException, UserNotFoundException {
            final List<QuestionEntity> questionEntity = questionService.getAllQuestionsByUser(userId, authorization);
            List<QuestionDetailsResponse> questionDetailsResponse = new ArrayList<QuestionDetailsResponse>();
            for (QuestionEntity questionEntity1: questionEntity) {
                 questionDetailsResponse.add(new QuestionDetailsResponse().id(questionEntity1.getUuid()).content(questionEntity1.getContent()));
            }
        return new ResponseEntity<List>(questionDetailsResponse, HttpStatus.OK);
        }
    @RequestMapping(method = RequestMethod.DELETE, path = "/question/delete/{questionId}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<QuestionDeleteResponse> deleteQuestion(
            @PathVariable("questionId") String questionId,
            @RequestHeader("authorization") String authorization)
            throws AuthorizationFailedException, InvalidQuestionException {

        QuestionEntity deletedQuestion = questionService.deleteQuestion(questionId,authorization);
        QuestionDeleteResponse questionDeleteResponse =
                new QuestionDeleteResponse().id(deletedQuestion.getUuid()).status("QUESTION DELETED");
        return new ResponseEntity<>(questionDeleteResponse, HttpStatus.OK);
    }
}
