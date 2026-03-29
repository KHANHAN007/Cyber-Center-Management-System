package model;

public class Topping {
    private int toppingId;
    private String name;
    private double price;

    public Topping() {}

    public Topping(String name, double price) {
        this.name = name;
        this.price = price;
    }

    public int getToppingId() {
        return toppingId;
    }

    public void setToppingId(int toppingId) {
        this.toppingId = toppingId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "Topping{" +
                "toppingId=" + toppingId +
                ", name='" + name + '\'' +
                ", price=" + price +
                '}';
    }
}
