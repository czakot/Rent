/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package garage;

import com.rent.entity.htmlmessage.HtmlMessage;
import java.util.List;

/**
 *
 * @author czakot
 */
public class AuthData {

    List<HtmlMessage> htmlMessageList = null;
    Boolean adminExists = null;

    public AuthData(List<HtmlMessage> htmlMessageList, Boolean adminExists) {
        this.htmlMessageList = htmlMessageList;
        this.adminExists = adminExists;
    }
    
    public List<HtmlMessage> getHtmlMessageList() {
        return htmlMessageList;
    }

    public void setHtmlMessageseList(List<HtmlMessage> htmlMessagesList) {
        this.htmlMessageList = htmlMessagesList;
    }

    public Boolean getAdminExists() {
        return adminExists;
    }

    public void setAdminExists(Boolean adminExists) {
        this.adminExists = adminExists;
    }

}
