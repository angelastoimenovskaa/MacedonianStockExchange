package program.program.Service.Impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import program.program.Service.AnalysisService;
import program.program.Utility.JsonUtils;


import java.util.Map;


@Service
public class AnalysisServiceImpl implements AnalysisService {

    private final RestTemplate restTemplate;
    private final String API_BASE_URL = "http://localhost:8000";


    public AnalysisServiceImpl() {
        this.restTemplate = new RestTemplate();
    }

    @Override
    public String getBasicAnalysis(String issuerName) {
        String url = API_BASE_URL + "/basic/" + issuerName;
        String jsonResponse = restTemplate.getForObject(url, String.class);
        return JsonUtils.prettyPrintJson(jsonResponse);
    }


    @Override
    public String getLSTMPrediction(String issuerName) {
        String url = API_BASE_URL + "/lstm/" + issuerName;
        Map<String, Object> response = restTemplate.getForObject(url, Map.class);

        // Prettify for debugging or logging purposes
        ObjectMapper mapper = new ObjectMapper();
        String prettyJson = null;
        try {
            prettyJson = JsonUtils.prettyPrintJson(mapper.writeValueAsString(response));
            System.out.println(prettyJson); // Debug or log the prettified JSON
        } catch (Exception e) {
            e.printStackTrace();
        }

        return prettyJson; // Return the original Map
    }


    @Override
    public String getSentimentAnalysis() {
        String url = API_BASE_URL + "/sentimentanalysis";
        Map<String, Object> response = restTemplate.getForObject(url, Map.class);

        // Debug or log prettified JSON
        ObjectMapper mapper = new ObjectMapper();
        String prettyJson = null;
        try {
            prettyJson = JsonUtils.prettyPrintJson(mapper.writeValueAsString(response));
            System.out.println(prettyJson);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return prettyJson; // Return the original Map
    }

}