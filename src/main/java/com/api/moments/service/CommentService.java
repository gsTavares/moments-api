package com.api.moments.service;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.api.moments.model.Comment;
import com.api.moments.repository.CommentRepository;
import com.api.moments.util.Response;

@Service
public class CommentService {
    
    @Autowired
    private CommentRepository commentRepository;

    public Response<Comment> save(Comment comment) {
        if(comment.getMoment() == null || comment.getMoment().getId() == null) {
            return new Response<Comment>(HttpStatus.NOT_ACCEPTABLE, null, Arrays.asList("Comment requires an valid moment to be sucessfuly posted"));
        }

        try {
            Comment result = commentRepository.saveAndFlush(comment);
            return result != null ? new Response<Comment>(HttpStatus.OK, result, Arrays.asList("Comment saved successfull!"))
                : new Response<Comment>(HttpStatus.BAD_REQUEST, null, Arrays.asList("Error while saving comment"));
        } catch (Exception e) {
            return new Response<Comment>(HttpStatus.INTERNAL_SERVER_ERROR, null, Arrays.asList(e.getMessage()));
        }
    }

}
