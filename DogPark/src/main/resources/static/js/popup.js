var acc = document.getElementsByClassName("accordion");
var i;

for (i = 0; i < acc.length; i++) {
    acc[i].addEventListener("click", function() {
        this.classList.toggle("active");
        var panel = this.nextElementSibling;
        if (panel.style.maxHeight) {
            panel.style.maxHeight = null;
        } else {
            panel.style.maxHeight = panel.scrollHeight + "px";
        }
    });
}

// var mainListDiv = document.getElementById("collapsibleNavbar"),
//     mediaButton = document.getElementById("collapseButton");
//
// mediaButton.onclick = function () {
//
//     "use strict";
//
//     mainListDiv.classList.toggle("show_list");
//     mediaButton.classList.toggle("active");
//
// };