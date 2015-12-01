package com.walmart.ckoutMonitor.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import java.io.Serializable;
import java.util.List;

/**
 * Created by sgovind on 11/13/15.
 */
@Table(name = "OrderLine")
public class OrderLine extends Model implements Serializable {

    //Column definitions
    @Column(name = "ProductId",  unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
    private String productId;

    @Column(name = "UPCDescription")
    private String upcDescription;

    @Column(name = "Quantity")
    private String quantity;

    //Constructors
    public OrderLine() {
        super();

    }

    public OrderLine(String productId, String upcDescription, String quantity) {
        super();
        this.productId = productId;
        this.upcDescription = upcDescription;
        this.quantity = quantity;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getUpcDescription() {
        return upcDescription;
    }

    public void setUpcDescription(String upcDescription) {
        this.upcDescription = upcDescription;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public static List<OrderLine> getAllOrderLine() {
        return new Select().from(OrderLine.class).execute();

    }

    public static OrderLine selectByProdId(OrderLine orderLine) {

        return new Select().from(OrderLine.class).where("ProductId=?", orderLine.getProductId()).executeSingle();

    }
}