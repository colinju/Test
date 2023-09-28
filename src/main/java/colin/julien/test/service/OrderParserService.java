package colin.julien.test.service;

import colin.julien.test.exception.ParsingOrderException;
import colin.julien.test.model.Order;
import colin.julien.test.model.Product;
import colin.julien.test.model.taxRate;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class OrderParserService {

    private static final Logger LOG = LoggerFactory.getLogger(OrderParserService.class);

    public Order parse(String order) throws ParsingOrderException {
        LOG.debug("Start parsing");
        if(StringUtils.isBlank(order)){
            throw new ParsingOrderException("Invalid order.");
        }

        // Split input because it's string and not DTO
        String[] productsList = order.split("\n");

        Order processedOrder = new Order();

        // Process each row
        for (String p: productsList) {
            Product processedProduct = parseRow(p);

            // add processed product to the order
            processedOrder.addProduct(processedProduct);

            // Increase price and taxes
            // adding the product number multiple in those calculations
            // wasn't specified
            processedOrder.setTotalTaxes(
                    (processedOrder.getTotalTaxes() + processedProduct.getTaxes()) * processedProduct.getAmount()
            );

            processedOrder.setTotalPrice(
                    (processedOrder.getTotalPrice() + processedProduct.getTaxes() + processedProduct.getPrice())
                            * processedProduct.getAmount()
            );
        }

        // round total price
        processedOrder.setTotalPrice(Math.floor(processedOrder.getTotalPrice() * 100) / 100);
        LOG.debug("End parsing");
        return processedOrder;
    }

    /**
     * Parse each row (as string) to extract important informations (amount, name, price)
     * and also build the returned object with the tax calculation
     * @param p
     * @return the product parsed
     * @throws ParsingOrderException
     */
    private Product parseRow(String p) throws ParsingOrderException {
        LOG.debug("Parsing row");
        Product product = new Product();

        // Splitting the quantity / name and the price
        String[] split1 = p.split(" at ");
        if(split1.length < 2 ){
            throw new ParsingOrderException("Unable to determine price.");
        }else{
            product.setPrice(Double.parseDouble(split1[1]));
        }

        // Splitting the amount and the name
        String[] split2 = split1[0].split(" ");

        try{
            int intValue = Integer.parseInt(split2[0]);
        }catch (NumberFormatException e){
            throw new ParsingOrderException("Product amount undefined.");
        }

        product.setAmount(Integer.parseInt(split2[0]));

        // Rebuild the name
        if(split2.length < 2){
            throw new ParsingOrderException("Product name undefined.");
        }
        StringBuilder sb = new StringBuilder();
        for(int i =1; i < split2.length;  i++){
            sb.append(split2[i]).append(" ");
        }
        product.setName(sb.toString());

        determineIfImported(product);

        determineTaxRate(product);

        taxCalculation(product);

        return product;
    }

    /**
     * Determine if product is an imported product
     * @param product
     */
    private void determineIfImported(Product product) {
        LOG.debug("Import determination");
        if(product.getName().contains("import")){
            product.setImported(true);
        }
    }

    /**
     * Determine tax rate depending the "name"
     * If it was an object we can have a category and filter on it with an enum
     * Basic enum has been created for the tax rate
     * @param product
     */
    private void determineTaxRate(Product product) {
        LOG.debug("Tax determination");
        String[] specific = new String[]{"pill","book","chocolate"};
        if(Arrays.stream(specific).anyMatch(product.getName()::contains)) {
            product.setTaxRate(taxRate.OTHER);
        }
    }

    /**
     * Tax calculation related with tax rate enum
     * @param product
     */
    private void taxCalculation(Product product) {
        LOG.debug("Tax calculation");
        double taxRate = product.getTaxRate().getValue();

        if(product.isImported()){
            taxRate += 0.05;
        }
        double taxedPrice = product.getPrice() * taxRate;

        // round taxes
        product.setTaxes(Math.round((taxedPrice - product.getPrice()) * 20) / 20.0);
    }
}
