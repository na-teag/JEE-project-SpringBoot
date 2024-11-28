package com.example.jeeprojectspringboot.service;

import com.example.jeeprojectspringboot.repository.AddressRepository;
import com.example.jeeprojectspringboot.schoolmanager.Address;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AddressService {

    @Autowired
    private AddressRepository addressRepository;

    // Méthode pour ajouter une nouvelle adresse
    @Transactional
    public Address addAddress(Address address) {
        return addressRepository.save(address);
    }
}