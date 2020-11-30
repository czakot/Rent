/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package garage;

import com.rent.domain.authmessage.AuthMessage;
import java.util.List;

/**
 *
 * @author czakot
 */
public class AuthData {

    List<AuthMessage> htmlMessageList = null;
    Boolean adminExists = null;

    public AuthData(List<AuthMessage> htmlMessageList, Boolean adminExists) {
        this.htmlMessageList = htmlMessageList;
        this.adminExists = adminExists;
    }
    
    public List<AuthMessage> getHtmlMessageList() {
        return htmlMessageList;
    }

    public void setHtmlMessageseList(List<AuthMessage> htmlMessagesList) {
        this.htmlMessageList = htmlMessagesList;
    }

    public Boolean getAdminExists() {
        return adminExists;
    }

    public void setAdminExists(Boolean adminExists) {
        this.adminExists = adminExists;
    }

}
