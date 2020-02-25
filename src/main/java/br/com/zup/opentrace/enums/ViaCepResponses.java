package br.com.zup.opentrace.enums;

import br.com.zup.opentrace.exception.InvalidOutputFormatException;

import java.util.Arrays;

public enum ViaCepResponses {

    XML("xml"),
    JSON("json"),
    PIPED("piped"),
    QUERTY("querty");

    private final String value;

    ViaCepResponses(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static ViaCepResponses fromString(String s) throws IllegalArgumentException {
        return Arrays.stream(ViaCepResponses.values())
                .filter(v -> v.value.equals(s))
                .findFirst()
                .orElseThrow(() -> new InvalidOutputFormatException("Unknown output format: " + s));
    }
}
