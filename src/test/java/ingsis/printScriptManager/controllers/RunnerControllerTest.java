package ingsis.printScriptManager.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import ingsis.printScriptManager.DTO.Response;
import ingsis.printScriptManager.DTO.TestContextDTO;
import ingsis.printScriptManager.DTO.ValidateRequestDTO;
import ingsis.printScriptManager.TestSecurityConfig;
import ingsis.printScriptManager.services.RunnerService;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@MockitoSettings(strictness = Strictness.LENIENT)
@Import(TestSecurityConfig.class)
@SpringBootTest
class RunnerControllerTest {

  @Autowired private RunnerController runnerController;

  @MockBean private RunnerService runnerService;

  @BeforeEach
  void setUp() {
    // vacio → NO MOCKEES SECURITY ACA
  }

  @Test
  void testValidate() {
    when(runnerService.validate(anyString(), anyString())).thenReturn(Response.withData(null));

    ResponseEntity<Object> response =
        runnerController.validate(new ValidateRequestDTO("code", "version"));

    assertEquals(200, response.getStatusCode().value());
  }

  @Test
  void testExecuteTest() {
    when(runnerService.execute(anyString(), anyString(), anyList()))
        .thenReturn(Response.withData(List.of("Hello World!")));

    ResponseEntity<Object> response =
        runnerController.executeTest(
            new TestContextDTO("snippet", "1.0", List.of("input"), List.of("Hello World!")));

    assertEquals(200, response.getStatusCode().value());

    ResponseEntity<Object> response2 =
        runnerController.executeTest(
            new TestContextDTO("snippet", "1.0", List.of("input"), List.of("Hello World!2")));

    assertEquals(417, response2.getStatusCode().value());
  }
}
