package model;

import enums.ItemStatus;

public class FoodItem {
    private int itemId;
    private String itemCode;
    private String name;
    private int categoryId;
    private String categoryName;
    private String description;
    private ItemStatus status;
    private FoodCategory category;
    private Double minPrice;
    private Double maxPrice;
    private String availableSizes;
    private String availableToppings;
    private boolean isDeleted;

    public FoodItem() {
    }

    public FoodItem(String itemCode, String name, int categoryId, String description) {
        this.itemCode = itemCode;
        this.name = name;
        this.categoryId = categoryId;
        this.description = description;
        this.status = ItemStatus.ACTIVE;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ItemStatus getStatus() {
        return status;
    }

    public void setStatus(ItemStatus status) {
        this.status = status;
    }

    public FoodCategory getCategory() {
        return category;
    }

    public void setCategory(FoodCategory category) {
        this.category = category;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public Double getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(Double minPrice) {
        this.minPrice = minPrice;
    }

    public Double getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(Double maxPrice) {
        this.maxPrice = maxPrice;
    }

    public String getAvailableSizes() {
        return availableSizes;
    }

    public void setAvailableSizes(String availableSizes) {
        this.availableSizes = availableSizes;
    }

    public String getAvailableToppings() {
        return availableToppings;
    }

    public void setAvailableToppings(String availableToppings) {
        this.availableToppings = availableToppings;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    @Override
    public String toString() {
        return "FoodItem{" +
                "itemId=" + itemId +
                ", itemCode='" + itemCode + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
