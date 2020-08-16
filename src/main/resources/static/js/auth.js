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

getAuthData();
window.addEventListener("DOMContentLoaded", initAuthPage);

function initAuthElement(key) {
    let tmp = document.getElementById("registration" + key);
    authElements.set(key, tmp);
    tmp.remove();
}

function initAuthPage() {
    window.removeEventListener("DOMContentLoaded", initAuthPage);
    pageMode = "login";
    document.getElementById("regEmail").addEventListener("input", validateEmail());
    // document.getElementById("regEmail").oninput = validateEmail() {...};
    document.getElementById("regPassword").addEventListener("input", validatePassword());
    document.getElementById("confirmPwd").addEventListener("input", validatePassword());
    elements = ["title", "form", "swapauth", "forgot"];
    elements.forEach(initAuthElement);
    processAuthData();
}

function getAuthData() {
    let http = new XMLHttpRequest();
    http.onreadystatechange = function() {
        if (http.readyState === 4 && http.status === 200) {
            let authData = JSON.parse(http.responseText);
            adminExists = authData.adminExists;
            messages = authData.htmlMessageList;
        }
    };
    let url = "/getauthdata" + (fromctl !== "true" ? "?clearmessages=true" : "");
    http.open("GET", url, true);
    http.send();
}

 function processAuthData(authData) {
    if (adminExists === false && pageMode === "login") {
        exchangePageModeDisplay(); // switch to registration
        document.getElementById("registrationswapauth").style.display = "none";
    }
    displayMessages(messages);
    document.getElementsByTagName("body")[0].style.display = "block";
}

function insertAfter(newNode, referenceNode) {
    referenceNode.parentNode.insertBefore(newNode, referenceNode.nextSibling);
}

function swapAuthElement(key) {
    let positioningElement = document.getElementById(pageMode + key);
    positioningElement.after(authElements.get(key));
//    insertAfter(authElements.get(key), positioningElement);
    positioningElement.remove();
    authElements.set(key, positioningElement);
}

function exchangePageModeDisplay() {
    elements.forEach((element) => {swapAuthElement(element);});
    pageMode = pageMode === "login" ? "registration": "login";
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
    ) 
}

function switchLang(languageCode) {
    document.cookie = 'rentLocal=' + languageCode;
    location.reload();
}

function setLanguageLinks() {
    let languageText = getCookie("rentLocal").toUpperCase();
    let linkLikes = document.querySelectorAll("div#languagelinks > span");
    for(let idx = 0; idx < linkLikes.length; idx++) {
        const childText = linkLikes[idx].firstChild.data.toString().trim();
        if (childText === languageText) {
            linkLikes[idx].removeAttribute("class");
            linkLikes[idx].removeAttribute("onclick");
        }
    }
}

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
    const urlParams = new URLSearchParams(queryString);
    const value = urlParams.get(name);
    return value;
}

function validateEmail() {
//    alert("Validate Email Address")
}

function validatePassword() {
//    alert("Validate password")
}

