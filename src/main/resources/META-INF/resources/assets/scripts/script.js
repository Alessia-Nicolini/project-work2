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
        disableWeekdays: [0, 6],
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
    const { Calendar } = window.VanillaCalendarPro;
    const calendar = new Calendar("#start", {
        disableWeekdays: [0, 6],
        inputMode: true,
        locale: "it-IT",
        positionToInput: "auto",
        selectionTimeMode: 24,
        timeMinHour: 8,
        timeMaxHour: 16,
        timeStepMinute: 5,
        onChangeToInput(self) {
            if (self.context.inputElement && self.context.selectedDates[0]) {
                const dateObj = new Date(self.context.selectedDates[0]);
                if (self.context.selectedTime) {
                    const parts = self.context.selectedTime.split(':');
                    if (parts.length === 2) {
                        const hours = parseInt(parts[0], 10);
                        const minutes = parseInt(parts[1], 10);
                        dateObj.setHours(hours, minutes);
                    }
                }
                const day = ('0' + dateObj.getDate()).slice(-2);
                const month = ('0' + (dateObj.getMonth() + 1)).slice(-2);
                const year = dateObj.getFullYear();
                const hours = ('0' + dateObj.getHours()).slice(-2);
                const minutes = ('0' + dateObj.getMinutes()).slice(-2);
                self.context.inputElement.value = `${day}/${month}/${year} ${hours}:${minutes}`;
            }
        },
        selectedTheme: "dark"
    });
    calendar.init();
} catch (e) {
    console.log(e);
}
