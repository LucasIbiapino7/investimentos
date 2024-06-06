package com.devsuperior.investimentos.services;

import com.devsuperior.investimentos.client.dto.StockBrapiFindByIdDTO;
import com.devsuperior.investimentos.entities.Stock;
import com.devsuperior.investimentos.repositories.StockRepository;
import com.devsuperior.investimentos.services.exceptions.ResourceNotFoundException;
import com.devsuperior.investimentos.testes.Factory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
class StockServiceTest {

    @InjectMocks
    private StockService service;

    @Mock
    private StockRepository repository;

    @Mock
    private BrapiService brapiService;

    private String existingId;
    private String notExistingId;
    private String notExistingIdInDatabase;
    private String longName;
    private StockBrapiFindByIdDTO stockBrapiFindByIdDTO;
    private Stock stock;

    @BeforeEach
    void setUp() {

        existingId = "AMZO34";
        notExistingId = "AMZ";
        longName = "Amazon.inc";
        notExistingIdInDatabase = "Amazon";

        stockBrapiFindByIdDTO = Factory.createStockBrapiFindByIdDTO();

        stock = Factory.createStock();

        Mockito.when(repository.existsById(existingId)).thenReturn(true);
        Mockito.when(repository.existsById(notExistingId)).thenReturn(false);

        Mockito.when(brapiService.findById(existingId)).thenReturn(stockBrapiFindByIdDTO);
        Mockito.when(brapiService.findById(notExistingId)).thenThrow(ResourceNotFoundException.class);

        Mockito.when(repository.getReferenceById(existingId)).thenReturn(stock);
        Mockito.when(repository.save(ArgumentMatchers.any())).thenReturn(stock);

    }

    @Test
    public void containsStockShouldReturnTrueWhenIdExist(){

        boolean response = service.containsStock(existingId);

        Assertions.assertEquals(response, true);

    }

    @Test
    public void containsStockShouldReturnFalseWhenIdNotExist(){

        boolean response = service.containsStock(notExistingId);

        Assertions.assertEquals(response, false);

    }

    @Test
    public void findByIdShouldReturnStockBrapiFindByIdDTOWhenIdExist(){

        StockBrapiFindByIdDTO response = service.findById(existingId);

        Assertions.assertNotNull(response);
        Assertions.assertEquals(response.getSymbol(), stockBrapiFindByIdDTO.getSymbol());
        Assertions.assertEquals(response.getLongName(), stockBrapiFindByIdDTO.getLongName());
        Assertions.assertEquals(response.getRegularMarketPrice(), stockBrapiFindByIdDTO.getRegularMarketPrice());

    }

    @Test
    public void findByIdShouldThrowResourceNotFoundExceptionWhenIdNotExisting(){

        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            StockBrapiFindByIdDTO response = service.findById(notExistingId);
        } );

    }

    @Test
    public void getStockShouldReturnStockWhenIdExistInDatabase(){

        StockService serviceSpy = Mockito.spy(service);

        Mockito.when(serviceSpy.containsStock(existingId)).thenReturn(true);

        Stock response = serviceSpy.getStock(existingId, longName);

        Assertions.assertNotNull(response);
        Assertions.assertEquals(response.getName(), stock.getName());
        Assertions.assertEquals(response.getLongName(), stock.getLongName());

    }

    @Test
    public void getStockShouldPersistAndReturnStockWhenIdNotExistInDatabase(){

        StockService serviceSpy = Mockito.spy(service);

        Mockito.when(serviceSpy.containsStock(notExistingId)).thenReturn(false);

        Stock response = serviceSpy.getStock(notExistingIdInDatabase, longName);

        Assertions.assertNotNull(response);
        Assertions.assertEquals(response.getName(), stock.getName());
        Assertions.assertEquals(response.getLongName(), stock.getLongName());

    }

}