package se.lnu.thesis_mangment.configurations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import se.lnu.thesis_mangment.services.UserDetailsServiceImpl;
import se.lnu.thesis_mangment.services.UserServices;

import static se.lnu.thesis_mangment.configurations.SecurityConstants.SIGN_IN;


/**
 * The type Web security.
 */
@EnableWebSecurity
public class WebSecurity extends WebSecurityConfigurerAdapter
{

    private UserDetailsServiceImpl userDetailsService;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private UserServices userServices;


    /**
     * Instantiates a new Web security.
     *
     * @param userDetailsService    the user details service
     * @param bCryptPasswordEncoder the b crypt password encoder
     */
    public WebSecurity(UserDetailsServiceImpl userDetailsService, BCryptPasswordEncoder bCryptPasswordEncoder)
    {
        this.userDetailsService = userDetailsService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception
    {
        http.cors().and().csrf().disable().authorizeRequests().antMatchers(HttpMethod.POST, SIGN_IN).permitAll().antMatchers(HttpMethod.POST, "/").permitAll().antMatchers(HttpMethod.GET, "/api/document/download").permitAll().antMatchers(HttpMethod.GET, "/api/supervisor-confirmation/get")

                .permitAll().antMatchers(HttpMethod.GET, "/api/feedbacks/get").permitAll()

                .anyRequest().authenticated().and().addFilter(new JWTAuthenticationFilter(authenticationManager(), userServices)).addFilter(new JWTAuthorizationFilter(authenticationManager()))
                // this disables session creation on Spring Security
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception
    {
        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder);
    }

    /**
     * Cors configuration source cors configuration source.
     *
     * @return the cors configuration source
     */
    @Bean
    CorsConfigurationSource corsConfigurationSource()
    {
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", new CorsConfiguration().applyPermitDefaultValues());
        return source;
    }
}