/**
 * @author Oleksandr Mukonin
 * 
 */

var messages;

$.ajax({
	type: "POST",
	url: "getparams/messages",
	datatype: "json",
	contentType: 'application/json',
	mimeType: 'application/json',
	success: function(data) {
		messages = data;
	}
});

function getMessage(code) {
	return messages[code];
}

function getLocale() {
	var locale = document.cookie.replace(/(?:(?:^|.*;\s*)org\.springframework\.web\.servlet\.i18n\.CookieLocaleResolver\.LOCALE\s*\=\s*([^;]*).*$)|^.*$/, "$1");
	return locale ? locale : 'en';
}

$(window).load(function() {
	if (getLocale() == 'ua') {
		$.extend( $.validator.messages, {
			required: 'Це поле необхідно заповнити.',
			remote: 'Будь ласка, введіть правильне значення.',
			email: 'Будь ласка, введіть коректну адресу електронної пошти.',
			url: 'Будь ласка, введіть коректний URL.',
			date: 'Будь ласка, введіть коректну дату.',
			dateISO: 'Будь ласка, введіть коректну дату у форматі ISO.',
			number: 'Будь ласка, введіть число.',
			digits: 'Вводите потрібно лише цифри.',
			creditcard: 'Будь ласка, введіть правильний номер кредитної карти.',
			equalTo: 'Будь ласка, введіть таке ж значення ще раз.',
			extension: 'Будь ласка, виберіть файл з правильним розширенням.',
			maxlength: $.validator.format( 'Будь ласка, введіть не більше {0} символів.' ),
			minlength: $.validator.format( 'Будь ласка, введіть не менше {0} символів.' ),
			rangelength: $.validator.format( 'Будь ласка, введіть значення довжиною від {0} до {1} символів.' ),
			range: $.validator.format( 'Будь ласка, введіть число від {0} до {1}.' ),
			max: $.validator.format( 'Будь ласка, введіть число, менше або рівно {0}.' ),
			min: $.validator.format( 'Будь ласка, введіть число, більше або рівно {0}.' )
		} );
	}
});