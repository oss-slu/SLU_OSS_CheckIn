package com.checkin.helper;

import com.opensourcewithslu.outputdevices.RGBLEDHelper;
import com.pi4j.context.Context;
import com.pi4j.io.pwm.Pwm;
import io.micronaut.context.annotation.Prototype;


@Prototype
public class CheckInHelper {
    //    private final RFidHelper rfid;
    private final RGBLEDHelper rgb;
//    private final LCD1602Helper lcd;

//    private HashMap<String, Boolean> names;



    int[] red = new int[] {100, 0, 0};
    int[] green = new int[] {0, 100, 0};

    public CheckInHelper(Pwm red, Pwm blue, Pwm green,
                         Context pi4jContext){

        this.rgb = new RGBLEDHelper(red, blue, green);
    }

    public String turnOnLight(Boolean checkedIn) {
        if (checkedIn) {
            rgb.setColor(green);
            return "Welcome!";
        }
        else {
            rgb.setColor(red);
            return "Goodbye!";
        }
    }
}
