package com.raithanna.dairy.RaithannaDairy.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table
@AllArgsConstructor
@NoArgsConstructor
@Data
public class supplier {
    @Id
@GeneratedValue
    private Integer id;
    private String supplierName;
   private String supplierCode;
   private String supAddress1;
    private String supAddress2;
    private String supAddress3;
   private String SupEmail;
   private String supMobile;
    private String pinCode;

}
