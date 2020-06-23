/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

function checkAdmin() {
    if (adminExists != "true") {
        window.location.href = "/checkadmin";
    }
//    if (adminExists == "true") {
//        // activ ADMIN exists, so stay where you are
//        //window.location.href = "/login";
//    } else {
//        if (adminExists == "false") {
//            window.location.href = "/registration";
//        } else {
//            // adminExists not set (undefined, null or not true/false)
//            window.location.href = "/checkadmin";
//        }
//    }
}

function switchLang(langCode) {
    document.cookie = 'rentLocal=' + langCode;
    location.reload();
}
