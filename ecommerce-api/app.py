from flask import Flask, request, jsonify
import random
import time
import uuid
from datetime import datetime

app = Flask(__name__)

# Sample data
products = [
    {"id": 1, "name": "iPhone 15 Pro", "price": 999.99, "category": "Electronics", "stock": 50},
    {"id": 2, "name": "Samsung Galaxy S24", "price": 899.99, "category": "Electronics", "stock": 30},
    {"id": 3, "name": "MacBook Pro M3", "price": 1999.99, "category": "Computers", "stock": 25},
    {"id": 4, "name": "Dell XPS 13", "price": 1299.99, "category": "Computers", "stock": 40},
    {"id": 5, "name": "Sony WH-1000XM5", "price": 399.99, "category": "Audio", "stock": 60},
    {"id": 6, "name": "AirPods Pro", "price": 249.99, "category": "Audio", "stock": 80},
    {"id": 7, "name": "Nike Air Max", "price": 129.99, "category": "Shoes", "stock": 100},
    {"id": 8, "name": "Adidas Ultraboost", "price": 180.99, "category": "Shoes", "stock": 75},
    {"id": 9, "name": "Levi's 501 Jeans", "price": 89.99, "category": "Clothing", "stock": 120},
    {"id": 10, "name": "Uniqlo T-Shirt", "price": 19.99, "category": "Clothing", "stock": 200}
]

customers = {}
carts = {}

@app.route('/api/health', methods=['GET'])
def health_check():
    return jsonify({"status": "healthy", "timestamp": datetime.now().isoformat()})

@app.route('/api/customers', methods=['POST'])
def create_customer():
    """Create a new customer"""
    data = request.get_json()
    customer_id = str(uuid.uuid4())
    
    # Simulate some processing time
    time.sleep(random.uniform(0.01, 0.05))
    
    customer = {
        "id": customer_id,
        "name": data.get("name", "John Doe"),
        "email": data.get("email", f"user{customer_id[:8]}@example.com"),
        "created_at": datetime.now().isoformat()
    }
    
    customers[customer_id] = customer
    return jsonify(customer), 201

@app.route('/api/customers/<customer_id>', methods=['GET'])
def get_customer(customer_id):
    """Get customer details"""
    if customer_id not in customers:
        return jsonify({"error": "Customer not found"}), 404
    
    # Simulate some processing time
    time.sleep(random.uniform(0.01, 0.03))
    
    return jsonify(customers[customer_id])

@app.route('/api/products', methods=['GET'])
def get_products():
    """Get all products with optional pagination and filtering"""
    page = int(request.args.get('page', 1))
    limit = int(request.args.get('limit', 10))
    category = request.args.get('category')
    
    # Simulate some processing time
    time.sleep(random.uniform(0.02, 0.08))
    
    filtered_products = products
    if category:
        filtered_products = [p for p in products if p['category'].lower() == category.lower()]
    
    start_idx = (page - 1) * limit
    end_idx = start_idx + limit
    paginated_products = filtered_products[start_idx:end_idx]
    
    return jsonify({
        "products": paginated_products,
        "total": len(filtered_products),
        "page": page,
        "limit": limit
    })

@app.route('/api/products/<int:product_id>', methods=['GET'])
def get_product(product_id):
    """Get product details by ID"""
    # Simulate some processing time
    time.sleep(random.uniform(0.01, 0.04))
    
    product = next((p for p in products if p['id'] == product_id), None)
    if not product:
        return jsonify({"error": "Product not found"}), 404
    
    return jsonify(product)

@app.route('/api/search', methods=['GET'])
def search_products():
    """Search products by name or category"""
    query = request.args.get('q', '')
    category = request.args.get('category')
    
    # Simulate search processing time
    time.sleep(random.uniform(0.05, 0.15))
    
    results = []
    for product in products:
        if query.lower() in product['name'].lower():
            if not category or product['category'].lower() == category.lower():
                results.append(product)
    
    # Return array directly for Gatling test compatibility
    return jsonify(results)

@app.route('/api/cart', methods=['POST'])
def add_to_cart():
    """Add item to cart"""
    data = request.get_json()
    # Support both snake_case and camelCase
    customer_id = data.get('customer_id') or data.get('customerId')
    product_id = data.get('product_id') or data.get('productId')
    quantity = data.get('quantity', 1)
    
    # Simulate some processing time
    time.sleep(random.uniform(0.02, 0.06))
    
    if not customer_id or not product_id:
        return jsonify({"error": "customer_id and product_id are required"}), 400
    
    if customer_id not in customers:
        return jsonify({"error": "Customer not found"}), 404
    
    product = next((p for p in products if p['id'] == product_id), None)
    if not product:
        return jsonify({"error": "Product not found"}), 404
    
    if product['stock'] < quantity:
        return jsonify({"error": "Insufficient stock"}), 400
    
    if customer_id not in carts:
        carts[customer_id] = []
    
    # Check if item already in cart
    for item in carts[customer_id]:
        if item['product_id'] == product_id:
            item['quantity'] += quantity
            break
    else:
        carts[customer_id].append({
            "product_id": product_id,
            "name": product['name'],
            "price": product['price'],
            "quantity": quantity
        })
    
    return jsonify({
        "message": "Item added to cart",
        "cart": carts[customer_id]
    })

@app.route('/api/cart/<customer_id>', methods=['GET'])
def get_cart(customer_id):
    """Get customer's cart"""
    # Simulate some processing time
    time.sleep(random.uniform(0.01, 0.03))
    
    if customer_id not in customers:
        return jsonify({"error": "Customer not found"}), 404
    
    cart_items = carts.get(customer_id, [])
    total = sum(item['price'] * item['quantity'] for item in cart_items)
    
    return jsonify({
        "customer_id": customer_id,
        "items": cart_items,
        "total": total,
        "item_count": len(cart_items)
    })

@app.route('/api/cart/<customer_id>/checkout', methods=['POST'])
def checkout(customer_id):
    """Process checkout"""
    # Simulate checkout processing time
    time.sleep(random.uniform(0.1, 0.3))
    
    if customer_id not in customers:
        return jsonify({"error": "Customer not found"}), 404
    
    cart_items = carts.get(customer_id, [])
    if not cart_items:
        return jsonify({"error": "Cart is empty"}), 400
    
    # Simulate payment processing
    total = sum(item['price'] * item['quantity'] for item in cart_items)
    order_id = str(uuid.uuid4())
    
    # Clear cart after successful checkout
    carts[customer_id] = []
    
    return jsonify({
        "order_id": order_id,
        "customer_id": customer_id,
        "total": total,
        "status": "completed",
        "timestamp": datetime.now().isoformat()
    })

if __name__ == '__main__':
    print("Starting Ecommerce API Server...")
    print("Available endpoints:")
    print("- POST /api/customers - Create customer")
    print("- GET /api/customers/<id> - Get customer")
    print("- GET /api/products - List products")
    print("- GET /api/products/<id> - Get product details")
    print("- GET /api/search?q=query - Search products")
    print("- POST /api/cart - Add to cart")
    print("- GET /api/cart/<customer_id> - Get cart")
    print("- POST /api/cart/<customer_id>/checkout - Checkout")
    print("- GET /api/health - Health check")
    
    app.run(host='0.0.0.0', port=5000, debug=True)

