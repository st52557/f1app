package cz.upce.inpia.f1app.controller;


import cz.upce.inpia.f1app.dto.*;
import cz.upce.inpia.f1app.repository.UserRepository;
import cz.upce.inpia.f1app.services.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;

@RestController(value = "/user")
@Api( tags = "users")
@CrossOrigin
public class UserController {

    private final UserRepository userRepository;
    private final UserService userService;

    public UserController(UserService userService, UserRepository userRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
    }


    @ApiOperation(value = "This method is used to login user.")
    @PostMapping(value = "/user/login")
    public ResponseEntity<UserLoginRetDTO> createAuthenticationToken(@RequestBody UserLoginDTO userLoginDTO) throws Exception {

        return userService.createLogin(userLoginDTO);
    }

    @ApiOperation(value = "This method is used to register new user.")
    @PostMapping(value = "/user/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody UserDTO userDTO) {

        return userService.registerUserService(userDTO);
    }

    @ApiOperation(value = "This method is used to change password.")
    @PutMapping(value = "/user/changePass")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> changePassword(ChangePassDTO changePassDTO) {

        return userService.changePass(changePassDTO);
    }

    @ApiOperation(value = "This method is used to get code for resetting password.")
    @PostMapping(value = "/user/resetPassword")
    public ResponseEntity<?> resetPassword(String email) throws IOException, javax.mail.MessagingException {

        return userService.resetPassService(email);
    }

    @ApiOperation(value = "This method is used to set new password.")
    @PostMapping(value = "/user/activateNewPassword")
    public ResponseEntity<?> activateNewPassword(ResetPassDTO resetPassDTO){

        return userService.activatePass(resetPassDTO);


    }

}
