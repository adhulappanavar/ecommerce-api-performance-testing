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
                constantUsersPerSec(0.5).during(10),  // 0.5 users/sec for 10 seconds
                constantUsersPerSec(1.0).during(10),  // 0.5 users/sec for 10 seconds
                constantUsersPerSec(2.0).during(10), // 2 users/sec for 10 seconds
                constantUsersPerSec(3.0).during(10), // 3 users/sec for 10 seconds
                constantUsersPerSec(4.0).during(10), // 4 users/sec for 10 seconds
                constantUsersPerSec(5.0).during(10), // 5 users/sec for 10 seconds
                constantUsersPerSec(6.0).during(10), // 6 users/sec for 10 seconds
                constantUsersPerSec(7.0).during(10), // 7 users/sec for 10 seconds
                constantUsersPerSec(8.0).during(10), // 8 users/sec for 10 seconds
                constantUsersPerSec(9.0).during(10), // 9 users/sec for 10 seconds
                


                // rampUsers(10).during(1),
                
                // Phase 2: Steady state at 10 users for 40 seconds
                // Calculate rate: 10 users / (response_time + pause) = 10 / (0.032 + 2) = ~4.9 users/sec
                constantUsersPerSec(10.0).during(40), // 5 users/sec to maintain ~10 concurrent users
                
                constantUsersPerSec(30.0).during(5), // 5 users/sec to maintain ~10 concurrent users
                // Phase 3: Peak load - ramp from 10 to 20 users in 10 seconds
                // rampUsers(10).during(10), // Add 10 more users (10->20)
                
                // Phase 4: Hold at 20 users for 10 seconds
                constantUsersPerSec(10.0).during(10), // 10 users/sec to maintain ~20 concurrent users
                
                // Phase 5: Steady state at 10 users for 25 seconds
                constantUsersPerSec(5.0).during(25), // 5 users/sec to maintain ~10 concurrent users
                
                // Phase 6: Gradual ramp down from 10 to 0 users over 50 seconds
                // Simulate ramp down by reducing users per second gradually
                constantUsersPerSec(4.0).during(10), // 4 users/sec for 10 seconds
                constantUsersPerSec(3.0).during(10), // 3 users/sec for 10 seconds
                constantUsersPerSec(2.0).during(10), // 2 users/sec for 10 seconds
                constantUsersPerSec(1.0).during(10), // 1 user/sec for 10 seconds
                constantUsersPerSec(0.5).during(10)  // 0.5 users/sec for 10 seconds
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
