package com.devsuperior.dsmeta.repositories;

import com.devsuperior.dsmeta.dto.SellerSummaryDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.devsuperior.dsmeta.entities.Sale;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface SaleRepository extends JpaRepository<Sale, Long> {

    @Query(" SELECT s FROM Sale s " +
            " WHERE s.date BETWEEN :minDate AND :maxDate " +
            " AND LOWER(s.seller.name) " +
            " LIKE LOWER(CONCAT('%', :sellerName, '%'))")
    Page<Sale> searchSales(
            LocalDate minDate,
            LocalDate maxDate,
            String sellerName,
            Pageable pageable);


    @Query("SELECT new com.devsuperior.dsmeta.dto.SellerSummaryDTO(s.seller.name, SUM(s.amount))" +
            " FROM Sale s" +
            " WHERE s.date BETWEEN :minDate AND :maxDate" +
            " GROUP BY s.seller.name")
    List<SellerSummaryDTO> summaryBySeller(LocalDate minDate, LocalDate maxDate);

}
