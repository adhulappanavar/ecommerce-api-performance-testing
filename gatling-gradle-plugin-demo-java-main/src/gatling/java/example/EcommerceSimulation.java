package example;

import io.gatling.javaapi.core.*;
import io.gatling.javaapi.http.*;

import java.time.Duration;
import java.util.List;
import java.util.Map;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;

public class EcommerceSimulation extends Simulation {

  // HTTP Protocol configuration
  private static final HttpProtocolBuilder httpProtocol = http
      .baseUrl("http://localhost:5000")
      .acceptHeader("application/json")
      .contentTypeHeader("application/json")
      .userAgentHeader("Gatling Performance Test");

  // Customer data feeder
  private static final FeederBuilder<String> customerFeeder = csv("customers.csv").circular();
  
  // Product data feeder
  private static final FeederBuilder<String> productFeeder = csv("products.csv").circular();

  // Complete user journey scenario
  private static final ScenarioBuilder userJourneyScenario = scenario("Complete User Journey")
      .feed(customerFeeder)
      .feed(productFeeder)
      .exec(
          // Create customer
          http("Create Customer")
              .post("/api/customers")
              .body(StringBody("""
                  {
                    "name": "${name}",
                    "email": "${email}"
                  }
                  """))
              .check(status().is(201))
              .check(jsonPath("$.id").saveAs("customerId"))
      )
      .pause(1, 3)
      .exec(
          // Get product details - use a fixed product ID for now
          http("Get Product Details")
              .get("/api/products/1")
              .check(status().is(200))
              .check(jsonPath("$.id").saveAs("productId"))
      )
      .pause(1, 3)
      .exec(
          // Add to cart
          http("Add to Cart")
              .post("/api/cart")
              .body(StringBody(session -> {
                  String customerId = session.getString("customerId");
                  String quantity = session.getString("quantity");
                  return String.format("""
                      {
                        "customerId": "%s",
                        "productId": 1,
                        "quantity": %s
                      }
                      """, customerId, quantity);
              }))
              .check(status().is(200))
      )
      .pause(1, 3)
      .exec(
          // Get cart
          http("Get Cart")
              .get(session -> "/api/cart/" + session.getString("customerId"))
              .check(status().is(200))
      )
      .pause(2, 5)
      .exec(
          // Checkout
          http("Checkout")
              .post(session -> "/api/cart/" + session.getString("customerId") + "/checkout")
              .check(status().is(200))
      );

  // Simple performance test with very low load
  {
    setUp(
        userJourneyScenario.injectOpen(
            // Ramp up users from 0 to 5 in 10 seconds
            rampUsers(5).during(Duration.ofSeconds(10)),
            // Maintain steady state for 5 users for 30 seconds
            constantUsersPerSec(5).during(Duration.ofSeconds(30)),
            // Ramp down to 0 users in 10 seconds
            rampUsers(0).during(Duration.ofSeconds(10))
        )
    ).protocols(httpProtocol)
     .assertions(
         global().responseTime().max().lt(10000), // Max response time < 10 seconds
         global().responseTime().mean().lt(2000), // Mean response time < 2 seconds
         global().successfulRequests().percent().gt(50.0) // Success rate > 50%
     );
  }
}
