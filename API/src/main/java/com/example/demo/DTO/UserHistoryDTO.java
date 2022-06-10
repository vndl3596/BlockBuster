package com.example.demo.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserHistoryDTO {
    private int id;
    private Integer user;
    private Integer movie;
    private Date historyDate;

}
