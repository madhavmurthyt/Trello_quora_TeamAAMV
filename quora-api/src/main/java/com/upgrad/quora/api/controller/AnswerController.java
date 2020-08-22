package com.upgrad.quora.api.controller;

import com.upgrad.quora.api.model.AnswerDetailsResponse;
import com.upgrad.quora.api.model.AnswerEditResponse;
import com.upgrad.quora.api.model.AnswerEditRequest;
import com.upgrad.quora.service.business.AnswerService;
import com.upgrad.quora.service.entity.AnswerEntity;
import com.upgrad.quora.service.entity.QuestionEntity;
import com.upgrad.quora.service.exception.AnswerNotFoundException;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.InvalidQuestionException;
import com.upgrad.quora.service.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping
public class AnswerController {
    @Autowired
    private AnswerService answerService;

    @RequestMapping(method = RequestMethod.PUT, path = "/answer/edit/{answerId}")
    public ResponseEntity<AnswerEditResponse> editAnswer(final AnswerEditRequest answerEditRequest, @PathVariable("answerId") final String uuId, @RequestHeader("authorization")  final String authorization) throws AuthorizationFailedException, AnswerNotFoundException {

        final AnswerEntity updateAnswer = answerService.updateAnswer(uuId,answerEditRequest.getContent(), authorization);
        AnswerEditResponse answerEditResponse = new AnswerEditResponse().id(updateAnswer.getUuid()).status("ANSWER EDITED");
        return new ResponseEntity<AnswerEditResponse>(answerEditResponse, HttpStatus.OK);
    }


    @RequestMapping(method = RequestMethod.GET, path="answer/all/{questionId}",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<List> getAllAnswersForQuestion(@PathVariable("questionId") String questionId, @RequestHeader("authorization") String authorization)  throws AuthorizationFailedException, InvalidQuestionException {
        final List<AnswerEntity> answerEntities = answerService.getAllAnswersToQuestion(questionId, authorization);
        List<AnswerDetailsResponse> answerDetailsResponses = new ArrayList<AnswerDetailsResponse>();
        for (AnswerEntity answerEntity: answerEntities) {
            answerDetailsResponses.add(new AnswerDetailsResponse().id(answerEntity.getUuid()).questionContent(answerEntity.getQuestion().getContent()).answerContent(answerEntity.getAns()));
        }
        return new ResponseEntity<List>(answerDetailsResponses, HttpStatus.OK);
    }

}
