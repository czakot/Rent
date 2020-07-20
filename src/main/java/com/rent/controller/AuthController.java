package com.rent.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rent.entity.htmlmessage.HtmlMessages;
import com.rent.entity.htmlmessage.MessageType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.rent.entity.User;
import com.rent.entity.htmlmessage.AuthData;
import com.rent.entity.htmlmessage.HtmlMessage;
import com.rent.entity.utility.RegistrationForm;
import com.rent.service.UserService;
import java.awt.PageAttributes;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
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
    public @ResponseBody AuthData getAuthData(@RequestParam String page) {
//        initHtmlMessages();
        if(htmlMessages == null) {
            htmlMessages = new HtmlMessages(messageSource);
        };
        boolean adminExists = userService.adminExists();
        
        String message =  userService.existsNotActivatedAdmin() ? "activateOrRegisterFirstAdmin" : "firstUserAsAdmin";
        htmlMessages.add(message, MessageType.WARNING);
        AuthData authData =  new AuthData(htmlMessages.getHtmlMessageList(), adminExists);
        return authData;
    }

//    @GetMapping("/checkadmin")
//    public String checkAdmin(@RequestParam String page, RedirectAttributes ra) {
//        checkInitHtmlMessages();
//        boolean adminExists = embedAdminExists(ra);
//        if(adminExists) {
//            if (page.equals("registration")) {
//                return registration(ra);
//            }
//            return  "redirect:/login";
//        }
//        return registrationWithoutAdmin(ra);
//    }

    @PostMapping("/processregistration")
    public String processRegistration(@RequestBody RegistrationForm registrationForm, RedirectAttributes ra) {
//    public String processRegistration(RedirectAttributes ra,
//                                      @RequestParam String fullName,
//                                      @RequestParam String email,
//                                      @RequestParam String password) {
        User userToRegister = new User(registrationForm);
        boolean registrationSuccessful = userService.registerUser(userToRegister);
        String message = registrationSuccessful ? "successfulRegistration" : "emailAlreadyRegistered";
        MessageType messageType = registrationSuccessful ? MessageType.SUCCESS : MessageType.DANGER;
        htmlMessages.clearAndAddFirst(message, messageType);
        
//        boolean adminExists = embedAdminExists(ra);
//        if(adminExists) {
//            ra.addFlashAttribute("messages", htmlMessages);
//            return "redirect:/login";
//        } else {
//            return registrationWithoutAdmin(ra);
//        }
        return "redirect:/login";
    }
    
//    @PostMapping("/processregistration")
//    public String processRegistration(@ModelAttribute User userToRegister, RedirectAttributes ra) {
//        boolean registrationSuccessful = userService.registerUser(userToRegister);
//        String message = registrationSuccessful ? "successfulRegistration" : "emailAlreadyRegistered";
//        MessageType messageType = registrationSuccessful ? MessageType.SUCCESS : MessageType.DANGER;
//        htmlMessages.clearAndAddFirst(message, messageType);
//        
//        boolean adminExists = embedAdminExists(ra);
//        if(adminExists) {
//            ra.addFlashAttribute("messages", htmlMessages);
//            return "redirect:/login";
//        } else {
//            return registrationWithoutAdmin(ra);
//        }
//    }
    
    @RequestMapping(path = "/activation/{code}", method = RequestMethod.GET)
    public String activation(@PathVariable("code") String code, RedirectAttributes ra) {
        initHtmlMessages();
        User activatedUser = userService.userActivation(code);
        String message = activatedUser != null ? "successfulActivation" : "unsuccessfulActivation";
        MessageType messageType = activatedUser != null ? MessageType.SUCCESS : MessageType.DANGER;
        htmlMessages.clearAndAddFirst(message, messageType);
        boolean adminExists = embedAdminExists(ra);
        if (activatedUser != null) {
            ra.addFlashAttribute("messages", htmlMessages);
            return "redirect:/login";
        }
        return adminExists ? registration(ra) : registrationWithoutAdmin(ra);
    }

    private void initHtmlMessages() {
        if(htmlMessages == null) {
            htmlMessages = new HtmlMessages(messageSource);
        } else {
            htmlMessages.clear();
        }
    }
    
    private boolean embedAdminExists(RedirectAttributes ra)  {
        boolean adminExists = userService.adminExists();
        ra.addFlashAttribute("adminExists", Boolean.toString(adminExists));
        return adminExists;
    }
    
    private String registrationWithoutAdmin(RedirectAttributes ra) {
        String message =  userService.existsNotActivatedAdmin() ? "firstUserAsAdmin" : "activateOrRegisterFirstAdmin";
        htmlMessages.add(message, MessageType.WARNING);
        return registration(ra);
    }
    
    private String registration(RedirectAttributes ra) {
        ra.addFlashAttribute("messages", htmlMessages);
        ra.addFlashAttribute("user", new User());
        return "redirect:/registration";
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
