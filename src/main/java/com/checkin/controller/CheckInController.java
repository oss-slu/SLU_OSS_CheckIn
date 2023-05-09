package com.checkin.controller;

import com.checkin.helper.CheckInHelper;
import com.pi4j.context.Context;
import com.pi4j.io.i2c.I2CConfig;
import com.pi4j.io.pwm.Pwm;
import com.pi4j.io.spi.SpiConfig;
import io.micronaut.core.annotation.Introspected;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import io.micronaut.core.annotation.Introspected;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.*;

import java.util.List;
import java.util.concurrent.TimeUnit;
@Controller("/checkin")
public class CheckInController {
    private final CheckInHelper helper;

    private final PersonRepository personRepository;

    public CheckInController(@Named("rfid") SpiConfig spi, @Named("lcd") I2CConfig i2CConfig,
                             @Named("red-pwm") Pwm red, @Named("blue-pwm") Pwm blue, @Named("green-pwm") Pwm green,
                             Context pi4jContext, PersonRepository personRepository) {

        this.helper = new CheckInHelper(spi, i2CConfig, red, blue, green, pi4jContext, personRepository);
        this.personRepository = personRepository;
    }

    @Get("/init")
    public void init() {
        try {
            helper.mainLoop();
        } catch (InterruptedException e) {
            System.out.println(e.toString());
        }
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
//        return helper.getCheckedOut();
//    }
//
//
//    @Get("/removeCard")
//    public boolean removeCard(String name){
//        return helper.removeCard(name);
//    }

//    @Get("/newCard/{value}")
//    public Boolean addNewCard(String value){
//        try {
//            boolean success = helper.addNewCard(value);
//            if (success) {
//                System.out.println("Adding a new card was a success");
//                return true;
//            }
//            else {
//                System.out.println("Adding the card failed");
//                return false;
//            }
//        }
//        catch (InterruptedException e){
//            System.out.println(e.toString());
//            return false;
//        }
//    }


    @Get("/newCard/{name}")
    public HttpResponse<Person> addNewCard(String name) {
        HttpResponse<Person> person = helper.createFromName(name);
        return person;
    }
}

