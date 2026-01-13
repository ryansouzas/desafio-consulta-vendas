package com.devsuperior.dsmeta.services;

import java.time.Instant;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.devsuperior.dsmeta.dto.SaleSellerDTO;
import com.devsuperior.dsmeta.dto.SellerSummaryDTO;
import com.devsuperior.dsmeta.dto.projections.SaleSellerProjection;
import com.devsuperior.dsmeta.dto.projections.SellerSummaryProjection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.devsuperior.dsmeta.dto.SaleMinDTO;
import com.devsuperior.dsmeta.entities.Sale;
import com.devsuperior.dsmeta.repositories.SaleRepository;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SaleService {

	@Autowired
	private SaleRepository repository;

    @Transactional(readOnly = true)
    public SaleMinDTO findById(Long id) {
		Optional<Sale> result = repository.findById(id);
		Sale entity = result.get();
		return new SaleMinDTO(entity);
	}

    @Transactional(readOnly = true)
    public Page<SaleSellerDTO> searchSales(String minDate, String maxDate, String sellerName, Pageable pageable){

        LocalDate finalDate;
        if(maxDate == null) {
            finalDate = LocalDate.ofInstant(Instant.now(), ZoneId.systemDefault());
        }
        else{
           finalDate = LocalDate.parse(maxDate);
        }

        LocalDate initialDate;
        if(minDate == null){
            initialDate = finalDate.minusYears(1L);
        }
        else{
            initialDate = LocalDate.parse(minDate);
        }

        sellerName = (sellerName == null) ? "" : sellerName;

        Page<SaleSellerProjection> page = repository.searchSales(initialDate, finalDate, sellerName, pageable);

        return page.map(p -> new SaleSellerDTO(p.getId(), p.getDate(), p.getAmount(), p.getSellerName()));
    }

    public List<SellerSummaryDTO> getSalesSummaryBySeller(String minDate, String maxDate){

        LocalDate finalDate;
        if(maxDate == null) {
            finalDate = LocalDate.ofInstant(Instant.now(), ZoneId.systemDefault());
        }
        else{
            finalDate = LocalDate.parse(maxDate);
        }

        LocalDate initialDate;
        if(minDate == null){
            initialDate = finalDate.minusYears(1L);
        }
        else{
            initialDate = LocalDate.parse(minDate);
        }

        List<SellerSummaryProjection> result = repository.summaryBySeller(initialDate, finalDate);
        return result.stream().map(r -> new SellerSummaryDTO(r.getSellerName(), r.getTotal())).collect(Collectors.toList());
    }
}
