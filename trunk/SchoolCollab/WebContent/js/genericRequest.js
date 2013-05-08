// Servlet Name
var $url = "QueryController";
// XML response
var $xml = null;
var $jsonResponse = null;
// Parameters variable
var $parameters = null;
// Spinner
var spinner = null;
// File used
var $file_used = null;
/**
 * Make an Ajax Request with JQuery.get() function
 */
function makeRequest() {
	// Make the request with the URL plus parameters if any.
	return $.get($url + $parameters, function(data) {
		// Take the response and store in the XML variable
		$xml = $(data);
	});
}
/**
 * Make an Ajax Request with JQuery.get() function and received JSon Object
 */
function makeJsonRequest() {
	// Make the request with the URL plus parameters if any.
	return $.getJSON($url + $parameters, function(data) {
		// Take the response and store in the XML variable
		$jsonResponse = data;
	});
}
/**
 * Build the request based on the operation and parameters.
 */
function buildRequest(operation, param1, param2, param3, param4) {
	// Clean the parameters for new request.
	$parameters = "";
	// Set the parameters.
	$parameters += "?query=" + operation;
	// Validate if the operations is any summary request.
	if (operation == "getTypesSummary") {
		// Make request.
		makeRequest().done(function() {
			// Validate the response
			validateResponse();
			// Populate Level one
			populatePublicationsLevelOne();
		});
	} else if (operation == "getAuthorsSummary") {
		$parameters += "&firstLetter=" + param1;
		// Make request.
		makeRequest().done(function() {
			// Validate the response
			validateResponse();
			// Populate Level one
			populateAuthorsLevelOne();
		});
	} else if (operation == "getYearSummary") {
		// Make request.
		makeRequest().done(function() {
			// Validate the response
			validateResponse();
			// Populate Level one
			populateYearsLevelOne();
		});
	}
	// For publication by type request.
	else if (operation == "getPublicationByType") {
		// Set the parameters.
		$parameters += "&publicationType=" + param1;
		// Make request.
		makeRequest().done(function() {
			// Validate the response
			validateResponse();
			// Populate Level one
			populatePublicationsLevelTwo(param1);
		});
	}
	// For publication by type and author name request.
	else if (operation == "getPublicationByAuthorByType") {
		// Set the parameters.
		$parameters += "&authorName=" + param1;
		$parameters += "&publicationType=" + param2;
		// Make request.
		makeRequest().done(function() {
			// Validate the response
			validateResponse();
			// Populate Level one
			populateAuthorsLevelTwo(param1, param2);
		});
	}
	// For publication by type and year request.
	else if (operation == "getPublicationsByYearByType") {
		// Set the parameters.
		$parameters += "&year=" + param1;
		$parameters += "&publicationType=" + param2;
		// Make request.
		makeRequest().done(function() {
			// Validate the response
			validateResponse();
			// Populate Level one
			populateYearsLevelTwo(param1, param2);
		});
	}
	// Statistics request
	// Stats of publications and authors
	else if (operation == "getStatsPublicationsAuthors") {
		// Make request.
		makeRequest().done(function() {
			// Validate the response
			validateResponse();
			// Populate Level one
			populatePublicationsAuthorsStatsTableLevelTwo();
		});
	}
	// Stats of publications and authors in years
	else if (operation == "getStatsByYears") {
		// Make request.
		makeRequest().done(function() {
			// Validate the response
			validateResponse();
			// Populate Level one
			populateYearsStatisticTableLevelOne();
		});
	}
	// Collaboration
	// Get initial letters by author
	else if (operation == "getAuthorNameInitialLettersByPubtypeOrByYear") {
		// Set the parameters.
		$parameters += "&yearMin=" + param1;
		$parameters += "&yearMax=" + param2;
		$parameters += "&publicationType=" + param3;
		// Make request.
		makeRequest().done(function() {
			// Validate the response
			validateResponse();
			// Populate Level one
			populateAlphabet();
		});
	}
	// Get coauthors by first letter
	else if (operation == "getCoauthorResponseByPubTypeByYearFilter") {
		// Set the parameters.
		$parameters += "&yearMin=" + param1;
		$parameters += "&yearMax=" + param2;
		$parameters += "&publicationType=" + param3;
		$parameters += "&firstLetter=" + param4;
		// Make request.
		makeRequest().done(function() {
			// Validate the response
			validateResponse();
			// Populate Level one
			populateCollaborationLevelOne();
		});
	}
	// Get years range
	else if (operation == "getResponseYearsRange") {
		// Make request.
		makeRequest().done(function() {
			// Validate the response
			validateResponse();
			// Populate Level one
			populateYearsList();
		});
	}
	// Get years range
	else if (operation == "getFileNameUsed") {
		// Make request.
		makeRequest().done(function() {
			// Validate the response
			validateResponse();
			// Populate Level one
			setFileNameUsed();
		});
	}
	// Get collaboration by author name
	else if (operation == "getCollaborationByAuthorNameJson") {
		// Set the parameters.
		$parameters += "&authorName=" + param1;
		// Make request.
		makeJsonRequest().done(function() {
			// Validate the response
			validateJsonResponse();
			// Call the init function from Tree visualizer
			init($jsonResponse);
		});
	}
	// if any of the previous request were valid, go to the validate
	// response with the XML variable null and create the error message.
	else {
		// Validate.
		validateResponse();
	}
}
/**
 * Validate the XML response after an Ajax request.
 */
function validateResponse() {
	// Validate null variable
	if ($xml === null) {
		alert("Null Error");
	}
	// Validate if the xml contains the tag <error>
	// generated in the server response.
	else if ($xml.find("error").size() > 0) {
		alert("Server Error");
	}
}

function validateJsonResponse() {
	// Validate null variable
	if ($jsonResponse === null) {
		alert("Null Error");
	}
}
/**
 * Initialize the spinner
 * 
 * @param targetPosition
 *            Id of the html element to put the spinner.
 */
function spinnerInit(targetPosition) {
	var opts = {
		lines : 13, // The number of lines to draw
		length : 20, // The length of each line
		width : 10, // The line thickness
		radius : 30, // The radius of the inner circle
		corners : 1, // Corner roundness (0..1)
		rotate : 0, // The rotation offset
		direction : 1, // 1: clockwise, -1: counterclockwise
		color : '#000', // #rgb or #rrggbb
		speed : 1, // Rounds per second
		trail : 60, // Afterglow percentage
		shadow : false, // Whether to render a shadow
		hwaccel : false, // Whether to use hardware acceleration
		className : 'spinner', // The CSS class to assign to the spinner
		zIndex : 2e9, // The z-index (defaults to 2000000000)
		top : 'auto', // Top position relative to parent in px
		left : 'auto' // Left position relative to parent in px
	};
	spinner = new Spinner(opts).spin(document.getElementById(targetPosition));
}
/**
 * Stop the spinner
 */
function spinnerStop() {
	spinner.stop();
}
/**
 * Set the file name used
 */
function setFileNameUsed() {
	$file_used = "File Used : " + $xml.find("filename").text();
	$("#filename").html($file_used);
}