package com.devsuperior.investimentos.services;

import com.devsuperior.investimentos.client.BrapiClient;
import com.devsuperior.investimentos.client.dto.BrapiResponseFindByIdDTO;
import com.devsuperior.investimentos.client.dto.BrapiResponseListDTO;
import com.devsuperior.investimentos.client.dto.StockBrapiFindByIdDTO;
import com.devsuperior.investimentos.services.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class BrapiService {

    @Value("#{environment.TOKEN}")
    private String TOKEN;

    @Autowired
    private BrapiClient brapiClient;

    public BrapiResponseListDTO getQuoteList(String search){
        return brapiClient.getQuoteList(TOKEN, search.toUpperCase());
    }

    public StockBrapiFindByIdDTO findById(String id){
        try {
            BrapiResponseFindByIdDTO response = brapiClient.getQuote(TOKEN, id);
            return response.getResults().getFirst();
        }catch (Exception e){
            throw new ResourceNotFoundException(e.getMessage());
        }
    }
}
