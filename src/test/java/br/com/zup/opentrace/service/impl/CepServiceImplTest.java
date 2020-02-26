package br.com.zup.opentrace.service.impl;

import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import java.util.Properties;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

//@RunWith(SpringRunner.class)
//@SpringBootTest
public class CepServiceImplTest {

    @InjectMocks
    private CepServiceImpl cepService;

    @MockBean
    private RestTemplate restTemplate;

    public static final String VALID_ZIP_CODE = "38400386";
    private static final String ZUP_CODE_RESPONSE = "{\n" +
            "  \"cep\": \"38400-386\",\n" +
            "  \"logradouro\": \"Rua Padre Pio\",\n" +
            "  \"complemento\": \"\",\n" +
            "  \"bairro\": \"Martins\",\n" +
            "  \"localidade\": \"Uberlandia\",\n" +
            "  \"uf\": \"MG\",\n" +
            "  \"unidade\": \"\",\n" +
            "  \"ibge\": \"3170206\",\n" +
            "  \"gia\": \"\"\n" +
            "}";

    @Before
    public void setUpStatic() {
        Properties props = System.getProperties();
        props.setProperty("viacep.address", "http://viacep.com.br/ws/{zipCode}/");
    }


    public void findByZipCode() {
        when(restTemplate.getForEntity(anyString(), any(), anyString()))
                .thenReturn(new ResponseEntity<>(ZUP_CODE_RESPONSE, HttpStatus.OK));
        final ResponseEntity<String> response = cepService.findByZipCode(VALID_ZIP_CODE, null);
        assertThat(response).isNotNull();
    }
}