package com.upgrad.quora.api.controller;

import com.upgrad.quora.api.model.AnswerEditRequest;
import com.upgrad.quora.api.model.AnswerEditResponse;
import com.upgrad.quora.service.business.AnswerService;
import com.upgrad.quora.service.entity.AnswerEntity;
import com.upgrad.quora.service.exception.AnswerNotFoundException;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



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

}
