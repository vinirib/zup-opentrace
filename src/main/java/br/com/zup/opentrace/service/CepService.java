package br.com.zup.opentrace.service;

import org.springframework.http.ResponseEntity;

public interface CepService {

    ResponseEntity<String> findByZipCode(String zipCode, String format);
}
