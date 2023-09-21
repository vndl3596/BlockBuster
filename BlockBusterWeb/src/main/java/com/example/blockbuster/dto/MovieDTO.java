package com.example.blockbuster.dto;

import lombok.*;

import java.time.LocalDate;
import java.util.Date;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MovieDTO {
    private int id;
    private String title;
    private String poster;
    private String detail;
    private Boolean movieStatus;
    private String linkTrailer;
    private String linkMovie;
    private Date releaseDate;
    private MembershipDTO requireMember;
    private String movieDuration;
    private int viewNumber;
}
