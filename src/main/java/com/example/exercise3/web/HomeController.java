package com.example.exercise3.web;


import com.example.exercise3.services.MqttService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/")
public class HomeController {

    @Autowired
    private MqttService mqttServices;


    @PostMapping("/")
    @ResponseBody
    public ResponseEntity receivePostRequest(@RequestBody String message) {
        mqttServices.setMessage(message);
        mqttServices.runClient();
        return new ResponseEntity(HttpStatus.ACCEPTED);

    }
}