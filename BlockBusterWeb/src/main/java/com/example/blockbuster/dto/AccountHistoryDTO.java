package com.example.blockbuster.dto;

import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AccountHistoryDTO {
    private int id;
    private int idAcc;
    private int idMovie;
    private String date;
}
