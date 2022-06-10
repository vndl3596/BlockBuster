package com.example.blockbuster.dto.address;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class DistrictDTO {
    private int id;
    private String name;
    private String genre;
    private int cityId;
}
