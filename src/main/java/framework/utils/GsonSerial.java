package framework.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import framework.classes.GsonFile;
import framework.classes.Product;
import io.qameta.allure.Attachment;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class GsonSerial {

    private static final File FILE = new File("src\\main\\resources\\product-data.json");
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private List<Product> products = new ArrayList<>();

    public void writeJson(Product product) {
        try (PrintWriter fileWriter = new PrintWriter(new FileWriter(FILE))) {
            products.add(product);
            GSON.toJson(new GsonFile(getMaxPriceProduct(), products), fileWriter);
        } catch (IOException ignore) {
        }
    }


    @Attachment()
    public String parse() {
        StringBuilder report = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE))) {
            StringBuilder productStr = new StringBuilder();
            String jsonFile = reader.readLine();
            while (jsonFile != null) {
                productStr.append(jsonFile);
                jsonFile = reader.readLine();
            }
            GsonFile gsonFile = GSON.fromJson(String.valueOf(productStr), GsonFile.class);
            report.append("Самый дорогой товар: \n").append(gsonFile.getMaxPriceProduct().toString()).append("\n")
                    .append("Всего товаров - ").append(gsonFile.getProducts().size()).append(": \n");
            for (Product pr : gsonFile.getProducts()) {
                report.append(pr.toString()).append("\n");
            }
        } catch (IOException ignore) {
        }
        return String.valueOf(report);
    }

    private Product getMaxPriceProduct() {
        if (products.size() > 1) {
            Product product = products.get(0);
            for (int i = 1; i < products.size(); i++) {
                if (product.getPrice() < products.get(i).getPrice()) {
                    product = products.get(i);
                }
            }
            return product;
        } else if (products.size() == 1) {
            return products.get(0);
        }
        return null;
    }
}
