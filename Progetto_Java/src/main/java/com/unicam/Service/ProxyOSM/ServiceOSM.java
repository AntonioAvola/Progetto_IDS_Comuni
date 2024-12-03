package com.unicam.Service.ProxyOSM;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.unicam.DTO.MunicipalityDetails;
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
        String url = NOMINATIM_URL + "?q=" + address + "&format=json&addressdetails=1&limit=1";
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

    @Override
    public MunicipalityDetails getDetails(String address) throws IOException {
        String url = NOMINATIM_URL + "?q=" + address + "&format=json&addressdetails=1&limit=1";
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
                    JsonNode boundingbox = firstResult.get("boundingbox");
                    List<Double> min = new ArrayList<>();
                    List<Double> max = new ArrayList<>();
                    if (boundingbox.isArray()) {
                        min.add(boundingbox.get(0).asDouble());
                        min.add(boundingbox.get(2).asDouble());
                        max.add(boundingbox.get(1).asDouble());
                        max.add(boundingbox.get(3).asDouble());
                    }
                    long osm_id = firstResult.get("osm_id").asLong();
                    MunicipalityDetails details = new MunicipalityDetails();
                    details.setSurface(min, max);
                    details.setProvince(firstResult.get("address").get("county").asText());
                    details.setRegion(firstResult.get("address").get("state").asText());
                    details.setCountry(firstResult.get("address").get("country").asText());
                    if(firstResult.get("address").has("postcode"))
                        details.setPostCode(firstResult.get("address").get("postcode").asLong());
                    else{
                        details.setPostCode(null);
                    }
                    return findPopulation(details, osm_id, httpClient);
                }
            }
        }
        return null;
    }

    private MunicipalityDetails findPopulation(MunicipalityDetails details, long osm_id, CloseableHttpClient httpClient) throws IOException {
        String urlDetails = "https://overpass-api.de/api/interpreter?data=[out:json];relation(" + osm_id + ");out%20tags;";
        HttpGet request = new HttpGet(urlDetails);
        try (CloseableHttpResponse response = httpClient.execute(request))
        {
            HttpEntity entity = response.getEntity();
            String responseBody = EntityUtils.toString(entity);
            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonNode = mapper.readTree(responseBody);
            JsonNode firstResult = jsonNode.get("elements");
            if(firstResult.get(0).get("tags").has("population"))
                details.setPopulation(firstResult.get(0).get("tags").get("population").asLong());
            else{
                details.setPopulation(null);
            }
            return details;
        }
    }


}
