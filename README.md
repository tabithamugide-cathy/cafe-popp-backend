# CafePopp Restaurant Management System

CafePopp is a Spring Boot and React restaurant management system backed by PostgreSQL. It supports a public guest ordering experience and authenticated staff workspaces for administrators, cashiers, waiters, and kitchen staff.

## Project Layout

- `src/main/java`: Spring Boot API, entities, repositories, services, controllers, and security
- `src/main/resources/application.properties`: local PostgreSQL, cache, CORS, and development security settings
- `TabyCafe`: React/Vite frontend

## Requirements

- Java 26
- PostgreSQL
- Node.js and npm
- Database: `cafe_popp`

## Run Locally

Start PostgreSQL and ensure the database exists, then run the backend from this directory:

```bash
./mvnw spring-boot:run
```

The API runs at `http://localhost:8080`.

In a second terminal:

```bash
cd TabyCafe
npm install
npm run dev
```

The Vite frontend normally runs at `http://localhost:5173`. If that port is busy, Vite may use `5174`; both origins are allowed by the current CORS configuration.

## Authentication and Security

Staff authentication is implemented with Spring Security and BCrypt password verification.

### Login

```http
POST /api/auth/login
Content-Type: application/json

{
  "email": "admin@restaurant.com",
  "password": "Admin@12345"
}
```

A successful response contains:

```json
{
  "token": "...",
  "userId": 2,
  "fullName": "John Doe",
  "email": "admin@restaurant.com",
  "role": "ADMIN"
}
```

Send the token on protected requests:

```http
Authorization: Bearer <token>
```

The frontend stores the token in `localStorage` and the shared API helper attaches it automatically. Logout removes the token and local user settings.

### Roles

| Role        | Intended access                                                                            |
| ----------- | ------------------------------------------------------------------------------------------ |
| `ADMIN`   | Dashboard, reports, staff, stock, products, categories, tables, payments, orders, settings |
| `CASHIER` | Dashboard, menu, orders, tables, payments, customers, settings                             |
| `WAITER`  | Menu, orders, tables, customers, settings                                                  |
| `KITCHEN` | Menu, orders, settings; kitchen order progression                                          |

Authorization is enforced twice:

1. Frontend route and sidebar filtering controls what users see.
2. Spring Security request rules and `@PreAuthorize` annotations protect API operations even when a user calls the API directly.

Unauthenticated guest access is intentionally available for menu browsing, table selection, guest order creation, and payment operations required by the current guest flow. Management endpoints remain role protected.

### Development Password Repair

`DevelopmentUserPasswordInitializer` repairs malformed hashes for seeded local users only when:

```properties
app.security.seed-development-passwords=true
```

It only replaces hashes that are missing or not valid BCrypt-length hashes. Disable this setting in production:

```properties
app.security.seed-development-passwords=false
```

Development users:

| User        | Email                            | Password          | Role        |
| ----------- | -------------------------------- | ----------------- | ----------- |
| John Doe    | `admin@restaurant.com`         | `Admin@12345`   | `ADMIN`   |
| Jane Smith  | `jane.waiter@restaurant.com`   | `Waiter@12345`  | `WAITER`  |
| Bob Johnson | `bob.cashier@restaurant.com`   | `Cashier@12345` | `CASHIER` |
| Chef Marco  | `marco.kitchen@restaurant.com` | `Chef@12345`    | `KITCHEN` |

These credentials are for local development only.

### Current Security Limitations

- Tokens are stored in an in-memory `ConcurrentHashMap`; all tokens become invalid when the backend restarts and tokens are not shared across backend instances.
- The frontend stores tokens in `localStorage`. A production deployment should consider secure, HTTP-only cookies or a carefully designed token strategy.
- The development password repair must be disabled in production.
- Guest order lookup currently relies on the active browser's local storage and order ID. A production multi-device flow should use an opaque customer order token or authenticated customer session.
- Payment endpoints currently model the restaurant workflow and do not connect to a real mobile-money or card gateway.

## Main API Areas

### Menu

- `GET /api/menu/items`
- `GET /api/menu/items/available`
- `GET /api/menu/items/{id}`
- `POST /api/menu/items` for authorized staff
- `PATCH /api/menu/items/{id}/availability?available=true` for authorized staff
- `GET /api/menu/categories`
- `POST /api/menu/categories` for authorized staff

Menu reads use Spring Cache with Caffeine. The cache expires after 60 seconds and is evicted after menu mutations. PostgreSQL indexes support availability and category filtering, and menu queries use deterministic name ordering.

### Orders

- `POST /api/orders?tableId={id}` creates a guest order
- `POST /api/orders/{id}/items?menuItemId={id}&quantity={n}` adds an item
- `POST /api/orders/{id}/confirm` confirms the guest order and starts preparation
- `GET /api/orders/{id}` returns a customer order status
- `GET /api/orders/all` returns staff order data
- `PATCH /api/orders/{id}/status?newStatus=IN_PROGRESS|SERVED|PAID|CANCELLED` changes status

Order lifecycle:

```text
OPEN -> IN_PROGRESS -> SERVED -> PAID
```

The kitchen or authorized staff marks an order ready with `SERVED`. The customer-facing view detects that status and reveals payment. When payment succeeds, the order becomes `PAID` and the table returns to `FREE`.

### Tables

- `GET /api/tables`
- `POST /api/tables`
- `PATCH /api/tables/{id}/status?status=FREE|OCCUPIED|RESERVED`

Guest checkout can select free tables. Authorized staff can add tables and update their status.

### Payments

- `GET /api/payments` for authorized operational users
- `POST /api/payments?orderId={id}&method=CASH|MOBILE_MONEY|CARD`

Only served orders can be paid, and duplicate payments for the same order are rejected.

### Dashboard

- `GET /api/dashboard/summary`

The dashboard uses live PostgreSQL aggregates for total orders, pending orders, menu items, available tables, revenue, recent orders, and top products. The frontend refreshes the summary periodically.

## Customer Flow

1. Guest opens `/` and sees the public menu.
2. Guest adds items to the cart.
3. Guest selects a free table.
4. Backend creates the order under the reusable `Guest Orders` system user.
5. Guest confirms the order.
6. Kitchen moves the order through preparation to `SERVED`.
7. The customer order view polls for status and exposes payment when ready.
8. Customer selects cash, mobile money, or card and pays through the payment endpoint.

## Validation

Backend:

```bash
./mvnw test
```

Frontend:

```bash
cd TabyCafe
npm run build
```

The repository currently has unrelated pre-existing ESLint findings in several frontend files; the production Vite build is the primary frontend validation command.
