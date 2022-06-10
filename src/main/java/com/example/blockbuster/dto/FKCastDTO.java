package com.example.blockbuster.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class FKCastDTO {
    private int movieDetailId;
    private int movieCastId;
    private String castCharacter;
}
