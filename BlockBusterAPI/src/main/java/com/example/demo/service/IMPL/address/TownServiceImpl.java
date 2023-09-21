package com.example.demo.service.IMPL.address;

import com.example.demo.dto.address.TownDTO;
import com.example.demo.map.address.TownMap;
import com.example.demo.repository.address.TownRepository;
import com.example.demo.service.addressService.TownService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class TownServiceImpl implements TownService {
    private final TownMap townMap;
    private final TownRepository townRepository;
    @Override
    public TownDTO getById(int id) {
        return townMap.townToDTO(townRepository.getById(id));
    }
}
