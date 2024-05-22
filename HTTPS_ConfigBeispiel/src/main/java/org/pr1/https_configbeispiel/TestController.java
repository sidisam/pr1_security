package org.pr1.https_configbeispiel;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    @GetMapping("/test")
    public ResponseEntity<String> getTestData(){
        return ResponseEntity.ok("This Endpoint was succesfully called by Https Protocol");
    }
}
