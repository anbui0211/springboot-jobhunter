package vn.andev.jobhunter.controller;

import org.springframework.web.bind.annotation.RestController;

import vn.andev.jobhunter.util.error.IdInvalidException;

@RestController
public class HelloController {
    public String getHelloWorld() throws IdInvalidException {
        return "Hello Andev";
    }
}
