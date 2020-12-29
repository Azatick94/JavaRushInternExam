package com.space.controller;

import com.space.model.Ship;
import com.space.model.ShipType;
import com.space.service.ShipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/rest")
public class ShipController {

    private final ShipService shipService;

    @Autowired
    public ShipController(ShipService shipService) {
        this.shipService = shipService;
    }


    @GetMapping("/ships")
    public List<Ship> getShipsList(@RequestParam(required=false) String name,
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
        List<Ship> lstShips = shipService.findAll();
        if (pageNumber==null)
            pageNumber=0;
        if (pageSize==null)
            pageSize=3;

        if (shipType!=null && after!=null && before!=null) {
            List<Ship> lstShipsFiltered = new ArrayList<Ship>();
            int skip = pageNumber * pageSize;
            for (int i = skip; i < Math.min(skip + pageSize, lstShips.size()); i++) {
                Ship currentShip = lstShips.get(i);
                Long dateMilliseconds = currentShip.getProdDate().getTime();

                if ((currentShip.getShipType().equals(shipType) && dateMilliseconds>=before && dateMilliseconds<=after) ||
                        (currentShip.getShipType().equals(shipType) && dateMilliseconds<=before && dateMilliseconds>=after)
                )
                    lstShipsFiltered.add(currentShip);
            }
            return lstShipsFiltered;
        }
        else if (name!=null && pageNumber!=null) {
//            getAllWithFiltersNamePageNumber
            List<Ship> lstShipsFiltered = new ArrayList<Ship>();
            int skip = pageNumber * pageSize;
            for (int i = skip; i < Math.min(skip + pageSize, lstShips.size()); i++) {
                Ship currentShip = lstShips.get(i);
                if (currentShip.getName().equals(name))
                    lstShipsFiltered.add(currentShip);
            }
            return lstShipsFiltered;
        }
        else {
            //        getAllWithoutFiltersReturnAllShips
            List<Ship> lstShipsFiltered = new ArrayList<Ship>();
            int skip = pageNumber * pageSize;
            for (int i = skip; i < Math.min(skip + pageSize, lstShips.size()); i++) {
                Ship currentShip = lstShips.get(i);
                lstShipsFiltered.add(currentShip);
            }
            return lstShipsFiltered;
        }

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

        List<Ship> lstShips = shipService.findAll();

        if (minRating!=null && minCrewSize!=null && minSpeed!=null) {
//        test2 - getCountWithFiltersMinRatingMinCrewSizeMinSpeed
            List<Ship> lstShipsFiltered = new ArrayList<Ship>();
            for (int i = 0; i < lstShips.size(); i++) {
                Ship currentShip = lstShips.get(i);
                if (currentShip.getRating()>=minRating && currentShip.getCrewSize()>=minCrewSize && currentShip.getSpeed()>=minSpeed)
                    lstShipsFiltered.add(currentShip);
            }
            return lstShipsFiltered.size();
        }
        else if (name!=null && after!=null && maxRating!=null) {
//        test3 - getCountWithFiltersNameAfterMaxRating ???
            List<Ship> lstShipsFiltered = new ArrayList<Ship>();
            for (int i = 0; i < lstShips.size(); i++) {
                Ship currentShip = lstShips.get(i);
                if (currentShip.getName().contains(name) && currentShip.getProdDate().before(new Date(after)) && currentShip.getRating()<=maxRating)
                    lstShipsFiltered.add(currentShip);
            }
            return lstShipsFiltered.size();
        }
        else if (shipType!=null && isUsed!=null) {
//            test4 - getCountWithFiltersShipTypeIsUsed
            List<Ship> lstShipsFiltered = new ArrayList<Ship>();
            for (int i = 0; i < lstShips.size(); i++) {
                Ship currentShip = lstShips.get(i);
                if (currentShip.getShipType().equals(shipType) && currentShip.getUsed()==isUsed)
                    lstShipsFiltered.add(currentShip);
            }
            return lstShipsFiltered.size();
        }
        else if (shipType!=null && maxCrewSize!=null) {
//            test5 - getCountWithFiltersShipTypeMaxCrewSize
            List<Ship> lstShipsFiltered = new ArrayList<Ship>();
            for (int i = 0; i < lstShips.size(); i++) {
                Ship currentShip = lstShips.get(i);
                if (currentShip.getShipType().equals(shipType) && currentShip.getCrewSize()<=maxCrewSize)
                    lstShipsFiltered.add(lstShips.get(i));
            }
            return lstShipsFiltered.size();
        }
        else if (planet!=null) {
//            test6 - getCountWithFiltersPlanet
            Set<String> set = new HashSet();
            for (int i = 0; i < lstShips.size(); i++)
                set.add(lstShips.get(i).getPlanet());
            return set.size();
        }
        else if (shipType!=null && before!=null && maxSpeed!=null) {
//            test7 - getCountWithFiltersShipTypeBeforeMaxSpeed
            List<Ship> lstShipsFiltered = new ArrayList<Ship>();
            for (int i = 0; i < lstShips.size(); i++) {
                Ship currentShip = lstShips.get(i);
                if ( (currentShip.getShipType().equals(shipType) && currentShip.getProdDate().getTime()>=before) && currentShip.getSpeed()<=maxSpeed ) {
                    lstShipsFiltered.add(lstShips.get(i));
                }
            }
            return lstShipsFiltered.size();
        }
        else if (isUsed!=null && minSpeed!=null && maxSpeed!=null) {
//            test8 - getCountWithFiltersIsUsedMinMaxSpeed
            List<Ship> lstShipsFiltered = new ArrayList<Ship>();
            for (int i = 0; i < lstShips.size(); i++) {
                Ship currentShip = lstShips.get(i);
                if (currentShip.getUsed()==isUsed && currentShip.getSpeed()>=minSpeed && currentShip.getSpeed()<=maxSpeed) {
                    lstShipsFiltered.add(lstShips.get(i));
                }
            }
            return lstShipsFiltered.size();
        }
        else
//            test1 - getCountWithoutFiltersReturnAllShips
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
