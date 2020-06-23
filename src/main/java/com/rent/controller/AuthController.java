package com.rent.controller;

import com.rent.entity.htmlmessage.HtmlMessage;
import com.rent.entity.htmlmessage.HtmlMessageFactory;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AuthController {

    private static final boolean ACTIVATED = true;

    @Qualifier("UserServiceImpl") // not nessessary, UserService unique here
    private UserService userService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }
    
    HtmlMessageFactory htmlMessageFactory;

    @Autowired
    public void setHtmlMessageFactory(HtmlMessageFactory htmlMessageFactory) {
        this.htmlMessageFactory = htmlMessageFactory;
    }
    
    @GetMapping("/checkadmin")
    public String checkAdmiForLogin(RedirectAttributes ra) {
        if(userService.getAdminExist()) {
            ra.addFlashAttribute("adminExists", "true");
            return "redirect:";
        }
        return registration(ra);
    }
    
    private void firstAdmin(RedirectAttributes ra) {
        ra.addFlashAttribute("adminExists", "false");
        String message = userService.numberOfUsers("ADMIN", !ACTIVATED) == 0 ? "firstUserAsAdmin" : "activateOrRegisterFirstAdmin";
        HtmlMessage htmlMessage = htmlMessageFactory.createHtmlMessage(message, MessageType.WARNING);
        ra.addFlashAttribute("message", htmlMessage);
    }
    
    @GetMapping("/registration")
    public String registration(RedirectAttributes ra) {
        ra.addFlashAttribute("user", new User());
        if (!userService.getAdminExist()) {
            firstAdmin(ra);
        }
        return "redirect:/registration";
    }
    
    @PostMapping("/processregistration")
    public String processRegistration(@ModelAttribute User userToRegister, RedirectAttributes redirectionAttributes) {
        System.err.println("process registration");
        String message = userService.registerUser(userToRegister) ? "successfulRegistration" : "emailAlreadyRegistered";
        HtmlMessage htmlMessage = htmlMessageFactory.createHtmlMessage(message, MessageType.SUCCESS);
        redirectionAttributes.addFlashAttribute("message", htmlMessage);
        return "redirect:/login";
    }
    
/*    

//	@RequestMapping(value = "/reg", method = RequestMethod.POST)
    @PostMapping("/registration")
    public String reg(@ModelAttribute User userToRegister, Model model) {
        if (!userService.registerUser(userToRegister)) {
            User newUser = new User();
            newUser.setEmail(userToRegister.getEmail());
            model.addAttribute("user", newUser);
            model.addAttribute("result", "already_exists");
            return "auth/registration";
        } else {
            model.addAttribute("result", "registered");
        }
        return "auth/login";
    }
*/
    @RequestMapping(path = "/activation/{code}", method = RequestMethod.GET)
    public String activation(@PathVariable("code") String code, RedirectAttributes ra) {
        if (userService.userActivation(code) == null) {
            // unsuccessful activation, no registered user belongs to this token (expired, historic, ...)
        } else {
            // successfull activation with [data] intu message
        };
        return "redirect:/login";
    }
}
