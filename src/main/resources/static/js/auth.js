/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

function checkAdmin(page) {
    alert(adminExists + '  ' + page);
    if (adminExists != "true" && adminExists != "false") {
        window.location.href = "/checkadmin?page=" + page;
        
    }
}

function switchLang(langCode) {
    document.cookie = 'rentLocal=' + langCode;
    //let newHref = "/checkadmin?page=" + location.href.substr(location.href.lastIndexOf("/") + 1)
//    let newHref = location.href.substr(location.href.lastIndexOf("/"))
//    alert(newHref);
//    window.location.href = newHref;
    alert(location.href);
    location.reload();
}

function confirmPassword() {
    alert("Confirm password")
}
