/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

setLanguageLinks();
setAuthLinks();

function setLanguageLinks() {
    let languageText = getCookieValue("rentLocal").toUpperCase();
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
    if (window.location.pathname !== "/" /* or authenticated */) {
        document.getElementById("authlinks").remove();
S    }
}

function getCookieValue(key) {
    let values = document.cookie.match('(^|;)\\s*' + key + '\\s*=\\s*([^;]+)');
    return values ? values.pop() : '';
}