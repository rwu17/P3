package dk.aau.cs.ds308e18.function;

import java.time.LocalDate;

public class TourGenerator {
    public enum planningMethod{
      distance,
      fuel
    };
    private LocalDate date;
    private String region;

    public void setDate(LocalDate date){
        this.date = date;
    }

    public void setRegion(String region){
        this.region = region;
    }

    public void generateTours(){
        if (region != null)
            System.out.println("[Region = " + region + "]");
        if (date != null)
            System.out.println("[Date = " + date + "]");
        System.out.println("Generating tour(s)...");
    }
}
