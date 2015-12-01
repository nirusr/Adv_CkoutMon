package com.walmart.ckoutMonitor.model;

import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Created by sgovind on 11/17/15.
 */
@Table( name = "OrderDetail")
public class OrderDetail {


    @Column(name = "OrderNumber", index = true, unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
    private String orderNumber;

    @Column( name = "OrderTotal")
    private float orderTotal;

    @Column ( name = "DeliverDateStr", index = true)
    private String deliveryDateStr;

    @Column(name = "Postcd")
    private String postCode;

    @Column (name = "GPSLocation")
    private String gpsLocation;

    @Column(name = "OrdersPerPostCode")
    private int ordersPerPostCode;

    @Column(name = "SlotTime")
    private String slotTime;

    @Column( name = "OrdersPerSlot")
    private int ordersPerSlot;

    public OrderDetail() {
        super();
    }

    public OrderDetail(String orderNumber, float orderTotal, String deliveryDateStr, String postCode, String gpsLocation, int ordersPerPostCode, String slotTime, int ordersPerSlot) {
        this.orderNumber = orderNumber;
        this.orderTotal = orderTotal;
        this.deliveryDateStr = deliveryDateStr;
        this.postCode = postCode;
        this.gpsLocation = gpsLocation;
        this.ordersPerPostCode = ordersPerPostCode;
        this.slotTime = slotTime;
        this.ordersPerSlot = ordersPerSlot;
    }



}
