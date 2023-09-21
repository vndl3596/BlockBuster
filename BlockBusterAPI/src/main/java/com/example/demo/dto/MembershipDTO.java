package com.example.demo.dto;

import com.example.demo.model.address.Town;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import java.io.Serializable;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MembershipDTO {
    private Integer id;
    private String name;
    private String detail;
    private List<MembershipDetailDTO> membershipDetails;
}
