package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Time;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserHistoryDTO {
    private int id;
    private Integer user;
    private Integer movie;
    private Date historyDate;
    private Time movieTime;
}
