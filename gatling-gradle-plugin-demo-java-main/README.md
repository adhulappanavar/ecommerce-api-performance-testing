Gatling Performance Testing - Ecommerce API
===========================================

A comprehensive performance testing setup using Gatling to test an ecommerce API. This project demonstrates load testing with realistic ecommerce scenarios including customer creation, product search, cart operations, and checkout flows.

## Project Structure

```
├── ecommerce-api/           # Flask API server
│   ├── app.py              # Main API application
│   ├── requirements.txt    # Python dependencies
│   └── README.md          # API documentation
└── gatling-gradle-plugin-demo-java-main/  # Gatling test suite
    ├── src/gatling/java/example/
    │   └── EcommerceSimulation.java  # Main test simulation
    ├── src/gatling/resources/
    │   ├── customers.csv   # Test customer data
    │   └── products.csv    # Test product data
    └── build.gradle       # Gradle build configuration
```

## Prerequisites

- **Java 8+** with `$JAVA_HOME` configured
- **Python 3.7+** (for the API server)
- **Gradle** (included via wrapper)

## Quick Start

### 1. Start the Ecommerce API

```bash
cd ecommerce-api
pip install -r requirements.txt
python app.py
```

The API will start on `http://localhost:5000` with the following endpoints:
- `POST /api/customers` - Create customer
- `GET /api/products` - List products  
- `GET /api/search?q=query` - Search products
- `POST /api/cart` - Add to cart
- `GET /api/cart/<customer_id>` - Get cart
- `POST /api/cart/<customer_id>/checkout` - Checkout

### 2. Run Performance Tests

```bash
cd gatling-gradle-plugin-demo-java-main
./gradlew gatlingRun
```

When prompted, select option `1` for `EcommerceSimulation`.

## Test Scenarios

The `EcommerceSimulation` tests the following user journey:

1. **Customer Creation** - Creates a new customer account
2. **Product Search** - Searches for products by name
3. **Product Details** - Views product details
4. **Add to Cart** - Adds products to shopping cart
5. **View Cart** - Retrieves cart contents
6. **Checkout** - Completes the purchase

## Test Configuration

### Load Profile
- **Users**: 155 concurrent users
- **Duration**: ~51 seconds
- **Ramp-up**: Gradual user increase
- **Pause**: 1-5 seconds between operations

### Test Data
- **Customers**: 155 unique test customers
- **Products**: 10 different products to search
- **Search Terms**: iPhone, Samsung, MacBook, Dell, Sony, AirPods, Nike, Adidas, T-Shirt, Levi

## Running Tests

### Basic Execution
```bash
./gradlew gatlingRun
```

### Run Specific Simulation
```bash
./gradlew gatlingRun -Dgatling.simulationClass=example.EcommerceSimulation
```

### Run with Custom Parameters
```bash
./gradlew gatlingRun -Dgatling.users=100 -Dgatling.duration=60
```

## Viewing Results

After test completion, results are available in:
```
build/reports/gatling/[simulation-name]-[timestamp]/index.html
```

Open the HTML report in your browser to view:
- Response time statistics
- Request success rates
- Throughput metrics
- Error analysis
- Detailed request logs

## Expected Results

With the current configuration, you should see:
- **Success Rate**: 100% (all operations working)
- **Response Times**: Mean ~67ms, Max ~306ms
- **Throughput**: ~15 requests/second
- **All Operations**: Customer creation, product search, cart operations, and checkout

## Troubleshooting

### Common Issues

1. **API Not Running**: Ensure the Flask API is running on port 5000
2. **Port Conflicts**: Check if port 5000 is available
3. **Java Issues**: Verify `$JAVA_HOME` is set correctly
4. **Permission Issues**: Make gradlew executable: `chmod +x gradlew`

### Debug Mode

To see detailed request logs, check the API server console output for HTTP request logs.

## Customization

### Modify Test Data
Edit the CSV files in `src/gatling/resources/`:
- `customers.csv` - Customer test data
- `products.csv` - Product search terms and quantities

### Adjust Load Profile
Modify `EcommerceSimulation.java`:
- Change user count in `setUp()`
- Adjust pause durations
- Modify test scenarios

### API Configuration
The test targets `http://localhost:5000` by default. To change the target:
1. Update the base URL in the simulation
2. Or set the `gatling.http.baseUrl` system property

## References

- [Gatling Documentation](https://docs.gatling.io/)
- [Gradle Plugin Documentation](https://docs.gatling.io/reference/integrations/build-tools/gradle-plugin/)
- [Flask API Documentation](https://flask.palletsprojects.com/)
