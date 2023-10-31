//package com.example.api.rest.controller;
//
//import com.example.api.rest.dto.CalculoFreteDTO;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//import org.springframework.web.client.HttpClientErrorException;
//import org.springframework.web.client.HttpServerErrorException;
//import org.springframework.web.client.RestClientException;
//import org.springframework.web.client.RestTemplate;
//
//@RestController
//@RequestMapping("/api/calculo-frete")
//public class CalculoFreteAPI {
//    @GetMapping("{cepOrigem}/{cepDestino}/{valorFrete}")
//    public CalculoFreteDTO calculoFrete(
//            @PathVariable("cepOrigem") String cepOrigem,
//            @PathVariable("cepDestino") String cepDestino,
//            @PathVariable("valorFrete") int valorFrete) {
//        RestTemplate restTemplate = new RestTemplate();
//        //try {
//            ResponseEntity<CalculoFreteDTO> resp =
//                    restTemplate.
//                            getForEntity(String.format("https://www.cepcerto.com/ws/json-frete-personalizado/%s/%s/%d/e7db6bd189a5fbbf053072b32964794e663ce243/", cepOrigem, cepDestino, valorFrete), CalculoFreteDTO.class);
//            return resp.getBody();
////        } catch (RestClientException e) {
////            throw new RuntimeException(e);
////        }
//    }
//}
//

package com.example.api.rest.controller;

import com.example.api.rest.dto.CalculoFreteDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/calculo-frete")
public class CalculoFreteAPI {

    private final WebClient webClient;

    public CalculoFreteAPI(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("https://www.cepcerto.com/ws/json-frete-personalizado").build();
    }

    @GetMapping("{cepOrigem}/{cepDestino}/{valorFrete}")
    public ResponseEntity<CalculoFreteDTO> calculoFrete(
            @PathVariable("cepOrigem") String cepOrigem,
            @PathVariable("cepDestino") String cepDestino,
            @PathVariable("valorFrete") int valorFrete) {
        //        RestTemplate restTemplate = new RestTemplate();
//        //try {
//            ResponseEntity<CalculoFreteDTO> resp =
//                    restTemplate.
//                            getForEntity(String.format("https://www.cepcerto.com/ws/json-frete-personalizado/%s/%s/%d/e7db6bd189a5fbbf053072b32964794e663ce243/", cepOrigem, cepDestino, valorFrete), CalculoFreteDTO.class);
//            return resp.getBody();

        try {
            CalculoFreteDTO calculoFreteDTO = webClient
                    .get()
                    .uri("/{cepOrigem}/{cepDestino}/{valorFrete}/e7db6bd189a5fbbf053072b32964794e663ce243/", cepOrigem, cepDestino, valorFrete)
                    .retrieve()
                    .bodyToMono(CalculoFreteDTO.class)
                    .block();

            //return ResponseEntity.ok(calculoFreteDTO);
            //return calculoFreteDTO.getBody();
            return ResponseEntity.ok(calculoFreteDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception e) {
        // Logar a exceção para fins de depuração
        e.printStackTrace();

        // Retornar uma mensagem de erro adequada ao cliente
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro interno do servidor");
    }
}
