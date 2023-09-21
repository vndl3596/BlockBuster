package com.example.blockbuster.dto;

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
    private int user;
    private int movie;
    private Date historyDate;
    private String movieTime;
}
