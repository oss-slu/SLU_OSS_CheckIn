package com.checkin.helper;


import com.opensourcewithslu.inputdevices.RFidHelper;
import com.opensourcewithslu.outputdevices.LCD1602Helper;
import com.opensourcewithslu.outputdevices.RGBLEDHelper;
import com.pi4j.context.Context;
import com.pi4j.io.i2c.I2CConfig;
import com.pi4j.io.pwm.Pwm;
import com.pi4j.io.spi.SpiConfig;
import io.micronaut.context.annotation.Prototype;
import io.micronaut.http.annotation.Get;
import jakarta.annotation.PostConstruct;

import java.util.HashMap;
import java.util.Objects;


@Prototype
public class CheckInHelper {
    private final RFidHelper rfid;
    private final RGBLEDHelper rgb;
    private final LCD1602Helper lcd;

    private HashMap<String, Boolean> names;

    public CheckInHelper(SpiConfig spi, I2CConfig i2CConfig,
                         Pwm red, Pwm blue, Pwm green,
                         Context pi4jContext){
        this.rfid = new RFidHelper(spi, 25, pi4jContext);
        this.rgb = new RGBLEDHelper(red, blue, green);
        this.lcd = new LCD1602Helper(i2CConfig, pi4jContext);
        this.names = new HashMap<String, Boolean>();
        names.put("Traison", false);
        names.put("Greih", false);
        names.put("Austin", false);
        names.put("Sinuo", false);
        postConstruct();
    }

    public void postConstruct(){
        lcd.writeText("Welcome to OSS");
        lcd.clearDisplay();
        int[] yellow = new int[] {100, 100, 0};
        rgb.setColor(yellow);
        lcd.clearDisplay();
        lcd.writeText("Please scan your card");
        int[] red = new int[] {100, 0, 0};
        int[] green = new int[] {0, 100, 0};

        while(true) {
            Object name = rfid.readFromCard();

            boolean result = this.names.get(name.toString());

            if (result) {
                rgb.setColor(green);
                this.names.put(name.toString(), false);
                lcd.writeText("Goodbye " + name.toString());
            } else if (!result) {
                rgb.setColor(green);
                this.names.put(name.toString(), true);
                lcd.writeText("Hello " + name.toString());
            } else {
                rgb.setColor(red);
                lcd.writeText("Unrecognized Card");
            }
        }
    }
    public void doNothing(){
        System.out.println("1");
    }
}
