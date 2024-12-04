package com.unicam.Service;

import com.unicam.DTO.Response.PromotionResponse;
import com.unicam.Entity.RolePromotion;
import com.unicam.Entity.User;
import com.unicam.Repository.PromotionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PromotionService {

    private final PromotionRepository promotionRepository;

    public PromotionService(PromotionRepository promotionRepository) {
        this.promotionRepository = promotionRepository;
    }

    public void addPromotion(RolePromotion promotion) {
        this.promotionRepository.save(promotion);
    }

    public boolean checkPromotion(User user) {
        return this.promotionRepository.existsByUser(user);
    }

    public RolePromotion getPromotion(long idPromotion) {
        return this.promotionRepository.findById(idPromotion);
    }

    public void removePromotionRequest(long idPromotion) {
        RolePromotion promotion = this.promotionRepository.findById(idPromotion);
        this.promotionRepository.delete(promotion);
    }

    public List<PromotionResponse> getAllPromotionRequests(String municipality) {
        List<RolePromotion> promotionResponses = this.promotionRepository.findAllByMunicipality(municipality);
        return convertResponse(promotionResponses);
    }

    private List<PromotionResponse> convertResponse(List<RolePromotion> promotionResponses) {
        List<PromotionResponse> promotionResponseList = new ArrayList<>();
        for (RolePromotion rolePromotion : promotionResponses) {
            PromotionResponse promotion = new PromotionResponse(rolePromotion.getId(), rolePromotion.getUser().getUsername(), rolePromotion.getUser().getRole().name(), rolePromotion.getPromotion().name());
            promotionResponseList.add(promotion);
        }
        return promotionResponseList;
    }


}
