package com.project.rest_api.controller;

import com.project.rest_api.service.ClearService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/clear")
@AllArgsConstructor
public class ClearController {

    private final ClearService clearService;

    @DeleteMapping
    @ResponseStatus(HttpStatus.OK)
    public void clearTable() {
        clearService.clearTable();
    }
}
