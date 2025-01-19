package program.program.Web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import program.program.Model.Issuer;
import program.program.Service.AnalysisService;
import program.program.Service.IssuerService;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/issuers")
public class IssuersController {
    private final IssuerService service;
    private final AnalysisService analysisService;

    public IssuersController(IssuerService service, AnalysisService analysisService) {
        this.service = service;
        this.analysisService = analysisService;
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

        // Get analysis results
        String basicAnalysis = analysisService.getBasicAnalysis(issuer.getName());
        String lstmPrediction = analysisService.getLSTMPrediction(issuer.getName());

        model.addAttribute("basicAnalysis", basicAnalysis);
        model.addAttribute("lstmPrediction", lstmPrediction);
        model.addAttribute("bodyContent", "issuer-details");
        return "issuers";
    }

    @GetMapping("/sentiment")
    public String sentimentAnalysis(Model model) {
        String sentimentResults = analysisService.getSentimentAnalysis();
        model.addAttribute("sentimentResults", sentimentResults);
        model.addAttribute("bodyContent", "sentiment-analysis");
        return "sentiment-analysis";
    }
}