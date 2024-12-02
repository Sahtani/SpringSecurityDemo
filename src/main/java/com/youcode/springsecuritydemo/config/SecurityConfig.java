package com.youcode.springsecuritydemo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class SecurityConfig {

    // Bean pour la gestion des utilisateurs en mémoire
    @Bean
    public InMemoryUserDetailsManager userDetailsService() {
        // Création des utilisateurs en mémoire avec des rôles et des mots de passe encodés
        UserDetails user = User.builder()
                .username("user")
                .password(passwordEncoder().encode("password"))  // Le mot de passe est encodé
                .roles("USER")  // Rôle "USER"
                .build();

        UserDetails admin = User.builder()
                .username("admin")
                .password(passwordEncoder().encode("admin"))  // Le mot de passe est encodé
                .roles("ADMIN")  // Rôle "ADMIN"
                .build();

        // Retourne les utilisateurs en mémoire
        return new InMemoryUserDetailsManager(user, admin);
    }

    // Bean pour le PasswordEncoder (utilise BCryptPasswordEncoder pour hacher les mots de passe)
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();  // Utilise BCrypt pour le hachage des mots de passe
    }

    // Configuration des règles de sécurité HTTP
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())  // Désactive CSRF pour les API REST (si nécessaire)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/admin/**").hasRole("ADMIN")  // Accès réservé aux administrateurs
                        .requestMatchers("/user/**").hasRole("USER")    // Accès réservé aux utilisateurs
                        .anyRequest().authenticated()                   // Toute autre demande nécessite une authentification
                )
                .formLogin(form -> form
                        .permitAll()  // Permet à tous d'accéder à la page de connexion
                )
                .logout(logout -> logout
                        .permitAll()  // Permet à tout le monde de se déconnecter
                );

        return http.build();
    }

    // Bean pour le AuthenticationManager avec DaoAuthenticationProvider
    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        // Utilisation de AuthenticationManagerBuilder pour la configuration de l'authentification
        AuthenticationManagerBuilder authenticationManagerBuilder =
                http.getSharedObject(AuthenticationManagerBuilder.class);

        // Configurer DaoAuthenticationProvider pour l'authentification basée sur le service en mémoire
        authenticationManagerBuilder.authenticationProvider(daoAuthenticationProvider());

        // Retourner le AuthenticationManager configuré
        return authenticationManagerBuilder.build();
    }

    // Bean pour le DaoAuthenticationProvider
    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService());  // Utilise le service en mémoire pour récupérer l'utilisateur
        provider.setPasswordEncoder(passwordEncoder());        // Utilise BCrypt pour le hachage des mots de passe
        return provider;
    }
}
