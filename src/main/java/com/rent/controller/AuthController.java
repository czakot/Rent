package com.rent.controller;

import com.rent.RentApplication;
import com.rent.entity.htmlmessage.HtmlMessage;
import com.rent.entity.htmlmessage.HtmlMessageFactory;
import com.rent.entity.htmlmessage.MessageType;
import com.rent.entity.Role;

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
import static java.lang.Thread.sleep;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AuthController {

    @Qualifier("UserServiceImpl") // not nessessary, UserService unique here
    private UserService userService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }
    
    private Role role;

    @Autowired
    public void setRole(Role role) {
        this.role = role;
    }

    HtmlMessageFactory htmlMessageFactory;

    @Autowired
    public void setHtmlMessageFactory(HtmlMessageFactory htmlMessageFactory) {
        this.htmlMessageFactory = htmlMessageFactory;
    }
    
    @RequestMapping("/checkadmin")
    public String checkadmin(RedirectAttributes redirectAttributes) {
        final boolean ACTIVATED = true;

        if (userService.numberOfUsers("ADMIN", ACTIVATED) == 0) {
            HtmlMessage htmlMessage;
            redirectAttributes.addFlashAttribute("adminExists", "false");
            
            if (userService.numberOfUsers("ADMIN", !ACTIVATED) == 0) {
                // no admin => register first user as admin
                htmlMessage = htmlMessageFactory.createHtmlMessage("registerFirstUserAsAdmin", MessageType.WARNING);
                redirectAttributes.addFlashAttribute("message", htmlMessage);
            } else {
                // first user registered as ADMIN, but not activated => activate or register first user as admin again
                htmlMessage = htmlMessageFactory.createHtmlMessage("activateOrRegisterFirstAdmin", MessageType.WARNING);
            }
            redirectAttributes.addFlashAttribute("message", htmlMessage);
            return "redirect:/register";
        }

        redirectAttributes.addFlashAttribute("adminExists", "true");
        return "redirect:/login";
    }
    
    @GetMapping("/masterreg")
    public String masterReg(Model model) {
        if (userService.notValidatedMasterExists()) {
            return "auth/activatemastermsg";
        } else {
            model.addAttribute("user", new User());
            return "auth/masterregform";
        }
    }

    @PostMapping("/masterregdo")
    public String masterReg(@ModelAttribute User masterToRegister) {
        userService.registerMaster(masterToRegister);

        return "auth/activatemastermsg";
    }

    @RequestMapping(value = "/registration")
    public String registration(Model model) {
        Role role;
        String title = "Próba title";
        model.addAttribute("title", title);
        model.addAttribute("user", new User());
        return "auth/registration";
    }

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

    @RequestMapping(path = "/activation/{code}", method = RequestMethod.GET)
    public String activation(@PathVariable("code") String code, Model model) {
        User activatedUser = userService.userActivation(code);
        if (userService.isMaster(activatedUser)) {
            Thread thread = new Thread(() -> {
                try {
                    sleep(1000);
                } catch (InterruptedException ex) {
                    /* nothing to do */ }
                RentApplication.restart();
            });

            thread.setDaemon(false);
            thread.start();

            return "auth/restartmsg";
        }
        if (userService.enabledMasterExists()) {
            model.addAttribute("result", activatedUser != null ? "activated" : "notactivated");
            return "auth/login";
        }
        
        return "auth/activatemastermsg";

    }
}
