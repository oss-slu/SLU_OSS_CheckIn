package com.checkin.controller;

import com.checkin.helper.CheckInHelper;
import com.pi4j.context.Context;
import com.pi4j.io.i2c.I2CConfig;
import com.pi4j.io.pwm.Pwm;
import com.pi4j.io.spi.SpiConfig;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import jakarta.inject.Named;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Controller("/checkin")
public class CheckInController {
    private final CheckInHelper helper;

    public CheckInController(@Named("rfid") SpiConfig spi, @Named("lcd") I2CConfig i2CConfig,
                             @Named("red-pwm") Pwm red, @Named("blue-pwm") Pwm blue, @Named("green-pwm") Pwm green,
                             Context pi4jContext){
        this.helper = new CheckInHelper(spi, i2CConfig, red, blue, green, pi4jContext);
    }

    @Get("/init")
    public void init(){
        helper.doNothing();
    }

//    @Get("/getAllCards")
//    public String getAllCards(){
//        return helper.getName();
//    }
//
//    @Get("/getCheckedIn")
//    public List<String> getCheckedIn(){
//        return helper.getCheckedIn();
//    }
//
//    @Get("/getCheckedOut")
//    public List<String> getCheckedOut(){
//        return helper.getCheckeOut();
//    }
//
//    @Get("/newCard/{value}")
//    public void addNewCard(String value){
//        helper.addNewCard(value);
//    }

}
