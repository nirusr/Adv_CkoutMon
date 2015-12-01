package com.walmart.ckoutMonitor.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

/**
 * Created by sgovind on 11/16/15.
 */
@Table(name = "Product")
public class Product extends Model {

    @Column (name = "ProdId", index = true, unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
    private String prodId;
    @Column (name = "ProdDesc")
    private String prodDesc;
    @Column( name = "Quantity")
    private int quantity;

    @Column(name = "OrderNumber")
    private String orderNumber;

    public Product() {
        super();
    }

    public Product(String prodId, String prodDesc, int quantity, String orderNumber) {
        this.prodId = prodId;
        this.prodDesc = prodDesc;
        this.quantity = quantity;
        this.orderNumber = orderNumber;
    }

    public String getProdId() {
        return prodId;
    }

    public void setProdId(String prodId) {
        this.prodId = prodId;
    }

    public String getProdDesc() {
        return prodDesc;
    }

    public void setProdDesc(String prodDesc) {
        this.prodDesc = prodDesc;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public static Product getProdByProdID ( Product product) {
        //return new Select().from(OrderLine.class).where("ProductId=?", orderLine.getProductId()).executeSingle();
        return new Select().from(Product.class).where("ProdId = ?", product.getProdId()).executeSingle();
    }
}
