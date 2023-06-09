package com.checkin.helper;

import com.opensourcewithslu.inputdevices.RFidHelper;
import com.opensourcewithslu.outputdevices.LCD1602Helper;
import com.opensourcewithslu.outputdevices.RGBLEDHelper;
import com.pi4j.context.Context;
import com.pi4j.io.i2c.I2CConfig;
import com.pi4j.io.pwm.Pwm;
import com.pi4j.io.spi.SpiConfig;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CheckInHelper {
    private final RFidHelper rfid;
    private final RGBLEDHelper rgb;
    private final LCD1602Helper lcd;

    private HashMap<String, Boolean> names;

    int[] red = new int[] {100, 0, 0};
    int[] green = new int[] {0, 100, 0};

    public CheckInHelper(SpiConfig spi, I2CConfig i2CConfig,
                         Pwm red, Pwm blue, Pwm green,
                         Context pi4jContext){

        this.rfid = new RFidHelper(spi, 25, pi4jContext);
        this.rgb = new RGBLEDHelper(red, blue, green);
        this.lcd = new LCD1602Helper(i2CConfig, pi4jContext);

        // temporary hashmap in place of h2 db
        this.names = new HashMap<String, Boolean>();
        names.put("Traison", false);
        names.put("Greih", false);
        names.put("Austin", false);
        names.put("Sinuo", false);
        names.put("Kate", false);
        names.put("Daniel", false);
    }

    public void mainLoop()
        throws InterruptedException{

        lcd.writeText("Welcome to OSS");
        Thread.sleep(3000);
        lcd.clearDisplay();

        while(true) {
            lcd.writeText("Please scan your card");
            // if valid read
            try{
                Object name = rfid.readFromCard();
                boolean inMap = this.names.containsKey(name.toString());
                // if in hashmap
                if (inMap){
                    rgb.setColor(green);
                    boolean checkedIn = this.names.get(name.toString());
                    // if already checked in
                    if (checkedIn) {
                        this.names.put(name.toString(), false);
                        lcd.writeText(name.toString() + " is now checked out.");
                    }
                    // if not checked in
                    else{
                        this.names.put(name.toString(), true);
                        lcd.writeText(name.toString() + " is now checked in.");
                    }
                }
                // if not in hashmap
                else{
                    rgb.setColor(red);
                    lcd.writeText("User: " + name.toString() + " not recognized.");
                    Thread.sleep(1000);
                    lcd.writeText("Please request new user access");
                }
            }
            catch (NullPointerException e){
                rgb.setColor(red);
                lcd.writeText("Invalid card read. Please try again");
            }
            rfid.resetScanner();
            Thread.sleep(2000);
            rgb.ledOff();
        }
    }

    public boolean addNewCard(String name)
        throws InterruptedException{

        rfid.resetScanner();
        rgb.ledOff();
        Boolean result;
        lcd.writeText("Please scan a card for " + name);

        try{
            rfid.writeToCard(name);
            rgb.setColor(green);
            names.put(name, false);
            lcd.clearDisplay();
            lcd.writeText("Write Successful");
            result = true;
        }
        catch(NullPointerException e){
            rgb.setColor(red);
            lcd.writeText("Unable to write to card. Please try again");
            result = false;
        }

        Thread.sleep(2000);
        lcd.clearDisplay();
        rgb.ledOff();

        return result;
    }

    public boolean removeCard(String name){
        boolean result = this.names.containsKey(name);
        if (result){
            this.names.remove(name);
            lcd.clearDisplay();
            lcd.writeText(name + " successfully removed.");
        }
        return result;
    }

    public HashMap<String, Boolean> getAllCards(){
        return this.names;
    }

    public String[] getCheckedIn(){
        List<String> result = new ArrayList<>();
        for (Map.Entry<String, Boolean> entry : names.entrySet()) {
        if (entry.getValue()) {
            result.add(entry.getKey());
            }
        }
        String[] arr = result.toArray(new String[result.size()]);

        return arr;
    }

    public String[] getCheckedOut(){
        List<String> result = new ArrayList<>();
        for (Map.Entry<String, Boolean> entry : names.entrySet()) {
        if (!entry.getValue()) {
            result.add(entry.getKey());
            }
        }
        String[] arr = result.toArray(new String[result.size()]);

        return arr;
    }
}
