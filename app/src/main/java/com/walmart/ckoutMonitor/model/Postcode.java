package com.walmart.ckoutMonitor.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

/**
 * Created by sgovind on 11/17/15.
 */

@Table(name = "Postcode")
public class Postcode extends Model {

    @Column(name = "OrderNumber", unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
    private String orderNumber;

    @Column(name = "Postcd", index = true)
    private String postCode;

    @Column (name = "GPSLat")
    private String gpsLat;

    @Column (name = "GPSlng")
    private String gpsLng;

    @Column(name = "OrdersPerPostCode")
    private int ordersPerPostCode;

    @Column(name = "DeliveryCity")
    private String city;


    public Postcode() {
        super();
    }

    public Postcode(String orderNumber, String postCode, String gpsLat, String gpsLng, int ordersPerPostCode, String city) {
        this.orderNumber = orderNumber;
        this.postCode = postCode;
        this.gpsLat = gpsLat;
        this.gpsLng = gpsLng;
        this.ordersPerPostCode = ordersPerPostCode;
        this.city = city;
    }

    public static Postcode getPostCodebyPostcd( Postcode postcode) {

        //return new Select().from(OrderLine.class).where("ProductId=?", orderLine.getProductId()).executeSingle();
        return new Select().from(Postcode.class).where("Postcd = ?", postcode.getPostCode()).executeSingle();
    }

    public static Postcode selectField(String fieldName, String fieldValue) {
        return new Select().from(Postcode.class)
                .where(fieldName + " = ?", fieldValue).executeSingle();
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getPostCode() {
        return postCode;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    public String getGpsLat() {
        return gpsLat;
    }

    public void setGpsLat(String gpsLat) {
        this.gpsLat = gpsLat;
    }

    public String getGpsLng() {
        return gpsLng;
    }

    public void setGpsLng(String gpsLng) {
        this.gpsLng = gpsLng;
    }

    public int getOrdersPerPostCode() {
        return ordersPerPostCode;
    }

    public void setOrdersPerPostCode(int ordersPerPostCode) {
        this.ordersPerPostCode = ordersPerPostCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
