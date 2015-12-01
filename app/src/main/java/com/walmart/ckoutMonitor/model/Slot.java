package com.walmart.ckoutMonitor.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

/**
 * Created by sgovind on 11/16/15.
 */
@Table(name = "Slot")
public class Slot extends Model {

    @Column(name = "OrderNumber", unique = true, onNullConflict = Column.ConflictAction.REPLACE)
    private String orderNumber;
    @Column(name = "SlotTime")
    private String slotTime;
    @Column( name = "OrdersPerSlot")
    private int ordersPerSlot;
    @Column ( name = "ValuePerSlot")
    private float valuePerSlot;



    public Slot() {
        super();
    }

    public Slot(String orderNumber, String slotTime, int ordersPerSlot, float valuePerSlot) {
        this.orderNumber = orderNumber;
        this.slotTime = slotTime;
        this.ordersPerSlot = ordersPerSlot;
        this.valuePerSlot = valuePerSlot;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getSlotTime() {
        return slotTime;
    }

    public void setSlotTime(String slotTime) {
        this.slotTime = slotTime;
    }

    public int getOrdersPerSlot() {
        return ordersPerSlot;
    }

    public void setOrdersPerSlot(int ordersPerSlot) {
        this.ordersPerSlot = ordersPerSlot;
    }

    public float getValuePerSlot() {
        return valuePerSlot;
    }

    public void setValuePerSlot(float valuePerSlot) {
        this.valuePerSlot = valuePerSlot;
    }

    public static Slot getSlotbySlotTime(Slot slot) {
        return new Select().from(Slot.class).where("SlotTime = ?", slot.getSlotTime()).executeSingle();
    }
}
