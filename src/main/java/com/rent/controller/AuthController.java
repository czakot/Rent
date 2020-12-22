package com.rent.controller;

import com.rent.domain.UserRegistrationDto;
import com.rent.domain.authmessage.AuthMessage;
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
import java.util.ArrayList;
import java.util.List;

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

        User userToRegister = new User(registrationDto);
        boolean registrationSuccessful = userService.registerUser(userToRegister);
        String messageKey = registrationSuccessful ? "successfulRegistration" : "emailAlreadyRegistered";
        MessageType messageType = registrationSuccessful ? MessageType.SUCCESS : MessageType.DANGER;
        return redirectToLoginHoldingMessage(messageKey, userToRegister.getEmail(), messageType);
    }
    
    @RequestMapping(path = {"/activation/", "/activation/{code}"}, method = RequestMethod.GET)
    public String activation(@PathVariable(name = "code", required = false) String code,
                             HttpServletRequest request,
                             Authentication authentication,
                             RedirectAttributes ra,
                             Model model) {
        // Just trying error handling
        if (code == null) {
            throw new MissingActivationCodeInUriException(request.getServletPath());
        }
        User activatedUser = userService.userActivation(code);
        String messageKey = activatedUser != null ? "successfulActivation" : "unsuccessfulActivation";
        MessageType messageType = activatedUser != null ? MessageType.SUCCESS : MessageType.DANGER;
        String userEmail = activatedUser == null ? null : activatedUser.getEmail();
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
