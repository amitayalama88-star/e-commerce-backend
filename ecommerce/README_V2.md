# My First Ecommerce App - The Full Story!

Hey there! This is a simple online store app made with Spring Boot. It helps you learn how to make a backend (the part of the app that runs on a server and talks to the database).

## What cool tech did we use?

We used some popular tools to build this:

*   **Spring Boot:** This is like a magic box for Java apps. It helps us start new projects super fast without lots of setup. We used it for:
    *   **Spring Web:** To make the "doors" (APIs) that other apps (like Postman) can talk to.
    *   **Spring Data JPA:** To easily save and get data from our database without writing tons of SQL code.
    *   **Spring Security:** To protect our app, so only the right people (like Admin or a customer) can do certain things. (We turned it off later for simplicity, but it was there!)
    *   **Spring Boot DevTools:** This is a helper that makes our app restart super fast when we change code, so we don't have to wait.
*   **Lombok:** This is a small library that writes boring code for us. Like, it makes `get` and `set` methods, constructors, and other stuff automatically. We just put `@Data` on our classes, and it does the rest!
*   **MySQL:** This is our database. It's like a big spreadsheet where we store all our items, users, carts, and orders.
*   **Maven:** This is how we manage all the different libraries (dependencies) our project needs. It also helps us build our project into a runnable app.

## How we built this step-by-step (like a story!)

### Step 0: Getting Ready (The `pom.xml` and `application.properties` files)

Before writing any Java code, we told our project what tools it needs and how to talk to the database.

*   **`pom.xml` (The Shopping List for our Project):**
    *   We added `spring-boot-starter-web` so our app can be a web server.
    *   We added `spring-boot-starter-data-jpa` to talk to the database easily.
    *   We added `spring-boot-starter-security` to handle logins (even if we removed it later).
    *   We added `mysql-connector-j` so our app knows how to speak MySQL.
    *   We added `lombok` so we don't have to type `get` and `set` methods.
    *   We added `spring-boot-devtools` for fast restarts.
    *   **Blind Spot:** If you see errors about "dependencies" or "starters," it often means something is wrong in `pom.xml` or Maven can't download the libraries.

*   **`application.properties` (The Settings for our App):**
    *   `spring.datasource.url`: This tells our app where the MySQL database is (like `localhost:3306/ecommerce_db`).
    *   `spring.datasource.username`: Usually `root` for local setup.
    *   `spring.datasource.password`: **THIS IS SUPER IMPORTANT!** If this is wrong, your app won't start. You need to put your actual MySQL password here. This was a common "no response" problem!
    *   `spring.jpa.hibernate.ddl-auto=update`: This tells Spring to automatically create or update our database tables based on our Java code. Super handy for beginners!
    *   `spring.jpa.show-sql=true`: This makes Spring show all the SQL commands it's running, which is good for learning.
    *   **Blind Spot:** If your app doesn't start and says "Access denied for user 'root'@'localhost' (using password: NO)", it means your `spring.datasource.password` is empty or wrong. Fix it!

*   **MySQL Workbench:** We made sure our MySQL server was running and that we had a database named `ecommerce_db` (or whatever you put in the URL).

### Step 1: Making the "Things" (Models - The Blueprints)

We created Java classes that represent the real-world things in our store. These are like blueprints for our database tables.

*   **`User.java`:**
    *   `id`: A unique number for each person.
    *   `username`, `password`: For logging in.
    *   `role`: To know if someone is an "ADMIN" or "CUSTOMER".
    *   `@Entity`, `@Table`: Tells Spring this class is a database table.
    *   `@Id`, `@GeneratedValue`: Tells Spring `id` is the main key and it should make new numbers automatically.
    *   `@Column`: Gives more details about a column (like `unique = true`).
    *   `@Data`, `@NoArgsConstructor`, `@AllArgsConstructor`: Lombok magic for `get`/`set` methods and constructors.

*   **`Item.java`:**
    *   `id`, `name`, `description`, `price`, `quantity`: Details about a product.
    *   Same Lombok and JPA annotations as `User.java`.

*   **`CartItem.java`:**
    *   `id`: Unique for each item in a cart.
    *   `user`: Who owns this cart item (`@ManyToOne` means many cart items can belong to one user).
    *   `item`: Which product is in the cart (`@ManyToOne`).
    *   `quantity`: How many of this product.

*   **`Order.java`:**
    *   `id`, `orderDate`, `totalAmount`: Details about a finished purchase.
    *   `user`: Who bought it.
    *   `items`: A list of all the `OrderItem`s in this order (`@OneToMany` means one order can have many order items).

*   **`OrderItem.java`:**
    *   `id`, `item`, `quantity`, `price`: Details of one product *inside* an order. We save the `price` here because prices can change later, but the order should remember the price at the time of purchase.

### Step 2: Making the "Storage Helpers" (Repositories - The Database Talkers)

These are special interfaces that help us talk to the database without writing SQL. Spring Data JPA does all the hard work!

*   `UserRepository`, `ItemRepository`, `CartItemRepository`, `OrderRepository`:
    *   They all `extend JpaRepository<OurModel, IdType>`. This gives us free methods like `save()`, `findById()`, `findAll()`, `deleteById()`.
    *   We can also add our own methods like `findByUsername(String username)` and Spring will figure out how to make the SQL for it! Amazing!
    *   **Blind Spot:** If you misspell a method name like `findByUserame`, Spring won't know what to do and your app might crash.

### Step 3: Making the "Brain" (Services - The Logic Keepers)

These classes hold all the important business rules and logic. They use the Repositories to talk to the database.

*   **`ItemService.java`:**
    *   `getAllItems(Pageable pageable)`: Gets items, but in pages (like pages in a book), so we don't load too many at once. This is for **pagination**.
    *   `getItemById(Long id)`: Finds one item.
    *   `saveItem(Item item)`: Adds a new item or updates an old one.
    *   `deleteItem(Long id)`: Removes an item.
    *   `buyItem(Long id, int quantity)`: This was an old method, now replaced by cart checkout.
    *   `@Service`: Tells Spring this is a service class.
    *   `@Transactional`: Makes sure that if something goes wrong during a database change (like updating stock), everything goes back to how it was before. It's like an "undo" button for database operations.

*   **`CartService.java`:**
    *   `addToCart(String username, Long itemId, int quantity)`: Puts an item into a user's cart. It finds the user and item using their repositories.
    *   `getCart(String username)`: Shows what's in a user's cart and calculates the **subtotal**.
    *   `clearCart(String username)`: Empties a user's cart.
    *   **Blind Spot:** We had to change this to take `username` as an input after removing Spring Security, because the app no longer knew who was logged in automatically.

*   **`OrderService.java`:**
    *   `checkoutFromCart(String username)`: This is the big one! It takes all items from a user's cart, checks if there's enough stock, updates the stock, creates a new `Order`, saves it, and then clears the cart. It also calculates the final total with tax.
    *   **Blind Spot:** If stock is too low, it throws an error! This is good, we don't want to sell what we don't have.

### Step 4: Making the "Doors" (Controllers - The API Endpoints)

These are the classes that receive requests from Postman (or a frontend app) and send back responses. They use the Services to do the actual work.

*   **`ItemController.java`:**
    *   `GET /api/items`: Shows all items (with pagination).
    *   `GET /api/items/{id}`: Shows one item.
    *   `POST /api/items/admin`: Admin adds a new item.
    *   `PUT /api/items/admin/{id}`: Admin changes an item.
    *   `DELETE /api/items/admin/{id}`: Admin removes an item.
    *   `@RestController`: Tells Spring this class handles web requests and sends back data (like JSON).
    *   `@RequestMapping`: Sets the base path for all methods in this controller (like `/api/items`).
    *   `@GetMapping`, `@PostMapping`, `@PutMapping`, `@DeleteMapping`: Map to HTTP methods.
    *   `@RequestBody`: Takes JSON data from Postman and turns it into a Java object.
    *   `@RequestParam`: Gets values from the URL like `?itemId=1`.
    *   `@PathVariable`: Gets values from the URL path like `/items/1`.

*   **`CartController.java`:**
    *   `POST /api/cart/add`: Adds an item to a user's cart.
    *   `GET /api/cart`: Shows a user's cart.
    *   `DELETE /api/cart/clear`: Clears a user's cart.
    *   **Blind Spot:** We added `username` as a `@RequestParam` here because we removed security.

*   **`OrderController.java`:**
    *   `POST /api/orders/checkout`: Lets a user buy everything in their cart.
    *   **Blind Spot:** We added `username` as a `@RequestParam` here too.

*   **`UserController.java`:**
    *   `POST /api/users/register`: Lets new customers create an account. It saves their password safely (encoded).

### Step 5: Initial Setup (DataInitializer, SecurityConfig)

These help our app get ready when it starts.

*   **`DataInitializer.java`:**
    *   `CommandLineRunner`: This makes the `run` method execute automatically when the app starts.
    *   It creates our special admin user (`Admin123`) and 10 dummy items if they don't exist. This is super helpful for testing!

*   **`SecurityConfig.java`:**
    *   This file was first used to set up logins (`admin`/`adminpass`, `customer`/`customerpass`) and roles.
    *   `PasswordEncoder`: This makes sure passwords are saved safely (hashed) and not in plain text.
    *   `SecurityFilterChain`: This is where we tell Spring which URLs need login and which roles can access them.
    *   **Blind Spot:** We later changed this to `anyRequest().permitAll()` to turn off all security, so you don't need to log in for Postman. This is good for learning but bad for real apps!

### Step 6: DTOs (Data Transfer Objects - The Message Carriers)

These are simple classes just for sending specific data between our app and Postman (or a frontend).

*   `CartResponse.java`, `CheckoutResponse.java`:
    *   They hold just the information we want to show to the user, like item names, quantities, and totals. We don't send *all* the database info, just what's needed.

## New Feature: See Your Past Orders!

Now, let's add a new cool thing: a customer can see all the stuff they bought before!

### How we added it:

1.  **`OrderRepository.java`:** We added a new method `List<Order> findByUser(User user);` so we can easily get all orders for a specific user.
2.  **`OrderService.java`:** We added a new method `getOrderHistory(String username)` that uses the repository to fetch orders for that user.
3.  **`OrderController.java`:** We added a new "door" (`GET /api/orders/history`) so Postman can ask for the order history.

## How to make it work (again!)

1.  **Open MySQL.** Make sure it's running.
2.  **`application.properties`:** Double-check your `spring.datasource.password` is correct! This is the most common problem.
3.  **Run the project:** In your IDE, find `EcommerceApplication.java` and click the green "Run" button. Wait until it says "Started EcommerceApplication".

## Postman Working Inputs (No Login Needed!)

### 1. Make an account (New!)
*   **Method:** `POST`
*   **URL:** `http://localhost:8080/api/users/register`
*   **Body (raw JSON):**
```json
{
  "username": "mycoolcustomer",
  "password": "mypassword123"
}
```
*   **What you get:** "You made an account! Now you can buy stuff."

### 2. Adding to cart
*   **Method:** `POST`
*   **URL:** `http://localhost:8080/api/cart/add?username=mycoolcustomer&itemId=1&quantity=2`
*   **Body:** None
*   **What you get:** "Item added to cart! Go see your cart now."

### 3. See my cart and subtotal
*   **Method:** `GET`
*   **URL:** `http://localhost:8080/api/cart?username=mycoolcustomer`
*   **What you get:** A list of items in your cart and the **subtotal** price.

### 4. Checkout (Buy)
*   **Method:** `POST`
*   **URL:** `http://localhost:8080/api/orders/checkout?username=mycoolcustomer`
*   **What you get:** A receipt with your `orderId`, `subtotal`, `tax`, and `total`. It will say "Oops! You didn't put anything in your cart!" if your cart is empty.

### 5. See my past orders (NEW FEATURE!)
*   **Method:** `GET`
*   **URL:** `http://localhost:8080/api/orders/history?username=mycoolcustomer`
*   **What you get:** A list of all your past orders!

### 6. Admin - Add new item
*   **Method:** `POST`
*   **URL:** `http://localhost:8080/api/items/admin`
*   **Body (raw JSON):**
```json
{
  "name": "Super Widget",
  "description": "A very useful thing",
  "price": 25.0,
  "quantity": 50
}
```

### 7. Admin - Change item
*   **Method:** `PUT`
*   **URL:** `http://localhost:8080/api/items/admin/1` (change `1` to the item ID you want to change)
*   **Body (raw JSON):**
```json
{
  "name": "Super Widget Pro",
  "description": "An even more useful thing",
  "price": 30.0,
  "quantity": 45
}
```
