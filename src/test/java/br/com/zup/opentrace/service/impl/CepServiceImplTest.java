package br.com.zup.opentrace.service.impl;

import br.com.zup.opentrace.exception.InvalidCepException;
import br.com.zup.opentrace.exception.InvalidOutputFormatException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CepServiceImplTest {

    @InjectMocks
    private CepServiceImpl cepService;

    @Mock
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
    public void setUp() {
        ReflectionTestUtils.setField(cepService, "viacepAddress", "http://viacep.com.br/ws/{zipCode}/");
    }

    @Test
    public void findByZipCode() {
        when(restTemplate.getForEntity(anyString(), any(), anyString()))
                .thenReturn(new ResponseEntity<>(ZUP_CODE_RESPONSE, HttpStatus.OK));
        final ResponseEntity<String> response = cepService.findByZipCode(VALID_ZIP_CODE, null);
        assertThat(response).isNotNull();
    }

    @Test(expected = InvalidCepException.class)
    public void shouldThrowInvalidCepExceptionOnFindByZipCode() {
        final ResponseEntity<String> response = cepService.findByZipCode("ABCDEF", null);
        assertThat(response).isNotNull();
    }

    @Test(expected = InvalidOutputFormatException.class)
    public void shouldThrowInvalidOutputFormatExceptionOnFindByZipCode() {
        final ResponseEntity<String> response = cepService.findByZipCode(VALID_ZIP_CODE, "INVALID_FORMAT");
        assertThat(response).isNotNull();
    }
}