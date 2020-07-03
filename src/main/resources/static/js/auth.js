/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

function checkAdmin(currentAuthPage) {
    if (!adminExists) {
        if (currentAuthPage === "registration") {
            window.location.href = "/checkadmin?page=" + currentAuthPage;
        } else {
            // currentAuthPage = "login"
            if (numberOfAdmins() === 0) {
                window.location.href = "/checkadmin?page=" + currentAuthPage;
            }
        }
    }
    setLanguageLinks();
}

// SQL Admin number
function numberOfAdmins() {
    return 0;
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
//            linkLikes[idx].className="";
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
