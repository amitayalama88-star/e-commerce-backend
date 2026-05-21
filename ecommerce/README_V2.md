# My First Ecommerce Backend Project (Explainer Edition)

Hey there! This is my very first try at making a backend for an online store. It's built with Spring Boot, which is a cool tool for making Java web stuff.

## What We Used and Why (The Tools)

*   **Spring Web:** This is like the main engine that lets our app talk to the internet. It helps us make "endpoints" (like special web addresses) that Postman can send requests to.
*   **Spring Boot DevTools:** This is super handy! When we change our code, it automatically restarts the app so we don't have to keep doing it ourselves. Saves a lot of time!
*   **Lombok:** This is a little helper that writes a lot of boring code for us. Like, instead of typing `getSomething()` and `setSomething()` for every variable, Lombok just does it automatically. It makes our code look much cleaner.
*   **Spring Data JPA:** This is how our Java code talks to the database. It helps us save, find, and change data without writing a ton of complicated database commands.
*   **MySQL:** This is our database! It's where all the information about users, items, and orders lives. We used MySQL Workbench to see and manage it.

## How We Built It (Step-by-Step Journey)

### Step 1: Getting Started (The Foundation)

1.  **`pom.xml` (The Shopping List for Our Project):**
    *   This file tells our project what other pieces of software (called "dependencies") it needs to work. We added `spring-boot-starter-web`, `spring-boot-starter-data-jpa`, `mysql-connector-j`, `lombok`, and `spring-boot-devtools`.
    *   **Why:** These are the basic building blocks for a web app that talks to a MySQL database and uses Lombok for cleaner code.
2.  **`application.properties` (The Settings File):**
    *   Here, we told our app how to connect to our MySQL database. We put in the database URL, username (`root`), and importantly, the password for MySQL.
    *   **Why:** Without this, our app can't find or use the database to store any information. We also set `spring.jpa.hibernate.ddl-auto=update` so Spring would automatically make tables in our database based on our Java code.

### Step 2: Making Our Data Look Good (The Blueprints)

We made some Java files that describe what our data looks like in the database. These are called "models" or "entities."

*   **`User.java`:** This is for people who use our store. It has their `username`, `password` (which we encrypt!), and `role` (like `ADMIN` or `CUSTOMER`).
    *   **Why:** We need to know who is using our app.
*   **`Item.java`:** This is for the things we sell. It has `name`, `description`, `price`, and `quantity`.
    *   **Why:** To keep track of what's in our store.
*   **`CartItem.java`:** This holds what a user has put in their shopping cart. It links a `User` to an `Item` and says how many of that item they want (`quantity`).
    *   **Why:** So users can collect items before buying.
*   **`Order.java`:** This is like a receipt after someone buys something. It saves the `orderDate`, `totalAmount`, who bought it (`User`), and what they bought (`OrderItem`s).
    *   **Why:** To keep a history of purchases.
*   **`OrderItem.java`:** This is a small part of an `Order`. It says which `Item` was bought, how many (`quantity`), and what the `price` was at that time.
    *   **Why:** To detail what was in each order.

### Step 3: Database Talkers (The Librarians)

We made special Java files called "repositories" that know how to talk to the database for each of our models.

*   **`UserRepository.java`:** Helps us find users, save new users, etc.
*   **`ItemRepository.java`:** Helps us find items, save new items, etc.
*   **`CartItemRepository.java`:** Helps us manage items in a user's cart.
*   **`OrderRepository.java`:** Helps us save new orders.
    *   **Why:** These make it easy to do common database tasks without writing complex SQL commands.

### Step 4: The Brains of the Operation (The Managers)

These are "service" files that contain the actual logic for what our app does.

*   **`ItemService.java`:**
    *   Has methods like `getAllItems`, `getItemById`, `saveItem`, `deleteItem`.
    *   **Why:** This is where we put the rules for managing items (like adding, changing, removing).
*   **`CartService.java`:**
    *   Has methods like `addToCart`, `getCart`, `clearCart`.
    *   **Why:** This handles all the shopping cart actions for a user.
*   **`OrderService.java`:**
    *   Has a method `checkoutFromCart`.
    *   **Why:** This is the big one! It takes everything from the cart, checks if we have enough stock, updates the stock, calculates the total (with tax!), saves the order, and then empties the cart.

### Step 5: The Front Desk (The Greeters)

These are "controller" files that listen for requests from Postman (or a web browser) and tell the right "service" what to do.

*   **`ItemController.java`:**
    *   Handles requests like `GET /api/items` (to see all items), `POST /api/items/admin` (to add an item as admin), `PUT /api/items/admin/1` (to change an item), and `DELETE /api/items/admin/1` (to remove an item).
    *   **Why:** This is how users and admins interact with the items in the store.
*   **`CartController.java`:**
    *   Handles `POST /api/cart/add` (to put something in the cart), `GET /api/cart` (to see what's in the cart), and `DELETE /api/cart/clear` (to empty the cart).
    *   **Why:** This is the interface for shopping cart actions.
*   **`OrderController.java`:**
    *   Handles `POST /api/orders/checkout` (to buy everything in the cart).
    *   **Why:** This is where the purchase happens.
*   **`UserController.java`:**
    *   Handles `POST /api/users/register` (to create a new customer account).
    *   **Why:** This lets new customers sign up.

### Step 6: Initial Data & Security (The Setup Crew)

*   **`DataInitializer.java`:**
    *   This special file runs once when our app starts. It creates a special admin user (`Admin123` with password `adminpass`) and puts 10 dummy items (`Item-1` to `Item-10`) into the database if they aren't there already.
    *   **Why:** So we have some data to play with right away without having to add it manually.
*   **`SecurityConfig.java`:**
    *   We started with some security rules, but then we removed them to make testing easier. Later, we added back a very basic form of security where you need to provide a `username` in the URL for cart and order actions.
    *   **Why:** Initially, to protect our endpoints. Then, to simplify for testing. Finally, to link cart/order actions to a specific user even without full login.

## How to Use It Now (Postman Time!)

Remember, for customer actions, you'll need to include your `username` in the URL. For admin actions, you don't need a username in the URL, but the admin user `Admin123` is created in the database.

### 1. Make an Account (New Customer)
*   **Method:** `POST`
*   **URL:** `http://localhost:8080/api/users/register`
*   **Body (raw JSON):**
```json
{
  "username": "mycustomer",
  "password": "mypassword"
}
```
*   **What you get:** "You made an account! Now you can buy stuff."

### 2. Add to Cart (As a Customer)
*   **Method:** `POST`
*   **URL:** `http://localhost:8080/api/cart/add?username=mycustomer&itemId=1&quantity=2`
*   **Body:** None
*   **What you get:** "Item added to cart! Go see your cart now."

### 3. See Your Cart and Subtotal (As a Customer)
*   **Method:** `GET`
*   **URL:** `http://localhost:8080/api/cart?username=mycustomer`
*   **Body:** None
*   **What you get:** A list of items in your cart, their prices, and the `subtotal` at the end.

### 4. Checkout (Buy Everything! As a Customer)
*   **Method:** `POST`
*   **URL:** `http://localhost:8080/api/orders/checkout?username=mycustomer`
*   **Body:** None
*   **What you get:** A "receipt" with your `orderId`, `subtotal`, `tax`, and `total`. It should say "buying successful" implicitly by giving you this receipt!

### 5. Admin - Add New Item
*   **Method:** `POST`
*   **URL:** `http://localhost:8080/api/items/admin`
*   **Body (raw JSON):**
```json
{
  "name": "Super Widget",
  "description": "A really useful thing",
  "price": 99.99,
  "quantity": 50
}
```
*   **What you get:** The details of the new item you just added.

### 6. Admin - Update an Item
*   **Method:** `PUT`
*   **URL:** `http://localhost:8080/api/items/admin/1` (Change `1` to the ID of the item you want to update)
*   **Body (raw JSON):**
```json
{
  "name": "Updated Item-1",
  "description": "Now even better!",
  "price": 12.00,
  "quantity": 95
}
```
*   **What you get:** The updated details of the item.

### 7. Admin - Delete an Item
*   **Method:** `DELETE`
*   **URL:** `http://localhost:8080/api/items/admin/1` (Change `1` to the ID of the item you want to delete)
*   **Body:** None
*   **What you get:** Nothing (204 No Content), meaning it worked!

## How to Make It Work (Again!)

1.  **MySQL:** Make sure your MySQL database is running.
2.  **`application.properties`:** Open `src/main/resources/application.properties` and put your actual MySQL password where it says `YOUR_PASSWORD`.
3.  **Run:** Go to `src/main/java/com/ecommerce/EcommerceApplication.java` in your IDE and click the green "Run" button.
4.  **Admin User:** The admin user `Admin123` with password `adminpass` is automatically created for you.
