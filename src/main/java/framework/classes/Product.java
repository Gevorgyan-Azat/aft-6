package framework.classes;

public class Product {

    private String name = null;
    private int price = 0;
    private transient int count = 0;

    public Product(String name, int price, int count) {
        this.name = name;
        this.price = price;
        this.count = count;
    }

    public boolean equals(Product pr) {
        return (this.name.equals(pr.name) && this.price == pr.price && this.count == pr.count);
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

    @Override
    public String toString() {
        return ("Товар: " + getName() + "\n" +
                "Стоимость: " + getPrice() + "\n");
    }
}
