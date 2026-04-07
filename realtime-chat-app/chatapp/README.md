# 💬 Real-Time Chat Application — Setup Guide

## Tech Stack
- **Backend**: Java 17, Spring Boot 3.2, Spring Security, Spring WebSocket (STOMP)
- **Database**: MySQL 8+
- **Cache**: Redis
- **Auth**: JWT (JSON Web Tokens)
- **Frontend**: HTML5 + Vanilla JS (SockJS + STOMP.js)
- **IDE**: IntelliJ IDEA

---

## Prerequisites

| Tool | Version | Download |
|------|---------|----------|
| Java JDK | 17+ | https://adoptium.net |
| Maven | 3.8+ | Bundled with IntelliJ |
| MySQL | 8.0+ | https://dev.mysql.com/downloads |
| Redis | 7+ | https://redis.io/download |
| IntelliJ IDEA | 2023+ | https://www.jetbrains.com/idea |

---

## Step-by-Step Setup

### Step 1 — Install & Start MySQL

```bash
# macOS (Homebrew)
brew install mysql
brew services start mysql

# Ubuntu/Debian
sudo apt install mysql-server
sudo systemctl start mysql

# Windows — use MySQL Installer from official site
```

Create the database:
```sql
mysql -u root -p
CREATE DATABASE chatapp_db;
EXIT;
```

### Step 2 — Install & Start Redis

```bash
# macOS
brew install redis
brew services start redis

# Ubuntu/Debian
sudo apt install redis-server
sudo systemctl start redis

# Windows — use Redis for Windows (https://github.com/microsoftarchive/redis/releases)
# OR use WSL2 with Ubuntu and run: sudo apt install redis-server
```

Verify Redis is running:
```bash
redis-cli ping
# Should return: PONG
```

### Step 3 — Open Project in IntelliJ IDEA

1. Open IntelliJ IDEA
2. Click **File → Open** and select the `realtime-chat` folder
3. IntelliJ will detect the `pom.xml` — click **"Load Maven Project"** when prompted
4. Wait for Maven to download all dependencies (bottom status bar)

### Step 4 — Configure application.properties

Open `src/main/resources/application.properties` and update:

```properties
# MySQL credentials
spring.datasource.username=root
spring.datasource.password=YOUR_MYSQL_PASSWORD

# Redis (if password protected)
# spring.data.redis.password=YOUR_REDIS_PASSWORD
```

### Step 5 — Run the Application

**Option A — IntelliJ UI:**
1. Open `RealtimeChatApplication.java`
2. Click the green ▶ **Run** button (or press `Shift+F10`)

**Option B — Terminal:**
```bash
cd realtime-chat
./mvnw spring-boot:run
```

You should see:
```
Started RealtimeChatApplication in X.XXX seconds
Tomcat started on port(s): 8080
```

### Step 6 — Open the App

Visit: **http://localhost:8080**

The built-in chat UI will load automatically.

---

## API Endpoints Reference

### Authentication
```
POST /api/auth/register    — Register new user
POST /api/auth/login       — Login, returns JWT token
```

### Users
```
GET  /api/users            — List all users
GET  /api/users/me         — Current user profile
PUT  /api/users/me         — Update profile
GET  /api/users/{id}       — Get user by ID
GET  /api/users/search?q=  — Search users
GET  /api/users/online     — List online users
POST /api/users/status     — Update status (ONLINE/OFFLINE/AWAY)
```

### Messages
```
POST /api/messages/private          — Send private message
POST /api/messages/group            — Send group message
GET  /api/messages/private/{userId} — Get private chat history
GET  /api/messages/group/{groupId}  — Get group messages
PUT  /api/messages/read/{senderId}  — Mark messages as read
DEL  /api/messages/{id}             — Delete a message
```

### Groups
```
POST   /api/groups                       — Create group
GET    /api/groups                       — My groups
GET    /api/groups/{id}                  — Group details
PUT    /api/groups/{id}                  — Update group
POST   /api/groups/{id}/members          — Add member
DELETE /api/groups/{id}/members/{userId} — Remove member
```

### Notifications
```
GET /api/notifications          — All notifications
GET /api/notifications/unread   — Unread notifications
GET /api/notifications/count    — Unread count
PUT /api/notifications/read-all — Mark all as read
PUT /api/notifications/{id}/read — Mark one as read
```

---

## WebSocket Events (STOMP)

### Client → Server
| Destination | Description |
|-------------|-------------|
| `/app/chat.private` | Send private message |
| `/app/chat.group` | Send group message |
| `/app/chat.typing` | Typing indicator |
| `/app/chat.presence` | Status change |

### Server → Client
| Destination | Description |
|-------------|-------------|
| `/user/queue/messages` | Incoming private message |
| `/user/queue/typing` | Typing notification |
| `/user/queue/read-receipt` | Read receipt |
| `/user/queue/notifications` | Push notifications |
| `/topic/presence` | Online/offline status broadcast |
| `/topic/group/{id}` | Group messages |

### Connection Example (JavaScript)
```javascript
const socket = new SockJS('http://localhost:8080/ws');
const client = Stomp.over(socket);

client.connect({ 'Authorization': 'Bearer ' + jwtToken }, () => {
    client.subscribe('/user/queue/messages', msg => {
        const data = JSON.parse(msg.body);
        console.log('New message:', data);
    });
});
```

---

## Architecture Overview

```
Browser / Client
     │
     ├── REST API (JWT Auth)    → AuthController, MessageController, etc.
     │
     └── WebSocket (STOMP)      → ChatWebSocketController
                                   WebSocketEventListener
                                        │
                              ┌─────────┴──────────┐
                              │                    │
                          MySQL DB             Redis Cache
                     (Users, Messages,      (Message history,
                      Groups, Notifs)        Session data)
```

---

## Project Structure

```
src/main/java/com/chatapp/
├── RealtimeChatApplication.java
├── config/
│   ├── SecurityConfig.java       ← JWT + CORS + Security rules
│   ├── WebSocketConfig.java      ← STOMP broker + JWT WS auth
│   └── RedisConfig.java          ← Redis template config
├── entity/
│   ├── User.java
│   ├── Message.java
│   ├── Group.java
│   └── Notification.java
├── repository/                   ← Spring Data JPA repos
├── dto/                          ← Request/Response POJOs
├── security/
│   ├── JwtUtils.java
│   ├── AuthTokenFilter.java
│   └── UserDetailsServiceImpl.java
├── service/
│   ├── AuthService.java
│   ├── UserService.java
│   ├── MessageService.java       ← Redis caching here
│   ├── GroupService.java
│   └── NotificationService.java
├── controller/                   ← REST endpoints
└── websocket/
    ├── ChatWebSocketController.java
    └── WebSocketEventListener.java

src/main/resources/
├── application.properties
└── static/
    └── index.html                ← Built-in WhatsApp-like UI
```

---

## Testing with Postman

1. **Register:**
```json
POST http://localhost:8080/api/auth/register
{
  "username": "alice",
  "email": "alice@test.com",
  "password": "password123",
  "fullName": "Alice Smith"
}
```

2. **Login** → copy the `token` from response

3. **Add Authorization header** to all subsequent requests:
```
Authorization: Bearer <your_token_here>
```

---

## Common Issues & Fixes

| Problem | Fix |
|---------|-----|
| `Access denied` errors | Check MySQL username/password in `application.properties` |
| Redis connection refused | Ensure Redis is running: `redis-cli ping` |
| Port 8080 in use | Change `server.port=8081` in properties |
| WebSocket not connecting | Ensure CORS is allowed; check browser console |
| `Table doesn't exist` | Spring auto-creates tables on first run with `ddl-auto=update` |

---

## Environment Variables (Production)

For production, use environment variables instead of hardcoded values:

```bash
export SPRING_DATASOURCE_URL=jdbc:mysql://prod-host:3306/chatapp_db
export SPRING_DATASOURCE_USERNAME=chatuser
export SPRING_DATASOURCE_PASSWORD=securepassword
export APP_JWT_SECRET=your-256-bit-secret-key-here
export SPRING_DATA_REDIS_HOST=redis-host
```
