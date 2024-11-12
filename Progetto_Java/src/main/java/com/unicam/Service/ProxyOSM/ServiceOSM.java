package com.unicam.Service.ProxyOSM;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.http.HttpEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class ServiceOSM implements OpenStreetMapProxy {
    private static final String NOMINATIM_URL = "https://nominatim.openstreetmap.org/search";
    public List<Double> getCoordinates(String address) throws IOException {
        String url = NOMINATIM_URL + "?q=" + address + "&format=json&limit=1";
        try (CloseableHttpClient httpClient = HttpClients.createDefault())
        {
            HttpGet request = new HttpGet(url);
            try (CloseableHttpResponse response = httpClient.execute(request))
            {
                HttpEntity entity = response.getEntity();
                String responseBody = EntityUtils.toString(entity);
                ObjectMapper mapper = new ObjectMapper();
                JsonNode jsonNode = mapper.readTree(responseBody);
                if (jsonNode.isArray() && jsonNode.size() > 0) {
                    JsonNode firstResult = jsonNode.get(0);
                    double lat = firstResult.get("lat").asDouble();
                    double lon = firstResult.get("lon").asDouble();
                    List<Double> coordinate = new ArrayList<>();
                    coordinate.add(lat);
                    coordinate.add(lon);
                    return coordinate;
                }
            }
        }
        return null;
    }
}
