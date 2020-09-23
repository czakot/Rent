/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

setLanguageLinks();
setAuthLinks();
setRoleSelector();

function setLanguageLinks() {
    const links = Array.from(document.querySelectorAll("#languagelinks a"));
    const curr = links.filter(link => link.firstChild.data.toString() === rentLocal);
    curr[0].className="activelanguage";
}

function setAuthLinks() {
    const authlinks = document.getElementById("authlinks");
    if (authlinks !== null) {
        switch (window.location.pathname) {
            case "/":
                const loginlink = document.getElementById("loginlink");
                if (loginlink !== null) {
                    loginlink.href = "/login";
                }
                document.getElementById("registrationlink").href = "/registration";
                break;
            case "/login":
                document.getElementById("loginlink").remove();
                break;
            case "/registration":
                document.getElementById("registrationlink").remove();
                break;
            default:
                authlinks.remove();
                break;
        }
    }
}

function setRoleSelector() {
    const roleSelector = document.querySelector("#roleselector");
    const roleSelectorForm = document.querySelector("#roleselectorform");
    if (roleSelector !== null && roleSelectorForm !== null)
    roleSelector.addEventListener("change",function(){roleSelectorForm.submit();});
}

    