package com.rent.controller;

import com.rent.domain.authmessage.AuthMessages;
import com.rent.domain.authmessage.MessageType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.rent.entity.User;
import com.rent.domain.UserRegistrationDto;
import com.rent.exception.MissingActivationCodeInUriException;
import com.rent.service.UserService;
import javax.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthController {

    private UserService userService;
    private AuthMessages authMessages;

    @GetMapping({"/login", "/registration"})
    public String auth(@RequestParam (required = false, defaultValue = "false") Boolean holdMessages,
                       @RequestParam (required = false, defaultValue = "false") Boolean expired,
                       HttpServletRequest request,
                       Model model,
                       Authentication authentication) {

        return isAuthenticated(authentication) ? "redirect:/index" : getAuthView(holdMessages, request, model, expired);
    }

    private String getAuthView(boolean holdMessages, HttpServletRequest request, Model model, boolean expired) {
        
        String targetUri;
        
        if (!holdMessages || expired) {
            authMessages.clear();
        }
        if (expired) {
            authMessages.add("sessionExpired", MessageType.WARNING);
        }

        boolean adminExists = userService.adminExists();
        
        targetUri = adminExists ? "/auth" + request.getRequestURI() : getRegistrationViewNoAdmin();
        model.addAttribute("adminExists", adminExists);
        model.addAttribute("messageList", authMessages.getAuthMessageList());
        
        return targetUri;
    }
    
    private String getRegistrationViewNoAdmin() {
        String messageKey =  userService.hasAdminNotActivated() ? "activateOrRegisterFirstAdmin" : "firstUserAsAdmin";
        authMessages.add(messageKey, MessageType.WARNING);

        return "/auth/registration";
    }

    @PostMapping(value = "/registrationprocess")
    public String processRegistration(UserRegistrationDto registrationDto) {
        User userToRegister = new User(registrationDto);
        boolean registrationSuccessful = userService.registerUser(userToRegister);
        String messageKey = registrationSuccessful ? "successfulRegistration" : "emailAlreadyRegistered";
        MessageType messageType = registrationSuccessful ? MessageType.SUCCESS : MessageType.DANGER;
        return redirectToLoginHoldingMessage(messageKey, new String[]{userToRegister.getEmail()}, messageType);
    }
    
    @RequestMapping(path = {"/activation/", "/activation/{code}"}, method = RequestMethod.GET)
    public String activation(@PathVariable(name = "code", required = false) String code,
                             HttpServletRequest request,
                             Model model) {
        // Just trying error handling
        if (code == null) {
            throw new MissingActivationCodeInUriException(request.getServletPath());
        }
        User activatedUser = userService.userActivation(code);
        String messageKey = activatedUser != null ? "successfulActivation" : "unsuccessfulActivation";
        MessageType messageType = activatedUser != null ? MessageType.SUCCESS : MessageType.DANGER;
        String[] userEmail = activatedUser == null ? null : new String[]{activatedUser.getEmail()};
        return redirectToLoginHoldingMessage(messageKey, userEmail, messageType);
    }

    private String redirectToLoginHoldingMessage(String messageKey, String[] inserts, MessageType messageType) {
        authMessages.clear();
        if (inserts == null) {
            authMessages.add(messageKey, messageType);
        } else {
            authMessages.add(messageKey, inserts, messageType);
        }

        return "redirect:/login?holdMessages=true";
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
