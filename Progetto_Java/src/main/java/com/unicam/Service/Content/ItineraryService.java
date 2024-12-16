package com.unicam.Service.Content;

import com.unicam.DTO.Response.InterestPointResponse;
import com.unicam.DTO.Response.ItineraryResponse;
import com.unicam.Entity.Content.ContentStatus;
import com.unicam.Entity.Content.InterestPoint;
import com.unicam.Entity.Content.Itinerary;
import com.unicam.Entity.User;
import com.unicam.Repository.Content.ItineraryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    private boolean exists(Itinerary itinerary) {
        List<Itinerary> itineraries = this.repoItinerary.findByMunicipality(itinerary.getMunicipality());
        for(Itinerary itineraryFound : itineraries){
            if(itineraryFound.getPath().size() == itinerary.getPath().size()){
                if(itineraryFound.getPath().containsAll(itinerary.getPath()))
                    return true;
            }
        }
        return false;
    }

    public void addFavorite(long idItnerary, long idUser){
        Itinerary itinerary = this.repoItinerary.findById(idItnerary);
        if(itinerary.getIdUserFavorites().contains(idUser))
            throw new IllegalArgumentException("L'itinerario è già presente tra i preferiti");
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

    public boolean getAndRemoveItinerary(long idItinerary, User author) {
        if(!this.repoItinerary.existsByIdAndAuthor(idItinerary, author))
            return false;
        this.removeItinerary(idItinerary);
        return true;
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

//    private boolean checkNoDuplicatedPoints(List<String> path) {
//        //l'hashset contiene tutti elementi univoci
//        HashSet<String> set = new HashSet<>(path);
//        //se il path ha la stessa lunghezza dell'hashset, allora non sono presenti punti di interesse duplicati
//        return path.size() == set.size();
//    }

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
        }
    }

    public boolean checkMunicipality(long idContent, String municipality) {
        return this.repoItinerary.existsByIdAndMunicipality(idContent, municipality);
    }

    /*public List<Itinerary> getByUser(User user) {
        return this.repoItinerary.findAllByAuthor(user);
    }*/

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
