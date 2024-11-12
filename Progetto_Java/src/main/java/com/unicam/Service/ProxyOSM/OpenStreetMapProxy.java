package com.unicam.Service.ProxyOSM;

import java.io.IOException;
import java.util.List;

public interface OpenStreetMapProxy {
    List<Double> getCoordinates(String address) throws IOException;
}
