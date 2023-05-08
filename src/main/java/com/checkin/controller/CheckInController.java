package com.checkin.controller;

import com.checkin.helper.CheckInHelper;
import com.pi4j.context.Context;
//import com.pi4j.io.i2c.I2CConfig;
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


    public CheckInController(@Named("red-pwm") Pwm red, @Named("blue-pwm") Pwm blue, @Named("green-pwm") Pwm green,
                             Context pi4jContext) {

        this.helper = new CheckInHelper(red, blue, green, pi4jContext);
    }

    //     Say "in" for checking in, or "out" for checking out, for now
    @Get("/led/{status}")
    public String checkIn(String status) {
        String message;
        if (status.toString() == "in"){
            message = helper.turnOnLight(true);
        }
        else if (status.toString() == "out"){
            message = helper.turnOnLight(false);
        }
        else {
            message = "Error checking you in";
        }
        return message;
    }
}

