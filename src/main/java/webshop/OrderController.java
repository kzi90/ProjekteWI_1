package webshop;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class OrderController {
    
    @Autowired
    private JdbcTemplate db;

    @Autowired
    private OrderPositionController orderPositionController;

    @GetMapping("/ordercompletion")
    public String ordercompletion(@CookieValue(value = "loggedInUser", defaultValue = "") String loggedInUser,
                                  @CookieValue(value = "SessionID", defaultValue = "") String sessID,
                                                                                        Model model){
        if (loggedInUser.isEmpty()){
            return "redirect:/login";
        }
        model.addAttribute("loggedInUser", loggedInUser);
        ShoppingCart shoppingCart = ShoppingCart.findBySessID(Integer.valueOf(sessID));
        Customer customer = db.queryForObject("SELECT * FROM customers WHERE email = ?;", new CustomerRowMapper(), loggedInUser);
        // build order
        Order order = new Order();
        order.setCustID(customer.getId());
        order.setStatus("ordered");

        // build orderpositions
        List<OrderPosition> orderPositions = new ArrayList<>();
        Product product;
        Double lineTotal;
        Double total = 0.0;
        Integer posNr = 1;
        for (ShoppingCartPosition pos : shoppingCart.getCartList()) {
            product = db.queryForObject("SELECT * FROM products WHERE id = ?", new ProductRowMapper(), pos.getProductID());
            lineTotal = Math.round(pos.getQuantity() * product.getPrice() * 100) / 100.0;
            total += lineTotal;
            orderPositions.add(new OrderPosition(posNr++, pos.getQuantity(), pos.getProductID(), 0));
        }
        total = Math.round(total * 100) / 100.0;
        model.addAttribute("total", String.format("%.2f", total));
        order.setTotalPrice(total);

        // write order to database
        order = saveOrder(order);
        model.addAttribute("orderNumber", order.getId());

        // write orderpositions to database
        for (OrderPosition orderPosition : orderPositions){
            orderPosition.setOrderID(order.getId());
            orderPositionController.saveOrderPos(orderPosition);
        }

        // clear shoppingCart
        shoppingCart.setCartList(new ArrayList<>());

        model.addAttribute("templateName", "ordercompletion");
        model.addAttribute("title", "Bestellabschluss");
        return "layout";
    }

    public Order saveOrder(Order order){
        Date dateTime = new Date();
        order.setOrderDate(new SimpleDateFormat("yyyy-MM-dd").format(dateTime));
        order.setOrderTime(new SimpleDateFormat("HH:mm:ss").format(dateTime));
        String saveOrderSQL = "INSERT INTO orders (order_date, order_time, total_price, "
                                + "order_status, cust_id) VALUES (?, ?, ?, ?, ?);";
        this.db.update(saveOrderSQL, order.getOrderDate(),
                                     order.getOrderTime(),
                                     order.getTotalPrice(),
                                     order.getStatus(),
                                     order.getCustID());
        String getSQL = "SELECT * FROM orders WHERE order_date = ? AND order_time = ?;"; // Ändern, anhand MAX(ID) abfragen
        order = db.queryForObject(getSQL, new OrderRowMapper(), order.getOrderDate(), order.getOrderTime());
        return order;
    }

}