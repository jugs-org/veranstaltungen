package de.ms.springai.dto;

import com.fasterxml.jackson.annotation.JsonPropertyDescription;

public record CapitalResponse(@JsonPropertyDescription("Das ist der Name der Stadt") String answer) {

}
