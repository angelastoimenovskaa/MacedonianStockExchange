package program.program.Web;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import program.program.Model.Issuer;
import program.program.Service.IssuerService;

import java.util.List;

@Controller
@RequestMapping("/issuers")
public class IssuersController {
    public IssuerService service;

    public IssuersController(IssuerService service) {
        this.service = service;
    }

    @GetMapping
    public String hello(Model model, @RequestParam(required = false) String error){
        if (error != null && !error.isEmpty()) {
            model.addAttribute("hasError", true);
            model.addAttribute("error", error);
        }
        List<Issuer> issuers = service.listAll();
        model.addAttribute("issuers", issuers);
        model.addAttribute("bodyContent","issuers");
        return "index";
    }

    @GetMapping("/details/{id}")
    public String details(@PathVariable int id, Model model){
        Issuer issu = this.service.getById(id);
        model.addAttribute("issuer", issu);
        model.addAttribute("bodyContent","issuers");
        return "issuers";
    }
}
