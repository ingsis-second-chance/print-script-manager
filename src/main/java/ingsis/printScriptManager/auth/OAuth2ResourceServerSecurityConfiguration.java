package ingsis.printScriptManager.auth;

import static org.springframework.security.config.Customizer.withDefaults;

import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.*;

@Configuration
@Profile("!test")
public class OAuth2ResourceServerSecurityConfiguration {

  @Value("${auth0.audience}")
  private String audience;

  @Value("${spring.security.oauth2.resourceserver.jwt.issuer-uri}")
  private String issuer;

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http.csrf(AbstractHttpConfigurer::disable)
        .cors(withDefaults())
        .authorizeHttpRequests(
            auth ->
                auth
                    // públicos
                    .requestMatchers("/", "/actuator/**")
                    .permitAll()
                    .requestMatchers(HttpMethod.GET, "/ping")
                    .permitAll() // smoke
                    // protegidos (ejemplo)
                    .requestMatchers(HttpMethod.GET, "/secure/**")
                    .hasAuthority("SCOPE_read:snippets")
                    .anyRequest()
                    .authenticated())
        .oauth2ResourceServer(
            oauth -> oauth.jwt(jwt -> jwt.jwtAuthenticationConverter(new JwtAuthConverter())));
    return http.build();
  }

  @Bean
  public JwtDecoder jwtDecoder() {
    NimbusJwtDecoder dec = NimbusJwtDecoder.withIssuerLocation(issuer).build();
    OAuth2TokenValidator<Jwt> withIssuer = JwtValidators.createDefaultWithIssuer(issuer);
    OAuth2TokenValidator<Jwt> withAudience = new AudienceValidator(audience);
    dec.setJwtValidator(new DelegatingOAuth2TokenValidator<>(withIssuer, withAudience));
    return dec;
  }

  @Bean
  public CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration cfg = new CorsConfiguration();
    cfg.setAllowedOrigins(List.of("http://localhost:5173")); // ajustá frontends
    cfg.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
    cfg.setAllowedHeaders(List.of("Authorization", "Content-Type"));
    cfg.setAllowCredentials(true);
    UrlBasedCorsConfigurationSource src = new UrlBasedCorsConfigurationSource();
    src.registerCorsConfiguration("/**", cfg);
    return src;
  }
}
