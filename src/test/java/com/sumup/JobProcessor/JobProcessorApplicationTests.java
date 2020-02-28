package com.sumup.JobProcessor;

import static com.sumup.JobProcessor.Mocks.correctMockRequest;
import static com.sumup.JobProcessor.Mocks.cyclicMockRequest;
import static com.sumup.JobProcessor.Mocks.expectedBashScriptResponse;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sumup.JobProcessor.model.Tasks;
import com.sumup.JobProcessor.service.JobService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class JobProcessorApplicationTests {

  private static JobService mockJobService = new JobService();

  @Test
  void testJobWithSeveralNonCyclicDependencies() throws JsonProcessingException {
    ObjectMapper objectMapper = new ObjectMapper();
    Tasks job = objectMapper.readValue(correctMockRequest, Tasks.class);
    String bash = mockJobService.processJob(job);
    Assertions.assertEquals(bash, expectedBashScriptResponse,
        "Problem with generation of the bash script");
  }

  @Test
  void testJobWithCyclicDependencies() throws JsonProcessingException {
    ObjectMapper objectMapper = new ObjectMapper();
    Tasks job = objectMapper.readValue(cyclicMockRequest, Tasks.class);
    Assertions.assertThrows(Exception.class, () -> mockJobService.processJob(job));
  }
}
