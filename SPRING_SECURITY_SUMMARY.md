# Spring Security Summary for this Project

This file summarizes the Spring Security flow and the specific code paths used in this project.
It is written for a beginner and explains step-by-step what happens during authentication.

## Key files in this project

- `demo/demo/src/main/java/com/example/demo/config/SecurityConfig.java`
  - configures Spring Security using `SecurityFilterChain`
  - defines a `PasswordEncoder` bean (`BCryptPasswordEncoder`)
  - permits `/api/posts` and `/api/posts/**` without authentication
- `demo/demo/src/main/java/com/example/demo/services/impl/CustomUserDetailService.java`
  - implements `UserDetailsService`
  - loads user data from the database by email
- `demo/demo/src/main/java/com/example/demo/entities/User.java`
  - entity class implementing `UserDetails`
  - returns username, password, authorities, and account status
- `demo/demo/src/main/java/com/example/demo/repositories/UserRepo.java`
  - repository used to find a `User` by email

## Step-by-step authentication flow

1. `HttpServletRequest` reaches the app.
   - The user sends a login request, usually with an email and a password.
   - This can be a form login or HTTP Basic login.

2. Spring Security applies filters from `SecurityFilterChain`.
   - `SecurityConfig` defines the filter chain with `http.formLogin(...)` and `http.httpBasic(...)`.
   - The filter chain is the set of rules that decides how requests are authenticated and authorized.

3. Spring Security extracts login credentials.
   - For form login, `UsernamePasswordAuthenticationFilter` reads the submitted username and password.
   - For HTTP Basic, `BasicAuthenticationFilter` does the same.

4. Spring creates an authentication token.
   - The token contains the submitted username/email and raw password.

5. The token is passed to `AuthenticationManager`.
   - In this project, Spring Boot auto-configures the authentication manager.
   - `SecurityConfig` defines a bean for `AuthenticationManager` by calling `config.getAuthenticationManager()`.

6. The authentication manager delegates to a provider.
   - The default provider is `DaoAuthenticationProvider`.
   - It needs `UserDetailsService` and `PasswordEncoder`.

7. `DaoAuthenticationProvider` calls `CustomUserDetailService.loadUserByUsername(...)`.
   - In this project, `CustomUserDetailService` is marked with `@Service`.
   - Because it implements `UserDetailsService`, Spring Boot can auto-detect it.
   - The method receives the submitted login identifier in its `username` parameter.

8. `loadUserByUsername(...)` loads the user from the database.
   - Your implementation calls `userRepo.findFirstByEmail(username)`.
   - This means the login identifier is treated as the email.
   - If the user is not found, it throws an exception.

9. The returned `User` object must implement `UserDetails`.
   - In `User.java`, the entity implements `UserDetails`.
   - Spring now has a `UserDetails` instance containing the stored user state.

10. Spring Security reads the `UserDetails` methods.
    - It calls `getPassword()` to get the stored password hash.
    - It calls `getUsername()` to get the principal identity.
    - It calls `getAuthorities()` to get roles/permissions.
    - It calls `isEnabled()`, `isAccountNonLocked()`, `isAccountNonExpired()`, and `isCredentialsNonExpired()` to check account state.

11. Password matching happens internally.
    - Spring Security does not compare passwords inside `loadUserByUsername(...)`.
    - After loading the user, it calls `PasswordEncoder.matches(rawPassword, storedEncodedPassword)`.
    - In this project, the `PasswordEncoder` bean is `BCryptPasswordEncoder`.

12. If password matching succeeds, authentication is granted.
    - If matching fails, login is rejected.
    - If matching succeeds, the user is authenticated and stored in the `SecurityContext`.

## How `BCryptPasswordEncoder` works

- `BCryptPasswordEncoder` hashes passwords with BCrypt.
- The hash includes a random salt and cost factor.
- Each encoded password looks like a string such as:
  - `$2a$10$...`
  - `$2b$10$...`
- The raw password is submitted by the user during login.
- The database stores only the hashed string.
- During login, Spring compares the raw password to the stored hash.

Example:
- raw password: `password123`
- stored value: `$2a$10$eBIDj...Zq9Q7Ud0cZ3e`

Important:
- the encoded string changes every time you encode the same raw password because BCrypt salts the value.

## User creation and password hashing

In this project, `UserServiceImpl.createUser(...)` currently maps the DTO to a `User` and saves it directly.
That means the password is saved exactly as provided by the user, not hashed.

To make authentication work with `BCryptPasswordEncoder`, the password must be encoded before saving, for example:

```java
user.setPassword(passwordEncoder.encode(dto.getPassword()));
```

Otherwise the stored value is plain text and `BCryptPasswordEncoder.matches(rawPassword, storedHash)` will fail.

## Why field names do not matter

Spring Security does not care about your actual entity field names.
It only cares about the methods defined by `UserDetails`.

For example, if `User` stores the password in a field named `secretKey`, that is fine as long as:

```java
@Override
public String getPassword() {
    return this.secretKey;
}
```

So the method names are important, not the field names.

## What `getUsername()` and `getAuthorities()` mean here

- `getUsername()` returns the identity of the authenticated user.
  - In your `User` class, it returns `getEmail()`.
  - That means the authenticated principal is the email.
- `getAuthorities()` returns roles or permissions.
  - In your `User` class, it converts each `Role` into a `SimpleGrantedAuthority`.

In this project, your security configuration currently only requires authentication:

```java
.authorizeHttpRequests(auth -> auth
    .anyRequest().authenticated()
)
```

That means:
- any authenticated user can access the app
- role-based checks are not enforced yet
- `getAuthorities()` is still stored in the security context and can be used later

## What is `SecurityFilterChain`?

- `SecurityFilterChain` is the modern configuration style for Spring Security.
- It replaces the older `WebSecurityConfigurerAdapter` approach.
- It defines the sequence of filters that perform security checks on requests.
- In this project, it configures:
  - CSRF disabled
  - form login enabled
  - HTTP Basic enabled
  - `/api/posts` and `/api/posts/**` are permitted without authentication
  - all other requests must be authenticated

## Summary for a beginner

1. User sends a login request with email and password.
2. Spring Security filters intercept the request.
3. Credentials are passed to the authentication manager.
4. Spring calls your `UserDetailsService` to load user data.
5. Your repository finds the user by email.
6. The returned user object implements `UserDetails`.
7. Spring reads the stored password from `getPassword()`.
8. Spring compares the submitted password with the hashed password using `BCryptPasswordEncoder`.
9. If the password matches, authentication succeeds.
10. If not, authentication fails.

## JWT Authentication in this Project

This project uses **JWT (JSON Web Token)** authentication instead of traditional session-based authentication. This is a stateless approach.

### Key JWT Files

- `JwtTokenHelper.java` - Generates and validates JWT tokens
- `JwtAuthenticationFilter.java` - Intercepts requests and validates tokens
- `JwtAuthRequest.java` - Request body for login (username + password)
- `JwtAuthResponse.java` - Response body with the JWT token
- `JwtAuthenticationEntryPoint.java` - Handles authentication errors

### What is JWT?

A JWT token is a signed string that contains:
- **Header**: type and algorithm (HS512 in this project)
- **Payload**: claims like subject (email), issued time, expiration time
- **Signature**: HMAC-SHA512 signature using a secret key

Example: `eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ1c2VyQGV4YW1wbGUuY29tIiwiaWF0IjoxNjcwODc5MzAwLCJleHAiOjE2NzA4OTcwMDB9.signature...`

The token is **signed but not encrypted**, so anyone can read it but cannot modify it (signature will break).

---

## SCENARIO 1: First-Time User Login

### What Happens Step-by-Step

```
User submits login form
        ↓
POST /api/v1/auth/login with { username: "user@example.com", password: "myPassword" }
        ↓
AuthController.createToken() is called
        ↓
authenticate(username, password) is called
        ↓
UsernamePasswordAuthenticationToken is created (username=email, password=plaintext)
        ↓
authenticationManager.authenticate(token) is called
        ↓
Spring delegates to DaoAuthenticationProvider
        ↓
CustomUserDetailService.loadUserByUsername(email) is called
        ↓
UserRepo.findFirstByEmail(email) queries the database
        ↓
User entity is returned (must implement UserDetails)
        ↓
BCryptPasswordEncoder.matches(submittedPassword, storedHash) compares passwords
        ↓
If match succeeds → Authentication is successful
        ↓
UserDetails object is available in AuthController
        ↓
JwtTokenHelper.generateToken(userDetails) creates JWT token with:
  - Subject: email (extracted from userDetails.getUsername())
  - IssuedAt: current timestamp
  - Expiration: 5 hours from now (JWT_TOKEN_VALIDITY = 5*60*60)
  - Signature: HMAC-SHA512(header.payload, SECRET)
        ↓
JwtAuthResponse { token: "eyJhbGciOi..." } is sent back to client
        ↓
Client stores the token (usually in localStorage or sessionStorage)
```

### Why No SecurityContext is Persisted

In your `SecurityConfig.java`, you have:

```java
.sessionManagement(session -> session
    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
)
```

**STATELESS** means:
- Spring does NOT create a `HttpSession` on the server
- Spring does NOT store session data in memory or a database
- The token itself is the authentication proof
- Every request must include the token

### Why SecurityFilterChain and JwtAuthenticationFilter Are NOT Called During Login

During login, the filters **ARE** called, but they don't stop or validate the request. Here's why:

In your `SecurityConfig.java`:

```java
.authorizeHttpRequests(auth -> auth
    .requestMatchers("/api/v1/auth/*", "/api/v1/auth").permitAll()  // ← This line!
    .anyRequest().authenticated()
)
```

The `.permitAll()` on `/api/v1/auth/*` means:
- SecurityFilterChain **DOES** intercept the login request
- JwtAuthenticationFilter runs but finds no JWT token (user doesn't have one yet, they're logging in!)
- The filter chain sees: *"This endpoint needs no authentication"*
- Request is allowed through **without validation**
- Request reaches `AuthController.createToken()`

```
POST /api/v1/auth/login
        ↓
SecurityFilterChain applies all filters (including JwtAuthenticationFilter)
        ↓
JwtAuthenticationFilter looks for "Authorization: Bearer <token>" header
        ↓
NO header found (user doesn't have a token yet!)
        ↓
JwtAuthenticationFilter does nothing, passes request forward
        ↓
AuthorizeHttpRequests filter checks: Is /api/v1/auth/login in permitAll list?
        ↓
YES! → Allow request through WITHOUT requiring authentication
        ↓
Request reaches AuthController.createToken()
        ↓
AuthController MANUALLY calls authenticationManager.authenticate() to validate credentials
        ↓
If credentials valid → Generate JWT token
        ↓
Return token to client
```

**Comparison with Protected Endpoints:**

| Scenario | Flow |
|----------|------|
| **Login Request** | SecurityFilterChain → permitAll rule applies → Request allowed to controller → Manual authentication in controller |
| **Access /api/v1/posts** | SecurityFilterChain → JwtAuthenticationFilter validates token → Sets SecurityContext → Request allowed to controller |

**Key Takeaway:**

- Login endpoint is **explicitly whitelisted** with `.permitAll()`
- JwtAuthenticationFilter is skipped because the endpoint doesn't require authentication
- The controller itself performs manual authentication using `authenticationManager.authenticate()`
- This is intentional design: the login endpoint is the **entry point** that issues tokens

### Important: Password Hashing Issue

Currently, `UserServiceImpl.createUser()` saves the password as plain text. To make login work correctly, the password must be hashed when the user is created:

```java
user.setPassword(passwordEncoder.encode(dto.getPassword()));
```

Without this, `BCryptPasswordEncoder.matches()` will always fail during login.

---

## SCENARIO 2: User Logs In Again (Already Has a Token)

### What Happens

```
User sends another login request with credentials
        ↓
POST /api/v1/auth/login with { username: "user@example.com", password: "myPassword" }
        ↓
Identical to SCENARIO 1: authenticate(), load user, compare password, generate JWT
        ↓
A NEW JWT token is generated (same email, new timestamp, new expiration)
        ↓
New token is sent back to client
        ↓
Old token is STILL VALID (until it expires in 5 hours) - there's no way to revoke it
```

### Key Difference from Sessions

- **Session-based**: Logging in again overwrites the session, old session is invalid
- **JWT-based**: Both tokens are valid simultaneously until they expire
- There's no conflict or issue with having multiple valid tokens

### Practical Impact

If a user logs in multiple times (different devices, browser tabs, etc.), they get multiple tokens. Each token works independently. This is actually useful for multi-device scenarios.

If you want to prevent this (e.g., "only one login per user"), you would need:
- A **token blacklist** (database of revoked tokens)
- Or a **token refresh mechanism** that invalidates old tokens
- These are **not implemented** in your current project

---

## SCENARIO 3: User Accesses Another API After Login

### Request Flow

```
User is now logged in with a JWT token
        ↓
User makes a request to /api/v1/posts (or any protected endpoint)
        ↓
Request includes header: Authorization: Bearer eyJhbGciOi...
        ↓
SecurityFilterChain is applied to the request
        ↓
JwtAuthenticationFilter.doFilterInternal() is called (added BEFORE UsernamePasswordAuthenticationFilter)
        ↓
Filter extracts the "Authorization" header
        ↓
Checks if header starts with "Bearer "
        ↓
Extracts token by removing "Bearer " prefix: token = requestTokenHeader.substring(7)
        ↓
JwtTokenHelper.getUsernameFromToken(token) parses the token signature and extracts the subject (email)
        ↓
Checks if username is not null and SecurityContext has no existing Authentication
        ↓
UserDetailsService.loadUserByUsername(username) loads the user from database
        ↓
JwtTokenHelper.validateToken(token, userDetails) performs two checks:
  1. Username in token matches userDetails.getUsername() ✓
  2. Token not expired (expiration date is in the future) ✓
        ↓
If both checks pass:
  - Creates UsernamePasswordAuthenticationToken with:
    * principal: userDetails
    * credentials: null (not needed, already authenticated)
    * authorities: userDetails.getAuthorities() (user's roles)
  - Sets authentication details from the request
  - Sets this token in SecurityContextHolder.getContext()
        ↓
Request proceeds to the controller
        ↓
Controller can access Authentication from SecurityContext:
  @Autowired private Authentication auth;  or  SecurityContextHolder.getContext().getAuthentication()
        ↓
Controller processes the request and returns response
        ↓
SecurityContext is CLEARED (because STATELESS mode clears context after each request)
        ↓
Response is sent to client
```

### Token Validation Errors

If any validation fails, the request continues to the next filter. If no filter sets authentication:

```
Request reaches the endpoint
        ↓
@Autowired private Authentication auth → null or throws exception
        ↓
Or endpoint has @PreAuthorize → throws AccessDeniedException
        ↓
JwtAuthenticationEntryPoint.commence() is triggered
        ↓
Sends 401 Unauthorized: "Access Denied"
        ↓
Response is sent to client
```

### Why Load User From Database Again?

The token contains only the username (email). To get the user's **roles/authorities**, we must:
1. Load the user from database
2. Call `getAuthorities()` to get their roles
3. Create Authentication object with these authorities

This ensures authorization rules like `@PreAuthorize("hasRole('ADMIN')")` work correctly.

---

## Comparison: Traditional Session vs JWT

| Feature | Session-Based | JWT (This Project) |
|---------|----------------|-------------------|
| **Storage** | Server (memory/database) | Client (browser) |
| **Statefulness** | Stateful | Stateless |
| **Logout** | Delete session immediately | Cannot logout (token valid until expiration) |
| **Multiple Logins** | Overwrites old session | Multiple tokens valid simultaneously |
| **Scalability** | Hard (need session replication) | Easy (no server state) |
| **Token Revocation** | Immediate | Requires blacklist |
| **Bandwidth** | Small (just session ID) | Larger (full token each request) |

---

## Flow Diagram Summary

```
FIRST LOGIN:
credentials → AuthController → authenticate → Database → GenerateToken → JwtResponse

SECOND LOGIN:
credentials → AuthController → authenticate → Database → GenerateToken → JwtResponse (new token, old still valid)

ACCESS API:
Authorization Header with Token → JwtAuthenticationFilter → ValidateToken → LoadUser → SetSecurityContext → Controller → Response
```

---

## Common Issues & Solutions

### Issue 1: Login Works but Other APIs Return 401

**Cause**: Client not sending token in Authorization header

**Solution**: Ensure your client includes:
```
Authorization: Bearer <your-jwt-token>
```

### Issue 2: Login Fails Immediately

**Cause**: Password not hashed when user was created

**Solution**: Encode password before saving user:
```java
user.setPassword(passwordEncoder.encode(dto.getPassword()));
userRepo.save(user);
```

### Issue 3: Token Expires and User Can't Access API

**Current behavior**: User gets 401 error

**Future enhancement**: Implement a refresh token mechanism to get a new access token without re-logging in.

### Issue 4: User Logs Out but Token Still Works

**Current behavior**: Token remains valid until expiration (5 hours)

**Future enhancement**: Implement a token blacklist to invalidate tokens immediately upon logout.

---

## Next Steps for This Project

To make the JWT system more production-ready:

1. **Fix password hashing** - Use `passwordEncoder.encode()` in UserServiceImpl
2. **Add token refresh** - Create a `/refresh` endpoint to get a new token before expiration
3. **Add logout** - Implement token blacklist or use short-lived tokens with refresh tokens
4. **Add role-based authorization** - Use `@PreAuthorize("hasRole('ADMIN')")` on endpoints
5. **Add token claims** - Store user ID, roles, permissions in the token itself to avoid database queries

---

This summary should help you understand JWT authentication and how your project's security flow works at each step.