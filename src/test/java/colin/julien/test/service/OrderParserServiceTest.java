package colin.julien.test.service;

import colin.julien.test.TestApplication;
import colin.julien.test.exception.ParsingOrderException;
import colin.julien.test.model.Order;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = TestApplication.class)
public class OrderParserServiceTest {

    @Autowired
    private OrderParserService orderParserService;

    @Test
    public void everythingOKCase1() throws ParsingOrderException {
        String testInput = "1 book at 12.49\n" +
                "1 music CD at 14.99\n" +
                "1 chocolate bar at 0.85";
        Order result = orderParserService.parse(testInput);
        Assertions.assertEquals(3,result.getProducts().size());
        Assertions.assertEquals(29.83,result.getTotalPrice());
        Assertions.assertEquals(1.5,result.getTotalTaxes());
    }

    @Test
    public void everythingOKCase2() throws ParsingOrderException {
        String testInput = "1 imported box of chocolates at 10.00\n" +
                "1 imported bottle of perfume at 47.50";
        Order result = orderParserService.parse(testInput);
        Assertions.assertEquals(2,result.getProducts().size());
        Assertions.assertEquals(65.15,result.getTotalPrice());
        Assertions.assertEquals(7.65,result.getTotalTaxes());
    }

    // This one will fail, probably mistake in the provided data
    // 11.25 *1.05 = 11.8125 -> rounded up 11.80 so correct total price is 74.63 (here we have the 0.5 missing)
    @Test
    public void everythingOKCase3() throws ParsingOrderException {
        String testInput = "1 imported bottle of perfume at 27.99\n" +
                "1 bottle of perfume at 18.99\n" +
                "1 packet of headache pills at 9.75\n" +
                "1 box of imported chocolates at 11.25\n";
        Order result = orderParserService.parse(testInput);
        Assertions.assertEquals(4,result.getProducts().size());

        // Provided in the pdf ////////////////
        Assertions.assertEquals(74.68,result.getTotalPrice());
        ///////////////////////////////////////

        Assertions.assertEquals(6.70,result.getTotalTaxes());
    }

    @Test
    public void ErrorCase1() {

        String testInput = "";
        ParsingOrderException exception = Assertions.assertThrows(ParsingOrderException.class, () -> orderParserService.parse(testInput));
        Assertions.assertEquals("Invalid order.", exception.getMessage());
    }

    @Test
    public void ErrorCase2() {

        String testInput = null;
        ParsingOrderException exception = Assertions.assertThrows(ParsingOrderException.class, () -> orderParserService.parse(testInput));
        Assertions.assertEquals("Invalid order.", exception.getMessage());
    }

    @Test
    public void ErrorCase3() {

        String testInput = "1 book at 12.49\n" +
                "1 music CD at\n" +
                "1 chocolate bar at 0.85";
        ParsingOrderException exception = Assertions.assertThrows(ParsingOrderException.class, () -> orderParserService.parse(testInput));
        Assertions.assertEquals("Unable to determine price.", exception.getMessage());
    }

    @Test
    public void ErrorCase4() {

        String testInput = "1 book at 12.49\n" +
                "1 music CD at 14.99\n" +
                "chocolate bar at 0.85";
        ParsingOrderException exception = Assertions.assertThrows(ParsingOrderException.class, () -> orderParserService.parse(testInput));
        Assertions.assertEquals("Product amount undefined.", exception.getMessage());
    }

    @Test
    public void ErrorCase5() {

        String testInput = "1 book at 12.49\n" +
                "1 at 14.99\n" +
                "1 chocolate bar at 0.85";
        ParsingOrderException exception = Assertions.assertThrows(ParsingOrderException.class, () -> orderParserService.parse(testInput));
        Assertions.assertEquals("Product name undefined.", exception.getMessage());
    }
}
