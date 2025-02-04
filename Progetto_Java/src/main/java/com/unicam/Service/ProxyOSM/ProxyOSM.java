package com.unicam.Service.ProxyOSM;

import com.unicam.DTO.MunicipalityDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ProxyOSM implements OpenStreetMapProxy {
    @Autowired
    private ServiceOSM servizioMappa;
    private Map<String, List<Double>> cache = new HashMap<String, List<Double>>();

    @Override
    public List<Double> getCoordinates(String address) throws IOException {
        if (cache.containsKey(address)) {
            System.out.println("Recupero coordinate da cache per: " + address);
            return cache.get(address);
        }
        System.out.println("Recupero coordinate da OSMService per: " + address);
        List<Double> coordinates = servizioMappa.getCoordinates(address);
        if (coordinates != null) {
            cache.put(address, coordinates);
        }
        return coordinates;
    }

    @Override
    public MunicipalityDetails getDetails(String address) throws IOException {
        MunicipalityDetails details = servizioMappa.getDetails(address);
        return details;
    }

}
