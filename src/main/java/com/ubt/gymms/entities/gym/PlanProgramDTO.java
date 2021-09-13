package com.ubt.gymms.entities.gym;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlanProgramDTO {

    private Long id;
    private String day;
    private String personFullName;
    private String categoryName;
    private Long personId;
    private Long categoryId;
    private boolean enabled;
}