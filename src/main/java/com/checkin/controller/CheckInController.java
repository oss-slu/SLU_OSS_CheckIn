package com.checkin.controller;

import java.util.HashMap;
import com.checkin.helper.CheckInHelper;
import com.pi4j.context.Context;
import com.pi4j.io.i2c.I2CConfig;
import com.pi4j.io.pwm.Pwm;
import com.pi4j.io.spi.SpiConfig;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import jakarta.inject.Named;


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
        try {
            helper.mainLoop();
        }
        catch (InterruptedException e){
            System.out.println(e.toString());
        }
    }

    @Get("/getAllCards")
    public HashMap<String, Boolean> getAllCards(){
        return helper.getAllCards();
    }

    @Get("/getCheckedIn")
    public String[] getCheckedIn(){
        return helper.getCheckedIn();
    }

    @Get("/getCheckedOut")
    public String[] getCheckedOut(){
        return helper.getCheckedOut();
    }

    @Get("/removeCard/{name}")
    public Boolean removeCard(String name){
        return helper.removeCard(name);
    }

    @Post("/newCard/{name}")
    public Boolean addNewCard(String name){
        try {
            return helper.addNewCard(name);
        }
        catch (InterruptedException e){
            System.out.println(e.toString());
            return false;
        }
    }
}
