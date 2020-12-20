/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
window.addEventListener("DOMContentLoaded", initAuthPage);
if (inIframe()) {
    window.top.location.href = "https://localhost:8443/login";
}

function initAuthPage() {
    if (window.location.pathname === "/registration") {
        document.getElementById("regPassword").addEventListener("input", checkMatchPasswords);
        document.getElementById("confirmPwd").addEventListener("input", checkMatchPasswords);
    }
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

function inIframe() {
    try {
        return window.self !== window.top;
    } catch (e) {
        return true;
    }
}

