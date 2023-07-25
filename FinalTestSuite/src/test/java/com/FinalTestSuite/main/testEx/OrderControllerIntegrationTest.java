package com.FinalTestSuite.main.testEx;

import com.FinalTestSuite.main.entity.OrderEntity;
import com.FinalTestSuite.main.entity.User;
import com.FinalTestSuite.main.repository.OrderRepository;
import com.FinalTestSuite.main.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class OrderControllerIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;
    
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void testCreateAndGetAllOrders() throws Exception {
        // Creating a test user
        User user = new User("testuser", "testuser@example.com");
        user = userRepository.save(user);

        // Prepare HTTP headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));

     // Create a new order request
        OrderEntity orderRequest = new OrderEntity("ORD123", 100.0);
        orderRequest.setUser(user);

        // Perform HTTP POST request to create the order
        ResponseEntity<OrderEntity> createResponse = restTemplate.exchange(
                "http://localhost:" + port + "/api/orders/" + user.getId(),
                HttpMethod.POST,
                new HttpEntity<>(orderRequest, headers),
                OrderEntity.class
        );


        // Validate the response after creating the order
        assertEquals(HttpStatus.OK, createResponse.getStatusCode());
        OrderEntity createdOrder = createResponse.getBody();
        assertNotNull(createdOrder);
        assertEquals(orderRequest.getOrderNumber(), createdOrder.getOrderNumber());
        assertEquals(orderRequest.getTotalPrice(), createdOrder.getTotalPrice(), 0.001); // 0.001 for delta due to double comparison

        // Perform HTTP GET request to retrieve all orders
        ResponseEntity<List<OrderEntity>> getAllResponse = restTemplate.exchange(
                "http://localhost:" + port + "/api/orders",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<OrderEntity>>() {}
        );

        // Validate the response for retrieving all orders
        assertEquals(HttpStatus.OK, getAllResponse.getStatusCode());
        List<OrderEntity> orders = getAllResponse.getBody();
        assertNotNull(orders);

        // Loop through the orders and get the associated users
        for (OrderEntity order : orders) {
            assertNotNull(order.getUser());
            System.out.println("Order ID: " + order.getId() + ", Order Number: " + order.getOrderNumber() +
                    ", Total Price: " + order.getTotalPrice() + ", User: " + order.getUser().getUsername());
        }
        assertThat(orders.size()>0);

        // Validate the retrieved order
        OrderEntity retrievedOrder = orders.get(0);
        assertEquals(createdOrder.getId(), retrievedOrder.getId());
        assertEquals(createdOrder.getOrderNumber(), retrievedOrder.getOrderNumber());
        assertEquals(createdOrder.getTotalPrice(), retrievedOrder.getTotalPrice(), 0.001); // 0.001 for delta due to double comparison
        assertEquals(user.getId(), retrievedOrder.getUser().getId());
        orderRepository.delete(retrievedOrder);
        userRepository.delete(user);
        
        
    }
   
}
