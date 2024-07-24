package com.example.test.controller;

import org.springframework.web.bind.annotation.RestController;

import com.example.test.core.error.BadRequestException;
import com.example.test.domain.Job;
import com.example.test.service.JobService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping("/api/v1/jobs")
public class JobController {
    private final JobService jobService;

    public JobController(JobService jobService) {
        this.jobService = jobService;
    }

    @PostMapping("/create")
    public ResponseEntity<Job> create(@Valid @RequestBody Job job) throws BadRequestException {
        Boolean isExist = this.jobService.findJobByNameAndCompany(job.getName(), job.getCompany());

        if (isExist) {
            throw new BadRequestException("Job is already exist");
        }

        Job entity = this.jobService.create(job);
        return new ResponseEntity<>(entity, HttpStatus.CREATED);
    }
}
