package com.walmart.ckoutMonitor.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

/**
 * Created by sgovind on 11/15/15.
 */
@Table( name = "OrderInfo")
public class OrderInfo extends Model {

    @Column( name = "RemoteId", unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
    private long remote_Id;

    @Column( name = "StoreNo")
    private String storeNo;

    @Column(name = "Postcode")
    private String postCode;

    @Column(name = "OrderNumber")
    private String orderNumber;

    @Column(name = "Slot", index = true)
    private String scheduleTimeSlot;

    @Column(name = "OrderTotal")
    private String orderTotal;

    @Column( name = "OrderLine", onUpdate = Column.ForeignKeyAction.CASCADE, onDelete = Column.ForeignKeyAction.CASCADE, onUniqueConflict = Column.ConflictAction.REPLACE)
    public OrderLine orderLine;

    public OrderInfo() {
        super();
    }

    public OrderInfo(long remote_Id, String storeNo, String postCode, String orderNumber, String scheduleTimeSlot, String orderTotal, OrderLine orderLine) {
        super();
        this.remote_Id = remote_Id;
        this.storeNo = storeNo;
        this.postCode = postCode;
        this.orderNumber = orderNumber;
        this.scheduleTimeSlot = scheduleTimeSlot;
        this.orderTotal = orderTotal;
        this.orderLine = orderLine;
    }

    public long getRemote_Id() {
        return remote_Id;
    }

    public void setRemote_Id(long remote_Id) {
        this.remote_Id = remote_Id;
    }

    public String getStoreNo() {
        return storeNo;
    }

    public void setStoreNo(String storeNo) {
        this.storeNo = storeNo;
    }

    public String getPostCode() {
        return postCode;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getScheduleTimeSlot() {
        return scheduleTimeSlot;
    }

    public void setScheduleTimeSlot(String scheduleTimeSlot) {
        this.scheduleTimeSlot = scheduleTimeSlot;
    }

    public String getOrderTotal() {
        return orderTotal;
    }

    public void setOrderTotal(String orderTotal) {
        this.orderTotal = orderTotal;
    }

    public OrderLine getOrderLine() {
        return orderLine;
    }

    public void setOrderLine(OrderLine orderLine) {
        this.orderLine = orderLine;
    }

    public static OrderInfo selectOrderDetailLine(OrderInfo orderInfo) {

        return new Select().from(OrderInfo.class).where("RemoteId = ?", orderInfo.getRemote_Id()).executeSingle();

    }


}
