package com.rent.controller;

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
import com.rent.service.UserService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AuthController {

    @Qualifier("UserServiceImpl") // not nessessary, UserService unique here
    private UserService userService;
    private MessageSource messageSource;
    private HtmlMessages htmlMessages = null;

    @GetMapping("/checkadmin")
    public String checkAdmin(@RequestParam String page, RedirectAttributes ra) {
        checkInitHtmlMessages();
        boolean adminExists = embedAdminExists(ra);
        if(adminExists) {
            if (page.equals("registration")) {
                return registration(ra);
            }
            return  "redirect:/login";
        }
        return registrationWithoutAdmin(ra);
    }
    
    @PostMapping("/processregistration")
    public String processRegistration(@ModelAttribute User userToRegister, RedirectAttributes ra) {
        boolean registrationSuccessful = userService.registerUser(userToRegister);
        String message = registrationSuccessful ? "successfulRegistration" : "emailAlreadyRegistered";
        MessageType messageType = registrationSuccessful ? MessageType.SUCCESS : MessageType.DANGER;
        htmlMessages.clearAndAddFirst(message, messageType);
        
        boolean adminExists = embedAdminExists(ra);
        if(adminExists) {
            ra.addFlashAttribute("messages", htmlMessages);
            return "redirect:/login";
        } else {
            return registrationWithoutAdmin(ra);
        }
    }
    
    @RequestMapping(path = "/activation/{code}", method = RequestMethod.GET)
    public String activation(@PathVariable("code") String code, RedirectAttributes ra) {
        checkInitHtmlMessages();
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

    private void checkInitHtmlMessages() {
        if(htmlMessages == null) {
            htmlMessages = new HtmlMessages(messageSource);
        } else {
            htmlMessages.clear();
        }
    }
    
    private boolean embedAdminExists(RedirectAttributes ra)  {
        boolean adminExists = userService.getAdminExist();
        ra.addFlashAttribute("adminExists", Boolean.toString(adminExists));
        return adminExists;
    }
    
    private String registrationWithoutAdmin(RedirectAttributes ra) {
        String message =  existsNotActivatedAdmin() ? "firstUserAsAdmin" : "activateOrRegisterFirstAdmin";
        htmlMessages.add(message, MessageType.WARNING);
        return registration(ra);
    }
    
    private String registration(RedirectAttributes ra) {
        ra.addFlashAttribute("messages", htmlMessages);
        ra.addFlashAttribute("user", new User());
        return "redirect:/registration";
    }
    
    private boolean existsNotActivatedAdmin() {
        final boolean ACTIVATED = true;
        return userService.numberOfUsers("ADMIN", !ACTIVATED) == 0;
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
