# Ecommerce API Performance Testing

A comprehensive performance testing solution for ecommerce APIs using Gatling load testing framework. This project demonstrates realistic ecommerce user journeys under load with 155 concurrent users achieving 100% success rate.

## 🚀 Quick Start

### Prerequisites
- **Java 8+** with `$JAVA_HOME` configured
- **Python 3.7+** (for the API server)
- **Git** and **GitHub CLI** (for repository management)

### 1. Clone the Repository
```bash
git clone https://github.com/adhulappanavar/ecommerce-api-performance-testing.git
cd ecommerce-api-performance-testing
```

### 2. Start the Ecommerce API
```bash
cd ecommerce-api
pip install -r requirements.txt
python app.py
```

### 3. Run Performance Tests
```bash
cd gatling-gradle-plugin-demo-java-main
./gradlew gatlingRun
```

## 📊 Performance Results

### EcommerceSimulation Results
| Metric | Target | Actual | Status |
|--------|--------|--------|--------|
| **Success Rate** | > 99.9% | **100%** | ✅ PASS |
| **Mean Response Time** | < 200ms | **72ms** | ✅ PASS |
| **Max Response Time** | < 1000ms | **307ms** | ✅ PASS |
| **Throughput** | > 15 rps | **15.2 rps** | ✅ PASS |
| **Concurrent Users** | 155 | **155** | ✅ PASS |

### ProductDetailsLoadTest Results
| Metric | Target | Actual | Status |
|--------|--------|--------|--------|
| **Success Rate** | > 99% | **100%** | ✅ PASS |
| **Mean Response Time** | < 200ms | **32ms** | ✅ PASS |
| **Max Response Time** | < 1000ms | **51ms** | ✅ PASS |
| **Throughput** | Variable | **2.89 rps** | ✅ PASS |
| **Test Duration** | 165 seconds | **165s** | ✅ PASS |

## 🏗️ Project Structure

```
├── ecommerce-api/                    # Flask API server
│   ├── app.py                       # Main API application
│   ├── requirements.txt             # Python dependencies
│   └── README.md                    # API documentation
├── gatling-gradle-plugin-demo-java-main/  # Gatling test suite
│   ├── src/gatling/java/example/
│   │   └── EcommerceSimulation.java # Main test simulation
│   ├── src/gatling/resources/
│   │   ├── customers.csv            # Test customer data
│   │   └── products.csv             # Test product data
│   ├── README.md                    # Test execution guide
│   └── LoadScenario.md              # Performance engineering scenarios
└── .gitignore                       # Git ignore rules
```

## 🧪 Test Scenarios

### EcommerceSimulation
The `EcommerceSimulation` tests a complete ecommerce user journey:

1. **Customer Creation** - New user account creation
2. **Product Search** - Search for products by name/category
3. **Product Details** - View detailed product information
4. **Add to Cart** - Add products to shopping cart
5. **View Cart** - Review cart contents
6. **Checkout** - Complete the purchase

### ProductDetailsLoadTest
The `ProductDetailsLoadTest` tests the Product Details API with a complex multi-phase load pattern:

1. **Ramp Up** - 0 to 10 users over 30 seconds
2. **Steady State** - 10 users for 40 seconds
3. **Peak Load** - 10 to 20 users over 10 seconds
4. **Peak Hold** - 20 users for 10 seconds
5. **Steady State** - 10 users for 25 seconds
6. **Ramp Down** - 10 to 0 users over 50 seconds

## 📈 Load Test Configuration

- **Users**: 155 concurrent users
- **Duration**: ~51 seconds (30s ramp-up + 21s sustained)
- **Load Pattern**: Gradual ramp-up for realistic simulation
- **User Behavior**: Realistic pauses between operations (1-5 seconds)

## 🔧 API Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| `POST` | `/api/customers` | Create customer account |
| `GET` | `/api/products` | List all products |
| `GET` | `/api/products/<id>` | Get product details |
| `GET` | `/api/search?q=query` | Search products |
| `POST` | `/api/cart` | Add item to cart |
| `GET` | `/api/cart/<customer_id>` | Get cart contents |
| `POST` | `/api/cart/<customer_id>/checkout` | Complete checkout |
| `GET` | `/api/health` | Health check |

## 📋 Documentation

- **[API Documentation](ecommerce-api/README.md)** - Flask API setup and usage
- **[Test Execution Guide](gatling-gradle-plugin-demo-java-main/README.md)** - How to run performance tests
- **[Load Scenario Mapping](gatling-gradle-plugin-demo-java-main/LoadScenario.md)** - Performance engineering scenarios and code mapping

## 🎯 Key Features

### Performance Testing
- **Realistic Load Simulation**: 155 concurrent users with realistic behavior patterns
- **Comprehensive Metrics**: Response times, throughput, success rates, and error analysis
- **Scalability Validation**: System performance under peak load conditions
- **Production-Ready**: Monitoring and alerting guidelines

### API Features
- **RESTful Design**: Clean, intuitive API endpoints
- **Error Handling**: Comprehensive error responses and validation
- **Data Management**: In-memory storage with realistic data structures
- **Performance Optimized**: Simulated processing delays for realistic testing

### Test Framework
- **Gatling Integration**: Industry-standard load testing framework
- **Gradle Build System**: Easy dependency management and execution
- **Data-Driven Testing**: CSV-based test data for realistic scenarios
- **Detailed Reporting**: HTML reports with comprehensive metrics

## 🚦 Getting Started

### For Developers
1. Clone the repository
2. Set up the API server
3. Run the performance tests
4. Analyze the results

### For Performance Engineers
1. Review the LoadScenario.md for detailed scenario mapping
2. Customize test data in CSV files
3. Modify load patterns in EcommerceSimulation.java
4. Set up monitoring and alerting

## 📊 Monitoring and Alerting

### Key Metrics to Monitor
- **Response Time**: Mean, 95th percentile, maximum
- **Success Rate**: Percentage of successful requests
- **Throughput**: Requests per second
- **Error Rate**: Failed requests percentage

### Alert Thresholds
- **Response Time**: > 500ms average
- **Success Rate**: < 99%
- **Error Rate**: > 1%
- **Throughput**: < 10 rps

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests if applicable
5. Submit a pull request

## 📄 License

This project is open source and available under the [MIT License](LICENSE).

## 🔗 Links

- **Repository**: [https://github.com/adhulappanavar/ecommerce-api-performance-testing](https://github.com/adhulappanavar/ecommerce-api-performance-testing)
- **Gatling Documentation**: [https://docs.gatling.io/](https://docs.gatling.io/)
- **Flask Documentation**: [https://flask.palletsprojects.com/](https://flask.palletsprojects.com/)

## 📞 Support

For questions or issues, please open an issue in the GitHub repository.

---

**Performance Test Results**: 155 concurrent users, 100% success rate, 72ms mean response time, 15.2 rps throughput
