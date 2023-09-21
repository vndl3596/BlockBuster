package com.example.demo.dto;

import com.example.demo.model.Membership;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Time;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MovieDetailDTO {
    private int id;
    private String title;
    private String poster;
    private String detail;
    private Boolean movieStatus;
    private String linkTrailer;
    private String linkMovie;
    private Date releaseDate;
    private Time movieDuration;
    private MembershipDTO requireMember;
    private int viewNumber;
}
