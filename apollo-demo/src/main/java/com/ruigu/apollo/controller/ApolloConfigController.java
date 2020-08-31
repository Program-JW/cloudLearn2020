package com.ruigu.apollo.controller;

import com.ruigu.apollo.config.DemoGlobalConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
public class ApolloConfigController {
    @Autowired
    DemoGlobalConfig demoGlobalConfig;

    @Autowired
    Environment environment;

    @CrossOrigin
    @RequestMapping(value = "/config", method = RequestMethod.GET)
    public ResponseEntity<String> getApolloConfig() {
        String currentVersion = demoGlobalConfig.getCurrentVersion();
        return new ResponseEntity<String>(currentVersion,HttpStatus.OK);
    }
    //check whether the parameter is valid or not
    @CrossOrigin
    @RequestMapping(value = "/parameter", method = RequestMethod.GET)
    public ResponseEntity<String> getApolloConfigByParameter(@RequestParam String parameter) {
        String result = environment.getProperty(parameter);
        if (result == null){
            result = "defaultValue";
        }
        return new ResponseEntity<String>(result,HttpStatus.OK);
    }
}