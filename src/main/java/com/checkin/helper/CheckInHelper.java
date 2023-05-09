package com.checkin.helper;

import com.checkin.controller.Person;
import com.checkin.controller.PersonDTO;
import com.checkin.controller.PersonRepository;
import com.opensourcewithslu.inputdevices.RFidHelper;
import com.opensourcewithslu.outputdevices.LCD1602Helper;
import com.opensourcewithslu.outputdevices.RGBLEDHelper;
import com.pi4j.context.Context;
import com.pi4j.io.i2c.I2CConfig;
import com.pi4j.io.pwm.Pwm;
import com.pi4j.io.spi.SpiConfig;
import io.micronaut.context.annotation.Prototype;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.http.annotation.Post;

import java.util.HashMap;


@Prototype
public class CheckInHelper {
    private final RFidHelper rfid;
    private final RGBLEDHelper rgb;
    private final LCD1602Helper lcd;

    private HashMap<String, Boolean> names;

    private PersonRepository personRepository;


    int[] red = new int[] {100, 0, 0};
    int[] green = new int[] {0, 100, 0};

    public CheckInHelper(SpiConfig spi, I2CConfig i2CConfig,
                         Pwm red, Pwm blue, Pwm green,
                         Context pi4jContext, PersonRepository personRepository){

        this.rfid = new RFidHelper(spi, 25, pi4jContext);
        this.rgb = new RGBLEDHelper(red, blue, green);
        this.lcd = new LCD1602Helper(i2CConfig, pi4jContext);
        this.personRepository = personRepository;

        // temporary hashmap in place of h2 db
        this.names = new HashMap<String, Boolean>();
        names.put("Traison", false);
        names.put("Greih", false);
        names.put("Austin", false);
        names.put("Sinuo", false);
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
                Boolean inMap = this.names.containsKey(name.toString());
                // if in hashmap
                if (inMap){
                    rgb.setColor(green);
                    Boolean checkedIn = this.names.get(name.toString());
                    // if already checked in
                    if (checkedIn) {
                        this.names.put(name.toString(), false);
                        lcd.writeText("Goodbye " + name.toString());
                    }
                    // if not checked in
                    else{
                        this.names.put(name.toString(), true);
                        lcd.writeText("Hello " + name.toString());
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

    public Boolean addNewCard(String name)
            throws InterruptedException{
        rfid.resetScanner();
        rgb.ledOff();
        lcd.writeText("Please scan a card for " + name);
        rfid.writeToCard(name);
        rgb.setColor(green);
//        names.put(name, false);
        lcd.writeText("Write Successful");
        Thread.sleep(2000);
        lcd.clearDisplay();
        rgb.ledOff();

        return true;
    }

    @Post("/")
    public HttpResponse<Person> create(@Body PersonDTO pd) {
        String firstName = pd.getFirstName();
        Boolean checkedIn = pd.getStatus();
        return HttpResponse.created(this.personRepository.save(firstName, checkedIn));
    }
    public HttpResponse<Person> createFromName(@PathVariable String name) {
        PersonDTO pd = new PersonDTO(name, false);
        HttpResponse<Person> person;
        try {
            addNewCard(name);
        }
        catch (InterruptedException e){
            System.out.println(e.toString());
        }
        person = create(pd);

        return person;
    }
}
