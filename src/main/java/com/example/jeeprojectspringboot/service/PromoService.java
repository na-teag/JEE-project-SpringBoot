package com.example.jeeprojectspringboot.service;

import com.example.jeeprojectspringboot.repository.PromoRepository;
import com.example.jeeprojectspringboot.schoolmanager.Promo;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
public class PromoService {

    private final PromoRepository promoRepository;

    @Autowired
    public PromoService(PromoRepository promoRepository) {
        this.promoRepository = promoRepository;
    }

    public Promo getPromo(Long id) {
        return promoRepository.findById(id).orElse(null);
    }

    public List<Promo> getAllPromo() {
        return promoRepository.findAll();
    }

    @Transactional
    public Promo save(Promo promo) {
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        Set<ConstraintViolation<Promo>> errors = validator.validate(promo);
        if (!errors.isEmpty()) {
            throw new ConstraintViolationException(errors);
        }
        return promoRepository.save(promo);
    }

    @Transactional
    public void deletePromo(Long id) {
        promoRepository.deleteById(id);
    }
}
