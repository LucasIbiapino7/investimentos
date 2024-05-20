package com.devsuperior.investimentos.services;

import com.devsuperior.investimentos.client.dto.StockBrapiFindByIdDTO;
import com.devsuperior.investimentos.entities.Stock;
import com.devsuperior.investimentos.repositories.StockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class StockService {

    @Autowired
    private StockRepository repository;

    @Autowired
    private BrapiService brapiService;

    @Transactional(readOnly = true)
    public Boolean containsStock(String stocksId){
        return repository.existsById(stocksId);
    }

    @Transactional
    public Stock getStock(String stocksId, String longName){
        if (containsStock(stocksId)){
            return repository.getReferenceById(stocksId);
        }
        Stock stock = new Stock(stocksId, longName);
        stock = repository.save(stock);
        return stock;
    }

    public StockBrapiFindByIdDTO findById(String stockId){
        return brapiService.findById(stockId);
    }

}
