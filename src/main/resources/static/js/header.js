/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

//alert("At header");
setLanguageLinks();

function switchLang(languageCode) {
    document.cookie = 'rentLocal=' + languageCode + "; Secure";
    document.cookie = "pageMode=" + pageMode + "; Secure";
    location.reload();
}

function setLanguageLinks() {
    let languageText = getCookieValue("rentLocal").toUpperCase();
    if (languageText === "") {
        
    }
    let linkLikes = document.querySelectorAll("div#languagelinks > span");
    for(let idx = 0; idx < linkLikes.length; idx++) {
        const childText = linkLikes[idx].firstChild.data.toString().trim();
        if (childText === languageText) {
            linkLikes[idx].removeAttribute("class");
            linkLikes[idx].removeAttribute("onclick");
        }
    }
}

function getCookieValue(key) {
    let values = document.cookie.match('(^|;)\\s*' + key + '\\s*=\\s*([^;]+)');
    return values ? values.pop() : '';
}