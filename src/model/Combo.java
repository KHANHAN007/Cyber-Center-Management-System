package model;

import enums.ComboStatus;

import java.math.BigDecimal;

public class Combo {
    private int comboId;
    private String comboCode;
    private String name;
    private BigDecimal price;
    private ComboStatus status;
    private String comboItems;
    private String containedItems;
    private boolean isDeleted;

    public Combo() {
    }

    public Combo(String comboCode, String name, BigDecimal price) {
        this.comboCode = comboCode;
        this.name = name;
        this.price = price;
    }

    public int getComboId() {
        return comboId;
    }

    public void setComboId(int comboId) {
        this.comboId = comboId;
    }

    public String getComboCode() {
        return comboCode;
    }

    public void setComboCode(String comboCode) {
        this.comboCode = comboCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getComboItems() {
        return comboItems;
    }

    public void setComboItems(String comboItems) {
        this.comboItems = comboItems;
    }

    public String getContainedItems() {
        return containedItems;
    }

    public void setContainedItems(String containedItems) {
        this.containedItems = containedItems;
    }

    public ComboStatus getStatus() {
        return status;
    }

    public void setStatus(ComboStatus status) {
        this.status = status;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    @Override
    public String toString() {
        return "Combo{" +
                "comboId=" + comboId +
                ", comboCode='" + comboCode + '\'' +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", comboItems='" + comboItems + '\'' +
                '}';
    }
}
