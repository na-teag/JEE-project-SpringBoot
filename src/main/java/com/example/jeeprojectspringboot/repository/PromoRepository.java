package com.example.jeeprojectspringboot.repository;

import com.example.jeeprojectspringboot.schoolmanager.Promo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PromoRepository  extends JpaRepository<Promo, Long> {
    Promo findById(long id);

    List<Promo> findAll();

    Promo save(Promo promo);

    void deleteById(long id);
}
