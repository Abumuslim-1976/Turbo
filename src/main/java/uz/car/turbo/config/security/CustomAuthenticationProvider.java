package uz.car.turbo.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import uz.car.turbo.domain.User;
import uz.car.turbo.repository.UserRepository;

import java.util.Optional;


@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    UserRepository userRepository;

    @Transactional
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String userName = authentication.getName();
        String password = authentication.getCredentials().toString();
        Optional<User> _user = userRepository.findByUsername(userName);
        if (_user.isPresent()) {
            User user = _user.get();
            userRepository.save(user);
        } else {
            User user = _user.get();
            if (user == null) {
                throw new BadCredentialsException("Введен неверный логин или пароль");
            }
            if (!user.isEnabled()) {
                throw new LockedException("BLOCK_ACCOUNT");
            }
            if (StringUtils.isEmpty(authentication.getCredentials()))
                throw new BadCredentialsException("Введен неверный логин или пароль");
            boolean matches = passwordEncoder.matches(password, user.getPassword());
            if (!matches) {
                throw new BadCredentialsException("Введен неверный логин или пароль");
            }
        }
        return new UsernamePasswordAuthenticationToken(userName, password, _user.get().getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return false;
    }

}
