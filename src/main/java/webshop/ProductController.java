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

@Controller
public class ProductController {

    @Autowired
    private JdbcTemplate db;

    @GetMapping("/products_edit")
    public String products_edit(@CookieValue(value = "loggedInUser", defaultValue = "") String loggedInUser,
            @CookieValue(value = "SessionID", defaultValue = "") String sessID, HttpServletResponse response,
            @CookieValue(value = "loggedInEmp", defaultValue = "") String loggedInEmp, Model model) {
        model.addAttribute("loggedInUser", loggedInUser);
        ShoppingCart shoppingCart = ShoppingCartController.getShoppingCart(sessID, response);
        model.addAttribute("shoppingcart", shoppingCart);
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

    @GetMapping("/product_edit{id}")
    public String product_edit(@CookieValue(value = "loggedInUser", defaultValue = "") String loggedInUser,
            @CookieValue(value = "SessionID", defaultValue = "") String sessID, HttpServletResponse response,
            @CookieValue(value = "loggedInEmp", defaultValue = "") String loggedInEmp, @PathVariable Integer id,
            Model model) {
        model.addAttribute("loggedInUser", loggedInUser);
        ShoppingCart shoppingCart = ShoppingCartController.getShoppingCart(sessID, response);
        model.addAttribute("shoppingcart", shoppingCart);
        if (loggedInEmp.isEmpty()) {
            return "redirect:/";
        }
        Product product = db.queryForObject("SELECT * FROM products WHERE id = ?", new ProductRowMapper(), id);
        model.addAttribute(product);
        model.addAttribute("loggedInEmp", loggedInEmp);
        model.addAttribute("title", "Produktbearbeitung");
        model.addAttribute("templateName", "product_edit");
        return "layout";
    }

    @PostMapping("/product_edit{id}")
    public String product_edit(@CookieValue(value = "loggedInEmp", defaultValue = "") String loggedInEmp,
            @ModelAttribute Product product, @PathVariable Integer id) {
        if (!loggedInEmp.isEmpty()) {
            updateProduct(product, id);
            return "redirect:/products_edit";
        } else {
            return "redirect:/";
        }
    }

    @GetMapping("/product_del{id}")
    public String product_del(@CookieValue(value = "loggedInEmp", defaultValue = "") String loggedInEmp,
            @PathVariable Integer id) {
        if (!loggedInEmp.isEmpty()) {
            deactivateProduct(id);
            return "redirect:/products_edit";
        } else {
            return "redirect:/";
        }
    }

    @GetMapping("/product_add")
    public String product_add(@CookieValue(value = "loggedInUser", defaultValue = "") String loggedInUser,
            @CookieValue(value = "SessionID", defaultValue = "") String sessID, HttpServletResponse response,
            @CookieValue(value = "loggedInEmp", defaultValue = "") String loggedInEmp, Model model) {
        model.addAttribute("loggedInUser", loggedInUser);
        ShoppingCart shoppingCart = ShoppingCartController.getShoppingCart(sessID, response);
        model.addAttribute("shoppingcart", shoppingCart);
        if (loggedInEmp.isEmpty()) {
            return "redirect:/";
        }
        model.addAttribute(new Product());
        model.addAttribute("loggedInEmp", loggedInEmp);
        model.addAttribute("title", "Produktbearbeitung");
        model.addAttribute("templateName", "product_add");
        return "layout";
    }

    @PostMapping("/product_add")
    public String product_add(@CookieValue(value = "loggedInEmp", defaultValue = "") String loggedInEmp,
            @ModelAttribute Product product) {
        if (!loggedInEmp.isEmpty()) {
            saveNewProduct(product);
            return "redirect:/products_edit";
        } else {
            return "redirect:/";
        }
    }

    public Product saveNewProduct(Product product) {
        String saveProductSQL = "INSERT INTO products (product_type, product_name, product_description, "
                + "image_url, amount_ml, price_eur) VALUES (?, ?, ?, ?, ?, ?);";
        db.update(saveProductSQL, product.getType(), product.getName(), product.getDescription(), product.getImgURL(),
                product.getAmount(), product.getPrice());
        String getSQL = "SELECT * FROM products WHERE id = (SELECT MAX(id) FROM products);";
        product = db.queryForObject(getSQL, new ProductRowMapper());
        return product;
    }

    public void updateProduct(Product product, Integer id) {
        String updateProductSQL = "UPDATE products SET product_type = ?, product_name = ?, "
                + "product_description = ?, image_url = ?, amount_ml = ?, price_eur = ? WHERE id = ?;";
        db.update(updateProductSQL, product.getType(), product.getName(), product.getDescription(), product.getImgURL(),
                product.getAmount(), product.getPrice(), id);
    }

    public void deactivateProduct(Integer id) {
        db.update("UPDATE products SET active = FALSE WHERE id = ?", id);
    }
}