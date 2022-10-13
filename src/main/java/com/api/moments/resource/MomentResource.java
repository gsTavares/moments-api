package com.api.moments.resource;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.api.moments.model.Moment;
import com.api.moments.service.MomentService;
import com.api.moments.util.Response;

@RestController
@RequestMapping(value = "/api-moments", produces = {MediaType.APPLICATION_JSON_VALUE})
public class MomentResource {
    
    @Autowired
    private MomentService service;

    @PostMapping(value = "/moment")
    public Response<Moment> save(@RequestBody Moment moment) {
        return service.save(moment);
    }

    @PutMapping(value = "/moment")
    public Response<Moment> update(@RequestBody Moment moment) {
        return service.update(moment);
    }

    @GetMapping(value = "/moments")
    public Response<List<Moment>> list() {
        return service.list();
    }

    @GetMapping(value = "/moments/{idMoment}")
    public Response<Moment> listById(@PathVariable(value = "idMoment") Long idMoment) {
        return service.listById(idMoment);
    }

    @DeleteMapping(value = "/moments/{idMoment}")
    public Response<Moment> deleteById(@PathVariable(value = "idMoment") Long idMoment) {
        return service.deleteById(idMoment);
    }

}
