package cz.upce.inpia.f1app.services;


import cz.upce.inpia.f1app.config.JwtTokenUtil;
import cz.upce.inpia.f1app.dto.*;
import cz.upce.inpia.f1app.entity.User;
import cz.upce.inpia.f1app.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Objects;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Qualifier("userService")
    @Autowired
    private UserDetailsService jwtInMemoryUserDetailsService;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByName(username);
        if(user != null){
            return new org.springframework.security.core.userdetails.User(user.getName(),user.getPassword(),
                    Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));
        }
        throw new UsernameNotFoundException("Username not found.");
    }

    private void authenticate(String username, String password) throws Exception {
        Objects.requireNonNull(username);
        Objects.requireNonNull(password);

        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }

    public ResponseEntity<UserLoginRetDTO> createLogin(UserLoginDTO userLoginDTO) throws Exception {
        authenticate(userLoginDTO.getName(), userLoginDTO.getPassword());
        final UserDetails userDetails = jwtInMemoryUserDetailsService.loadUserByUsername(userLoginDTO.getName());

        String token = jwtTokenUtil.generateToken(userDetails);
        token = "Bearer "+token;

        Integer isAdmin = userRepository.isAdmin(userRepository.findByName(userLoginDTO.getName()).getId());

        UserLoginRetDTO userLoginRetDTO = new UserLoginRetDTO();
        userLoginRetDTO.setToken(token);
        userLoginRetDTO.setAdmin(isAdmin == 1);

        return ResponseEntity.ok(userLoginRetDTO);
    }


    public ResponseEntity<?> registerUserService(UserDTO userDTO) {
        if(userRepository.findByName(userDTO.getName()) != null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("username already exists!");
        }

        if(userRepository.findByEmail(userDTO.getEmail()) != null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email already exists!");

        }

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(userDTO.getPassword());

        User user = new User();
        user.setName(userDTO.getName());
        user.setEmail(userDTO.getEmail());
        user.setPassword(encodedPassword);

        userRepository.save(user);

        return ResponseEntity.ok(userDTO);
    }


    public ResponseEntity<?> changePass(ChangePassDTO changePassDTO) {
        if(!Objects.equals(changePassDTO.getNewPass(), changePassDTO.getNewPass2())){

            return new ResponseEntity<>("Passwords are not same!", HttpStatus.BAD_REQUEST);
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User loggedUser = userRepository.findByName(authentication.getName());

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        if(passwordEncoder.matches(changePassDTO.getOldPass(), loggedUser.getPassword())){

            String encodedNewPass = passwordEncoder.encode(changePassDTO.getNewPass());

            loggedUser.setPassword(encodedNewPass);
            userRepository.save(loggedUser);
            return ResponseEntity.ok("");

        }else {
            return new ResponseEntity<>("Old password is incorrect", HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<?> resetPassService(String email) throws MessagingException {
        User user = userRepository.findByEmail(email);

        if(!user.getEmail().equals(email)){
            return new ResponseEntity<>("Neznámý email.", HttpStatus.BAD_REQUEST);
        }

        int code = (int) ((Math.random() * (9999 - 1)) + 1);
        String recoveryCode = String.format("%04d", code);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.MINUTE, 10);

        user.setRecoveryCode(recoveryCode);
        user.setRecoveryCodeExp(calendar.getTime());

        userRepository.save(user);
        EmailService.sendMail(email,recoveryCode);

        return new ResponseEntity<>("Activation code send. ", HttpStatus.OK);
    }


    public ResponseEntity<?> activatePass(ResetPassDTO resetPassDTO) {
        User user = userRepository.findByEmail(resetPassDTO.getEmail());

        if(Objects.equals(user.getRecoveryCode(), resetPassDTO.getActivationCode()) &&
                user.getRecoveryCodeExp().after(new Date())){

            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            String encodedPassword = passwordEncoder.encode(resetPassDTO.getPassword());

            user.setPassword(encodedPassword);

            userRepository.save(user);

            return new ResponseEntity<>("Password has ben changed.", HttpStatus.OK);

        } else
        {
            return new ResponseEntity<>("Code is incorrect or no longer valid.", HttpStatus.BAD_REQUEST);
        }
    }




}
