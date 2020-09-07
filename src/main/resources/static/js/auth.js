/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
let pageMode;
let elements;
let authElements = new Map();
let adminExists;
let messages;

window.addEventListener("DOMContentLoaded", initAuthPage);

function initAuthElement(key, pageModeToSave) {
    let tmp = document.getElementById(pageModeToSave + key);
    authElements.set(key, tmp);
    tmp.remove();
}

function initAuthPage() {
    fetch("/getauthdata" + (fromctl !== "true" ? "?clearmessages=true" : ""))
            .then(response => response.json())
            .then(responseJson => processAuthData(responseJson));
    let urlPageMode = getUrlParam("pageMode");
    alert("urlPageMode: '" + urlPageMode + "'");
    if (urlPageMode === null) {
        pageMode = getCookie("pageMode");
        document.cookie = "pageMode=; secure; max-age=0";
        if (pageMode === "") {
            pageMode = "login";
        }
    } else {
        pageMode = urlPageMode;
    }
    
    if (getCookie("rentLocal") === "") {
        document.cookie = "rentLocal=" + local + ";secure";
    }
    
//    setLanguageLinks();
    //document.getElementById("regEmail").addEventListener("input", validateEmail());
    //// document.getElementById("regEmail").oninput = validateEmail() {...};
    //document.getElementById("regPassword").addEventListener("input", validatePassword());
    elements = ["title", "form", "swapauth", "forgot"];
    let pageModeToSave = otherPageMode();
    for (let x of elements) {
        initAuthElement(x, pageModeToSave);
    }
    // elements.forEach(initAuthElement);
}

 function processAuthData(authData) {
    adminExists = authData.adminExists;
    messages = authData.htmlMessageList;
    if (adminExists === false && pageMode === "login") {
        exchangePageModeDisplay(); // switch to registration
        document.getElementById("registrationswapauth").style.display = "none";
    }
    displayMessages(messages);
    document.getElementsByTagName("body")[0].style.display = "block";
}

function swapAuthElement(key) {
    let positioningElement = document.getElementById(pageMode + key);
    positioningElement.after(authElements.get(key));
//    insertAfter(authElements.get(key), positioningElement);
    positioningElement.remove();
    authElements.set(key, positioningElement);
}

function otherPageMode() {
    return pageMode === "login" ? "registration": "login";
}

function exchangePageModeDisplay() {
    elements.forEach((element) => {swapAuthElement(element);});
    pageMode = otherPageMode();
}

function changePageMode() {
    let body = document.getElementsByTagName("body")[0];
    body.style.display = "none";
    exchangePageModeDisplay();
    body.style.display = "block";    
}

function displayMessages(messages) {
    let messagesdiv = document.getElementById("messages");
    messages.forEach(
        function(message) {
            let messagediv = document.createElement("div");
            messagediv.className = "auth-message " + message["cssClass"];
            messagediv.innerHTML = message["text"];
            messagesdiv.appendChild(messagediv);
        }
    ); 
}

////function switchLang(languageCode) {
////    document.cookie = 'rentLocal=' + languageCode + "; Secure";
////    document.cookie = "pageMode=" + pageMode + "; Secure";
////    location.reload();
////}
////
//function setLanguageLinks() {
//    let languageText = getCookie("rentLocal").toUpperCase();
//    let linkLikes = document.querySelectorAll("div#languagelinks > span");
//    for(let idx = 0; idx < linkLikes.length; idx++) {
//        const childText = linkLikes[idx].firstChild.data.toString().trim();
//        if (childText === languageText) {
//            linkLikes[idx].removeAttribute("class");
//            linkLikes[idx].removeAttribute("onclick");
//        }
//    }
//}
//
function getCookie(cookieName) {
  const internalCookieName = cookieName + "=";
  let decodedCookie = decodeURIComponent(document.cookie);
  var cookieArray = decodedCookie.split(';');
  for(let idx = 0; idx <cookieArray.length; idx++) {
    let cookie = cookieArray[idx];
    while (cookie.charAt(0) === ' ') {
      cookie = cookie.substring(1);
    }
    if (cookie.indexOf(internalCookieName) === 0) {
      return cookie.substring(internalCookieName.length, cookie.length);
    }
  }
  return "";}

function getUrlParam(name) {
    const queryString = window.location.search;
    alert("queryString: " + queryString);
    const urlParams = new URLSearchParams(queryString);
    alert("urlParams: " + urlParams);
    const value = urlParams.get(name);
    alert("value: " + value);
    return value;
}

function validateEmail() {
//    alert("Validate Email Address")
}

function validatePassword() {
//    alert("Validate password")
}

function checkMatchPasswords(e) {
    let password = document.getElementById("regPassword").value;
    let confirmPwd = document.getElementById("confirmPwd").value;
    let button = document.getElementById("regButton");
    let msg = document.getElementById("pwdmismatchmsg");
    let showMsg = !(password.length === 0 || confirmPwd.length === 0 || password === confirmPwd);
    msg.style.display = showMsg ? "block" : "none";
    button.disabled = showMsg;
}

// NOT USED NOT USED NOT USED NOT USED NOT USED NOT USED NOT USED NOT USED
//
//let promisGetAuth = 
//    fetch("/getauthdata" + (fromctl !== "true" ? "?clearmessages=true" : "")) // fromctl is not "ready"
//        .then(response => response.json());
//    
//    Promise.all([promisGetAuth]).then(responsJsons => processAuthData(responsJsons[0]));

function insertAfter(newNode, referenceNode) {
    referenceNode.parentNode.insertBefore(newNode, referenceNode.nextSibling);
}

function getAuthData() {
    let http = new XMLHttpRequest();
    http.onreadystatechange = function() {
        if (http.readyState === 4 && http.status === 200) {
            processAuthData(JSON.parse(http.responseText));
        }
    };
    let url = "/getauthdata" + (fromctl !== "true" ? "?clearmessages=true" : "");
    http.open("GET", url, true);
    http.send();
}
