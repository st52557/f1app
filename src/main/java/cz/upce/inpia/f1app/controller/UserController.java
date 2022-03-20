package cz.upce.inpia.f1app.controller;


import cz.upce.inpia.f1app.config.JwtTokenUtil;
import cz.upce.inpia.f1app.dto.ChangePassDTO;
import cz.upce.inpia.f1app.dto.ResetPassDTO;
import cz.upce.inpia.f1app.dto.UserDTO;
import cz.upce.inpia.f1app.dto.UserLoginDTO;
import cz.upce.inpia.f1app.entity.User;
import cz.upce.inpia.f1app.jwt.JwtResponse;
import cz.upce.inpia.f1app.repository.UserRepository;
import cz.upce.inpia.f1app.services.EmailService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

@RestController(value = "/user")
@Api( tags = "users")
@CrossOrigin
public class UserController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Qualifier("userService")
    @Autowired
    private UserDetailsService jwtInMemoryUserDetailsService;

    @ApiOperation(value = "This method is used to login user.")
    @RequestMapping(value = "/user/login", method = RequestMethod.POST)
    public ResponseEntity<?> createAuthenticationToken(@RequestBody UserLoginDTO UserLoginDTO) throws Exception {

        authenticate(UserLoginDTO.getName(), UserLoginDTO.getPassword());
        final UserDetails userDetails = jwtInMemoryUserDetailsService.loadUserByUsername(UserLoginDTO.getName());

        String token = jwtTokenUtil.generateToken(userDetails);
        token = "Bearer "+token;
        return ResponseEntity.ok(new JwtResponse(token));
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
    @ApiOperation(value = "This method is used to register new user.")
    @PostMapping(value = "/user/register")
    public ResponseEntity<?> registerUser(@RequestBody UserDTO userDTO) {

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

        return ResponseEntity.ok(user);
    }

    @ApiOperation(value = "This method is used to change password.")
    @PutMapping(value = "/user/changePass")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> changePassword(ChangePassDTO changePassDTO) {

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

    @ApiOperation(value = "This method is used to get code for resetting password.")
    @PostMapping(value = "/user/resetPassword")
    public ResponseEntity<?> resetPassword(String email) throws MessagingException, IOException, javax.mail.MessagingException {

        User user = userRepository.findByEmail(email);

        if(!user.getEmail().equals(email)){
            return new ResponseEntity<>("Neznámý email.",HttpStatus.BAD_REQUEST);
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

        return new ResponseEntity<>("Activation code send. ",HttpStatus.OK);
    }

    @ApiOperation(value = "This method is used to set new password.")
    @PostMapping(value = "/user/activateNewPassword")
    public ResponseEntity<?> activateNewPassword(ResetPassDTO resetPassDTO) throws MessagingException, IOException {

        User user = userRepository.findByEmail(resetPassDTO.getEmail());

        if(Objects.equals(user.getRecoveryCode(), resetPassDTO.getActivationCode()) &&
                user.getRecoveryCodeExp().after(new Date())){

            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            String encodedPassword = passwordEncoder.encode(resetPassDTO.getPassword());

            user.setPassword(encodedPassword);

            userRepository.save(user);

            return new ResponseEntity<>("Password has ben changed.",HttpStatus.OK);

        } else
        {
            return  new ResponseEntity<>("Code is incorrect or no longer valid.",HttpStatus.BAD_REQUEST);
        }


    }

}
