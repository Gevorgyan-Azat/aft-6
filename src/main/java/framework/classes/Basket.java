package framework.classes;

import java.util.ArrayList;
import java.util.List;

public class Basket {

    private List<Product> products = new ArrayList<>();
    private static int id = 0;
    private static Basket INSTANCE = null;

    public static Basket getINSTANCE() {
        if (INSTANCE == null) {
            INSTANCE = new Basket();
        }
        return INSTANCE;
    }

    public void setProducts(String name, int price, int count) {
        products.add(new Product(name, price, count));
    }

    public List<Product> getProducts() {
        return products;
    }

    public static int getId() {
        return id;
    }
}
