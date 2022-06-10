package com.example.demo.map.address;

import com.example.demo.DTO.address.CityDTO;
import com.example.demo.model.address.City;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CityMap {
    public CityDTO cityToDTO(City city) {
        return new CityDTO(city.getId(), city.getName(), city.getGenre());
    }

    public List<CityDTO> cityDTOList(List<City> cities) {
        List<CityDTO> cityDTOS = new ArrayList<>();
        for (City city : cities) {
            cityDTOS.add(cityToDTO(city));
        }
        return cityDTOS;
    }
}
