package example;

import io.gatling.javaapi.core.*;
import io.gatling.javaapi.http.*;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;

public class ProductDetailsLoadTest extends Simulation {

  private static final HttpProtocolBuilder httpProtocol = http
      .baseUrl("http://localhost:5000")
      .acceptHeader("application/json")
      .contentTypeHeader("application/json");

  private static final ScenarioBuilder productDetailsScenario = scenario("Product Details Load Test")
      .exec(
          http("Get Product Details")
              .get("/api/products/1")
              .check(status().is(200))
              .check(jsonPath("$.id").is("1"))
              .check(jsonPath("$.name").exists())
              .check(jsonPath("$.price").exists())
              .check(jsonPath("$.category").exists())
      )
      .pause(1, 3); // Realistic pause between requests

  {
    setUp(
        productDetailsScenario
            .injectOpen(
                // Phase 1: Ramp up from 0 to 10 users in 30 seconds
                rampUsers(10).during(30),
                
                // Phase 2: Steady state at 10 users for 40 seconds
                nothingFor(40),
                
                // Phase 3: Peak load - ramp from 10 to 20 users in 10 seconds
                rampUsers(10).during(10), // Add 10 more users (10->20)
                
                // Phase 4: Hold at 20 users for 10 seconds
                nothingFor(10),
                
                // Phase 5: Steady state at 10 users for 25 seconds
                // Note: We can't directly ramp down, so we'll use constantUsersPerSec
                constantUsersPerSec(10).during(25),
                
                // Phase 6: Gradual ramp down from 10 to 0 users over 50 seconds
                // Simulate ramp down by reducing users per second gradually
                constantUsersPerSec(8).during(10),  // 8 users for 10 seconds
                constantUsersPerSec(6).during(10),  // 6 users for 10 seconds
                constantUsersPerSec(4).during(10),  // 4 users for 10 seconds
                constantUsersPerSec(2).during(10),  // 2 users for 10 seconds
                constantUsersPerSec(1).during(10)   // 1 user for 10 seconds
            )
    )
    .protocols(httpProtocol)
    .assertions(
        global().responseTime().max().lt(1000), // Max response time < 1s
        global().responseTime().mean().lt(200), // Mean response time < 200ms
        global().successfulRequests().percent().gt(99.0) // Success rate > 99%
    );
  }
}
