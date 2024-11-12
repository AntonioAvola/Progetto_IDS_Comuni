package com.unicam.Controller;

import com.unicam.DTO.Request.InterestPointRequest;
import com.unicam.Entity.CommandPattern.InterestPointCommand;
import com.unicam.Service.Content.InterestPointService;
import com.unicam.Service.ProxyOSM.ProxyOSM;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@RestController
@RequestMapping(name = "api/contributor")
public class ContributorController {
    @Autowired
    private InterestPointService interestPointService;
    @Autowired
    private ProxyOSM proxy;

    @PostMapping("Api/Contributor/addInterestPoint")
    public void AddInterestPoint(InterestPointRequest request) throws IOException {
        //TODO controlla autorizzazioni
        String address = request.getTitle();
        String currentMunicipality = URLEncoder.encode("castelfidardo", StandardCharsets.UTF_8.toString());
        address = URLEncoder.encode(address, StandardCharsets.UTF_8.toString());
        List<Double> coordinates = proxy.getCoordinates(address + "," + currentMunicipality);


    }


}
