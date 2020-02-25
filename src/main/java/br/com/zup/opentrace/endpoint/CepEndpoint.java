package br.com.zup.opentrace.endpoint;

import br.com.zup.opentrace.service.CepService;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;

@RestController
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@RequestMapping("/address")
public class CepEndpoint {

    private final CepService cepService;

    @PostMapping({"/{zipCode}", "/{zipCode}/{format}"})
    public ResponseEntity<String> findCepByZipCode(@NotNull @PathVariable("zipCode") String zipCode,
                                                   @PathVariable(name = "format", required = false) String format) {
        final ResponseEntity<String> result = cepService.findByZipCode(zipCode, format);
        return result;
    }
}
