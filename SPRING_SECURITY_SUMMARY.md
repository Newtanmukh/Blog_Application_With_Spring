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

## Next steps for this project

To make `getAuthorities()` actually matter, you can add role-based authorization rules, for example:

- `.hasRole("ADMIN")`
- `@PreAuthorize("hasAuthority('ROLE_ADMIN')")`

This will make Spring Security enforce permissions based on the roles returned by `getAuthorities()`.

---

This summary should help a newcomer understand the basic Spring Security flow used in this project and how your code pieces fit together.