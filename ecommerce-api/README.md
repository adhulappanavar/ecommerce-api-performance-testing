# Ecommerce API

A simple Flask-based ecommerce API for performance testing.

## Setup

1. Install dependencies:
```bash
pip install -r requirements.txt
```

2. Run the server:
```bash
python app.py
```

The API will be available at `http://localhost:5000`

## Endpoints

- `POST /api/customers` - Create a new customer
- `GET /api/customers/<id>` - Get customer details
- `GET /api/products` - List all products (supports pagination and filtering)
- `GET /api/products/<id>` - Get product details
- `GET /api/search?q=query` - Search products
- `POST /api/cart` - Add item to cart
- `GET /api/cart/<customer_id>` - Get customer's cart
- `POST /api/cart/<customer_id>/checkout` - Process checkout
- `GET /api/health` - Health check

## Sample Usage

```bash
# Create a customer
curl -X POST http://localhost:5000/api/customers \
  -H "Content-Type: application/json" \
  -d '{"name": "John Doe", "email": "john@example.com"}'

# Search products
curl "http://localhost:5000/api/search?q=iPhone"

# Add to cart
curl -X POST http://localhost:5000/api/cart \
  -H "Content-Type: application/json" \
  -d '{"customer_id": "customer-id", "product_id": 1, "quantity": 2}'
```

