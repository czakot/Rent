window.addEventListener("DOMContentLoaded", initActivationLoggedIn);

function initActivationLoggedIn() {
    let forwardButton = document.getElementById("forwardButton");
    forwardButton.addEventListener("click", logout);
}

function logout() {
    let hostname = location.protocol + "//" + location.host;
    window.location.replace(hostname + "/logout");
}