package webshop;

import java.io.File;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletResponse;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;

/**
 * @author Lukas Kröker
 */
@Controller
public class UploadController {

    @Autowired
    SessionController sessionController;

    @GetMapping("/img_upload")
    public String imgUplaod(@CookieValue(value = "SessionID", defaultValue = "") String sessID,
            HttpServletResponse response, Model model) {
        Session session = sessionController.getOrSetSession(sessID, response);
        // TODO: check Mitarbeiter login
        model.addAttribute("loggedInUser", session.getLoggedInUser());
        model.addAttribute("shoppingcart", session.getShoppingCart());
        model.addAttribute("templateName", "img_upload");
        model.addAttribute("title", "Upload");
        return "layout";
    }

    @PostMapping("/upload")
    public ResponseEntity<String> handleFileUpload(@RequestParam("img") MultipartFile img) {
        String imgName = img.getOriginalFilename();
        try {
            String filePath = new File("").getAbsolutePath();
            img.transferTo(new File(filePath + "\\src\\main\\resources\\static\\images\\" + imgName));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        return ResponseEntity.ok("Bild wurde erfolgreich hochgeladen.");
    }
}