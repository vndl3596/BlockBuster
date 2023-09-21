package com.example.blockbuster.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MovieEvaluateDTO {
    MovieDTO movieId;
    AccountDTO accId;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy' 'HH:mm:ss")
    private Date evaluateTime;
    private String evaluateContent;
    private int evaluateRate;
}
