# My First Ecommerce App

This is my app for making a store. It can do items and a cart.

## Things you can do

### 1. Make an account (New!)
*   Post here to make an account: `POST http://localhost:8080/api/users/register`
*   Put this in the JSON box:
```json
{
  "username": "myname123",
  "password": "mypassword"
}
```

### 2. Buying things
*   See everything: `GET http://localhost:8080/api/items`
*   Put in cart: `POST http://localhost:8080/api/cart/add?username=myname123&itemId=1&quantity=2`
*   See my cart and subtotal: `GET http://localhost:8080/api/cart?username=myname123`
*   Buy it (Successful!): `POST http://localhost:8080/api/orders/checkout?username=myname123`

### 3. Stuff for the Boss (Admin123)
*   Add new item: `POST http://localhost:8080/api/items/admin`
*   Change item: `PUT http://localhost:8080/api/items/admin/1`
*   Delete item: `DELETE http://localhost:8080/api/items/admin/1`

## How to use Postman (copy and paste this)

### 1. Register
Pick `POST`, link `http://localhost:8080/api/users/register`.
Paste this in the JSON box:
```json
{
  "username": "myname123",
  "password": "mypassword"
}
```

### 2. Adding to cart
Pick `POST`, use this link: `http://localhost:8080/api/cart/add?username=myname123&itemId=1&quantity=2`.
It will say "Item added to cart! Go see your cart now."

### 3. See subtotal
Pick `GET`, link `http://localhost:8080/api/cart?username=myname123`.
It will show all your items and the **subtotal** price at the bottom.

### 4. Checkout (Buy)
Pick `POST`, link: `http://localhost:8080/api/orders/checkout?username=myname123`. 
If it works, it shows your receipt with the final total.

## How to make it work
1. Open MySQL.
2. Go to `application.properties` and type your password there.
3. Run the project in the IDE.
4. The admin username is now `Admin123`.
