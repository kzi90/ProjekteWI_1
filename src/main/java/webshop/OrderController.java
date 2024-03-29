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

/**
 * @author Kasimir Eckhardt
 */
@Controller
public class OrderController {

    @Autowired
    private JdbcTemplate db;

    @Autowired
    private OrderPositionController orderPositionController;

    @Autowired
    private SessionController sessionController;

    /**
     * complete order from shoppingcart (if not empty). If the customer isn't logged
     * in, he/she will be redirected to /login.
     * 
     * @param sessID
     * @param response
     * @param model
     * @return ordercompletion.html template (if shoppingcart not empty and customer
     *         logged in)
     */
    @GetMapping("/ordercompletion")
    public String ordercompletion(@CookieValue(value = "SessionID", defaultValue = "") String sessID,
            HttpServletResponse response, Model model) {
        Session session = sessionController.getOrSetSession(sessID, response);
        String loggedInUser = session.getLoggedInUser();
        if (loggedInUser.isEmpty()) {
            return "redirect:/login";
        }
        model.addAttribute("loggedInUser", loggedInUser);

        // get shoppingCart
        ShoppingCart shoppingCart = session.getShoppingCart();
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
            String message = "Guten Tag " + fullName + ",\n\n"
                    + "vielen Dank für deine Bestellung mit der Bestellnummer " + order.getId().toString()
                    + "! Hier noch einmal die Zahlungsdetails:\nZahle den Rechnungsbetrag ("
                    + String.format("%.2f", total) + " €) bitte auf folgendes Bankkonto:\n"
                    + "Inhaber: Bielefelder Unikat\nIBAN: DE86 1203 0000 1061 8459 45\nBIC: BYLADEM1001\n"
                    + "Als Verwendungszweck gib bitte die Bestellnummer (" + order.getId().toString() + ") an."
                    + " Die Lieferung wird nach Eingang der Zahlung unverzüglich veranlasst. "
                    + "Vielen Dank für deinen Einkauf und Prost!";
            JavaMail.sendMessage(customer.getEmail(), fullName, "Bestellbestätigung", message);

            model.addAttribute("templateName", "ordercompletion");
            model.addAttribute("title", "Bestellabschluss");
            model.addAttribute("cookiesAccepted", session.getCookiesAccepted());
            return "layout";
        } else {
            return "redirect:/";
        }
    }

    /**
     * show order history for logged in customer
     * 
     * @param sessID
     * @param response
     * @param model
     * @return my_orders.html template
     */
    @GetMapping("my_orders")
    public String myOrders(@CookieValue(value = "SessionID", defaultValue = "") String sessID,
            HttpServletResponse response, Model model) {
        Session session = sessionController.getOrSetSession(sessID, response);
        String loggedInUser = session.getLoggedInUser();
        if (loggedInUser.isEmpty()) {
            return "redirect:/login";
        }
        model.addAttribute("loggedInUser", loggedInUser);
        model.addAttribute("shoppingcart", session.getShoppingCart());

        Customer customer = db.queryForObject("SELECT * FROM customers WHERE email = ?", new CustomerRowMapper(),
                loggedInUser);
        List<Order> orders = db.query("SELECT * FROM orders WHERE cust_id = ?", new OrderRowMapper(), customer.getId());
        if (orders.isEmpty()) {
            return "redirect:/shoppingcart";
        }
        model.addAttribute("orders", orders);
        List<OrderPosition> orderPositions = new ArrayList<>();
        for (Order order : orders) {
            orderPositions.addAll(db.query("SELECT * FROM orderpositions WHERE order_id = ?",
                    new OrderPositionRowMapper(), order.getId()));
        }
        model.addAttribute("orderPositions", orderPositions);

        List<Product> products = db.query("SELECT * FROM products", new ProductRowMapper());
        model.addAttribute("products", products);

        model.addAttribute("templateName", "my_orders");
        model.addAttribute("title", "Bestellungen");
        model.addAttribute("cookiesAccepted", session.getCookiesAccepted());
        return "layout";
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