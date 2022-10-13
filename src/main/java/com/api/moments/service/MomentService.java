package com.api.moments.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.api.moments.model.Comment;
import com.api.moments.model.Moment;
import com.api.moments.repository.CommentRepository;
import com.api.moments.repository.MomentRepository;
import com.api.moments.util.Response;

@Service
public class MomentService {

    @Autowired
    private MomentRepository momentRepository;

    @Autowired
    private CommentRepository commentRepository;

    public Response<Moment> save(Moment moment) {
        try {
            Moment result = momentRepository.saveAndFlush(moment);
            return result != null
                    ? new Response<Moment>(HttpStatus.OK, result, Arrays.asList("Moment saved successfull!"))
                    : new Response<Moment>(HttpStatus.BAD_REQUEST, null, Arrays.asList("Error while savind moment..."));

        } catch (Exception e) {
            return new Response<Moment>(HttpStatus.INTERNAL_SERVER_ERROR, null, Arrays.asList(e.getMessage()));
        }
    }

    public Response<Moment> update(Moment moment) {

        if(moment.getId() == null) {
            return new Response<Moment>(HttpStatus.NOT_ACCEPTABLE, null, Arrays.asList("Moment id is required"));
        } else if(!momentRepository.existsById(moment.getId())) {
            return new Response<Moment>(HttpStatus.NOT_ACCEPTABLE, null, Arrays.asList("Moment id is invalid"));
        }

        try {
            Moment result = momentRepository.saveAndFlush(moment);
            return result != null
                    ? new Response<Moment>(HttpStatus.OK, result, Arrays.asList("Moment saved successfull!"))
                    : new Response<Moment>(HttpStatus.BAD_REQUEST, null, Arrays.asList("Error while savind moment..."));

        } catch (Exception e) {
            return new Response<Moment>(HttpStatus.INTERNAL_SERVER_ERROR, null, Arrays.asList(e.getMessage()));
        }
    }

    public Response<List<Moment>> list() {
        try {
            List<Moment> result = momentRepository.findAll();

            if(result.isEmpty()) {
                return new Response<List<Moment>>(HttpStatus.NOT_FOUND, null, Arrays.asList("Moment list not found"));
            }

            result.forEach(moment -> {
                List<Comment> comments = commentRepository.findAll();
                comments = comments == null ? new ArrayList<>() : comments;

                comments.forEach(comment -> {
                    String username = comment.getUsername() == null ? "Anonymous user" : comment.getUsername();
                    comment.setUsername(username);
                });

                moment.setComments(comments);
            });

            return new Response<List<Moment>>(HttpStatus.OK, result, Arrays.asList("Moment list returned successfull"));
        } catch (Exception e) {
            return new Response<List<Moment>>(HttpStatus.INTERNAL_SERVER_ERROR, null, Arrays.asList(e.getMessage()));
        }
    }

    public Response<Moment> listById(Long idMoment) {
        if(idMoment == null) {
            return new Response<Moment>(HttpStatus.NOT_ACCEPTABLE, null, Arrays.asList("Moment id is required"));
        }

        try {
            Optional<Moment> find = momentRepository.findById(idMoment);
            if(find.isPresent()) {
                Moment result = find.get();
                List<Comment> comments = commentRepository.findByMomentId(idMoment);

                comments.forEach(comment -> {
                    String username = comment.getUsername() == null ? "Anonymous user" : comment.getUsername();
                    comment.setUsername(username);
                });

                result.setComments(comments == null ? new ArrayList<>() : comments);

                return new Response<Moment>(HttpStatus.OK, result, Arrays.asList("Moment returned sucessfull!"));
            } else {
                return new Response<Moment>(HttpStatus.NOT_FOUND, null, Arrays.asList("Moment not found"));
            }
        } catch (Exception e) {
            return new Response<Moment>(HttpStatus.INTERNAL_SERVER_ERROR, null, Arrays.asList(e.getMessage()));
        }
    }

    public Response<Moment> deleteById(Long idMoment) {
        if(idMoment == null) {
            return new Response<Moment>(HttpStatus.NOT_ACCEPTABLE, null, Arrays.asList("Moment id is required"));
        }

        try {
            List<Comment> comments = commentRepository.findByMomentId(idMoment);
            commentRepository.deleteAll(comments);
            momentRepository.deleteById(idMoment);

            return new Response<Moment>(HttpStatus.OK, null, Arrays.asList("Moment deleted sucessfull"));
        } catch (Exception e) {
            return new Response<Moment>(HttpStatus.INTERNAL_SERVER_ERROR, null, Arrays.asList(e.getMessage()));
        }
    }

}
