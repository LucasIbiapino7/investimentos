package com.devsuperior.investimentos.services;

import com.devsuperior.investimentos.client.BrapiClient;
import com.devsuperior.investimentos.client.dto.BrapiResponseListDTO;
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
}
