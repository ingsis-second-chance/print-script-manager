package ingsis.printScriptManager.controllers;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;

import com.auth0.jwt.JWT;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;

import ingsis.printScriptManager.DTO.Response;
import ingsis.printScriptManager.DTO.TestContextDTO;
import ingsis.printScriptManager.DTO.ValidateRequestDTO;
import ingsis.printScriptManager.TestSecurityConfiguration;
import ingsis.printScriptManager.redis.FormatConsumer;
import ingsis.printScriptManager.redis.LintConsumer;
import ingsis.printScriptManager.services.RunnerService;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@MockitoSettings(strictness = Strictness.LENIENT)
@Import(TestSecurityConfiguration.class)
@SpringBootTest
class RunnerControllerTest {

    @Autowired
    private RunnerController runnerController;

    @MockBean
    private RunnerService runnerService;

    @MockBean
    private LintConsumer lintConsumer;

    @MockBean
    private FormatConsumer formatConsumer;

    @BeforeEach
    void setUp() {
        // vacio → NO MOCKEES SECURITY ACA
    }

    @Test
    void testValidate() {
        when(runnerService.validate(anyString(), anyString()))
                .thenReturn(Response.withData(null));

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
                        new TestContextDTO("snippet", "1.0", List.of("input"), List.of("Hello World!"))
                );

        assertEquals(200, response.getStatusCode().value());

        ResponseEntity<Object> response2 =
                runnerController.executeTest(
                        new TestContextDTO("snippet", "1.0", List.of("input"), List.of("Hello World!2"))
                );

        assertEquals(417, response2.getStatusCode().value());
    }
}