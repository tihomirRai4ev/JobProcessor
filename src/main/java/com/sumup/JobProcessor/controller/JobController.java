package com.sumup.JobProcessor.controller;

import com.sumup.JobProcessor.model.Tasks;
import com.sumup.JobProcessor.service.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

/**
 * RestController exposing api for processing jobs.
 *
 * @author traychev
 */
@RestController
@RequestMapping("/api/v1")
public class JobController {

  private JobService jobService;

  @Autowired
  public JobController(JobService jobService) {
    this.jobService = jobService;
  }

  @PostMapping(path = "/processJob", consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.ALL_VALUE)
  public ResponseEntity<String> processJobExecution(@RequestBody Tasks job) {
    try {
      return ResponseEntity.ok(jobService.processJob(job));
    } catch (Exception e) {
      throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
          e.getMessage(), e);
    }
  }
}
