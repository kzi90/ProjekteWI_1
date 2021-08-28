package webshop;

import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * @author Kasimir Eckhardt
 */
@Controller
public class ProductController {

    @Autowired
    private JdbcTemplate db;

    @Autowired
    private SessionController sessionController;

    @Autowired
    private ShoppingCartController shoppingCartController;

    /**
     * edit / add products when logged in as employee
     * 
     * @param loggedInUser
     * @param sessID
     * @param response
     * @param loggedInEmp
     * @param model
     * @return products_edit.html template
     */
    @GetMapping("/products_edit")
    public String productsEdit(@CookieValue(value = "SessionID", defaultValue = "") String sessID,
            HttpServletResponse response, Model model) {
        Session session = sessionController.getOrSetSession(sessID, response);
        String loggedInUser = session.getLoggedInUser();
        model.addAttribute("loggedInUser", loggedInUser);
        ShoppingCart shoppingCart = ShoppingCart.findBySessID(session.getId());
        model.addAttribute("shoppingcart", shoppingCart);
        String loggedInEmp = session.getLoggedInEmp();
        if (loggedInEmp.isEmpty()) {
            return "redirect:/";
        }
        List<Product> products = db.query("SELECT * FROM products WHERE active = TRUE", new ProductRowMapper());
        model.addAttribute("products", products);
        model.addAttribute("loggedInEmp", loggedInEmp);
        model.addAttribute("title", "Produktbearbeitung");
        model.addAttribute("templateName", "products_edit");
        return "layout";
    }

    /**
     * edit product when logged in as employee
     * 
     * @param loggedInUser
     * @param sessID
     * @param response
     * @param loggedInEmp
     * @param id
     * @param model
     * @return product_edit.html template
     */
    @GetMapping("/product_edit{id}")
    public String productEdit(@CookieValue(value = "SessionID", defaultValue = "") String sessID,
            HttpServletResponse response, @PathVariable Integer id, Model model) {
        Session session = sessionController.getOrSetSession(sessID, response);
        String loggedInEmp = session.getLoggedInEmp();
        if (loggedInEmp.isEmpty()) {
            return "redirect:/";
        }
        String loggedInUser = session.getLoggedInUser();
        model.addAttribute("loggedInUser", loggedInUser);
        ShoppingCart shoppingCart = ShoppingCart.findBySessID(session.getId());
        model.addAttribute("shoppingcart", shoppingCart);
        Product product = db.queryForObject("SELECT * FROM products WHERE id = ?", new ProductRowMapper(), id);
        model.addAttribute(product);
        model.addAttribute("loggedInEmp", loggedInEmp);
        model.addAttribute("title", "Produktbearbeitung");
        model.addAttribute("templateName", "product_edit");
        return "layout";
    }

    /**
     * save product changes made on page /product_edit
     * 
     * @param loggedInEmp
     * @param product
     * @param id
     * @return redirect to /products_edit or to homepage if not logged in as
     *         employee
     */
    @PostMapping("/product_edit{id}")
    public String productEdit(@CookieValue(value = "SessionID", defaultValue = "") String sessID,
            @ModelAttribute Product product, @PathVariable Integer id, HttpServletResponse response) {
        if (!sessionController.getOrSetSession(sessID, response).getLoggedInEmp().isEmpty()) {
            updateProduct(product, id);
            return "redirect:/products_edit";
        } else {
            return "redirect:/";
        }
    }

    /**
     * delete product when logged in as employee
     * 
     * @param loggedInEmp
     * @param id
     * @return redirect to /products_edit or to homepage if not logged in as
     *         employee
     */
    @GetMapping("/product_del{id}")
    public String productDel(@CookieValue(value = "SessionID", defaultValue = "") String sessID,
            @PathVariable Integer id, HttpServletResponse response) {
        if (!sessionController.getOrSetSession(sessID, response).getLoggedInEmp().isEmpty()) {
            deactivateProduct(id);
            return "redirect:/products_edit";
        } else {
            return "redirect:/";
        }
    }

    /**
     * add product when logged in as employee
     * 
     * @param loggedInUser
     * @param sessID
     * @param response
     * @param loggedInEmp
     * @param model
     * @return product_add.html template
     */
    @GetMapping("/product_add")
    public String productAdd(@CookieValue(value = "SessionID", defaultValue = "") String sessID,
            HttpServletResponse response, Model model) {
        Session session = sessionController.getOrSetSession(sessID, response);
        String loggedInEmp = session.getLoggedInEmp();
        if (loggedInEmp.isEmpty()) {
            return "redirect:/";
        }
        String loggedInUser = session.getLoggedInUser();
        model.addAttribute("loggedInUser", loggedInUser);
        ShoppingCart shoppingCart = ShoppingCart.findBySessID(session.getId());
        model.addAttribute("shoppingcart", shoppingCart);
        model.addAttribute(new Product());
        model.addAttribute("loggedInEmp", loggedInEmp);
        model.addAttribute("title", "Produktbearbeitung");
        model.addAttribute("templateName", "product_add");
        return "layout";
    }

    /**
     * save product which was added via /product_add
     * 
     * @param loggedInEmp
     * @param product
     * @return redirect to /products_edit or to homepage if not logged in as
     *         employee
     */
    @PostMapping("/product_add")
    public String productAdd(@CookieValue(value = "SessionID", defaultValue = "") String sessID,
            @ModelAttribute Product product, HttpServletResponse response) {
        if (!sessionController.getOrSetSession(sessID, response).getLoggedInEmp().isEmpty()) {
            saveNewProduct(product);
            return "redirect:/products_edit";
        } else {
            return "redirect:/";
        }
    }

    /**
     * save new product
     * 
     * @param product
     * @return saved product
     */
    public Product saveNewProduct(Product product) {
        String saveProductSQL = "INSERT INTO products (product_type, product_name, product_description, "
                + "alc_content, ingredients, image_url, amount_ml, price_eur) VALUES (?, ?, ?, ?, ?, ?, ?, ?);";
        db.update(saveProductSQL, product.getType(), product.getName(), product.getDescription(),
                product.getAlcContent(), product.getIngredients(), product.getImgURL(), product.getAmount(),
                product.getPrice());
        String getSQL = "SELECT * FROM products WHERE id = (SELECT MAX(id) FROM products);";
        product = db.queryForObject(getSQL, new ProductRowMapper());
        return product;
    }

    /**
     * update existing product
     * 
     * @param product
     * @param id
     */
    public void updateProduct(Product product, Integer id) {
        String updateProductSQL = "UPDATE products SET product_type = ?, product_name = ?, product_description = ?,"
                + "alc_content = ?, ingredients = ?, image_url = ?, amount_ml = ?, price_eur = ? WHERE id = ?;";
        db.update(updateProductSQL, product.getType(), product.getName(), product.getDescription(),
                product.getAlcContent(), product.getIngredients(), product.getImgURL(), product.getAmount(),
                product.getPrice(), id);
    }

    /**
     * deactivate product so it won't be shown in the sortiment anymore
     * 
     * @param id
     */
    public void deactivateProduct(Integer id) {
        db.update("UPDATE products SET active = FALSE WHERE id = ?", id);
    }
}