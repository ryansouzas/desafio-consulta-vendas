package com.devsuperior.dsmeta.services;

import java.time.Instant;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

import com.devsuperior.dsmeta.dto.SellerSummaryDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.devsuperior.dsmeta.dto.SaleMinDTO;
import com.devsuperior.dsmeta.entities.Sale;
import com.devsuperior.dsmeta.repositories.SaleRepository;

@Service
public class SaleService {

	@Autowired
	private SaleRepository repository;
	
	public SaleMinDTO findById(Long id) {
		Optional<Sale> result = repository.findById(id);
		Sale entity = result.get();
		return new SaleMinDTO(entity);
	}

    public Page<SaleMinDTO> findSales(String minDate, String maxDate, String sellerName, Pageable pageable){

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

        if(sellerName == null){
            sellerName = "";
        }

        Page<Sale> page = repository.searchSales(initialDate, finalDate, sellerName, pageable);
        return page.map(SaleMinDTO::new);
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

        return repository.summaryBySeller(initialDate, finalDate);
    }
}
