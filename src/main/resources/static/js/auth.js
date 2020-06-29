/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

function checkAdmin(page) {
//    if (adminExists != "true" && adminExists != "false") {
    if (!adminExists) {
        window.location.href = "/checkadmin?page=" + page;
    }
    setLanguageLinks();
}

function switchLang(langCode, obj) {
    document.cookie = 'rentLocal=' + langCode;
    location.reload();
}

function setLanguageLinks() {
//    console.log(obj);
//    let linkLikes = document.querySelectorAll("header span");
//    for(let idx = 0; idx < linkLikes.length; idx++) {
//        if (obj. != linkLikes[idx]) {
//            linkLikes[idx].class = "btn-link button-link-like";
//        } else {
//            linkLikes[idx].class = "";
//        }
//    }
    alert();
    obj.className = "";
}

function confirmPassword() {
    alert("Confirm password")
}
