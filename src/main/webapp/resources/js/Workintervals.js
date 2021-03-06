/**
 * Created by igortsapyak on 27.05.16.
 */
$(document).ready(function () {
    var principal = $('#principal').text();
    var did = document.getElementById("1").textContent;
    var begin;
    var end;
    var dayOfAppointment;
    var schedulerConfig;
    var blockYear;
    var blockMonth;
    var blockDay;
    $.ajax({
        type: "GET",
        async: false,
        url: "getWorkScheduler?id=" + did,
        datatype: "json",
        contentType: "application/json",
        mimeType: "application/json",
        success: function (data) {
            schedulerConfig = data[data.length - 1];
            data.splice(data.length - 1, 1);
            data.forEach(function (item, i) {
                var workDay = data[i].start_date.substring(0, 10);
                var hourOne = data[i].start_date.substring(11, 13);
                var hourLast = data[i].end_date.substring(11, 13);
                    scheduler.blockTime(new Date(workDay), [0, hourOne * 60, hourLast * 60,
                        24 * 60]);
                    console.log(i + ": " + item.start_date + " " + item.end_date)
            });
            blockYear = new Date(data[data.length - 1].start_date.substring(0, 10)).getFullYear();
            blockMonth = new Date(data[data.length - 1].start_date.substring(0, 10)).getMonth();
            blockDay = new Date(data[data.length - 1].start_date.substring(0, 10)).getDate() + 1;
        },
        error: function () {
            $('#mySchedulerErrorModal').modal('show');
        }
    });
    var dayOfWeek = new Date().getDay() - 1;
    var day = new Date().getDate() - 1;
    var month = new Date().getMonth();
    var year = new Date().getFullYear();
    var hour = new Date().getHours() + 1;
    var step = schedulerConfig.appSize.substring(0, 2);
    var format = scheduler.date.date_to_str("%H:%i");
    scheduler.config.hour_size_px = (60 / step) * 44;
    scheduler.config.time_step = step;
    while (day >= 1) {
        scheduler.blockTime(new Date(year, month, day), "fullday");
        day--;
    }
    while (dayOfWeek >= 1) {
        scheduler.blockTime(new Date(year, month, day), "fullday");
        dayOfWeek--;
        day--;
    }
    scheduler.blockTime(new Date(), [0 * 60, hour * 60]);
    var deysCount = schedulerConfig.weekSize;
    scheduler.config.xml_date = "%Y-%m-%d %H:%i";
    scheduler.config.details_on_dblclick = true;
    scheduler.config.details_on_create = true;
    switch (schedulerConfig.weekSize) {
        case '5' :
        {
            scheduler.ignore_week = function (date) {
                if (date.getDay() == 6 || date.getDay() == 0)
                    return true;
            };
        }
            break;
        case '6':
        {
            scheduler.ignore_week = function (date) {
                if (date.getDay() == 0)
                    return true;
            };
        }
            break;
    }

    $.ajax({
        type: "GET",
        async: false,
        url: "getAppointments?id=" + did,
        datatype: "json",
        contentType: "application/json",
        mimeType: "application/json",
        success: function (data) {
            data.forEach(function (item, i) {
                dayOfAppointment = data[i].start_date.substring(0, 10);
                var start_date_hour = parseInt(data[i].start_date.substring(11, 13));
                var start_date_minutes = parseInt(data[i].start_date.substring(14, 16));
                var end_date_hour = parseInt(data[i].end_date.substring(11, 13));
                var end_date_minutes = parseInt(data[i].end_date.substring(14, 16));
                var minutes = (start_date_hour * 60 + start_date_minutes) / step;
                var minutes2 = (end_date_hour * 60 + end_date_minutes) / step;
                begin = minutes;
                end = minutes2;
                    scheduler.blockTime(new Date(dayOfAppointment), [begin * step, end * step]);
            });
        }
    });

    scheduler.config.readonly = (!$('#patient').val());
    scheduler.config.first_hour = schedulerConfig.dayStart;
    scheduler.config.last_hour = schedulerConfig.dayEnd;
    scheduler.config.limit_time_select = true;
    scheduler.init('scheduler_here', null, "week");
    scheduler.config.limit_start = new Date(year, month, day);
    scheduler.config.limit_end = new Date(blockYear, blockMonth, blockDay);
    //scheduler.load('getAppointmentsByPatient?patient='+principal,'json');
    var dp = new dataProcessor("supplyAppointment?id=" + did + "&principal=" + principal);
    dp.init(scheduler);
});
var html = function (id) {
    return document.getElementById(id);
};
scheduler.showLightbox = function (id) {
    var tex_local_from = getMessage('workscheduler.modal.appointment.time.from');
    var tex_local_to = getMessage('workscheduler.modal.appointment.time.to');
    $('#myModal').modal('show');
    var ev = scheduler.getEvent(id);
    scheduler.startLightbox(id, html("myModal"));
    $('#date').text(new Date(ev.start_date).toLocaleDateString() + ' ' + tex_local_from + ' '+
        new Date(ev.start_date).toLocaleTimeString().replace(':00', '') + ' ' + tex_local_to + ' ' +
        new Date(ev.end_date).toLocaleTimeString().replace(':00', ''));
    $('#doctorName').text($('#profDoctorsName').text());
    html("TheReasonForVisit").focus();
};
function save_form() {
    blockAppointmensAdd();
    var ev = scheduler.getEvent(scheduler.getState().lightbox_id);
    ev.text = html("TheReasonForVisit").value;
    scheduler.endLightbox(true, html("my_form"));
}

function close_form() {
    scheduler.endLightbox(false, html("my_form"));
}

function delete_event() {
    var event_id = scheduler.getState().lightbox_id;
    scheduler.endLightbox(false, html("my_form"));
    scheduler.deleteEvent(event_id);
}

function blockAppointmensAdd() {
    location.reload();
    scheduler.config.readonly = true;
}
scheduler.attachEvent("onLimitViolation", function (id, obj) {
    dhtmlx.message('The appointment is not allowed at this time');
});

function dismissMyModal(event) {
    if ($(event.target).is('#myModal')) {
        close_form();
    }
}

function startModal() {
    setTimeout(changeModalContentFirstStep, 500);
    setTimeout(changeModalContentSecondStep, 4000);
}

function changeModalContentFirstStep() {
    $('#modb').slideToggle(500);
    $('#modal-header').slideToggle(500);
    $('#modal-footer').slideToggle(500);
    $('#modbs').slideToggle(500);
}

function changeModalContentSecondStep() {
    save_form();
    $('#myModal').modal('hide');
}


$(document).keyup(function (e) {
    if (e.keyCode == 27) { // escape key maps to keycode `27`
        close_form();
    }
});

function goBack() {
    window.history.back();
}


