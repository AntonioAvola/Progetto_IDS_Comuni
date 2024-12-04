package com.unicam.Repository;

import com.unicam.Entity.RolePromotion;
import com.unicam.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PromotionRepository extends JpaRepository<RolePromotion, Long> {


    RolePromotion findById(long id);

    boolean existsByUser(User user);

    List<RolePromotion> findAllByMunicipality(String municipality);
}
