package com.devsuperior.dsmeta.dto.projections;

import java.time.LocalDate;

public interface SaleSellerProjection {

    Long getId();
    LocalDate getDate();
    Double getAmount();
    String getSellerName();

}
