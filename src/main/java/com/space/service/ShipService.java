package com.space.service;

import com.space.model.Ship;
import com.space.repository.ShipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

// класс сервис, который предоставляяет операции над базой данных.
@Service
public class ShipService {

    private final ShipRepository shipRepository;

    @Autowired
    public ShipService(ShipRepository shipRepository) {
        this.shipRepository = shipRepository;
    }

    public List<Ship> findAll() {
        return shipRepository.findAll();
    }

    public long count() {
        return shipRepository.count();
    }

    public void deleteByID(Long shipId) {
        shipRepository.deleteById(shipId);
    }

    public Ship saveShip(Ship ship) {
        return shipRepository.save(ship);
    }



}
