package com.example.demo.DTO.address;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TownDTO {
    private int id;
    private String name;
    private String genre;
    private int districtId;
}
