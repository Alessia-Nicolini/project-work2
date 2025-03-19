document.querySelector("h1").addEventListener("click", () => location.reload());

try {
    document.getElementById("l-err-canc").addEventListener(
        "click",
        () => document.getElementById("l-err-cont").style.display = "none"
    );
} catch (e) {
    console.log(e);
}

try {
    document.getElementById("d-err-canc").addEventListener(
        "click",
        () => document.getElementById("d-err-cont").style.display = "none"
    );
} catch (e) {
    console.log(e);
}

try {
    const {Calendar} = window.VanillaCalendarPro;
    const calendar = new Calendar("#date", {
        inputMode: true,
        locale: "it-IT",
        positionToInput: "auto",
        onChangeToInput(self) {
            if (self.context.inputElement && self.context.selectedDates[0]) {
                self.context.inputElement.value = self.context.selectedDates[0];
                self.hide();
            }
        },
        selectedTheme: "dark"
    });
    calendar.init();
} catch (e) {
    console.log(e);
}

try {
    const today = new Date();
    const {Calendar} = window.VanillaCalendarPro;
    const calendar = new Calendar("#start", {
        dateMin: today.setDate(today.getDate() + 1),
        inputMode: true,
        locale: "it-IT",
        positionToInput: "auto",
        onChangeToInput(self) {
            if (self.context.inputElement && self.context.selectedDates[0]) {
                self.context.inputElement.value = self.context.selectedDates[0];
                self.hide();
            }
        },
        selectedTheme: "dark",
        selectionTimeMode: 24,
        timeMaxHour: 16,
        timeMinHour: 8
    });
    calendar.init();
} catch (e) {
    console.log(e);
}
