package model;

import enums.ItemStatus;


public class FoodCategory {
    private int categoryId;
    private String categoryCode;
    private String name;
    private ItemStatus status;

    public FoodCategory() {
    }

    public FoodCategory(String categoryCode, String name) {
        this.categoryCode = categoryCode;
        this.name = name;
        this.status = ItemStatus.ACTIVE;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryCode() {
        return categoryCode;
    }

    public void setCategoryCode(String categoryCode) {
        this.categoryCode = categoryCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ItemStatus getStatus() {
        return status;
    }

    public void setStatus(ItemStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "FoodCategory{" +
                "categoryId=" + categoryId +
                ", categoryCode='" + categoryCode + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
