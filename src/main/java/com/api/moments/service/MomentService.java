package com.api.moments.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.api.moments.model.Comment;
import com.api.moments.model.Moment;
import com.api.moments.repository.CommentRepository;
import com.api.moments.repository.MomentRepository;
import com.api.moments.util.MessageBuilder;
import com.api.moments.util.Response;

@Service
public class MomentService {

    @Autowired
    private MomentRepository momentRepository;

    @Autowired
    private CommentRepository commentRepository;

    public ResponseEntity<Response<Moment>> save(Moment moment) {
        try {
            Moment result = momentRepository.saveAndFlush(moment);
            return result != null
                    ? new ResponseEntity<Response<Moment>>(
                            new Response<Moment>(
                                    HttpStatus.OK, result,
                                    MessageBuilder.generate("Moment saved successfull")),
                            HttpStatus.OK)
                    : new ResponseEntity<Response<Moment>>(
                            new Response<Moment>(
                                    HttpStatus.BAD_REQUEST,
                                    null,
                                    MessageBuilder.generate("Error while saving moment")),
                            HttpStatus.BAD_REQUEST);

        } catch (Exception e) {
            return new ResponseEntity<Response<Moment>>(
                    new Response<Moment>(
                            HttpStatus.INTERNAL_SERVER_ERROR,
                            null,
                            MessageBuilder.generate(e.getMessage())),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Response<Moment>> update(Moment moment) {

        if (moment.getId() == null) {
            return new ResponseEntity<Response<Moment>>(
                    new Response<Moment>(
                            HttpStatus.NOT_ACCEPTABLE,
                            null,
                            MessageBuilder.generate("Moment id is required")),
                    HttpStatus.NOT_ACCEPTABLE);
        } else if (!momentRepository.existsById(moment.getId())) {
            return new ResponseEntity<Response<Moment>>(
                    new Response<Moment>(
                            HttpStatus.NOT_ACCEPTABLE,
                            null,
                            MessageBuilder.generate("Moment id is invalid")),
                    HttpStatus.NOT_ACCEPTABLE);
        }

        try {
            Moment result = momentRepository.saveAndFlush(moment);
            return result != null
                    ? new ResponseEntity<Response<Moment>>(
                            new Response<Moment>(
                                    HttpStatus.OK, result,
                                    MessageBuilder.generate("Moment saved successfull")),
                            HttpStatus.OK)
                    : new ResponseEntity<Response<Moment>>(
                            new Response<Moment>(
                                    HttpStatus.BAD_REQUEST,
                                    null,
                                    MessageBuilder.generate("Error while saving moment")),
                            HttpStatus.BAD_REQUEST);

        } catch (Exception e) {
            return new ResponseEntity<Response<Moment>>(
                    new Response<Moment>(
                            HttpStatus.INTERNAL_SERVER_ERROR,
                            null,
                            MessageBuilder.generate(e.getMessage())),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Response<List<Moment>>> list() {
        try {
            List<Moment> result = momentRepository.findAll();

            if (result.isEmpty()) {
                return new ResponseEntity<Response<List<Moment>>>(
                        new Response<List<Moment>>(
                                HttpStatus.NOT_FOUND,
                                null,
                                Arrays.asList("Moment list not found")),
                        HttpStatus.NOT_FOUND);
            }

            result.forEach(moment -> {
                List<Comment> comments = commentRepository.findByMomentId(moment.getId());
                comments = comments == null ? new ArrayList<>() : comments;

                comments.forEach(comment -> {
                    String username = comment.getUsername() == null ? "Anonymous user" : comment.getUsername();
                    comment.setUsername(username);
                });

                moment.setComments(comments);
            });

            return new ResponseEntity<Response<List<Moment>>>(
                    new Response<List<Moment>>(
                            HttpStatus.OK, result,
                            Arrays.asList("Moment list returned successfull")),
                    HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<Response<List<Moment>>>(
                    new Response<List<Moment>>(
                            HttpStatus.INTERNAL_SERVER_ERROR,
                            null,
                            Arrays.asList(e.getMessage())),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Response<Moment>> listById(Long idMoment) {
        if (idMoment == null) {
            return new ResponseEntity<Response<Moment>>(
                    new Response<Moment>(
                            HttpStatus.NOT_ACCEPTABLE,
                            null,
                            MessageBuilder.generate("Moment id is required")),
                    HttpStatus.NOT_ACCEPTABLE);
        }

        try {
            Optional<Moment> find = momentRepository.findById(idMoment);
            if (find.isPresent()) {
                Moment result = find.get();
                List<Comment> comments = commentRepository.findByMomentId(idMoment);

                comments.forEach(comment -> {
                    String username = comment.getUsername() == null ? "Anonymous user" : comment.getUsername();
                    comment.setUsername(username);
                });

                result.setComments(comments == null ? new ArrayList<>() : comments);

                return new ResponseEntity<Response<Moment>>(
                        new Response<Moment>(
                                HttpStatus.OK,
                                result,
                                MessageBuilder.generate("Moment returned successfull")),
                        HttpStatus.OK);
            } else {
                return new ResponseEntity<Response<Moment>>(
                        new Response<Moment>(
                                HttpStatus.NOT_FOUND,
                                null,
                                MessageBuilder.generate("Moment not found")),
                        HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<Response<Moment>>(
                    new Response<Moment>(
                            HttpStatus.INTERNAL_SERVER_ERROR,
                            null,
                            MessageBuilder.generate(e.getMessage())),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Response<Moment>> deleteById(Long idMoment) {
        if (idMoment == null) {
            return new ResponseEntity<Response<Moment>>(
                    new Response<Moment>(
                            HttpStatus.NOT_ACCEPTABLE,
                            null,
                            MessageBuilder.generate("Moment id is required")),
                    HttpStatus.NOT_ACCEPTABLE);
        }

        try {
            List<Comment> comments = commentRepository.findByMomentId(idMoment);
            commentRepository.deleteAll(comments);
            momentRepository.deleteById(idMoment);

            return new ResponseEntity<Response<Moment>>(
                    new Response<Moment>(
                            HttpStatus.OK,
                            null,
                            MessageBuilder.generate("Moment deleted successfull")),
                    HttpStatus.NOT_ACCEPTABLE);
        } catch (Exception e) {
            return new ResponseEntity<Response<Moment>>(
                    new Response<Moment>(
                            HttpStatus.INTERNAL_SERVER_ERROR,
                            null,
                            MessageBuilder.generate(e.getMessage())),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
