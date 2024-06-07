package com.devsuperior.investimentos.services;

import com.devsuperior.investimentos.client.BrapiClient;
import com.devsuperior.investimentos.client.dto.BrapiResponseFindByIdDTO;
import com.devsuperior.investimentos.client.dto.BrapiResponseListDTO;
import com.devsuperior.investimentos.client.dto.StockBrapiFindByIdDTO;
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
class BrapiServiceTest {

    @InjectMocks
    private BrapiService service;

    @Mock
    private BrapiClient brapiClient;

    private String TOKEN;
    private String search;
    private String searchNotFound;
    private String idValid;
    private String idInvalid;
    private BrapiResponseListDTO brapiResponseListDTO;
    private BrapiResponseListDTO brapiResponseListDTOEmpty;
    private BrapiResponseFindByIdDTO brapiResponseFindByIdDTO;

    @BeforeEach
    void setUp() {

        search = "AMZO34";
        searchNotFound = "";
        idValid = "AMZO34";
        idInvalid = "";
        brapiResponseListDTO = Factory.createBrapiResponseListDTO();
        brapiResponseListDTOEmpty = Factory.createBrapiResponseListDTOEmpty();
        brapiResponseFindByIdDTO = Factory.createBrapiResponseFindByIdDTO();

        Mockito.when(brapiClient.getQuoteList(ArgumentMatchers.any(), ArgumentMatchers.eq(search))).thenReturn(brapiResponseListDTO);
        Mockito.when(brapiClient.getQuoteList(ArgumentMatchers.any(), ArgumentMatchers.eq(searchNotFound))).thenReturn(brapiResponseListDTOEmpty);

        Mockito.when(brapiClient.getQuote(ArgumentMatchers.any(), ArgumentMatchers.eq(idValid))).thenReturn(brapiResponseFindByIdDTO);
        Mockito.when(brapiClient.getQuote(ArgumentMatchers.any(), ArgumentMatchers.eq(idInvalid))).thenThrow(RuntimeException.class);

    }

    @Test
    public void getQuoteListShouldReturnBrapiResponseListDTOWithStocksFilledWhenSearchFound(){

        BrapiResponseListDTO response = service.getQuoteList(search);

        Assertions.assertNotNull(response);
        Assertions.assertEquals(response.getStocks().get(0).getStock(), brapiResponseListDTO.getStocks().get(0).getStock());
        Assertions.assertFalse(response.getStocks().isEmpty());
    }

    @Test
    public void getQuoteListShouldReturnBrapiResponseListDTOWithStocksEmptyWhenSearchNotFound(){

        BrapiResponseListDTO response = service.getQuoteList(searchNotFound);

        Assertions.assertNotNull(response);
        Assertions.assertTrue(response.getStocks().isEmpty());
    }

    @Test
    public void findByIdShouldReturnStockBrapiFindByIdDTOWhenIdFound(){

        StockBrapiFindByIdDTO response = service.findById(idValid);

        Assertions.assertNotNull(response);
        Assertions.assertEquals(response.getSymbol(), brapiResponseFindByIdDTO.getResults().get(0).getSymbol());

    }

    @Test
    public void findByIdShouldThrowExceptionWhenIdNotFound(){

        Assertions.assertThrows(Exception.class, () -> {
            StockBrapiFindByIdDTO response = service.findById(idInvalid);
        });

    }

}