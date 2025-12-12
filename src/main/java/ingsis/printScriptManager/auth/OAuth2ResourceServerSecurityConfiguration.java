package ingsis.printScriptManager.auth;

import static org.springframework.security.config.Customizer.withDefaults;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.*;

@Configuration
@EnableWebSecurity
@Profile("!test")
public class OAuth2ResourceServerSecurityConfiguration {

  @Value("${auth0.audience}")
  private String audience;

  @Value("${spring.security.oauth2.resourceserver.jwt.issuer-uri}")
  private String issuer;

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http.authorizeHttpRequests(
            auth ->
                auth.requestMatchers("/")
                    .permitAll()
                    .requestMatchers(HttpMethod.POST, "/runner/**")
                    .hasAuthority("SCOPE_write:snippets")
                    .requestMatchers(HttpMethod.GET, "/swagger-ui")
                    .permitAll()
                    .requestMatchers(HttpMethod.GET, "/swagger-ui/*")
                    .permitAll()
                    .requestMatchers(HttpMethod.GET, "/v3/api-docs")
                    .permitAll()
                    .requestMatchers(HttpMethod.GET, "/v3/api-docs/*")
                    .permitAll()
                    .anyRequest()
                    .authenticated())
        .oauth2ResourceServer(oauth2 -> oauth2.jwt(withDefaults()))
        .csrf(AbstractHttpConfigurer::disable);
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
}
