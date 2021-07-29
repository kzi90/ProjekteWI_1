package webshop;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
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

    /**
     * complete order from shoppingcart (if not empty). If the customer isn't logged
     * in, he/she will be redirected to /login.
     * 
     * @param loggedInUser
     * @param sessID
     * @param model
     * @return ordercompletion.html template (if shoppingcart not empty and customer
     *         logged in)
     */
    @GetMapping("/ordercompletion")
    public String ordercompletion(@CookieValue(value = "loggedInUser", defaultValue = "") String loggedInUser,
            @CookieValue(value = "SessionID", defaultValue = "") String sessID, HttpServletResponse response,
            Model model) {
        model.addAttribute("loggedInUser", loggedInUser);

        if (loggedInUser.isEmpty()) {
            return "redirect:/login";
        }
        model.addAttribute("loggedInUser", loggedInUser);

        // get shoppingCart by session id
        ShoppingCart shoppingCart = ShoppingCartController.getShoppingCart(sessID, response);
        model.addAttribute("shoppingcart", shoppingCart);

        if (!shoppingCart.getCartList().isEmpty()) {

            // get customer via cookie
            Customer customer = db.queryForObject("SELECT * FROM customers WHERE email = ?;", new CustomerRowMapper(),
                    loggedInUser);

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
                product = db.queryForObject("SELECT * FROM products WHERE id = ?", new ProductRowMapper(),
                        pos.getProductID());
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
            for (OrderPosition orderPosition : orderPositions) {
                orderPosition.setOrderID(order.getId());
                orderPositionController.saveOrderPos(orderPosition);
            }

            // clear shoppingCart
            shoppingCart.setCartList(new ArrayList<>());

            // automatischer E-Mail-Versand
            String fullName = customer.getFirstname() + " " + customer.getLastname();
            // recipient format: "Real Name <email@addre.ss>"
            String message = "Guten Tag " + fullName + ",\n\n"
                    + "vielen Dank für deine Bestellung mit der Bestellnummer " + order.getId().toString() + "! "
                    + "Hier noch einmal die Zahlungsdetails:\n" + "Zahle den Rechnungsbetrag ("
                    + String.format("%.2f", total) + " €) " + "bitte auf folgendes Bankkonto:\n"
                    + "Inhaber: Bielefelder Unikat\n" + "IBAN: DE86 1203 0000 1061 8459 45\n" + "BIC: BYLADEM1001\n"
                    + "Als Verwendungszweck gib bitte die Bestellnummer (s.o.) an."
                    + " Die Lieferung wird nach Eingang der Zahlung unverzüglich veranlasst. "
                    + "Vielen Dank für deinen Einkauf und Prost!";
            JavaMail.sendMessage(customer.getEmail(), fullName, message);

            model.addAttribute("templateName", "ordercompletion");
            model.addAttribute("title", "Bestellabschluss");
            return "layout";
        } else {
            return "redirect:/";
        }
    }

    /**
     * save Order
     * 
     * @param order
     * @return order (with id)
     */
    public Order saveOrder(Order order) {
        Date dateTime = new Date();
        order.setOrderDate(new SimpleDateFormat("yyyy-MM-dd").format(dateTime));
        order.setOrderTime(new SimpleDateFormat("HH:mm:ss").format(dateTime));
        String saveOrderSQL = "INSERT INTO orders (order_date, order_time, total_price, "
                + "order_status, cust_id) VALUES (?, ?, ?, ?, ?);";
        this.db.update(saveOrderSQL, order.getOrderDate(), order.getOrderTime(), order.getTotalPrice(),
                order.getStatus(), order.getCustID());
        // the highest id is the one of the order, added one line before, because of
        // the auto_increment function of the database
        String getSQL = "SELECT * FROM orders WHERE id = (SELECT MAX(id) FROM orders);";
        order = db.queryForObject(getSQL, new OrderRowMapper());
        return order;
    }

}