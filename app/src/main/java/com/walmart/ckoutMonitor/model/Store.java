package com.walmart.ckoutMonitor.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

/**
 * Created by sgovind on 11/16/15.
 */
@Table( name = "Store")
public class Store extends Model {
    @Column(name = "RemoteId", unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
    private long remoteId;

    @Column(name = "StoreNumber")
    private String storeNumber;
    @Column( name = "StoreName")
    private String storeName;
    @Column( name = "StoreAddress")
    private String storeAddress;
    @Column ( name = "OrderTotal")
    private float orderTotal;
    @Column ( name = "DeliverDateStr", index = true)
    private String deliveryDateStr;

    @Column ( name = "NumberOfOrders")
    private int totalNumberOfOrders;
    public Store() {
        super();
    }

    public Store(long remoteId, String storeNumber, String storeName, String storeAddress, float orderTotal, String deliveryDateStr, int totalNumberOfOrders) {
        this.remoteId = remoteId;
        this.storeNumber = storeNumber;
        this.storeName = storeName;
        this.storeAddress = storeAddress;
        this.orderTotal = orderTotal;
        this.deliveryDateStr = deliveryDateStr;
        this.totalNumberOfOrders = totalNumberOfOrders;
    }


    public long getRemoteId() {
        return remoteId;
    }

    public void setRemoteId(long remoteId) {
        this.remoteId = remoteId;
    }

    public String getStoreNumber() {
        return storeNumber;
    }

    public void setStoreNumber(String storeNumber) {
        this.storeNumber = storeNumber;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getStoreAddress() {
        return storeAddress;
    }

    public void setStoreAddress(String storeAddress) {
        this.storeAddress = storeAddress;
    }

    public float getOrderTotal() {
        return orderTotal;
    }

    public void setOrderTotal(float orderTotal) {
        this.orderTotal = orderTotal;
    }

    public String getDeliveryDateStr() {
        return deliveryDateStr;
    }

    public int getTotalNumberOfOrders() {
        return totalNumberOfOrders;
    }

    public void setTotalNumberOfOrders(int totalNumberOfOrders) {
        this.totalNumberOfOrders = totalNumberOfOrders;
    }

    public void setDeliveryDateStr(String deliveryDateStr) {
        this.deliveryDateStr = deliveryDateStr;
    }

    public static Store getStoreByStoreNo(Store store) {

        return new Select().from(Store.class).where("StoreNumber = ?", store.getStoreNumber()).executeSingle();
    }
}
