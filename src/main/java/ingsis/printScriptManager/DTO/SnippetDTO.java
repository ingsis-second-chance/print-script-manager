package ingsis.printScriptManager.DTO;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class SnippetDTO {
  private String snippetId;

  private String version;

  private List<String> inputs;
}
