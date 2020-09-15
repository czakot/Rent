package com.rent.controller;

import com.rent.entity.htmlmessage.HtmlMessages;
import com.rent.entity.htmlmessage.MessageType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.rent.entity.User;
import com.rent.entity.utility.UserRegistrationDto;
import com.rent.service.UserService;
import javax.servlet.http.HttpServletRequest;
import org.springframework.context.MessageSource;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthController {

    private UserService userService;
    private MessageSource messageSource;
    private HtmlMessages htmlMessages = null;
    
    @GetMapping({"/login", "/registration"})
    public String login(@RequestParam (required = false) String holdmessages,
                        HttpServletRequest request,
                        Model model) {
        initHtmlMessages();
        if (holdmessages == null || !holdmessages.equals("true")) {
            htmlMessages.clear();
        }
        boolean adminExists = userService.adminExists();
        String targetUri = request.getRequestURI();
        if (!adminExists) {
            targetUri = "/registration";
            String message =  userService.existsNotActivatedAdmin() ? "activateOrRegisterFirstAdmin" : "firstUserAsAdmin";
            htmlMessages.add(message, MessageType.WARNING);
        }
        model.addAttribute("adminExists", adminExists);
        model.addAttribute("messageList", htmlMessages.getHtmlMessageList());
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
    
    @RequestMapping(path = "/activation/{code}", method = RequestMethod.GET)
    public String activation(@PathVariable("code") String code) {
        initHtmlMessages();
        User activatedUser = userService.userActivation(code);
        String message = activatedUser != null ? "successfulActivation" : "unsuccessfulActivation";
        MessageType messageType = activatedUser != null ? MessageType.SUCCESS : MessageType.DANGER;
        return redirectToLoginHoldingMessage(message, messageType);
    }
    
    private String redirectToLoginHoldingMessage(String message, MessageType messageType) {
        htmlMessages.clearAndAddFirst(message, messageType);
        return "redirect:/login?holdmessages=true";
    }
 
    private void initHtmlMessages() {
        if(htmlMessages == null) {
            htmlMessages = new HtmlMessages(messageSource);
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
    
}
