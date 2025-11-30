package ingsis.printScriptManager.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.startsWith;
import static org.mockito.Mockito.when;

import DTO.FormatConfigDTO;
import DTO.LintingConfigDTO;
import Utils.FormatSerializer;
import Utils.LintSerializer;
import ingsis.printScriptManager.DTO.Response;
import ingsis.printScriptManager.Error.ParsingError;
import ingsis.printScriptManager.TestSecurityConfig;
import ingsis.printScriptManager.web.BucketRequestExecutor;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@MockitoSettings(strictness = Strictness.LENIENT)
@Import(TestSecurityConfig.class)
@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class RunnerServiceTest {
  @Autowired private RunnerService runnerService;

  @MockBean private BucketRequestExecutor bucketRequestExecutor;

  @BeforeEach
  void setUp() {
    when(bucketRequestExecutor.get(startsWith("snippets/"), anyString()))
        .thenReturn(Response.withData("println('Hello World!');"));

    LintingConfigDTO lintingConfigDTO = new LintingConfigDTO();
    lintingConfigDTO.setIdentifierFormat(LintingConfigDTO.IdentifierFormat.CAMEL_CASE);
    lintingConfigDTO.setRestrictPrintln(true);
    lintingConfigDTO.setRestrictReadInput(false);
    String lintJson = new LintSerializer().serialize(lintingConfigDTO);

    when(bucketRequestExecutor.get(startsWith("lint/"), anyString()))
        .thenReturn(Response.withData(lintJson));

    FormatConfigDTO formatConfigDTO = new FormatConfigDTO();
    formatConfigDTO.setIndentInsideBraces(4);
    formatConfigDTO.setIfBraceBelowLine(false);
    formatConfigDTO.setSpaceAfterColon(true);
    formatConfigDTO.setSpaceBeforeColon(true);
    formatConfigDTO.setEnforceSpacingAroundOperators(true);
    formatConfigDTO.setNewLineAfterSemicolon(true);
    formatConfigDTO.setSpaceAroundEquals(true);
    formatConfigDTO.setEnforceSpacingBetweenTokens(false);
    formatConfigDTO.setLinesBeforePrintln(0);
    String formatJson = new FormatSerializer().serialize(formatConfigDTO);

    when(bucketRequestExecutor.get(startsWith("format/"), anyString()))
        .thenReturn(Response.withData(formatJson));

    when(bucketRequestExecutor.put(startsWith("formatted/"), anyString(), anyString()))
        .thenReturn(Response.withData(null));
  }

  @Test
  void testValidate() {
    Response<List<ParsingError>> response =
        runnerService.validate("println('Hello World!');", "1.1");

    assertNull(response.getData());

    Response<List<ParsingError>> response2 =
        runnerService.validate("println('Hello World!')", "1.1");

    assertEquals(1, response2.getData().size());
  }

  @Test
  void testExecute() {
    Response<List<String>> response = runnerService.execute("snippetId", "1.1", List.of());

    assertEquals(List.of("Hello World!"), response.getData());
  }

  @Test
  void testGetLintingErrors() {
    Response<Void> response =
        runnerService.getLintingErrors("println('Hello ' + 'World!');", "1.1", "userId");

    assertNull(response.getData());
  }

  @Test
  void testFormatFile() {
    Response<Void> response =
        runnerService.formatFile("println('Hello World!');", "1.1", "userId", "snippetId");

    assertNull(response.getData());
  }
}
