package com.example.demo.DTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class MovieEvaluateDTO {
    MovieDetailDTO movieDetailDTO;
    AccountDTO accountDTO;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy' 'HH:mm:ss")
    private Date evaluateTime;
    private String evaluateContent;
    private int evaluateRate;
}
