package org.coursera.capstone.auth;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;

public class User implements UserDetails {
    private static final long serialVersionUID = 8140371140830590386L;

    public enum UserAuthority {
        DOCTOR("doctor"), PATIENT("patient");

        private final String name;

        private UserAuthority(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public static String[] userAuthoritesToStrings(UserAuthority... userAuthorities) {
            List<String> result = new ArrayList<String>();
            for (UserAuthority userAuthority : userAuthorities) {
                result.add(userAuthority.getName());
            }
            return result.toArray(new String[userAuthorities.length]);
        }
    }

    public static UserDetails create(String username, String password, UserAuthority... authorities) {
        return new User(username, password, authorities);
    }

    private final Collection<GrantedAuthority> authorities;
    private final String password;
    private final String username;

    private User(String username, String password) {
        this(username, password, new UserAuthority[0]);
    }

    private User(String username, String password, UserAuthority... authorities) {
        this.username = username;
        this.password = password;
        this.authorities = AuthorityUtils.createAuthorityList(UserAuthority.userAuthoritesToStrings(authorities));
    }

    public Collection<GrantedAuthority> getAuthorities() {
        return authorities;
    }

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}
