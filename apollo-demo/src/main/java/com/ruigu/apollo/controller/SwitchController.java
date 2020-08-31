package com.ruigu.apollo.controller;

import com.ruigu.apollo.service.SwitchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SwitchController {

    @Autowired
    private SwitchService switchService;

    @GetMapping(value = "/switch")
    public ResponseEntity<String> getSwitchOpen(){
        return new ResponseEntity<String>(switchService.getSwitchData(),HttpStatus.OK);
    }
}