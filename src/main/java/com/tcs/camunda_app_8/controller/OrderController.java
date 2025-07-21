package com.tcs.camunda_app_8.controller;


import io.camunda.zeebe.client.ZeebeClient;
import io.camunda.zeebe.client.api.response.ProcessInstanceEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor // For injecting ZeebeClient via constructor, uses Lombok
@Slf4j
public class OrderController {

	@Autowired
    private ZeebeClient zeebeClient;

    @PostMapping("/start-process")
    public String startOrderProcess(@RequestBody Map<String, Object> orderDetails) {
        String orderId = (String) orderDetails.getOrDefault("orderId", UUID.randomUUID().toString());
        orderDetails.put("orderId", orderId); // Ensure orderId is present

        log.info("Received request to start order process for orderId: {}", orderId);

        try {
            ProcessInstanceEvent processInstance = zeebeClient.newCreateInstanceCommand()
                    .bpmnProcessId("order-processing-flow") // The ID from your BPMN file
                    .latestVersion()
                    .variables(orderDetails)
                    .send()
                    .join(); // Blocks until the command is sent and acknowledged

            log.info("Started process instance for orderId: {} with process instance key: {}",
                    orderId, processInstance.getProcessInstanceKey());
            return "Order process started successfully for Order ID: " + orderId +
                   " (Process Instance Key: " + processInstance.getProcessInstanceKey() + ")";
        } catch (Exception e) {
            log.error("Failed to start order process for orderId: {}. Error: {}", orderId, e.getMessage(), e);
            return "Failed to start order process: " + e.getMessage();
        }
    }
}
