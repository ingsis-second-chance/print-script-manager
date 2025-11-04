package ingsis.printScriptManager.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {
    @GetMapping("/ping")
    public String ping() { return "pong 🟢"; }

    @GetMapping("/secure/ping")
    public String securePing() { return "secure pong 🔒"; }
}