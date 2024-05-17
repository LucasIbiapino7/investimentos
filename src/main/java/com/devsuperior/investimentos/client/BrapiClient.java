package com.devsuperior.investimentos.client;

import com.devsuperior.investimentos.client.dto.BrapiResponseListDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(
        name = "BrapiClient",
        url = "https://brapi.dev"
)
public interface BrapiClient {

    @GetMapping(value = "/api/quote/list")
    BrapiResponseListDTO getQuoteList(
            @RequestParam(name = "token") String token,
            @RequestParam(name = "search") String search
    );

}
