package colin.julien.test.model;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Order {
    private List<Product> products;
    private Double totalPrice;
    private Double totalTaxes;

    public Order(){
        this.products = new ArrayList<>();
        this.totalPrice = 0.0;
        this.totalTaxes = 0.0;
    }

    public void addProduct(Product processedProduct) {
        products.add(processedProduct);
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        for (Product p: products) {
            sb.append(p.toString()).append("\n");
        }
        sb.append("Sales Taxes : ").append(totalTaxes).append(" Total : ").append(totalPrice);
        return sb.toString();
    }
}
