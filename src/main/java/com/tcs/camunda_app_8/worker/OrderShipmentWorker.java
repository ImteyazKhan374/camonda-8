
package com.tcs.camunda_app_8.worker;

import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.spring.client.annotation.JobWorker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@Slf4j
public class OrderShipmentWorker {

    @JobWorker(type = "ship-order-task", autoComplete = true)
    public Map<String, Object> handleShipOrderTask(final ActivatedJob job) {
        log.info("Processing job for type: {}. Key: {}", job.getType(), job.getKey());
        log.info("Order shipment request received. Variables: {}", job.getVariables());

        String orderId = (String) job.getVariablesAsMap().get("orderId");
        Boolean isValidOrder = (Boolean) job.getVariablesAsMap().get("isValidOrder");

        if (isValidOrder != null && isValidOrder) {
            // --- Simulate Business Logic ---
            log.info("Shipping order: {}...", orderId);
            // Imagine calling an external shipping service here
            log.info("Order {} shipped successfully.", orderId);
            return Map.of("shipmentStatus", "Shipped");
        } else {
            log.warn("Order {} was not valid, not shipping.", orderId);
            return Map.of("shipmentStatus", "Not Shipped - Invalid Order");
        }
    }
}
