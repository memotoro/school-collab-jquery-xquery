/**
 * Ready function.
 */
$(document).ready(function() {
	buildRequest('getFileNameUsed');
});
/**
 * Upload function
 */
$(function() {
	$('#fileupload').fileupload({
		dataType : 'xml',
		done : function(e, data) {
			$xml = $(data.result);
			$message = $xml.find("error").text();
			validateUploadResponse();
			buildRequest('getFileNameUsed');
		},
		progressall : function(e, data) {
			var progress = parseInt(data.loaded / data.total * 100, 10);
			$('#progress .bar').css('width', progress + '%');
		}
	});
});
/**
 * Validate upload reponse
 */
function validateUploadResponse() {
	$("#error").empty();
	$("#error").hide();
	$("#success").empty();
	$("#success").hide();
	if ($xml.find("error").size() > 0) {
		$("#error").show();
		$("#error").html($xml.find("error").text());
	} else {
		$("#success").show();
		$("#success").html($xml.find("upload").text());
	}
}
/**
 * Clean the previous upload
 */
function cleanPreviousUpload() {
	$("#error").empty();
	$("#error").hide();
	$("#success").empty();
	$("#success").hide();
	$("#progress").empty();
	$("#progress").empty();
	$divProgress = "<div class='bar' style='width: 0%;'></div>";
	$("#progress").append($divProgress);
}