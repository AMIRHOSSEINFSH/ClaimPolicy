package org.riton.fetchdbconnector.connector;

import io.camunda.connector.api.annotation.OutboundConnector;
import io.camunda.connector.api.outbound.OutboundConnectorContext;
import io.camunda.connector.api.outbound.OutboundConnectorFunction;
import io.camunda.connector.generator.java.annotation.ElementTemplate;
import org.riton.fetchdbconnector.connector.dto.MyConnectorRequest;
import org.riton.fetchdbconnector.repository.ClaimRepository;
import org.riton.fetchdbconnector.repository.PolicyRepository;
import org.riton.fetchdbconnector.repository.VehicleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@OutboundConnector(
    name = "dbFetch",
    inputVariables = {"tableEntity", "id"},
    type = "dbFetch")
@ElementTemplate(
    id = "dbFetch.v1",
    name = "Template connector",
    version = 1,
    description = "Describe this connector",
        propertyGroups = {
                @ElementTemplate.PropertyGroup(id = "tableInfo", label = "table information")
        },
    inputDataClass = MyConnectorRequest.class)
public class MyConnectorFunction implements OutboundConnectorFunction {

  @Autowired
  private ClaimRepository claimRepository;

  @Autowired
  private PolicyRepository policyRepository;

  @Autowired
  private VehicleRepository vehicleRepository;

  private static final Logger LOGGER = LoggerFactory.getLogger(MyConnectorFunction.class);

  @Override
  public Object execute(OutboundConnectorContext context) {
    context.getJobContext().getProcessInstanceKey();
    final var connectorRequest = context.bindVariables(MyConnectorRequest.class);
    return executeConnector(connectorRequest);
  }

  private Object executeConnector(final MyConnectorRequest connectorRequest) {

    LOGGER.info("Executing my connector with request {}", connectorRequest);
    Optional result = null;
    switch (connectorRequest.tableEntity()) {
      case "ClaimEntity"-> {
        result=  claimRepository.findById(UUID.fromString(connectorRequest.id()));
      }
      case "PolicyEntity"-> {
        result = policyRepository.findById(UUID.fromString(connectorRequest.id()));
      }
      case "VehicleEntity"-> {
        result = vehicleRepository.findById(UUID.fromString(connectorRequest.id()));
      }
    }
    return result.get();

  }
}
