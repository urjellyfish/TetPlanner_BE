package com.wecamp.TetPlanner_BE.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SummaryDTO {
    private long grandTotalBudget;
    private long grandTotalExpectedSpent;
    private long grandTotalActualSpent;
    private long grandTotalVariance;
}