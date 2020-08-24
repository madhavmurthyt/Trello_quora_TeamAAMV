package com.upgrad.quora.api.controller;

import com.upgrad.quora.api.model.*;
import com.upgrad.quora.service.business.AnswerService;
import com.upgrad.quora.service.business.QuestionService;
import com.upgrad.quora.service.entity.AnswerEntity;
import com.upgrad.quora.service.entity.QuestionEntity;
import com.upgrad.quora.service.entity.UserAuthTokenEntity;
import com.upgrad.quora.service.exception.AnswerNotFoundException;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.InvalidQuestionException;
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
@RequestMapping
public class AnswerController {
    @Autowired
    private AnswerService answerService;

    @Autowired
    private QuestionService questionService;

    @RequestMapping(method = RequestMethod.POST, path = "/question/{questionId}/answer/create", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_PROBLEM_JSON_UTF8_VALUE)
    public ResponseEntity<AnswerResponse> createAnswer(@PathVariable("questionId") final String questionId, @RequestHeader("authorization") final String authorization, AnswerRequest answerRequest) throws AuthorizationFailedException, InvalidQuestionException {

        UserAuthTokenEntity userAuthToken = answerService.getUserAuthToken(authorization);
        QuestionEntity questionByUUID = questionService.getQuestionByUUID(questionId);

        AnswerEntity answerEntity = new AnswerEntity();
        answerEntity.setUuid(UUID.randomUUID().toString());
        answerEntity.setAns(answerRequest.getAnswer());
        answerEntity.setDate(ZonedDateTime.now());
        answerEntity.setUser(userAuthToken.getUser());
        answerEntity.setQuestion(questionByUUID);
        final AnswerEntity createdAnswer = answerService.createAnswer(answerEntity);

        final AnswerResponse answerResponse = new AnswerResponse().id(createdAnswer.getUuid()).status("ANSWER CREATED");
        return new ResponseEntity<AnswerResponse>(answerResponse, HttpStatus.CREATED);
    }

    @RequestMapping(method = RequestMethod.PUT, path = "/answer/edit/{answerId}")
    public ResponseEntity<AnswerEditResponse> editAnswer(final AnswerEditRequest answerEditRequest, @PathVariable("answerId") final String uuId, @RequestHeader("authorization") final String authorization) throws AuthorizationFailedException, AnswerNotFoundException {

        final AnswerEntity updateAnswer = answerService.updateAnswer(uuId, answerEditRequest.getContent(), authorization);
        AnswerEditResponse answerEditResponse = new AnswerEditResponse().id(updateAnswer.getUuid()).status("ANSWER EDITED");
        return new ResponseEntity<AnswerEditResponse>(answerEditResponse, HttpStatus.OK);
    }


    @RequestMapping(method = RequestMethod.GET, path = "answer/all/{questionId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<List<AnswerDetailsResponse>> getAllAnswersForQuestion(@PathVariable("questionId") String questionId, @RequestHeader("authorization") String authorization) throws AuthorizationFailedException, InvalidQuestionException {
        final List<AnswerEntity> answerEntities = answerService.getAllAnswersToQuestion(questionId, authorization);
        List<AnswerDetailsResponse> answerDetailsResponses = new ArrayList<AnswerDetailsResponse>();
        for (AnswerEntity answerEntity : answerEntities) {
            answerDetailsResponses.add(new AnswerDetailsResponse().id(answerEntity.getUuid()).questionContent(answerEntity.getQuestion().getContent()).answerContent(answerEntity.getAns()));
        }
        return new ResponseEntity<>(answerDetailsResponses, HttpStatus.OK);

    }

    @RequestMapping(method = RequestMethod.DELETE, path = "/answer/delete/{answerId}", produces = MediaType.APPLICATION_PROBLEM_JSON_UTF8_VALUE)
    public ResponseEntity<AnswerDeleteResponse> deleteAnswer(@PathVariable("answerId") String answerId, @RequestHeader("authorization") final String authorization) throws AuthorizationFailedException, AnswerNotFoundException {

        UserAuthTokenEntity userAuthToken = answerService.getUserAuthToken(authorization);

        final AnswerEntity answerByUUID = answerService.getAnswerByUUID(answerId);
        answerService.deleteAnswer(answerByUUID, userAuthToken);

        final AnswerDeleteResponse answerDeleteResponse = new AnswerDeleteResponse().id(answerByUUID.getUuid()).status("ANSWER DELETED");

        return new ResponseEntity<AnswerDeleteResponse>(answerDeleteResponse, HttpStatus.OK);
    }

}
