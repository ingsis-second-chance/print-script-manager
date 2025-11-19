package ingsis.printScriptManager.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class TestContextDTO {
    private String snippetId;

    private String version;

    private List<String> inputs;

    private List<String> expected;
}
