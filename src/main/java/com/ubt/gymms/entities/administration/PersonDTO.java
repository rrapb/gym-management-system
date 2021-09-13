package com.ubt.gymms.entities.administration;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PersonDTO {

    private Long id;
    private String firstName;
    private String lastName;
    private String birthDateString;
    private Date birthDate;
    private String gender;
    private String personalId;
    private boolean enabled;
}