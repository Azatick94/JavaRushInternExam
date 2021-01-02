package com.space.controller;

import com.space.model.Ship;
import com.space.model.ShipType;
import com.space.service.ShipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
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
                               @RequestParam(required=false, defaultValue="0") Integer pageNumber,
                               @RequestParam(required=false, defaultValue="3") Integer pageSize)

    {
        List<Ship> lstShips = shipService.findAll();

        if (planet!=null && pageSize!=null) {
//            3) getAllWithFiltersPlanetPageSize OK!!!
            return getShipInfosByPage(pageNumber, pageSize, getShipInfosByPlanet(planet, lstShips));
        }
        else if (shipType!=null && after!=null & before!=null) {
//            4) getAllWithFiltersShipTypeAfterBefore OK!!!
            return getShipInfosByPage(pageNumber, pageSize, getShipInfosByShipType(shipType,
                    getShipInfosByAfter(after, getShipInfosByBefore(before, lstShips))));
        }
        else if (shipType!=null && minSpeed!=null && maxSpeed!=null) {
//           5) getAllWithFiltersShipTypeMinSpeedMaxSpeed OK!!!
            return getShipInfosByPage(pageNumber, pageSize,
                    getShipInfosByShipType(shipType,
                            getShipInfosByMinSpeed(minSpeed, getShipInfosByMaxSpeed(maxSpeed, lstShips))));
        }
        else if (shipType!=null && minCrewSize!=null && maxCrewSize!=null) {
//            6) getAllWithFiltersShipTypeMinCrewSizeMaxCrewSize OK!!!
            return getShipInfosByPage(pageNumber, pageSize,
                    getShipInfosByShipType(shipType,
                            getShipInfosByMinCrewSize(minCrewSize,
                                    getShipInfosByMaxCrewSize(maxCrewSize, lstShips))));
        }
        else if (isUsed!=null && minRating!=null && maxRating!=null) {
//            7) getAllWithFiltersIsUsedMinMaxRating OK!!!
            return getShipInfosByPage(pageNumber, pageSize,
                    getShipInfosByIsUsed(isUsed,
                            getShipInfosByMinRating(minRating,
                                    getShipInfosByMaxRating(maxRating, lstShips))));
        }
        else if (isUsed!=null && maxSpeed!=null && maxRating!=null) {
//            8) getAllWithFiltersIsUsedMaxSpeedMaxRating OK!!!
            return getShipInfosByPage(pageNumber, pageSize,
                    getShipInfosByIsUsed(isUsed,
                            getShipInfosByMaxSpeed(maxSpeed,
                                    getShipInfosByMaxRating(maxRating, lstShips))));
        }
        else if (name!=null && order!=null) {
//            9) getAllWithFiltersNameOrderSpeed OK!!!
            return getShipInfosByPage(pageNumber, pageSize,
                    getShipInfosByOrder(order,
                            getShipInfosByName(name, lstShips)));
        }
        else if (after!=null && before!=null && minCrewSize!=null && maxCrewSize!=null) {
//            10) getAllWithFiltersAfterBeforeMinCrewMaxCrew OK!!!
            return getShipInfosByPage(pageNumber, pageSize,
                    getShipInfosByAfter(after,
                            getShipInfosByBefore(before,
                                    getShipInfosByMinCrewSize(minCrewSize,
                                            getShipInfosByMaxCrewSize(maxCrewSize, lstShips)))));
        }
        else if (name!=null && pageNumber!=null) {
//            2) getAllWithFiltersNamePageNumber OK!!!
            return getShipInfosByPage(pageNumber, pageSize, getShipInfosByName(name, lstShips));
        }
        else {
            // 1) getAllWithoutFiltersReturnAllShips - OK!!!
            return getShipInfosByPage(pageNumber, pageSize, lstShips);
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
                if (currentShip.getName().contains(name) && currentShip.getProdDate().after(new Date(after)) && currentShip.getRating()<=maxRating)
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
                if ( (currentShip.getShipType().equals(shipType) && currentShip.getProdDate().getTime()<=before) && currentShip.getSpeed()<=maxSpeed ) {
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
    public ResponseEntity<Ship> createShip(@RequestBody Ship requestShip)
    {
        if (requestShip.getUsed()==null)
            requestShip.setUsed(false);

//        - указаны не все параметры из Data Params (кроме isUsed);
        if (requestShip.getName()==null || requestShip.getPlanet()==null || requestShip.getShipType()==null ||
            requestShip.getProdDate()==null || requestShip.getSpeed()==null || requestShip.getCrewSize()==null)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//        - длина значения параметра “name” или “planet” превышает размер соответствующего поля в БД (50 символов);
//        - значение параметра “name” или “planet” пустая строка;
//        скорость или размер команды находятся вне заданных пределов;
//        "prodDate”:[Long] < 0;
//        год производства находятся вне заданных пределов.
        if (requestShip.getName().length()>50 || requestShip.getPlanet().length()>50 ||
                requestShip.getName().equals("") || requestShip.getPlanet().equals("") ||
                requestShip.getSpeed()>0.99 ||
                requestShip.getSpeed()<0.01 || requestShip.getCrewSize()<1 || requestShip.getCrewSize()>9999 ||
                requestShip.getProdDate().getYear()+1900<0 || requestShip.getProdDate().getYear()+1900>3019 ||
                requestShip.getProdDate().getTime()<0)
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        Double calcRating = requestShip.calculateRating(requestShip.getSpeed(), requestShip.getUsed(),
                requestShip.getProdDate());
        Double calcRatingRounded = round(calcRating, 2);
        requestShip.setRating(calcRatingRounded);
        return new ResponseEntity<>(shipService.saveShip(requestShip), HttpStatus.OK);

    }

    @GetMapping("/ships/{id}")
    public ResponseEntity<Ship> getShip(@PathVariable("id") String id)
    {
        // Не валидным считается id, если он: 1) не числовой, 2) не целое число, 3) не положительный
        Integer idInt = null;
        try {
            idInt = Integer.parseInt(id);
            if (idInt>0) {
                // правильное id. тут ищем в БД.
                List<Ship> lstShips = shipService.findAll();
                Ship foundShip = null;
                for (Ship ship: lstShips) {
                    if (Integer.parseInt(String.valueOf(ship.getId()))==idInt) {
                        foundShip=ship;
                        break;
                    }
                }

                // проверка если мы нашли ship
                if (foundShip!=null)
                    return new ResponseEntity<>(foundShip, HttpStatus.OK);
                else
                    return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            else
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        catch (Exception exception) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }


    @PostMapping(value = "/ships/{id}")
    public ResponseEntity<Ship> updateShip(@PathVariable("id") String id, @RequestBody Ship requestShip)
    {

        Integer idInt = null;
        try {
            idInt = Integer.parseInt(id);
            if (idInt>0) {
                // правильное id. тут ищем в БД.
                Optional<Ship> foundShip = shipService.findId(idInt.longValue());

                if (foundShip.isPresent()) {

                    Ship foundShipInstance = foundShip.get();

                    // нашли id в базе. теперь проверяем можно ли что-то обновить.
                    if (requestShip.getName()==null && requestShip.getPlanet()==null && requestShip.getShipType()==null &&
                            requestShip.getProdDate()==null && requestShip.getSpeed()==null && requestShip.getCrewSize()==null)
                        return new ResponseEntity<>(shipService.saveShip(foundShipInstance), HttpStatus.OK);


                    if (requestShip.getName()!=null) {
                        if (requestShip.getName().length()>50 || requestShip.getName().equals(""))
                            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
                        else
                            foundShipInstance.setName(requestShip.getName());
                    }
                    if (requestShip.getPlanet()!=null) {
                        if (requestShip.getPlanet().length() > 50 || requestShip.getPlanet().equals(""))
                            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
                        else
                            foundShipInstance.setPlanet(requestShip.getPlanet());
                    }
                    if (requestShip.getShipType()!=null)
                        foundShipInstance.setShipType(requestShip.getShipType());
                    if (requestShip.getProdDate()!=null) {
                        if (requestShip.getProdDate().getTime()<0 || requestShip.getProdDate().getYear()+1900<2800 ||
                                requestShip.getProdDate().getYear()+1900>3019)
                            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
                        else
                            foundShipInstance.setProdDate(requestShip.getProdDate());
                    }
                    if (requestShip.getUsed()!=null)
                        foundShipInstance.setUsed(requestShip.getUsed());
                    if (requestShip.getSpeed()!=null) {
                        if (requestShip.getSpeed()<0.01 || requestShip.getSpeed()>0.99)
                            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
                        else
                            foundShipInstance.setSpeed(requestShip.getSpeed());
                    }
                    if (requestShip.getCrewSize()!=null) {
                        if (requestShip.getCrewSize()>9999 || requestShip.getCrewSize()<1)
                            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
                        else
                            foundShipInstance.setCrewSize(requestShip.getCrewSize());
                    }

                    Double calcRating = foundShipInstance.calculateRating(foundShipInstance.getSpeed(),
                            foundShipInstance.getUsed(), foundShipInstance.getProdDate());

                    Double calcRatingRounded = round(calcRating, 2);
                    foundShipInstance.setRating(calcRatingRounded);
                    return new ResponseEntity<>(shipService.saveShip(foundShipInstance), HttpStatus.OK);
                }
                else
                    return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            else
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        catch (Exception exception) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }


    @DeleteMapping("ships/{id}")
    public ResponseEntity<Ship> deleteShip(@PathVariable("id") String id)
    {
        // Не валидным считается id, если он: 1) не числовой, 2) не целое число, 3) не положительный
        Integer idInt = null;
        try {
            idInt = Integer.parseInt(id);
            if (idInt>0) {
                try {
                    shipService.deleteByID(idInt.longValue());
                    return new ResponseEntity<>(HttpStatus.OK);
                }
                catch (Exception exception) {
                    return new ResponseEntity<>(HttpStatus.NOT_FOUND);
                }
            }
            else
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        catch (Exception exception) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }


    public List<Ship> getShipInfosByPage(Integer pageNumber, Integer pageSize, List<Ship> ships) {
        int skip = pageNumber * pageSize;
        List<Ship> result = new ArrayList<>();
        for (int i = skip; i < Math.min(skip + pageSize, ships.size()); i++) {
            result.add(ships.get(i));
        }
        return result;
    }

    public List<Ship> getShipInfosByName(String name, List<Ship> ships) {
        List<Ship> result = new ArrayList<>();
        for (Ship ship : ships) {
            if (ship.getName().contains(name)) {
                result.add(ship);
            }
        }
        return result;
    }

    public List<Ship> getShipInfosByPlanet(String planet, List<Ship> ships) {
        List<Ship> result = new ArrayList<>();
        for (Ship ship : ships) {
            if (ship.getPlanet().contains(planet)) {
                result.add(ship);
            }
        }
        return result;
    }

    public List<Ship> getShipInfosByAfter(Long after, List<Ship> ships) {
        List<Ship> result = new ArrayList<>();
        for (Ship ship : ships) {
            if (ship.getProdDate().getTime() >= after) {
                result.add(ship);
            }
        }
        return result;
    }

    public List<Ship> getShipInfosByBefore(Long before, List<Ship> ships) {
        List<Ship> result = new ArrayList<>();
        for (Ship ship : ships) {
            if (ship.getProdDate().getTime() <= before) {
                result.add(ship);
            }
        }
        return result;
    }

    public List<Ship> getShipInfosByShipType(ShipType type, List<Ship> ships) {
        List<Ship> result = new ArrayList<>();
        for (Ship ship : ships) {
            if (ship.getShipType() == type) {
                result.add(ship);
            }
        }
        return result;
    }

    public List<Ship> getShipInfosByMinSpeed(Double minSpeed, List<Ship> ships) {
        List<Ship> result = new ArrayList<>();
        for (Ship ship : ships) {
            if (ship.getSpeed() >= minSpeed) {
                result.add(ship);
            }
        }
        return result;
    }

    public List<Ship> getShipInfosByMaxSpeed(Double maxSpeed, List<Ship> ships) {
        List<Ship> result = new ArrayList<>();
        for (Ship ship : ships) {
            if (ship.getSpeed() <= maxSpeed) {
                result.add(ship);
            }
        }
        return result;
    }


    public List<Ship> getShipInfosByMinCrewSize(Integer minCrewSize, List<Ship> ships) {
        List<Ship> result = new ArrayList<>();
        for (Ship ship : ships) {
            if (ship.getCrewSize() >= minCrewSize) {
                result.add(ship);
            }
        }
        return result;
    }

    public List<Ship> getShipInfosByMaxCrewSize(Integer maxCrewSize, List<Ship> ships) {
        List<Ship> result = new ArrayList<>();
        for (Ship ship : ships) {
            if (ship.getCrewSize() <= maxCrewSize) {
                result.add(ship);
            }
        }
        return result;
    }

    public List<Ship> getShipInfosByIsUsed(Boolean isUsed, List<Ship> ships) {
        List<Ship> result = new ArrayList<>();
        for (Ship ship : ships) {
            if (ship.getUsed() == isUsed) {
                result.add(ship);
            }
        }
        return result;
    }


    public List<Ship> getShipInfosByMinRating(Double minRating, List<Ship> ships) {
        List<Ship> result = new ArrayList<>();
        for (Ship ship : ships) {
            if (ship.getRating() >= minRating) {
                result.add(ship);
            }
        }
        return result;
    }

    public List<Ship> getShipInfosByMaxRating(Double maxRating, List<Ship> ships) {
        List<Ship> result = new ArrayList<>();
        for (Ship ship : ships) {
            if (ship.getRating() <= maxRating) {
                result.add(ship);
            }
        }
        return result;
    }

    public List<Ship> getShipInfosByOrder(ShipOrder order, List<Ship> ships) {

        if (order.equals(ShipOrder.ID)) {
            ships.sort((o1, o2) -> (int) (o1.getId() - o2.getId()));
        } else if (order.equals(ShipOrder.DATE)) {
            ships.sort((o1, o2) -> (int) (o1.getProdDate().getTime() - o2.getProdDate().getTime()));
        } else if (order.equals(ShipOrder.SPEED)) {
            ships.sort((o1, o2) -> {
                if (o1.getSpeed() > o2.getSpeed())
                    return 1;
                else if (o1.getSpeed().equals(o2.getSpeed()))
                    return 0;
                else
                    return -1;
            });
        } else if (order.equals(ShipOrder.RATING)) {
            ships.sort((o1, o2) -> {
                if (o1.getRating() > o2.getRating())
                    return 1;
                else if (o1.getRating().equals(o2.getRating()))
                    return 0;
                else
                    return -1;
            });
        }

        return ships;
    }


    private static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(Double.toString(value));
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }


}
