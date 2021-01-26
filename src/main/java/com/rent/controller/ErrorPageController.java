/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rent.controller;

import java.util.Map;
import javax.servlet.http.HttpServletRequest;

import com.rent.domain.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.error.ErrorAttributeOptions.Include;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

@Controller
public class ErrorPageController implements ErrorController {

    final String ERROR_PATH = "/error/detailedError";
//    final String ERROR_PATH = "/error";
    
    private ErrorAttributes errorAttributes;
// todo userfriendly 403 - Access Forbidden error page filtered for auth pages
    @RequestMapping("/error")
    public String error(Model model, HttpServletRequest request) {
        
        WebRequest wr = new ServletWebRequest(request);
        Map<String, Object> errors = this.errorAttributes.getErrorAttributes(wr, ErrorAttributeOptions.of(Include.STACK_TRACE));
        model.addAttribute("errors", errors);

        String errorView;
        switch ((Integer)errors.get("status")) {
/*
            case 403:
                errorView = "/error/forbidden403";
                break;
*/
            default:
            errorView = ERROR_PATH;
        }
        return errorView;
    }
    
    // getErrorPath() deprecated in ErrorController class in favour of server.error.path in application.properties
    @Deprecated
    @Override
    public String getErrorPath() {
        return null;
    }

    @Autowired
    public void setErrorAttributes(ErrorAttributes errorAttributes) {
        this.errorAttributes = errorAttributes; 
    }

}
