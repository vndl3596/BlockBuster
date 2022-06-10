package com.example.demo.service.IMPL.address;

import com.example.demo.DTO.address.CityDTO;
import com.example.demo.DTO.address.DistrictDTO;
import com.example.demo.map.address.CityMap;
import com.example.demo.map.address.DistrictMap;
import com.example.demo.model.address.City;
import com.example.demo.repository.address.CityRepository;
import com.example.demo.service.addressService.CityService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class CityServiceImpl implements CityService {
    private final CityRepository cityRepository;
    private final CityMap cityMap;
    private final DistrictMap districtMap;

    @Override
    public List<CityDTO> getAll() {
        return cityMap.cityDTOList(cityRepository.findAll());
    }

    @Override
    public List<DistrictDTO> getDistrictsByCity(Integer id) {
        City city = cityRepository.findById(id).orElse(null);
        assert city != null;
        return districtMap.districtDTOList(city.getDistricts());
    }
}
