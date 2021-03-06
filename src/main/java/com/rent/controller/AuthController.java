package com.rent.controller;

import com.rent.domain.UserRegistrationDto;
import com.rent.domain.authmessage.AuthMessages;
import com.rent.domain.authmessage.MessageType;
import com.rent.entity.User;
import com.rent.exception.MissingActivationCodeInUriException;
import com.rent.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;

@Controller
public class AuthController {

    private UserService userService;
    private AuthMessages authMessages;

    @GetMapping("/forgottenpassword")
    public String forgottenPassword(Model model) {
        model.addAttribute("adminExists", userService.adminExists());
        return "/auth/forgottenpassword";
    }

    @PostMapping("/generatepassword")
    public String generatePassword() {
        // check email existence, in case of fail create appropriate authMessages and return to /forgottenpassword
        // create arbitrary random password
        // save password
        // send email with password
        // create authMessages
        return "/auth/login";
    }

    @GetMapping({"/login", "/registration"})
    public String auth(@RequestParam (required = false, defaultValue = "false") Boolean holdMessages,
                       @RequestParam (required = false, defaultValue = "false") Boolean expired,
                       HttpServletRequest request,
                       Model model,
                       Authentication authentication) {

        String viewTarget = "redirect:/index";

        if (!isAuthenticated(authentication)) {
            viewTarget = getAuthView(holdMessages, request, model, expired);
        }

        return viewTarget;
    }

    private String getAuthView(boolean holdMessages, HttpServletRequest request, Model model, boolean expired) {
        
        String targetUri = request.getRequestURI();
        
        if (!holdMessages) {
            authMessages.clear();
        }
        if (expired) {
            authMessagesClearAndAdd("sessionExpired", null, MessageType.WARNING);
        }

        boolean adminExists = userService.adminExists();
        if (!adminExists) {
            targetUri = "/registration";
            String messageKey =  userService.hasAdminNotActivated() ? "activateOrRegisterFirstAdmin" : "firstUserAsAdmin";
            authMessages.add(messageKey, MessageType.WARNING);
        }
        model.addAttribute("adminExists", adminExists);
        model.addAttribute("messageList", authMessages.getAuthMessageList());

        return "auth" + targetUri;
    }

    @PostMapping(value = "/registrationprocess")
    public String processRegistration(UserRegistrationDto registrationDto) {

        // todo extract email already registered into an exception
        User userToRegister = new User(registrationDto);
        boolean registrationSuccessful = userService.registerUser(userToRegister);
        String messageKey = registrationSuccessful ? "successfulRegistration" : "emailAlreadyRegistered";
        MessageType messageType = registrationSuccessful ? MessageType.SUCCESS : MessageType.DANGER;
        return redirectToLoginHoldingMessage(messageKey, userToRegister.getEmail(), messageType);
    }

    @GetMapping(path = {"/activation/", "/activation/{code}"})
    public String activation(@PathVariable(name = "code", required = false) String code,
                             HttpServletRequest request,
                             Authentication authentication,
                             RedirectAttributes ra,
                             Model model) {
        // Just trying error handling
        if (code == null) {
            throw new MissingActivationCodeInUriException(request.getServletPath());
        }
        // todo extract unsuccessful activation into an exception
        User activatedUser = userService.userActivation(code);
        String messageKey = activatedUser != null ? "successfulActivation" : "unsuccessfulActivation";
        MessageType messageType = activatedUser != null ? MessageType.SUCCESS : MessageType.DANGER;
        String userEmail = activatedUser == null ? null : activatedUser.getEmail();
        // todo simplify due to single session existence (no activation when logged in)
        if (isAuthenticated(authentication)) {
            return forwardToActivationLoggedIn(model, messageKey, userEmail, messageType);
        } else {
            return redirectToLoginHoldingMessage(messageKey, userEmail, messageType);
        }
    }

    private String forwardToActivationLoggedIn(Model model, String messageKey, String userEmail, MessageType messageType) {
        authMessagesClearAndAdd(messageKey, userEmail, messageType);
        model.addAttribute("activatedUserEmail", userEmail);
        model.addAttribute("messageList", authMessages.getAuthMessageList());
        return "forward:/activationloggedin";
    }

    private String redirectToLoginHoldingMessage(String messageKey, String insert, MessageType messageType) {
        authMessagesClearAndAdd(messageKey, insert, messageType);

        return "redirect:/login?holdMessages=true";
    }

    private void authMessagesClearAndAdd(String messageKey, String insert, MessageType messageType) {
        authMessages.clear();
        if (insert == null) {
            authMessages.add(messageKey, messageType);
        } else {
            authMessages.add(messageKey, insert, messageType);
        }
    }

    private boolean isAuthenticated(Authentication authentication) {
        return authentication!=null && authentication.isAuthenticated();
    }
    
    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }
    
    @Autowired
    public void setAuthMessages(AuthMessages authMessages) {
        this.authMessages = authMessages;
    }
    
}
