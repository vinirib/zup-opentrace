package br.com.zup.opentrace.endpoint;


import br.com.zup.opentrace.enums.ViaCepResponses;
import br.com.zup.opentrace.service.CepService;
import io.micrometer.core.instrument.util.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import java.util.EnumSet;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(CepEndpoint.class)
public class CepEndpointTest {

    @Autowired
    private MockMvc mvc;

    @InjectMocks
    private CepEndpoint cepEndpoint;

    private static final String BASE_URL = "/address/";
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
    @MockBean
    private CepService cepService;

    @Test
    public void contextLoads() {
        assertThat(cepEndpoint).isNotNull();
        assertThat(mvc).isNotNull();
    }

    @Test
    public void findCepByZipCodeWithValidZip() throws Exception {
        when(cepService.findByZipCode(anyString(), anyString())).thenReturn(new ResponseEntity<>(ZUP_CODE_RESPONSE, HttpStatus.OK));
        when(cepService.findByZipCode(anyString(), isNull())).thenReturn(new ResponseEntity<>(ZUP_CODE_RESPONSE, HttpStatus.OK));
        MvcResult result = invokeFindCep(VALID_ZIP_CODE, null)
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        assertThat(result).isNotNull();
        final String response = result.getResponse().getContentAsString();
        assertThat(response).isEqualTo(ZUP_CODE_RESPONSE);
    }

    @Test
    public void findCepByZipCodeWithValidZipAndValidFormat() throws Exception {
        when(cepService.findByZipCode(anyString(), anyString())).thenReturn(new ResponseEntity<>(ZUP_CODE_RESPONSE, HttpStatus.OK));
        when(cepService.findByZipCode(anyString(), isNull())).thenReturn(new ResponseEntity<>(ZUP_CODE_RESPONSE, HttpStatus.OK));
        for (ViaCepResponses viaCepResponses : EnumSet.allOf(ViaCepResponses.class)) {
            MvcResult result = invokeFindCep("38400386", viaCepResponses.getValue())
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andReturn();
            assertThat(result).isNotNull();
        }
    }

    private ResultActions invokeFindCep(String zipCode, String outputFormat) throws Exception {
        String address = "";
        if (!StringUtils.isBlank(outputFormat)) {
            address = new StringBuilder(BASE_URL)
                    .append(zipCode)
                    .append("/")
                    .append(outputFormat)
                    .append("/")
                    .toString();
        } else {
            address = new StringBuilder(BASE_URL).append(zipCode).toString();
        }

        return mvc.perform(post(address).accept(MediaType.APPLICATION_JSON));
    }
}