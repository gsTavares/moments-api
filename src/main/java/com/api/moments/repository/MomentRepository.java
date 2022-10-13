package com.api.moments.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.api.moments.model.Moment;

@Repository
public interface MomentRepository extends JpaRepository<Moment, Long> {
    
}
