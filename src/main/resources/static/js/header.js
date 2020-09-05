/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

//alert("At header");
setLanguageLinks();

function switchLang(languageCode) {
    document.cookie = 'rentLocal=' + languageCode + "; Secure";
//    document.cookie = "pageMode=" + pageMode + "; Secure";
//    location.reload();
//    window.location.href += "lang=" + languageCode;
}

function setLanguageLinks() {
    let languageText = getCookieValue("rentLocal").toUpperCase();
    if (languageText === "") {
        // set default browser language
//        languageText="HU";
    }
    console.log("languageText = " + languageText);
//    let linkLikes = document.querySelectorAll("div#languagelinks > span");
    let linkLikes = document.querySelectorAll("#languagelinks a");
    console.log("linkLikes = ");
    console.log(linkLikes);
//    linkLikes = linkLikes.filter(e.firstChild.data.toString().trim() === languageText);
//    console.log("linkLikes = ");
//    console.log(linkLikes);
    for(let idx = 0; idx < linkLikes.length; idx++) {
        const childText = linkLikes[idx].firstChild.data.toString().trim();
        if (childText === languageText) {
//            linkLikes[idx].removeAttribute("class");
//            linkLikes[idx].removeAttribute("onclick");
//            linkLikes[idx].onclick = null;
//            linkLikes[idx].setAttribute("onclick", "");
            linkLikes[idx].style.opacity = "1";
//            linkLikes[idx].style.cursor = "default";
//            linkLikes[idx].setAttribute("style", "opacity: 1; cursor: default");
//            linkLikes[idx].setAttribute("class", "activelanguage");
//            linkLikes[idx].classList.add("activelanguage");
//            linkLikes[idx].href="";
            linkLikes[idx].className="activelanguage";
        }
    }
}

function getCookieValue(key) {
    let values = document.cookie.match('(^|;)\\s*' + key + '\\s*=\\s*([^;]+)');
    return values ? values.pop() : '';
}