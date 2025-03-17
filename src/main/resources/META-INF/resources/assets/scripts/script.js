document.querySelector("h1").addEventListener("click", () => location.reload());

try {
    document.getElementById("l-err-canc").addEventListener(
        "click",
        () => document.getElementById("l-err-cont").style.display = "none"
    );
} catch (e) {
    document.getElementById("d-err-canc").addEventListener(
        "click",
        () => document.getElementById("d-err-cont").style.display = "none"
    );
}
