package com.unicam.Service.ProxyOSM;

import com.unicam.DTO.MunicipalityDetails;

import java.io.IOException;
import java.util.List;

public interface OpenStreetMapProxy {
    List<Double> getCoordinates(String address) throws IOException;
    MunicipalityDetails getDetails(String address) throws IOException;
}
