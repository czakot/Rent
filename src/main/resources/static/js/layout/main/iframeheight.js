var id = "ifrm";
var ifrm;
window.addEventListener("DOMContentLoaded", iframeonload,);

function iframeonload() {
    ifrm = document.getElementById(id);
    ifrm.addEventListener("load", setIframeHeight);
}

function setIframeHeight() {
    // var ifrm = document.getElementById(id);
    var doc = ifrm.contentDocument ? ifrm.contentDocument : ifrm.contentWindow.document;
    ifrm.style.visibility = 'hidden';
    ifrm.style.height = "10px"; // reset to minimal height ...
    // IE opt. for bing/msn needs a bit added or scrollbar appears
    ifrm.style.height = getDocHeight( doc ) + 4 + "px";
    ifrm.style.visibility = 'visible';
}

function getDocHeight(doc) {
    doc = doc || document;
    // stackoverflow.com/questions/1145850/
    var body = doc.body, html = doc.documentElement;
    return Math.max(body.scrollHeight, body.offsetHeight, html.clientHeight, html.scrollHeight, html.offsetHeight);
}
