package org.coursera.capstone.auth;

import java.util.ArrayList;
import java.util.List;

import org.coursera.capstone.InitialTestData;
import org.coursera.capstone.entity.Doctor;
import org.coursera.capstone.entity.Patient;
import org.coursera.capstone.repository.DoctorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.config.annotation.builders.InMemoryClientDetailsServiceBuilder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

/**
 * Configure this web application to use OAuth 2.0.
 *
 * The resource server is located at "/video", and can be accessed only by retrieving a token from "/oauth/token" using
 * the Password Grant Flow as specified by OAuth 2.0.
 * 
 * Most of this code can be reused in other applications. The key methods that would definitely need to be changed are:
 * 
 * ResourceServer.configure(...) - update this method to apply the appropriate set of scope requirements on client
 * requests
 * 
 * OAuth2Config constructor - update this constructor to create a "real" (not hard-coded) UserDetailsService and
 * ClientDetailsService for authentication. The current implementation should never be used in any type of production
 * environment as these hard-coded credentials are highly insecure.
 * 
 * OAuth2SecurityConfiguration.containerCustomizer(...) - update this method to use a real keystore and certificate
 * signed by a CA. This current version is highly insecure.
 * 
 */
@Configuration
public class OAuth2SecurityConfiguration {

    // This first section of the configuration just makes sure that Spring
    // Security picks
    // up the UserDetailsService that we create below.
    @Configuration
    @EnableWebSecurity
    protected static class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

        @Autowired
        private UserDetailsService userDetailsService;

        @Autowired
        protected void registerAuthentication(final AuthenticationManagerBuilder auth) throws Exception {
            auth.userDetailsService(userDetailsService);
        }
    }

    /**
     * This method is used to configure who is allowed to access which parts of our resource server (i.e. the "/video"
     * endpoint)
     */
    @Configuration
    @EnableResourceServer
    protected static class ResourceServer extends ResourceServerConfigurerAdapter {

        // This method configures the OAuth scopes required by clients to access
        // all of the paths in the video service.
        @Override
        public void configure(HttpSecurity http) throws Exception {

            http.csrf().disable();

            http.authorizeRequests().antMatchers("/oauth/token").anonymous();

            // Only doctors should have access to doctor api, and medication put
            http.authorizeRequests().antMatchers(HttpMethod.GET, "/doctor/**")
                    .hasRole(User.UserAuthority.DOCTOR.getRole());
            http.authorizeRequests().antMatchers(HttpMethod.PUT, "/medication/**")
                    .hasRole(User.UserAuthority.DOCTOR.getRole());
            // Only patients can submit checkins
            http.authorizeRequests().antMatchers(HttpMethod.POST, "/checkin/**")
                    .hasRole(User.UserAuthority.PATIENT.getRole());
            // Require all other GET requests to have client "read" scope
            http.authorizeRequests().antMatchers(HttpMethod.GET, "/**").access("#oauth2.hasScope('read')");
        }

    }

    /**
     * This class is used to configure how our authorization server (the "/oauth/token" endpoint) validates client
     * credentials.
     */
    @Configuration
    @EnableAuthorizationServer
    @Order(Ordered.LOWEST_PRECEDENCE - 100)
    protected static class OAuth2Config extends AuthorizationServerConfigurerAdapter {

        // Delegate the processing of Authentication requests to the framework
        @Autowired
        private AuthenticationManager authenticationManager;
        @Autowired
        private DoctorRepository doctorRepo;

        // A data structure used to store both a ClientDetailsService and a
        // UserDetailsService
        private ClientAndUserDetailsService combinedService;

        /**
         * 
         * This constructor is used to setup the clients and users that will be able to login to the system. This is a
         * VERY insecure setup that is using hard-coded lists of clients / users / passwords and should never be used
         * for anything other than local testing on a machine that is not accessible via the Internet. Even if you use
         * this code for testing, at the bare minimum, you should consider changing the passwords listed below and
         * updating the VideoSvcClientApiTest.
         * 
         * @param auth
         * @throws Exception
         */
        public OAuth2Config() throws Exception {

            // If you were going to reuse this class in another
            // application, this is one of the key sections that you
            // would want to change

            // Create a service that has the credentials for all our clients
            ClientDetailsService csvc = new InMemoryClientDetailsServiceBuilder()
                    // Create a mobile client that has access to patient and doctor
                    .withClient("mobile").authorizedGrantTypes("password").scopes("read")
                    .resourceIds("patient", "doctor").accessTokenValiditySeconds(3600).and().build();

            // Use the doctors and patients that are created in the initial test data
            List<Doctor> doctors = InitialTestData.createTestDoctorAndPatients(InitialTestData.createPainMedications());
            List<UserDetails> users = new ArrayList<>();
            for (Doctor d : doctors) {
                users.add(User.create(d.getUsername(), "pass", User.UserAuthority.DOCTOR));
                for (Patient p : d.getPatients()) {
                    users.add(User.create(p.getUsername(), "pass", User.UserAuthority.PATIENT));
                }
            }
            UserDetailsService svc = new InMemoryUserDetailsManager(users);

            // Since clients have to use BASIC authentication with the client's
            // id/secret,
            // when sending a request for a password grant, we make each client
            // a user
            // as well. When the BASIC authentication information is pulled from
            // the
            // request, this combined UserDetailsService will authenticate that
            // the
            // client is a valid "user".
            combinedService = new ClientAndUserDetailsService(csvc, svc);
        }

        /**
         * Return the list of trusted client information to anyone who asks for it.
         */
        @Bean
        public ClientDetailsService clientDetailsService() throws Exception {
            return combinedService;
        }

        /**
         * Return all of our user information to anyone in the framework who requests it.
         */
        @Bean
        public UserDetailsService userDetailsService() {
            return combinedService;
        }

        /**
         * This method tells our AuthorizationServerConfigurerAdapter to use the delegated AuthenticationManager to
         * process authentication requests.
         */
        @Override
        public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
            endpoints.authenticationManager(authenticationManager);
        }

        /**
         * This method tells the AuthorizationServerConfigurerAdapter to use our self-defined client details service to
         * authenticate clients with.
         */
        @Override
        public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
            clients.withClientDetails(clientDetailsService());
        }

    }

    // This version uses the Tomcat web container and configures it to
    // support HTTPS. The code below performs the configuration of Tomcat
    // for HTTPS. Each web container has a different API for configuring
    // HTTPS.
    //
    // The app now requires that you pass the location of the keystore and
    // the password for your private key that you would like to setup HTTPS
    // with. In Eclipse, you can set these options by going to:
    // 1. Run->Run Configurations
    // 2. Under Java Applications, select your run configuration for this app
    // 3. Open the Arguments tab
    // 4. In VM Arguments, provide the following information to use the
    // default keystore provided with the sample code:
    //
    // -Dkeystore.file=src/main/resources/private/keystore
    // -Dkeystore.pass=changeit
    //
    // 5. Note, this keystore is highly insecure! If you want more securtiy, you
    // should obtain a real SSL certificate:
    //
    // http://tomcat.apache.org/tomcat-7.0-doc/ssl-howto.html
    //
    /*@Bean
    EmbeddedServletContainerCustomizer containerCustomizer(
            @Value("${keystore.file:src/main/resources/private/keystore}") String keystoreFile,
            @Value("${keystore.pass:changeit}") final String keystorePass) throws Exception {

        // If you were going to reuse this class in another
        // application, this is one of the key sections that you
        // would want to change

        final String absoluteKeystoreFile = new File(keystoreFile).getAbsolutePath();

        return new EmbeddedServletContainerCustomizer() {

            @Override
            public void customize(ConfigurableEmbeddedServletContainer container) {
                TomcatEmbeddedServletContainerFactory tomcat = (TomcatEmbeddedServletContainerFactory) container;
                tomcat.addConnectorCustomizers(new TomcatConnectorCustomizer() {
                    @Override
                    public void customize(Connector connector) {
                        connector.setPort(8443);
                        connector.setSecure(true);
                        connector.setScheme("https");

                        Http11NioProtocol proto = (Http11NioProtocol) connector.getProtocolHandler();
                        proto.setSSLEnabled(true);
                        proto.setKeystoreFile(absoluteKeystoreFile);
                        proto.setKeystorePass(keystorePass);
                        proto.setKeystoreType("JKS");
                        proto.setKeyAlias("tomcat");
                    }
                });

            }
        };
    }*/

}
