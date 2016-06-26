/**
 * 
 */
$(document).ready(function() {
	$("#pref-perpage").children().each(function() {

		if ($(this).hasClass('selected')) {
			$(this).attr('selected', 'selected');
		}
	});
	$("#map-button").tooltip();
	jQuery.goup({trigger: 100,
        containerSize: 50,
        bottomOffset: 25,
        locationOffset: 25,
        containerRadius:75,
        title: 'Back To Top',
        entryAnimation:'slide',
        containerColor: '#428bca',
        titleAsText: true});
});
function sendPageConfig(event) {
	var itemPerPage = $('#pref-perpage option:selected').text();
	if (itemPerPage === "" || itemPerPage == null) {
		itemPerPage = "10";
	}
	var sort = $("#pref-orderby").val();
	var url = $('#path').val() + '/hospital/page/config';
	$.get(url, {
		'itemPerPage' : itemPerPage,
		'type' : sort
	}, function(data) {
		window.location.replace($('#path').val() + "/hospitals");
	});

}

function showModalMap(lat, lng) {
	
	
	$("#modalMap").modal();

	var latLng = new google.maps.LatLng(lat, lng);

	$('#modalMap').on(
			'shown.bs.modal',
			function() {
				var mapProp = {
					zoom : 17,
					mapTypeId : google.maps.MapTypeId.ROADMAP,
					mapTypeControl : false,
					streetViewControl : false
				};
				map = new google.maps.Map(document.getElementById('googleMap'),
						mapProp);

				map.setCenter(latLng);
				
				marker = new google.maps.Marker({
					position : latLng,
					map : map
				});
				google.maps.event.addListener(marker, 'click', function() {
					window.location = "./hospital/" + $("#map_id").data("id");
				});
			});
	
}
