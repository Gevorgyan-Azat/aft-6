package framework.classes;


import java.util.HashMap;
import java.util.Map;

public class Basket {
   private Map<Integer, Product> products = new HashMap<>();
   private static int id = 0;
   private static Basket INSTANCE = null;

    public static Basket getINSTANCE() {
        if (INSTANCE == null) {
            INSTANCE = new Basket();
        }
        return INSTANCE;
    }

   public void setProducts(String name, int price, int count) {
       products.put(++id, new Product(name, price, count));
   }

    public Map<Integer, Product> getProducts() {
        return products;
    }

    public static int getId() {
        return id;
    }
}
