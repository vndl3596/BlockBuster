package com.example.demo.service.IMPL.address;

import com.example.demo.dto.address.DistrictDTO;
import com.example.demo.dto.address.TownDTO;
import com.example.demo.map.address.DistrictMap;
import com.example.demo.map.address.TownMap;
import com.example.demo.repository.address.DistrictRepository;
import com.example.demo.service.addressService.DistrictService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@AllArgsConstructor
public class DistrictServiceImpl implements DistrictService {
    private final DistrictRepository districtRepository;
    private final DistrictMap districtMap;
    private final TownMap townMap;

    @Override
    public List<TownDTO> getTownByDistrictId(int id) {
        return townMap.townDTOList(Objects.requireNonNull(districtRepository.findById(id).orElse(null)).getTowns());
    }

    @Override
    public DistrictDTO getById(int id) {
        return districtMap.districtToDTO(districtRepository.getById(id));
    }
}
