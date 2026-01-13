package com.devsuperior.dsmeta.repositories;

import com.devsuperior.dsmeta.dto.SellerSummaryDTO;
import com.devsuperior.dsmeta.dto.projections.SaleSellerProjection;
import com.devsuperior.dsmeta.dto.projections.SellerSummaryProjection;
import com.devsuperior.dsmeta.entities.Sale;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface SaleRepository extends JpaRepository<Sale, Long> {

    @Query(" SELECT" +
            " obj.id AS id," +
            " obj.date AS date," +
            " obj.amount AS amount," +
            " sel.name AS sellerName" +
            " FROM Sale obj " +
            " JOIN obj.seller sel" +
            " WHERE obj.date BETWEEN :minDate AND :maxDate " +
            " AND LOWER(sel.name) " +
            " LIKE LOWER(CONCAT('%', :sellerName, '%'))")
    Page<SaleSellerProjection> searchSales(
            LocalDate minDate,
            LocalDate maxDate,
            String sellerName,
            Pageable pageable);


    @Query("SELECT " +
            " sel.name AS sellerName," +
            " SUM(obj.amount) AS total" +
            " FROM Sale obj" +
            " JOIN obj.seller sel" +
            " WHERE obj.date BETWEEN :minDate AND :maxDate" +
            " GROUP BY sel.name")
    List<SellerSummaryProjection> summaryBySeller(@Param("minDate") LocalDate minDate, @Param("maxDate") LocalDate maxDate);
}
