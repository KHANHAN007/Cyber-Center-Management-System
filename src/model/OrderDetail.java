package model;

public class OrderDetail {
    private int detailId;
    private int orderId;
    private Integer itemId;
    private Integer comboId;
    private Integer sizeId;
    private int quantity;
    private double price;

    public OrderDetail() {
    }

    public OrderDetail(int orderId, Integer itemId, Integer sizeId, int quantity, double price) {
        this.orderId = orderId;
        this.itemId = itemId;
        this.sizeId = sizeId;
        this.quantity = quantity;
        this.price = price;
    }

    public int getDetailId() {
        return detailId;
    }

    public void setDetailId(int detailId) {
        this.detailId = detailId;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

    public Integer getComboId() {
        return comboId;
    }

    public void setComboId(Integer comboId) {
        this.comboId = comboId;
    }

    public Integer getSizeId() {
        return sizeId;
    }

    public void setSizeId(Integer sizeId) {
        this.sizeId = sizeId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "OrderDetail{" +
                "detailId=" + detailId +
                ", orderId=" + orderId +
                ", quantity=" + quantity +
                ", price=" + price +
                '}';
    }
}
