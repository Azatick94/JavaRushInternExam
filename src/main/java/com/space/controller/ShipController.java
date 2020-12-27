package com.space.controller;

import com.space.model.Ship;
import com.space.model.ShipType;
import com.space.service.ShipService;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
//@Controller
@RequestMapping("/rest")
public class ShipController {

    private final ShipService shipService;

    @Autowired
    public ShipController(ShipService shipService) {
        this.shipService = shipService;
    }


    @GetMapping("/ships")
    public String getShipsList(@RequestParam(required=false) String name,
                               @RequestParam(required=false) String planet,
                               @RequestParam(required=false) ShipType shipType,
                               @RequestParam(required=false) Long after,
                               @RequestParam(required=false) Long before,
                               @RequestParam(required=false) Boolean isUsed,
                               @RequestParam(required=false) Double minSpeed,
                               @RequestParam(required=false) Double maxSpeed,
                               @RequestParam(required=false) Integer minCrewSize,
                               @RequestParam(required=false) Integer maxCrewSize,
                               @RequestParam(required=false) Double minRating,
                               @RequestParam(required=false) Double maxRating,
                               @RequestParam(required=false) ShipOrder order,
                               @RequestParam(required=false) Integer pageNumber,
                               @RequestParam(required=false) Integer pageSize)
    {
        System.out.println(shipService.findAll());
//        return (ResponseEntity<List<Ship>>) shipService.findAll();

        return null;
    }

    @GetMapping("/ships/count")
    public Integer getShipsCount(@RequestParam(required=false) String name,
                                @RequestParam(required=false) String planet,
                                @RequestParam(required=false) ShipType shipType,
                                @RequestParam(required=false) Long after,
                                @RequestParam(required=false) Long before,
                                @RequestParam(required=false) Boolean isUsed,
                                @RequestParam(required=false) Double minSpeed,
                                @RequestParam(required=false) Double maxSpeed,
                                @RequestParam(required=false) Integer minCrewSize,
                                @RequestParam(required=false) Integer maxCrewSize,
                                @RequestParam(required=false) Double minRating,
                                @RequestParam(required=false) Double maxRating
                                )
    {

        Integer count = 0;
//        List<Ship> shipsAll = shipService.selectAllFromTable();

        if (planet!=null) {
            System.out.println();

        }

        return Math.toIntExact(shipService.count());
    }

    @PostMapping("/ships")
    public String createShip()
    {
    return null;
    }

    @GetMapping("/ships/{id}")
    public String getShip()
    {
    return null;
    }

    @PostMapping("/ships/{id}")
    public String updateShip()
    {
        return null;
    }

    @DeleteMapping("ships/{id}")
    public String deleteShip()
    {
        return null;
    }




}
