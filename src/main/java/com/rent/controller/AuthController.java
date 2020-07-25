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
import com.rent.entity.htmlmessage.AuthData;
import com.rent.entity.utility.RegistrationForm;
import com.rent.service.UserService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AuthController {

    @Qualifier("UserServiceImpl") // not nessessary, UserService unique here
    private UserService userService;
    private MessageSource messageSource;
    private HtmlMessages htmlMessages = null;

    @GetMapping(value = "/getauthdata", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody AuthData getAuthData(@RequestParam String page,
                                              @RequestParam(required = false) String keepmessages) {
        initHtmlMessages();
        if(keepmessages == null) {
            htmlMessages.clear();
        }
        boolean adminExists = userService.adminExists();
        if (!adminExists) {
            String message =  userService.existsNotActivatedAdmin() ? "activateOrRegisterFirstAdmin" : "firstUserAsAdmin";
            htmlMessages.add(message, MessageType.WARNING);
        }

        return new AuthData(htmlMessages.getHtmlMessageList(), adminExists);
    }

    @PostMapping("/processregistration")
    public String processRegistration(RegistrationForm registrationForm, RedirectAttributes ra) {
        User userToRegister = new User(registrationForm);
        boolean registrationSuccessful = userService.registerUser(userToRegister);
        String message = registrationSuccessful ? "successfulRegistration" : "emailAlreadyRegistered";
        MessageType messageType = registrationSuccessful ? MessageType.SUCCESS : MessageType.DANGER;
        htmlMessages.clearAndAddFirst(message, messageType);
        ra.addFlashAttribute("keepmessages", "true");
        
        System.err.println("/processregistration controller | first message: " + message);
        return "redirect:" + (userService.adminExists() ? "/login" : "/registration");
    }
    
    @RequestMapping(path = "/activation/{code}", method = RequestMethod.GET)
    public String activation(@PathVariable("code") String code, RedirectAttributes ra) {
        initHtmlMessages();
        User activatedUser = userService.userActivation(code);
        String message = activatedUser != null ? "successfulActivation" : "unsuccessfulActivation";
        MessageType messageType = activatedUser != null ? MessageType.SUCCESS : MessageType.DANGER;
        htmlMessages.clearAndAddFirst(message, messageType);
        ra.addFlashAttribute("keepmessages", "true");
        boolean adminExists = userService.adminExists();

        System.err.println("/activation/{code} controller | first message: " + message + " | adminExists: " + adminExists + "| redirect: " + (adminExists ? "/login" : "/registration"));
        return "redirect:" + (adminExists ? "/login" : "/registration");
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
