package com.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.filter.OAuth2ClientAuthenticationProcessingFilter;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeResourceDetails;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;
import org.springframework.security.oauth2.provider.token.RemoteTokenServices;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.Filter;

@SpringBootApplication
@EnableOAuth2Client
//@EnableOAuth2Sso
@EnableZuulProxy
@EnableEurekaClient
public class WebappApplication extends WebSecurityConfigurerAdapter {

   public static void main(String[] args) {

      SpringApplication.run(WebappApplication.class, args);
   }
   @Autowired
   private OAuth2ClientContext oauth2ClientContext;


   @Override
   protected void configure(HttpSecurity http) throws Exception {
      // @formatter:off
      http.antMatcher("/**")
            .authorizeRequests()
               .antMatchers("/resources/**").permitAll()
               .antMatchers("/**").authenticated()
            .and().exceptionHandling().authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint("/login"))
//            .and().logout().logoutSuccessUrl("/").permitAll()
            .and().addFilterBefore(ssoFilter(), BasicAuthenticationFilter.class);
      // @formatter:on
   }

   private Filter ssoFilter() {
      OAuth2ClientAuthenticationProcessingFilter facebookFilter = new OAuth2ClientAuthenticationProcessingFilter("/login");
      OAuth2RestTemplate facebookTemplate = new OAuth2RestTemplate(customAuthrozationResource(), oauth2ClientContext);
      facebookFilter.setRestTemplate(facebookTemplate);
      facebookFilter.setTokenServices(remoteTokenServices());
      return facebookFilter;
   }

   @Bean
   public OAuth2RestTemplate oAuth2RestTemplate() {
      return new OAuth2RestTemplate(customAuthrozationResource(), oauth2ClientContext);
   }

   private RemoteTokenServices remoteTokenServices() {
      RemoteTokenServices remoteTokenServices = new RemoteTokenServices();
      remoteTokenServices.setClientId("acme");
      remoteTokenServices.setClientSecret("acmesecret");
//      remoteTokenServices.setCheckTokenEndpointUrl("http://127.0.0.1:8081/oauth/check_token");
      remoteTokenServices.setCheckTokenEndpointUrl("http://oauth-service:8081/oauth/check_token");
      return remoteTokenServices;
   }

   @Bean
   @ConfigurationProperties("security.oauth2.client")
   OAuth2ProtectedResourceDetails customAuthrozationResource() {
      return new AuthorizationCodeResourceDetails();
   }

}
