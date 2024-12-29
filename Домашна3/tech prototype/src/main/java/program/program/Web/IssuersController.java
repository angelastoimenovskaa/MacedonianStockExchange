// Modified IssuersController.java
package program.program.Web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import program.program.Model.Issuer;
import program.program.Service.IssuerService;
import program.program.Service.MLPredictionService;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/issuers")
public class IssuersController {
    private final IssuerService service;
    private final MLPredictionService mlService;

    public IssuersController(IssuerService service, MLPredictionService mlService) {
        this.service = service;
        this.mlService = mlService;
    }

    @GetMapping
    public String hello(Model model, @RequestParam(required = false) String error) {
        if (error != null && !error.isEmpty()) {
            model.addAttribute("hasError", true);
            model.addAttribute("error", error);
        }
        List<Issuer> issuers = service.listAll();
        model.addAttribute("issuers", issuers);
        model.addAttribute("bodyContent", "issuers");
        return "index";
    }

    @GetMapping("/details/{id}")
    public String details(@PathVariable int id, Model model) {
        Issuer issuer = this.service.getById(id);
        model.addAttribute("issuer", issuer);

        if (issuer != null) {
            try {
                Map<String, Double> predictions = mlService.getMetrics(issuer.getName());
                Map<String, Double> metrics = mlService.getMetrics(issuer.getName());

                model.addAttribute("predictions", predictions);
                model.addAttribute("metrics", metrics);
                model.addAttribute("hasPredictions", true);
            } catch (Exception e) {
                model.addAttribute("predictionError", "Failed to generate predictions: " + e.getMessage());
                model.addAttribute("hasPredictions", false);
            }
        }

        model.addAttribute("bodyContent", "issuer-details");
        return "master-template";
    }
}