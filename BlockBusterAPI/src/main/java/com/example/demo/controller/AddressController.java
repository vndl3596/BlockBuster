package com.example.demo.controller;

import com.example.demo.dto.address.CityDTO;
import com.example.demo.dto.address.DistrictDTO;
import com.example.demo.dto.address.TownDTO;
import com.example.demo.map.address.CityMap;
import com.example.demo.map.address.DistrictMap;
import com.example.demo.map.address.TownMap;
import com.example.demo.repository.address.CityRepository;
import com.example.demo.repository.address.DistrictRepository;
import com.example.demo.repository.address.TownRepository;
import com.example.demo.service.addressService.CityService;
import com.example.demo.service.addressService.DistrictService;
import com.example.demo.service.addressService.TownService;
import com.nimbusds.jose.shaded.json.JSONObject;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@CrossOrigin("*")
@AllArgsConstructor
@RequestMapping("/api/address")
public class AddressController {
    private final CityService cityService;
    private final CityMap cityMap;
    private final CityRepository cityRepository;
    private final DistrictService districtService;
    private final DistrictMap districtMap;
    private final DistrictRepository districtRepository;
    private final TownService townService;
    private final TownMap townMap;
    private final TownRepository townRepository;

    @GetMapping("/get-all-city")
    public ResponseEntity<List<CityDTO>> getAllCity() {
        return new ResponseEntity<>(cityService.getAll(), HttpStatus.OK);
    }

    @GetMapping("/get-district/{cityId}")
    public ResponseEntity<List<DistrictDTO>> getDistrictByCityId(@PathVariable("cityId") int id) {
        return new ResponseEntity<>(cityService.getDistrictsByCity(id), HttpStatus.OK);
    }

    @GetMapping("/get-town/{districtId}")
    public ResponseEntity<List<TownDTO>> getTownByDistrictId(@PathVariable("districtId") int id) {
        return new ResponseEntity<>(districtService.getTownByDistrictId(id), HttpStatus.OK);
    }
    @GetMapping("/getTownById/{id}")
    private ResponseEntity<TownDTO> getTownById(@PathVariable("id") int id) {
        return new ResponseEntity<>(this.townService.getById(id), HttpStatus.OK);
    }
    @GetMapping("/getDistrictById/{id}")
    private ResponseEntity<DistrictDTO> getDistrictById(@PathVariable("id") int id) {
        return new ResponseEntity<>(this.districtService.getById(id), HttpStatus.OK);
    }

    @GetMapping("/getAddressByTownId/{id}")
    public ResponseEntity<JSONObject> getAddresNyTownId(@PathVariable("id") int id) {
        JSONObject address = new JSONObject();
        TownDTO townDTO = townMap.townToDTO(townRepository.findById(id).orElse(null));
        List<TownDTO> townDTOs = districtService.getTownByDistrictId(townDTO.getDistrictId());
        DistrictDTO districtDTO = districtMap.districtToDTO(districtRepository.findById(townDTO.getDistrictId()).orElse(null));
        List<DistrictDTO> districtDTOS = cityService.getDistrictsByCity(districtDTO.getCityId());
        CityDTO cityDTO = cityMap.cityToDTO(Objects.requireNonNull(cityRepository.findById(districtDTO.getCityId()).orElse(null)));
        address.put("townDTO", townDTO);
        address.put("townDTOs", townDTOs);
        address.put("districtDTO", districtDTO);
        address.put("districtDTOS", districtDTOS);
        address.put("cityDTO", cityDTO);
        return new ResponseEntity<>(address, HttpStatus.OK);
    }
}
