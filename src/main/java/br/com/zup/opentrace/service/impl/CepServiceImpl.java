package br.com.zup.opentrace.service.impl;

import br.com.zup.opentrace.enums.ViaCepResponses;
import br.com.zup.opentrace.exception.InvalidCepException;
import br.com.zup.opentrace.service.CepService;
import io.micrometer.core.instrument.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class CepServiceImpl implements CepService {

    @Autowired
    private RestTemplate restTemplate;
    @Value("${viacep.address}")
    private String viacepAddress;

    private final String CEP_FORMAT = "^\\d{5}[-]\\d{3}$|\\d{8}";

    @Override
    public ResponseEntity<String> findByZipCode(String zipCode, String format) {
        String viacepFind  = viacepAddress;
        if (!zipCode.matches(CEP_FORMAT)) {
            throw new InvalidCepException("CEP Informado inválido! " + zipCode);
        }
        if (!StringUtils.isBlank(format)){
            viacepFind = new StringBuilder(viacepAddress)
                    .append(ViaCepResponses.fromString(format).getValue())
                    .append("/").toString();
        } else {
            viacepFind = new StringBuilder(viacepAddress)
                    .append(ViaCepResponses.JSON.getValue())
                    .append("/").toString();
        }

        return restTemplate.getForEntity(viacepFind, String.class, zipCode);
    }
}
