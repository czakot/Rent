/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
let adminExists;;
let messages = [];
getAuthData();

function getAuthData() {
    let http = new XMLHttpRequest();
    http.onreadystatechange = function() {
        if (http.readyState === 4 && http.status === 200) {
            let authData = JSON.parse(http.responseText);
            adminExists = authData.adminExists;
            if (adminExists === false) {
                if (window.location.pathname === "/login") {
                    window.location.pathname = "/registration";
                    return;
                }
                let loginlink = document.getElementsByTagName("a");
                loginlink[0].style.display = "none";
            }
            messages = authData.htmlMessageList;
            displayMessages();
            let body = document.getElementsByTagName("body");
            body[0].style.display = "block";
        }
    };
    let url = "/getauthdata?page=" + window.location.pathname;
    http.open("GET", url, true);
    http.send();
}

function displayMessages() {
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

function confirmPassword() {
    alert("Confirm password")
}
