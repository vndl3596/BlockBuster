package com.example.demo.model;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "voucher_detail")
public class Voucher {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "voucher_code")
    @NotBlank(message = "Voucher code cannot be empty")
    private String voucherCode;

    @Column(name = "voucher_name")
    @NotBlank(message = "Voucher name cannot be empty")
    private String voucherName;

    @Column(name = "time_from")
    @NotBlank(message = "Time from cannot be empty")
    private Date timeFrom;

    @Column(name = "time_to")
    @NotBlank(message = "Time to cannot be empty")
    private Date timeTo;

    @Column(name = "status")
    @NotNull(message = "Status cannot be empty")
    private Boolean status;

    @Column(name = "reduce")
    @NotNull(message = "Reduce cannot be empty")
    private int reduce;

    @Column(name = "type")
    @NotNull(message = "Type cannot be empty")
    private int type;

    @OneToMany(mappedBy = "voucher", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @EqualsAndHashCode.Exclude
    private List<VoucherForMembershipDetail> membershipDetails = new ArrayList<>();
}
