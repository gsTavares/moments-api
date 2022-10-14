package com.api.moments.resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.api.moments.model.Comment;
import com.api.moments.service.CommentService;
import com.api.moments.util.Response;

@RestController
@RequestMapping(value = "/api-moments", produces = {MediaType.APPLICATION_JSON_VALUE})
public class CommentResource {
    
    @Autowired
    private CommentService service;

    @PostMapping(value = "/comment")
    public ResponseEntity<Response<Comment>> save(@RequestBody Comment comment) {
        return service.save(comment);
    }

}
