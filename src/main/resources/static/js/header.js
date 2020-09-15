/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

setLanguageLinks();
setAuthLinks();

function setLanguageLinks() {
    let languageText = rentLocal.toUpperCase();
//    let languageText = getCookieValue("rentLocal").toUpperCase();
    let linkLikes = document.querySelectorAll("#languagelinks a");
//    linkLikes = linkLikes.filter(e.firstChild.data.toString().trim() === languageText);
//    console.log("linkLikes = ");
//    console.log(linkLikes);
    for(let idx = 0; idx < linkLikes.length; idx++) {
        const childText = linkLikes[idx].firstChild.data.toString().trim();
        if (childText === languageText) {
            linkLikes[idx].className="activelanguage";
        }
    }
}

function setAuthLinks() {
    let authlinks = document.getElementById("authlinks");
    if (authlinks !== null) {
        switch (window.location.pathname) {
            case "/":
                let loginlink = document.getElementById("loginlink");
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
