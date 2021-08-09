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
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class ProductController {

    @Autowired
    private JdbcTemplate db;

    @GetMapping("/product_edit")
    public String product_edit(@CookieValue(value = "loggedInUser", defaultValue = "") String loggedInUser,
            @CookieValue(value = "SessionID", defaultValue = "") String sessID, HttpServletResponse response,
            @CookieValue(value = "loggedInEmp", defaultValue = "") String loggedInEmp, Model model) {
        model.addAttribute("loggedInUser", loggedInUser);
        ShoppingCart shoppingCart = ShoppingCartController.getShoppingCart(sessID, response);
        model.addAttribute("shoppingcart", shoppingCart);
        if (loggedInEmp.isEmpty()) {
            return "redirect:/";
        }
        List<Product> products = db.query("SELECT * FROM products", new ProductRowMapper());
        model.addAttribute("products", products);
        model.addAttribute("loggedInEmp", loggedInEmp);
        model.addAttribute("title", "Produktbearbeitung");
        model.addAttribute("templateName", "product_edit");
        return "layout";
    }

    @PostMapping("/product_edit")
    public String product_edit(@ModelAttribute Product product) {
        // Hier muss die Logik zum Löschen / Ändern / Hinzufügen eines Produkts
        // implementiert werden. Durch klicken des jeweiligen Submit-Buttons muss die
        // Information übergeben werden, was mit welchem Produkt gemacht werden soll.
        // Ggf. zusätzl. ModelAttribute, das aussagt, ob gelöscht, geändert, oder hinzugefügt werden soll?
        return "redirect:/product_edit";
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

    public void updateProduct(Product product) {
        String updateProductSQL = "UPDATE products SET product_type = ?, product_name = ?, "
                + "product_description = ?, image_url = ?, amount_ml = ?, price_eur = ? WHERE id = ?;";
        db.update(updateProductSQL, product.getType(), product.getName(), product.getDescription(), product.getImgURL(),
                product.getAmount(), product.getPrice(), product.getId());
    }

    public void deleteProduct(Product product) {
        db.update("DELETE FROM products WHERE id = ?", product.getId());
    }
}