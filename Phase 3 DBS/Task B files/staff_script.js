document.addEventListener('DOMContentLoaded', () => {

    //Sample Appointment Data
    const allAppointments = [
        //Patient 1: John Doe
        { patientId: 'p1', patientName: 'John Doe', date: '2023-05-10', time: '09:00 AM', description: 'Initial Consult (Past)', id: 'appt-101' },
        { patientId: 'p1', patientName: 'John Doe', date: '2023-09-15', time: '02:00 PM', description: 'Checkup (Past)', id: 'appt-102' },
        { patientId: 'p1', patientName: 'John Doe', date: '2022-10-25', time: '10:00 AM', description: 'Old Annual Checkup (Past)', id: 'appt-100' },
        { patientId: 'p1', patientName: 'John Doe', date: '2023-11-01', time: '09:00 AM', description: 'Quick Check', id: 'appt-125' },
        { patientId: 'p1', patientName: 'John Doe', date: '2023-11-15', time: '02:30 PM', description: 'Follow-up', id: 'appt-124' },
        { patientId: 'p1', patientName: 'John Doe', date: '2023-11-28', time: '11:00 AM', description: 'Results Review', id: 'appt-126' },
        { patientId: 'p1', patientName: 'John Doe', date: '2023-12-12', time: '03:00 PM', description: 'Pre-holiday Check', id: 'appt-127' },
        { patientId: 'p1', patientName: 'John Doe', date: '2024-01-20', time: '11:00 AM', description: 'Future Checkup', id: 'appt-150' },
        //Patient 2: Jane Smith
        { patientId: 'p2', patientName: 'Jane Smith', date: '2023-08-01', time: '09:30 AM', description: 'Blood Work (Past)', id: 'appt-149' },
        { patientId: 'p2', patientName: 'Jane Smith', date: '2023-08-18', time: '01:00 PM', description: 'Lab Results (Past)', id: 'appt-150' },
        { patientId: 'p2', patientName: 'Jane Smith', date: '2023-11-01', time: '11:30 AM', description: 'Consultation', id: 'appt-201' },
        { patientId: 'p2', patientName: 'Jane Smith', date: '2023-11-09', time: '10:00 AM', description: 'Physical Therapy', id: 'appt-202' },
        { patientId: 'p2', patientName: 'Jane Smith', date: '2023-11-22', time: '09:00 AM', description: 'Follow-up PT', id: 'appt-203' },
        { patientId: 'p2', patientName: 'Jane Smith', date: '2023-12-05', time: '10:30 AM', description: 'Specialist Consult', id: 'appt-204' },
        { patientId: 'p2', patientName: 'Jane Smith', date: '2024-02-10', time: '01:00 PM', description: 'Post-Specialist F/U', id: 'appt-205' },

        { patientId: 'p3', patientName: 'Bob Johnson', date: '2023-11-15', time: '10:00 AM', description: 'New Patient Visit', id: 'appt-301' },
        { patientId: 'p3', patientName: 'Bob Johnson', date: '2023-12-20', time: '02:00 PM', description: 'Procedure Consult', id: 'appt-302' },
    ];


    //Helper Functions (Unchanged)
    const M_NAMES = ["January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"];

    function formatDateYYYYMMDD(date) {
        const y = date.getFullYear();
        const m = String(date.getMonth() + 1).padStart(2, '0');
        const d = String(date.getDate()).padStart(2, '0');
        return `${y}-${m}-${d}`;
    }

     function parseTimeToMinutes(timeStr) {
        const lowerTime = timeStr.toLowerCase();
        const parts = lowerTime.match(/(\d+):(\d+)\s*(am|pm)?/);
        if (!parts) return 0;
        let hours = parseInt(parts[1], 10);
        const minutes = parseInt(parts[2], 10);
        const modifier = parts[3];
        if (modifier === "am" && hours === 12) { hours = 0; }
        else if (modifier === "pm" && hours !== 12) { hours += 12; }
        return hours * 60 + minutes;
    }

    //Global Calendar Variables
    const staffCalendarTableBody = document.getElementById('staff-calendar-body');
    const calendarNavContainer = document.querySelector('.calendar-navigation'); // Use the new nav container
    let currentDisplayDate = new Date();

      //Calendar Rendering Logic
      function renderStaffCalendar(targetDate) {
        if (!staffCalendarTableBody || !calendarNavContainer) return;

        const monthYearDisplay = calendarNavContainer.querySelector('.month-year');
        const currentYear = targetDate.getFullYear();
        const currentMonth = targetDate.getMonth();

        monthYearDisplay.textContent = `${M_NAMES[currentMonth]} ${currentYear}`;
        staffCalendarTableBody.innerHTML = ''; 

        let firstDayOfMonth = new Date(currentYear, currentMonth, 1).getDay();

        const daysInMonth = new Date(currentYear, currentMonth + 1, 0).getDate();

        const appointmentsThisMonth = {};
        const today = new Date();
        today.setHours(0, 0, 0, 0); 

        allAppointments.forEach(appt => {
            const apptDate = new Date(appt.date + 'T00:00:00');
             if (isNaN(apptDate)) return;
            if (apptDate.getFullYear() === currentYear && apptDate.getMonth() === currentMonth) {
                const dayOfMonth = apptDate.getDate();
                if (!appointmentsThisMonth[dayOfMonth]) appointmentsThisMonth[dayOfMonth] = [];
                appointmentsThisMonth[dayOfMonth].push(appt);
            }
        });

        for (const day in appointmentsThisMonth) {
            appointmentsThisMonth[day].sort((a, b) => parseTimeToMinutes(a.time) - parseTimeToMinutes(b.time));
        }

        let date = 1;
        let currentWeekRow = document.createElement('tr');

        // Blank cells before the 1st
        for (let i = 0; i < firstDayOfMonth; i++) {
            const cell = document.createElement('td');
            cell.classList.add('other-month');
            currentWeekRow.appendChild(cell);
        }

        // Fill days of the month
        while (date <= daysInMonth) {
             // Determine how many cells needed in the current row
            let cellsInRow = currentWeekRow.getElementsByTagName('td').length;

            for (let i = cellsInRow; i < 7; i++) { // Start from current cell count up to 7
                if (date > daysInMonth) break;

                const cell = document.createElement('td');
                const cellDate = new Date(currentYear, currentMonth, date);
                cellDate.setHours(0,0,0,0);

                const dateNumSpan = document.createElement('span');
                dateNumSpan.classList.add('date-number');
                dateNumSpan.textContent = date;
                cell.appendChild(dateNumSpan);

                if (cellDate < today) {
                    cell.classList.add('past');
                } else if (cellDate.getTime() === today.getTime()) {
                    cell.classList.add('today');
                } else {
                    cell.classList.add('future');
                }

                if (appointmentsThisMonth[date]) {
                    appointmentsThisMonth[date].forEach(appt => {
                        const apptSpan = document.createElement('span');
                        apptSpan.classList.add('appt-detail');
                        apptSpan.innerHTML = `
                            <span class="patient-name">${appt.patientName}:</span>
                            ${appt.time} ${appt.description}
                            <a href="#edit-appt-${appt.id}">Edit</a>
                        `;
                        apptSpan.title = `${appt.patientName} - ${appt.time} - ${appt.description}`;
                        cell.appendChild(apptSpan);
                    });
                }

                currentWeekRow.appendChild(cell);
                date++;
            }
            staffCalendarTableBody.appendChild(currentWeekRow);
            currentWeekRow = document.createElement('tr'); 
        }

         //Fill remaining cells in the last week if it wasn't appended yet
        if (currentWeekRow.cells.length > 0 && currentWeekRow.cells.length < 7) {
             while (currentWeekRow.cells.length < 7) {
                 const cell = document.createElement('td');
                 cell.classList.add('other-month');
                 currentWeekRow.appendChild(cell);
             }
             staffCalendarTableBody.appendChild(currentWeekRow); 
        } else if (currentWeekRow.cells.length === 7) {
             staffCalendarTableBody.appendChild(currentWeekRow);
        }
    }


    //Find and display next appointment FOR A SPECIFIC PATIENT
    function displayNextAppointmentForPatient(patientId) {
         const displaySpan = document.querySelector(`li[data-patient-id="${patientId}"] .next-appointment-display`);
        if (!displaySpan) return;

        const patientAppts = allAppointments.filter(appt => appt.patientId === patientId);
        const now = new Date();
        let nextAppt = null;
        const todayDateOnly = new Date(now.getFullYear(), now.getMonth(), now.getDate());

        patientAppts.forEach(appt => {
             const apptDateOnly = new Date(appt.date + 'T00:00:00');
             if (isNaN(apptDateOnly)) return;

             if (apptDateOnly >= todayDateOnly) {
                try {
                    const timeParts = appt.time.match(/(\d+):(\d+)\s*(am|pm)?/i);
                    let hours = parseInt(timeParts[1], 10);
                    const minutes = parseInt(timeParts[2], 10);
                    const modifier = timeParts[3] ? timeParts[3].toLowerCase() : null;

                    if (modifier === "am" && hours === 12) hours = 0;
                    if (modifier === "pm" && hours !== 12) hours += 12;

                    const fullApptDateTime = new Date(apptDateOnly.getFullYear(), apptDateOnly.getMonth(), apptDateOnly.getDate(), hours, minutes);

                    if (!isNaN(fullApptDateTime) && fullApptDateTime > now) {
                        if (!nextAppt || fullApptDateTime.getTime() < new Date(`${nextAppt.date} ${nextAppt.time}`).getTime()) {
                            nextAppt = appt;
                        }
                    }
                } catch (e) {
                     if (!nextAppt && apptDateOnly > todayDateOnly) {
                         nextAppt = appt;
                     }
                }
            }
        });

        if (nextAppt) {
            displaySpan.textContent = `${nextAppt.date} ${nextAppt.time}`;
        } else {
            displaySpan.textContent = "None upcoming";
        }
    }

    //Populate patient-specific fallback appointment lists
    function populatePatientFallbackList(patientId) {
          const fallbackList = document.querySelector(`li[data-patient-id="${patientId}"] .appointment-list-fallback-patient`);
         if (!fallbackList) return;
         fallbackList.innerHTML = '';

         const patientAppts = allAppointments
             .filter(appt => appt.patientId === patientId)
             .sort((a, b) => new Date(b.date) - new Date(a.date));

        if(patientAppts.length === 0) {
            fallbackList.innerHTML = '<li>No appointment history.</li>';
            return;
        }

        const todayStr = formatDateYYYYMMDD(new Date());
        patientAppts.forEach(appt => {
             const li = document.createElement('li');
             const isFuture = new Date(appt.date + 'T00:00:00') >= new Date(todayStr + 'T00:00:00');
             li.innerHTML = `
                 ${appt.date} ${appt.time} - ${appt.description}
                 ${isFuture
                     ? `<a href="#edit-appt-${appt.id}">Edit</a>` 
                     : `<a href="#view-appt-${appt.id}">View</a>`
                  }
             `;
             fallbackList.appendChild(li);
         });
    }

    //Function to Enable Editing for a specific form 
    function enablePatientFormEdit(formElement) {
        const saveBtn = formElement.querySelector('.save-btn');
        const editBtn = formElement.querySelector('.edit-btn');

        Array.from(formElement.elements).forEach(el => {
            if (el.tagName === 'INPUT' || el.tagName === 'TEXTAREA' || el.tagName === 'SELECT') {
                el.removeAttribute("readonly");
                el.removeAttribute("disabled");

            }
        });

        if (saveBtn) saveBtn.style.display = "inline-block";
        if (editBtn) editBtn.style.display = "none"; 
    }

     //Function to Disable Editing
     function disablePatientFormEdit(formElement) {
        const saveBtn = formElement.querySelector('.save-btn');
        const editBtn = formElement.querySelector('.edit-btn');

        Array.from(formElement.elements).forEach(el => {
            if (el.tagName === 'INPUT' || el.tagName === 'TEXTAREA') {
                el.setAttribute("readonly", "true");
            }
             if (el.tagName === 'SELECT') {
                el.setAttribute("disabled", "true");
            }
        });

        if (saveBtn) saveBtn.style.display = "none";
        if (editBtn) editBtn.style.display = "inline-block"; 
    }


    //Initial Setup
    renderStaffCalendar(currentDisplayDate);

    const patientListItems = document.querySelectorAll('.patient-list-panel ul > li[data-patient-id]');
    patientListItems.forEach(item => {
        const patientId = item.dataset.patientId;
        const form = item.querySelector('.staff-edit-form');
        const editButton = item.querySelector('.edit-btn');

        if (patientId) {
            displayNextAppointmentForPatient(patientId);
            populatePatientFallbackList(patientId);
        }

        // Add event listener for the EDIT button for this specific patient
        if(editButton && form) {
             editButton.addEventListener('click', () => {
                 enablePatientFormEdit(form);
            });
        }

        // Add event listener for the SAVE button (simulated save)
        if (form) {
            form.addEventListener('submit', (event) => {
                event.preventDefault(); 
                console.log(`Simulating save for patient ${patientId || 'unknown'}`);
                disablePatientFormEdit(form); 
                alert('Patient information "saved" (simulation).');
            });
        }
    });

    //Add Event Listeners for Main Calendar Navigation
    if (calendarNavContainer) {
        const prevButton = calendarNavContainer.querySelector('.prev-month');
        const nextButton = calendarNavContainer.querySelector('.next-month');

        prevButton.addEventListener('click', () => {
            currentDisplayDate.setMonth(currentDisplayDate.getMonth() - 1);
            renderStaffCalendar(currentDisplayDate);
        });

        nextButton.addEventListener('click', () => {
            currentDisplayDate.setMonth(currentDisplayDate.getMonth() + 1);
            renderStaffCalendar(currentDisplayDate);
        });
    }

}); 