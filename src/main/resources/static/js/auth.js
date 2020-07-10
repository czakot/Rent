/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
let adminExists = checkAdminExists();

if (adminExists === "false") {
    if (window.location.pathname === "/login") {
        window.location.pathname = "/registration";
    } else {
        // if msg = null then msg: first admin
    }
}

let messages = getMessages();

function getMessages() {    
    let http = new XMLHttpRequest();
    let messages;
    http.onreadystatechange = function() {
        if (http.readyState === 4 && http.status === 200) {
            messages = http.responseXML;
        }
    };
    http.open("GET", "/getmessages", false);
    http.send();
    return messages;
}

function checkAdminExists() {
    let http = new XMLHttpRequest();
    let adminExists;
    http.onreadystatechange = function() {
        if (http.readyState === 4 && http.status === 200) {
            adminExists = http.responseText;
        }
    };
    http.open("GET", "/adminexists", false);
    http.send();
    return adminExists;
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

function confirmPassword() {
    alert("Confirm password")
}
