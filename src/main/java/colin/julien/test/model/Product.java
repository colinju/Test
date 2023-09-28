package colin.julien.test.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Product {
    private Integer amount;
    private String name;
    private Double price;
    private Double taxes;
    private boolean isImported = false;
    //setting tax rate with goods by default
    private taxRate taxRate = colin.julien.test.model.taxRate.GOODS;

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        return sb.append(amount).append(" ").append(name).append(" ").append(price+taxes).toString();
    }
}
