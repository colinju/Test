package colin.julien.test.controller;

import colin.julien.test.exception.ParsingOrderException;
import colin.julien.test.model.Order;
import colin.julien.test.service.OrderParserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderController {

    private final OrderParserService orderParserService;
    private static final Logger LOG = LoggerFactory.getLogger(OrderController.class);


    public OrderController(
            OrderParserService orderParserService) {
        this.orderParserService = orderParserService;
    }


    /**
     * Basic Rest api used as input
     * Send the order in body as string and with \n as line return
     * @param order
     * @return
     */
    @PostMapping("v1/process-order")
    public ResponseEntity<Order> processOrder(@RequestBody String order) {
        LOG.debug("order controller -> processing order : \n{}", order);

        try {
            Order processedOrder = orderParserService.parse(order);
            LOG.debug("order controller -> order processed correctly : \n{}", processedOrder.toString());
            return ResponseEntity.ok(processedOrder);
        }catch (ParsingOrderException e){
            return ResponseEntity.internalServerError().build();
        }
    }
}
