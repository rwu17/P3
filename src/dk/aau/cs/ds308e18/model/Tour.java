package dk.aau.cs.ds308e18.model;

import java.time.LocalDate;
import java.util.ArrayList;

public class Tour {

    public enum tourStatus{
        invalidEmpty,
        valid,
        validFull,
        validReleased
    }

    ArrayList<Order> orders;

    private LocalDate tourDate;
    private LocalDate packingDate;
    private String region;
    private String regionDetail;
    private String driver;
    private String status;
    private Boolean consignor;

    //Used in our database to see which tour an order belongs to.
    private int tourID;

    //Used for Vibocold's tour IDs.
    private int id;

    public Tour() {
        id = 0;
        tourDate = LocalDate.now();
        packingDate = LocalDate.now();
        orders = new ArrayList<>();
        region = "";
        regionDetail = "";
        driver = "";
        status = "";
        consignor = false;
    }

    public void addOrder(Order order) {
        orders.add(order);
    }

    /*
    Used in our database to see which tour an order belongs to.
    */
    public int getTourID() {
        return tourID;
    }

    public void setTourID(int tourID) {
        this.tourID = tourID;
    }

    /*
    Used for Vibocold's tour IDs.
    */
    public int getID() {
        return id;
    }

    public void setID(int id) {
        this.id = id;
    }

    public ArrayList<Order> getOrders() {
        return orders;
    }

    public int getOrderAmount() {
        return orders.size();
    }

    public LocalDate getTourDate() {
        return tourDate;
    }

    public void setTourDate(LocalDate tourDate) {
        this.tourDate = tourDate;
    }

    public LocalDate getPackingDate() {
        return packingDate;
    }

    public void setPackingDate(LocalDate packingDate) {
        this.packingDate = packingDate;
    }

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public Boolean getConsignor() {
        return consignor;
    }

    public void setConsignor(Boolean consignor) {
        this.consignor = consignor;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRegionDetail() {
        return regionDetail;
    }

}
