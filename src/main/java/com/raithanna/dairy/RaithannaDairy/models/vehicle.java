package com.raithanna.dairy.RaithannaDairy.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDate;

@Entity
@Table
@AllArgsConstructor
@NoArgsConstructor
@Data
public class vehicle {
    @Id
    @GeneratedValue
    private Integer id;
    private String vehicleNo;
    private String vehCode;
    private LocalDate vehStartDate;

    private LocalDate vehEndDate;

    private String suplAttached1;

    private String suplAttached2;
    private String suplAttached3;

    private String active = "";

    private String remove;
    private String chasisNumber;
    private String owner;
    private String capacity;
    private  String compartments;
    private String rateperKm;


}
