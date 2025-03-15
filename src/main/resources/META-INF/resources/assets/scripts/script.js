document.querySelector("h1").addEventListener("click", () => location.reload());

document.getElementById("l-err-canc").addEventListener(
    "click", () => document.getElementById("l-err-cont").style.display = "none"
);
