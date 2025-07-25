package com.abkatk.camunda_app_8.worker;

import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.spring.client.annotation.JobWorker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@Slf4j // For logging using Lombok
public class OrderValidationWorker {

    @JobWorker(type = "validate-order-task", autoComplete = true)
    public Map<String, Object> handleValidateOrderTask(final ActivatedJob job) {
        log.info("Processing job for type: {}. Key: {}", job.getType(), job.getKey());
        log.info("Order validation request received. Variables: {}", job.getVariables());

        String orderId = (String) job.getVariablesAsMap().get("orderId");
        Double amount = (Double) job.getVariablesAsMap().get("orderAmount");

        // --- Simulate Business Logic ---
        boolean isValid = true;
        String validationMessage = "Order is valid.";

        if (amount == null || amount <= 0) {
            isValid = false;
            validationMessage = "Order amount must be positive.";
        } else if (orderId == null || orderId.isEmpty()) {
            isValid = false;
            validationMessage = "Order ID is missing.";
        }

        log.info("Order ID: {}, Amount: {}. Validation Result: {}. Message: {}",
                orderId, amount, isValid, validationMessage);

        // Return variables to the process
        return Map.of(
                "isValidOrder", isValid,
                "validationMessage", validationMessage
        );
    }
}
