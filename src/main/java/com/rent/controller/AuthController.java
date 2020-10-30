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
import org.springframework.context.MessageSource;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthController {

    private UserService userService;
    private MessageSource messageSource;
    private AuthMessages authMessages = null;

    @GetMapping({"/login", "/registration"})
    public String auth(@RequestParam (required = false) String holdmessages,
                        HttpServletRequest request,
                        Model model,
                        Authentication authentication) {
        if (isAuthenticated(authentication, model)) {
            
        }
        initHtmlMessages();
        if (holdmessages == null || !holdmessages.equals("true")) {
            authMessages.clear();
        }
        boolean adminExists = userService.adminExists();
        String targetUri = request.getRequestURI();
        if (!adminExists) {
            targetUri = "/registration";
            String message =  userService.existsNotActivatedAdmin() ? "activateOrRegisterFirstAdmin" : "firstUserAsAdmin";
            authMessages.add(message, MessageType.WARNING);
        }
        model.addAttribute("adminExists", adminExists);
        model.addAttribute("messageList", authMessages.getAuthMessageList());
        return "/auth" + targetUri;
    }

    @PostMapping(value = "/registrationprocess")
    public String processRegistration(UserRegistrationDto registrationDto) {
        User userToRegister = new User(registrationDto);
        boolean registrationSuccessful = userService.registerUser(userToRegister);
        String message = registrationSuccessful ? "successfulRegistration" : "emailAlreadyRegistered";
        MessageType messageType = registrationSuccessful ? MessageType.SUCCESS : MessageType.DANGER;
        return redirectToLoginHoldingMessage(message, messageType);
    }
    
    @RequestMapping(path = {"/activation/", "/activation/{code}"}, method = RequestMethod.GET)
    public String activation(@PathVariable(name = "code", required = false) String code,
                             HttpServletRequest request,
                             Model model) {
        // Just trying error handling
        if (code == null) {
            throw new MissingActivationCodeInUriException("Activation link incomplete, missing activation code.", request.getServletPath());
        }
        initHtmlMessages();
        User activatedUser = userService.userActivation(code);
        String message = activatedUser != null ? "successfulActivation" : "unsuccessfulActivation";
        MessageType messageType = activatedUser != null ? MessageType.SUCCESS : MessageType.DANGER;
        return redirectToLoginHoldingMessage(message, messageType);
    }
    
    private String redirectToLoginHoldingMessage(String message, MessageType messageType) {
        authMessages.clearAndAddFirst(message, messageType);
        return "redirect:/login?holdmessages=true";
    }
 
    private void initHtmlMessages() {
        if(authMessages == null) {
            authMessages = new AuthMessages(messageSource);
        }
    }
    
    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }
    
    @Autowired
    public void setMessageSource(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    private boolean isAuthenticated(Authentication authentication, Model model) {
        if (authentication!=null && authentication.isAuthenticated()) {
            // prepare HtmlMessages: Already logged in
            initHtmlMessages();
            return true;
        }
        return false;
    }
    
}
