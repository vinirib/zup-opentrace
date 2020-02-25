package br.com.zup.opentrace.endpoint;


import br.com.zup.opentrace.enums.ViaCepResponses;
import br.com.zup.opentrace.exception.InvalidCepException;
import br.com.zup.opentrace.exception.InvalidOutputFormatException;
import br.com.zup.opentrace.service.CepService;
import io.micrometer.core.instrument.util.StringUtils;
import org.junit.Rule;
import org.junit.jupiter.api.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
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

@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
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

    @Rule
    public ExpectedException exception = ExpectedException.none();

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
        for (ViaCepResponses viaCepResponses : EnumSet.allOf(ViaCepResponses.class)){
            MvcResult result = invokeFindCep("38400386", viaCepResponses.getValue())
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andReturn();
            assertThat(result).isNotNull();
        }
    }

    @Test
    public void findCepByZipCodeWithInValidZip() throws Exception {
        when(cepService.findByZipCode(anyString(), anyString())).thenThrow(InvalidCepException.class);
        when(cepService.findByZipCode(anyString(), isNull())).thenThrow(InvalidCepException.class);
        exception.expect(InvalidCepException.class);
        exception.expectMessage("CEP Informado inválido! ABCDEFGH");
        MvcResult result = invokeFindCep("ABCDEFGH", "json")
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andReturn();
        assertThat(result).isNotNull();
    }

    @Test
    public void findCepByZipCodeWithValidZipButInvalidFormat() throws Exception {
        when(cepService.findByZipCode(anyString(), anyString())).thenThrow(InvalidOutputFormatException.class);
        when(cepService.findByZipCode(anyString(), isNull())).thenThrow(InvalidOutputFormatException.class);
        exception.expect(InvalidOutputFormatException.class);
        exception.expectMessage("Unknown output format: INVALIDFORMAT");
        MvcResult result = invokeFindCep(VALID_ZIP_CODE, "INVALIDFORMAT")
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andReturn();
        assertThat(result).isNotNull();
    }

    private ResultActions invokeFindCep(String zipCode, String outputFormat) throws Exception {
        String address = "";
        if (!StringUtils.isBlank(outputFormat)){
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