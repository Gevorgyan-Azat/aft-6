package framework.classes;

public class Product {

    private String name = null;
    private int price = 0;
    private int count = 0;

    protected Product(String name, int price, int count) {
        this.name = name;
        this.price = price;
        this.count = count;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public int getCount() {
        return count;
    }
}
