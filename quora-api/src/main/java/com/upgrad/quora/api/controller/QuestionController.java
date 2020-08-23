package com.upgrad.quora.api.controller;

import com.upgrad.quora.api.model.*;
import com.upgrad.quora.service.business.QuestionService;
import com.upgrad.quora.service.entity.QuestionEntity;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.InvalidQuestionException;
import com.upgrad.quora.service.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
public class QuestionController {

    @Autowired
    private QuestionService questionService;

    @RequestMapping(method = RequestMethod.GET, path = "/question/all/{userId}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<QuestionDetailsResponse>> getAllQuestionsByUser(
            @PathVariable("userId") String userId,
            @RequestHeader("authorization") String authorization)
            throws AuthorizationFailedException, UserNotFoundException {

        List<QuestionEntity> questionList = questionService.getAllQuestionsByUser(userId, authorization);
        List<QuestionDetailsResponse> qDetailsList = new ArrayList<>();
        questionList.forEach((q) -> {
            QuestionDetailsResponse questionDetailsResponse = new QuestionDetailsResponse();
            questionDetailsResponse.content(q.getContent());
            questionDetailsResponse.id(q.getUuid());
            qDetailsList.add(questionDetailsResponse);
        });
        return new ResponseEntity<>(qDetailsList, HttpStatus.OK);
    }


    @RequestMapping(method = RequestMethod.DELETE, path = "/question/delete/{questionId}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<QuestionDeleteResponse> deleteQuestion(
            @PathVariable("questionId") String questionId,
            @RequestHeader("authorization") String authorization)
            throws AuthorizationFailedException, InvalidQuestionException {

        QuestionEntity deletedQuestion = questionService.deleteQuestion(questionId, authorization);
        QuestionDeleteResponse questionDeleteResponse =
                new QuestionDeleteResponse().id(deletedQuestion.getUuid()).status("QUESTION DELETED");
        return new ResponseEntity<>(questionDeleteResponse, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST, path = "/question/create", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<QuestionResponse> createQuestion(QuestionRequest questionRequest,
                                                           @RequestHeader("authorization")
                                                                   String authorization)
            throws AuthorizationFailedException, UserNotFoundException {
        QuestionEntity questionEntity = new QuestionEntity();
        //adding data to the entity object for db
        questionEntity.setUuid(UUID.randomUUID().toString());
        questionEntity.setContent(questionRequest.getContent());
        questionEntity.setCreateDate(ZonedDateTime.now());
        QuestionEntity createQuestionEntity = questionService.createQuestion(questionEntity, authorization);
        QuestionResponse questionResponse = new QuestionResponse().id(createQuestionEntity.getUuid()).status("Question Successfully Added");
        return new ResponseEntity<QuestionResponse>(questionResponse, HttpStatus.CREATED);

    }

    @RequestMapping(method = RequestMethod.GET, path = "/question/all", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<List<QuestionEntity>> getAllQuestion(@RequestHeader("authorization") String authorization)
            throws AuthorizationFailedException, UserNotFoundException {
        List<QuestionEntity> questionEntity = questionService.getAllQuestion(authorization);
        return new ResponseEntity<List<QuestionEntity>>(questionEntity, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.PUT, path = "/question/edit/{questionId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<QuestionEditResponse> editQuestion(@RequestHeader("authorization") String authorization,
                                                             @PathVariable("questionId") final String uuId, final QuestionEditRequest questionEditRequest)
            throws AuthorizationFailedException, UserNotFoundException, InvalidQuestionException {
        final QuestionEntity editQuestion = questionService.updateQuestion(uuId, questionEditRequest.getContent(), authorization);
        QuestionEditResponse questionEditResponse = new QuestionEditResponse().id(editQuestion.getUuid()).status("Question Edited");
        return new ResponseEntity<QuestionEditResponse>(questionEditResponse, HttpStatus.OK);
    }

}
