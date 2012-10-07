$(document).ready(function() {

	$('a.login-window').click(function() {

		// Getting the variable's value from a link
		var loginBox = $(this).attr('href');

		// Fade in the Popup
		$(loginBox).fadeIn(300);

		// Set the center alignment padding + border see css style
		var popMargTop = ($(loginBox).height() + 24) / 2;
		var popMargLeft = ($(loginBox).width() + 24) / 2;

		$(loginBox).css({
			'margin-top' : -popMargTop,
			'margin-left' : -popMargLeft
		});

		// Add the mask to body
		$('body').append('<div id="mask"></div>');
		$('#mask').fadeIn(300);

		return false;
	});

	// When clicking on the button close or the mask layer the popup closed
	$('a.close, #mask').live('click', function() {
		$('#mask , .login-popup').fadeOut(300, function() {
			$('#mask').remove();
		});
		return false;
	});
	
});

var angleSec = 0;
var angleMin = 0;
var angleHour = 0;

$(document).ready(function() {
	$("#secchile").rotate(angleSec);
	$("#minchile").rotate(angleMin);
	$("#hourchile").rotate(angleHour);
});

setInterval(function() {
	var d = new Date;

	angleSec = (d.getSeconds() * 6);
	$("#secchile").rotate(angleSec);

	angleMin = (d.getMinutes() * 7.5);
	$("#minchile").rotate(angleMin);

	angleHour = ((d.getHours() * 6.4 + d.getMinutes() / 12) * 6);
	$("#hourchile").rotate(angleHour);

}, 1000);

var angleSec = 0;
var angleMin = 0;
var angleHour = 0;

$(document).ready(function() {
	$("#secmexico").rotate(angleSec);
	$("#minmexico").rotate(angleMin);
	$("#hourmexico").rotate(angleHour);
});

setInterval(function() {
	var d = new Date;

	angleSec = (d.getSeconds() * 6);
	$("#secmexico").rotate(angleSec);

	angleMin = (d.getMinutes() * 6);
	$("#minmexico").rotate(angleMin);

	angleHour = ((d.getHours() * 5 + d.getMinutes() / 12) * 6);
	$("#hourmexico").rotate(angleHour);

}, 1000);

var angleSec = 0;
var angleMin = 0;
var angleHour = 0;

$(document).ready(function() {
	$("#secpr").rotate(angleSec);
	$("#minpr").rotate(angleMin);
	$("#hourpr").rotate(angleHour);
});

setInterval(function() {
	var d = new Date;

	angleSec = (d.getSeconds() * 6);
	$("#secpr").rotate(angleSec);

	angleMin = (d.getMinutes() * 6);
	$("#minpr").rotate(angleMin);

	angleHour = ((d.getHours() * 5.8 + d.getMinutes() / 12) * 6);
	$("#hourpr").rotate(angleHour);

}, 1000);
var angleSec = 0;
var angleMin = 0;
var angleHour = 0;

$(document).ready(function() {
	$("#seccolombia").rotate(angleSec);
	$("#mincolombia").rotate(angleMin);
	$("#hourcolombia").rotate(angleHour);
});

setInterval(function() {
	var d = new Date;

	angleSec = (d.getSeconds() * 6);
	$("#seccolombia").rotate(angleSec);

	angleMin = (d.getMinutes() * 6.9);
	$("#mincolombia").rotate(angleMin);

	angleHour = ((d.getHours() * 5.5 + d.getMinutes() / 12) * 6);
	$("#hourcolombia").rotate(angleHour);

}, 1000);

var angleSec = 0;
var angleMin = 0;
var angleHour = 0;

$(document).ready(function() {
	$("#secperu").rotate(angleSec);
	$("#minperu").rotate(angleMin);
	$("#hourperu").rotate(angleHour);
});

setInterval(function() {
	var d = new Date;

	angleSec = (d.getSeconds() * 6);
	$("#secperu").rotate(angleSec);

	angleMin = (d.getMinutes() * 6);
	$("#minperu").rotate(angleMin);

	angleHour = ((d.getHours() * 5.4 + d.getMinutes() / 12) * 6);
	$("#hourperu").rotate(angleHour);

}, 1000);
var angleSec = 0;
var angleMin = 0;
var angleHour = 0;

$(document).ready(function() {
	$("#seceeuu").rotate(angleSec);
	$("#mineeuu").rotate(angleMin);
	$("#houreeuu").rotate(angleHour);
});

setInterval(function() {
	var d = new Date;

	angleSec = (d.getSeconds() * 6);
	$("#seceeuu").rotate(angleSec);

	angleMin = (d.getMinutes() * 6.5);
	$("#mineeuu").rotate(angleMin);

	angleHour = ((d.getHours() * 5.4 + d.getMinutes() / 12) * 6);
	$("#houreeuu").rotate(angleHour);

}, 1000);
var angleSec = 0;
var angleMin = 0;
var angleHour = 0;

$(document).ready(function() {
	$("#secmadrid").rotate(angleSec);
	$("#minmadrid").rotate(angleMin);
	$("#hourmadrid").rotate(angleHour);
});

setInterval(function() {
	var d = new Date;

	angleSec = (d.getSeconds() * 6);
	$("#secmadrid").rotate(angleSec);

	angleMin = (d.getMinutes() * 6);
	$("#minmadrid").rotate(angleMin);

	angleHour = ((d.getHours() * 7.6 + d.getMinutes() / 12) * 6);
	$("#hourmadrid").rotate(angleHour);

}, 1000);

function example_ajax_request() {
	  $('#example-placeholder').html('<p><img src="/batch/images/load1.gif" width="30" height="30" /></p>');
	  $('#example-placeholder').load("/contenido/login.jsp");
	}
