package framework.classes;

import java.util.List;

public class GsonFile {
    private final Product maxPriceProduct;
    private final List<Product> products;

    public GsonFile(Product maxPriceProduct, List<Product> products) {
        this.maxPriceProduct = maxPriceProduct;
        this.products = products;
    }

    public Product getMaxPriceProduct() {
        return maxPriceProduct;
    }

    public List<Product> getProducts() {
        return products;
    }
}
