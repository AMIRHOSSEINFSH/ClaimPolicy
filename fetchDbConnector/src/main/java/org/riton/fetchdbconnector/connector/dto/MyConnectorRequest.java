package org.riton.fetchdbconnector.connector.dto;

import io.camunda.connector.generator.java.annotation.TemplateProperty;
import io.camunda.connector.generator.java.annotation.TemplateProperty.PropertyType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record MyConnectorRequest(
    @NotEmpty @TemplateProperty(group = "tableInfo", type = PropertyType.String) String tableEntity,
    @NotEmpty @TemplateProperty(group = "tableInfo", type = PropertyType.String) String id) {}
