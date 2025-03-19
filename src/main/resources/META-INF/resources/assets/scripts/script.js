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
    const tomorrow = new Date();
    tomorrow.setDate(today.getDate() + 1);

    const { Calendar } = window.VanillaCalendarPro;
    const calendar = new Calendar("#start", {
        dateMin: tomorrow, // Ensure dateMin is set correctly
        inputMode: true,
        locale: "it-IT",
        positionToInput: "auto",
        onChangeToInput(self) {
            updateInputWithTime(self);
        },
        selectedTheme: "dark",
        selectionTimeMode: 24,
        timeMaxHour: 16,
        timeMinHour: 8
    });
    calendar.init();

    // Function to combine the date and time slider values
    function updateInputWithTime(self) {
        if (self.context.inputElement && self.context.selectedDates[0]) {
            // Adjust the selectors below if your time slider elements have different classes
            const hourEl = document.querySelector('.vc-time-hour');
            const minuteEl = document.querySelector('.vc-time-minute');
            let hours = "00", minutes = "00";
            if (hourEl && minuteEl) {
                hours = hourEl.value.padStart(2, '0');
                minutes = minuteEl.value.padStart(2, '0');
                console.log("Slider values:", hours, minutes);
            } else {
                console.log("Time slider elements not found. Check your selectors.");
            }
            // Use the date part from the current input value if available, or from selectedDates
            const currentValue = self.context.inputElement.value;
            const datePart = currentValue.split('T')[0] || new Date(self.context.selectedDates[0]).toISOString().split('T')[0];
            const formattedDate = `${datePart}T${hours}:${minutes}`;
            self.context.inputElement.value = formattedDate;
            console.log("Updated input value:", formattedDate);
        }
    }

    // Function to attach event listeners to time sliders
    function attachTimeListeners() {
        const hourEl = document.querySelector('.vc-time-hour');
        const minuteEl = document.querySelector('.vc-time-minute');
        if (hourEl) {
            hourEl.addEventListener('input', () => updateInputWithTime(calendar));
            hourEl.addEventListener('change', () => updateInputWithTime(calendar));
        }
        if (minuteEl) {
            minuteEl.addEventListener('input', () => updateInputWithTime(calendar));
            minuteEl.addEventListener('change', () => updateInputWithTime(calendar));
        }
    }

    // Use an interval to attach listeners in case the time slider elements are rendered after initialization
    const listenerInterval = setInterval(() => {
        const hourEl = document.querySelector('.vc-time-hour');
        const minuteEl = document.querySelector('.vc-time-minute');
        if (hourEl && minuteEl) {
            attachTimeListeners();
            clearInterval(listenerInterval);
        }
    }, 500);
} catch (e) {
    console.log(e);
}