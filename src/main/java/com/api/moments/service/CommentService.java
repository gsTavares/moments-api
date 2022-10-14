package com.api.moments.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.api.moments.model.Comment;
import com.api.moments.repository.CommentRepository;
import com.api.moments.util.MessageBuilder;
import com.api.moments.util.Response;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    public ResponseEntity<Response<Comment>> save(Comment comment) {
        if (comment.getMoment() == null || comment.getMoment().getId() == null) {
            return new ResponseEntity<Response<Comment>>(
                    new Response<Comment>(
                            HttpStatus.NOT_ACCEPTABLE,
                            null,
                            MessageBuilder.generate("Comment requires an valid moment ID to be saved")),
                    HttpStatus.NOT_ACCEPTABLE);
        }

        try {
            Comment result = commentRepository.saveAndFlush(comment);
            return result != null ? new ResponseEntity<Response<Comment>>(
                    new Response<Comment>(
                            HttpStatus.OK,
                            result,
                            MessageBuilder.generate("Comment saved successfull")),
                    HttpStatus.OK)
                    : new ResponseEntity<Response<Comment>>(
                            new Response<Comment>(
                                    HttpStatus.BAD_REQUEST,
                                    result,
                                    MessageBuilder.generate("Error while saving comment")),
                            HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<Response<Comment>>(
                new Response<Comment>(
                        HttpStatus.INTERNAL_SERVER_ERROR,
                        null,
                        MessageBuilder.generate(e.getMessage())),
                HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
