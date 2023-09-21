package com.example.demo.service.addressService;


import com.example.demo.dto.address.CityDTO;
import com.example.demo.dto.address.DistrictDTO;

import java.util.List;

public interface CityService {

    List<CityDTO> getAll();
    List<DistrictDTO> getDistrictsByCity(Integer id);
}
