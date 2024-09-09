package software.kasunkavinda.Travel_Planner.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import software.kasunkavinda.Travel_Planner.dto.ResponseDto;

import java.util.Arrays;

@Service
@RequiredArgsConstructor
public class OpenAIService {

    @Value("${openai.api.key}")
    private String openaiApiKey;

    private final RestTemplate restTemplate;

    private final ObjectMapper objectMapper;

    Logger logger = LoggerFactory.getLogger(OpenAIService.class);

    public ResponseDto<String> getSuggestions(String origin, String destination) throws JsonProcessingException {
        logger.info("Request received to get recommendations for trip from {} to {}", origin, destination);
        StringBuilder recommendations = new StringBuilder();
        ObjectMapper objectMapper = new ObjectMapper();

        for (int i = 1; i <= 5; i++) {
            // Generate a prompt asking for specific details
            String prompt = String.format("Please recommend a top place number %d in %s. Provide the place name, a description of its specialty or uniqueness, and the Google Maps location URL.Format the response as follows:\\nPlace Name: [Name]\\nGoogle Maps Location: [URL]\\nDescription: [Description]", i, destination);

            // Call OpenAI API
            String url = "https://api.openai.com/v1/chat/completions";
            String requestJson = String.format("{\"model\": \"gpt-3.5-turbo\", \"messages\": [{\"role\": \"system\", \"content\": \"You are a helpful travel assistant.\"}, {\"role\": \"user\", \"content\": \"%s\"}], \"max_tokens\": 200}", prompt);

            // Set the headers
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + openaiApiKey);
            headers.set("Content-Type", "application/json");

            HttpEntity<String> entity = new HttpEntity<>(requestJson, headers);

            logger.info("Sending request to OpenAI API for place {}", i);

            // Send the request and parse the response
            ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);

            // Parse the response
            JsonNode responseBody = objectMapper.readTree(response.getBody());
            String content = responseBody.path("choices").get(0).path("message").path("content").asText();

            // Extract details from the response
            String[] lines = content.split("\n");
            String placeName = lines[0].trim();
            String googleMapsUrl = lines[1].trim();
            String description = String.join("\n", Arrays.copyOfRange(lines, 2, lines.length)).trim();

            // Format and append the content
            recommendations.append(" ").append(i).append(": ").append(placeName).append("\n");
            recommendations.append(" ").append(googleMapsUrl).append("\n");
            recommendations.append(" ").append(description).append("\n\n");

            // Log the recommendation
            logger.info("Received content for place {}: {}", i, content);
        }
        return new ResponseDto<>(recommendations.toString(), "Success", "Recommendations for " + origin + " to " + destination);
    }



    public ResponseDto<String> chatBot(String chat) throws JsonProcessingException {
        logger.info("Request received to get chat.");

        // Call OpenAI API
        String url = "https://api.openai.com/v1/chat/completions";
        String requestJson = String.format("{\"model\": \"gpt-3.5-turbo\", \"messages\": [{\"role\": \"system\", \"content\": \"You are a helpful assistant.\"}, {\"role\": \"user\", \"content\": \"%s\"}], \"max_tokens\": 150}", chat+"limit your response to 150 characters");

        // Set the headers
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + openaiApiKey);
        headers.set("Content-Type", "application/json");

        HttpEntity<String> entity = new HttpEntity<>(requestJson, headers);

        logger.info("Sending request to OpenAI API");

        // Send the request
        ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);

        logger.info("Received response from OpenAI API" + response.getBody());
        // Process the response
        return new ResponseDto<>(extractChatResponse(response.getBody()), "Success", "Chat response");

    }

    private String extractChatResponse(String jsonResponse) throws JsonProcessingException {
            JsonNode rootNode = objectMapper.readTree(jsonResponse);
            JsonNode choicesNode = rootNode.path("choices");
            if (choicesNode.isArray() && choicesNode.size() > 0) {
                JsonNode messageNode = choicesNode.get(0).path("message").path("content");
                return messageNode.asText();
            } else {
                return "No response content available.";
            }
    }
}

