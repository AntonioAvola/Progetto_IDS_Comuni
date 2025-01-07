package com.unicam.Service.Content;

import com.unicam.DTO.Response.InterestPointResponse;
import com.unicam.DTO.Response.ItineraryResponse;
import com.unicam.Entity.Content.ContentStatus;
import com.unicam.Entity.Content.InterestPoint;
import com.unicam.Entity.Content.Itinerary;
import com.unicam.Entity.Municipality;
import com.unicam.Entity.User;
import com.unicam.Repository.Content.ItineraryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Service
public class ItineraryService {

    @Autowired
    private ItineraryRepository repoItinerary;

    public void addItineray(Itinerary itinerary){
        this.repoItinerary.save(itinerary);
    }

    public void removeItinerary(long idItinerary){
        Itinerary itinerary = this.repoItinerary.findById(idItinerary);
        this.repoItinerary.delete(itinerary);
    }

    public void addFavorite(long idItnerary, long idUser){
        Itinerary itinerary = this.repoItinerary.findById(idItnerary);
        if(itinerary.getIdUserFavorites().contains(idUser))
            throw new ResponseStatusException(HttpStatus.CONFLICT, "L'itinerario è già presente tra i preferiti");
        itinerary.getIdUserFavorites().add(idUser);
        this.repoItinerary.save(itinerary);
    }

    public void checkItinerary(InterestPoint point) {
        List<Itinerary> allItineraries = this.repoItinerary.findByMunicipality(point.getMunicipality());
        for(Itinerary itinerary : allItineraries){
            if(itinerary.getPath().contains(point)){
                if(itinerary.getPath().size() == 2){
                    this.repoItinerary.delete(itinerary);
                }else{
                    itinerary.getPath().remove(point);
                    this.repoItinerary.save(itinerary);
                }
            }
        }
    }

    public void removeItineraryUser(User user) {
        List<Itinerary> itineraries = this.repoItinerary.findAllByAuthor(user);
        this.repoItinerary.deleteAll(itineraries);
    }

    public boolean checkPathLength(List<Long> path) {
        if(path.size()>=2)
            return true;
        return false;
    }

    public List<ItineraryResponse> getItinerary(String municipality, ContentStatus status) {
        List<Itinerary> itineraries = this.repoItinerary.findByMunicipalityAndStatus(municipality, status);
        return convertResponse(itineraries);
    }

    private List<ItineraryResponse> convertResponse(List<Itinerary> itineraries) {
        List<ItineraryResponse> response = new ArrayList<>();
        for(Itinerary itinerary : itineraries){
            List<InterestPointResponse> path = convertPathInResponse(itinerary.getPath());
            ItineraryResponse itineraryResponse = new ItineraryResponse(itinerary.getId(), itinerary.getTitle(),
                    itinerary.getDescription(), path);
            response.add(itineraryResponse);
        }
        return response;
    }

    private List<InterestPointResponse> convertPathInResponse(List<InterestPoint> path) {
        List<InterestPointResponse> response = new ArrayList<>();
        for(InterestPoint point : path){
            response.add(new InterestPointResponse(point.getId(), point.getTitle(), point.getDescription(), point.getReference().getName(),
                    point.getOpen(), point.getClose()));
        }
        return response;
    }

    public void approveOrRejectItinerary(long idContent, ContentStatus approved) {
        if(approved.equals(ContentStatus.REJECTED)){
            this.removeItinerary(idContent);
        }
        else{
            Itinerary itinerary = this.repoItinerary.findById(idContent);
            itinerary.setStatus(approved);
            this.repoItinerary.save(itinerary);
            deleteItineraryPending(itinerary.getPath(), itinerary.getMunicipality());
        }
    }

    public void deleteItineraryPending(List<InterestPoint> path, String municipality) {
        List<Itinerary> list = this.repoItinerary.findByMunicipalityAndStatus(municipality, ContentStatus.PENDING);
        for(Itinerary itinerary2 : list){
            if(itinerary2.getPath().size() == path.size()){
                if(itinerary2.getPath().containsAll(path)){
                    this.repoItinerary.delete(itinerary2);
                }
            }
        }
    }

    public List<ItineraryResponse> getByUser(User user) {
        List<Itinerary> itineraries = this.repoItinerary.findAllByAuthor(user);
        return convertResponse(itineraries);
    }

    public void reportItinerary(long idContent) {
        Itinerary itinerary = this.repoItinerary.findById(idContent);
        itinerary.setStatus(ContentStatus.REPORTED);
        this.repoItinerary.save(itinerary);
    }
}
