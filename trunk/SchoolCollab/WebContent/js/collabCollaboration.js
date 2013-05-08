/**
 * Ready function
 */
$(document).ready(function() {
	buildRequest('getFileNameUsed');
	buildRequest('getResponseYearsRange');
});
/**
 * Refresh the author view.
 */
function refresAuthorView(header) {
	var parentDivs = $('#nestedAccordion div'), childDivs = $(
			'#nestedAccordion h3').siblings('div');
	grandDivs = $('#nestedAccordion h4').siblings('div');
	// Validate if the header to update is h2
	if (header == 'h2') {
		$('#nestedAccordion h2').click(function() {
			parentDivs.slideUp();
			if ($(this).next().is(':hidden')) {
				$(this).next().slideDown();
			} else {
				$(this).next().slideUp();
			}
		});
	}
}
/**
 * Populate Years List
 */
function populateYearsList() {
	// Clean the nested accordion
	$("#yearMin").empty();
	$("#yearMax").empty();
	// Get all the years.
	$years = $xml.find('year');
	// Loop for each year
	$years.each(function(index, element) {
		// Take the value
		$yearValue = $(element).text();
		$option = "<option value='" + $yearValue + "'>" + $yearValue
				+ "</option>";
		$("#yearMin").append($option);
		$("#yearMax").append($option);
	});
}
/**
 * Prepare request for alphabet
 */
function prepareRequestAlphabet() {
	// Clean alphabet and nested accordion
	$("#nestedAccordion").empty();
	$("#alphabet").empty();
	cleanDivs();
	var $yearMin = "";
	var $yearMax = "";
	var $publicationType = "";
	// Take the values from the web page
	var $checkBokYear = $("#yearFilter");
	if ($checkBokYear.is(":checked")) {
		$yearMin = $("#yearMin").val();
		$yearMax = $("#yearMax").val();
	}
	var $checkBokPubType = $("#pubTypeFilter");
	if ($checkBokPubType.is(":checked")) {
		$publicationType = $("#pubType").val();
	}
	// Make the request
	buildRequest('getAuthorNameInitialLettersByPubtypeOrByYear', $yearMin,
			$yearMax, $publicationType);
}
/**
 * Prepare request for co-authors with first letter
 * 
 * @param firstLetter
 */
function prepareCoAuthorsRequest(firstLetter) {
	$("#nestedAccordion").empty();
	cleanDivs();
	spinnerInit('alphabet');
	var $yearMin = "";
	var $yearMax = "";
	var $publicationType = "";
	// Take the values from the web page
	var $checkBokYear = $("#yearFilter");
	if ($checkBokYear.is(":checked")) {
		$yearMin = $("#yearMin").val();
		$yearMax = $("#yearMax").val();
	}
	var $checkBokPubType = $("#pubTypeFilter");
	if ($checkBokPubType.is(":checked")) {
		$publicationType = $("#pubType").val();
	}
	// Make the request
	buildRequest('getCoauthorResponseByPubTypeByYearFilter', $yearMin,
			$yearMax, $publicationType, firstLetter);
}
/**
 * Function to prepare the JSON request for the server
 * 
 * @param authorName
 */
function prepareCollaborationJsonRequest(authorName) {
	buildRequest("getCollaborationByAuthorNameJson", authorName);
}
/**
 * Create the alphabet in the page
 */
function populateAlphabet() {
	// Alphabet
	var str = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	var $letters = $xml.find('initialLetter');
	var $initialLetters = "";
	$letters.each(function(index, element) {
		$initialLetters += $(element).text();
	});
	if ($initialLetters.length > 0) {
		// Loop
		for ( var i = 0; i < str.length; i++) {
			var nextChar = str.charAt(i);
			if ($initialLetters.indexOf(nextChar) != -1) {
				// Create the list with the alphabet.
				$list = "<li onclick=\"prepareCoAuthorsRequest('" + nextChar
						+ "')\" class=\"letter\"><a>" + nextChar + "</a></li>";
			} else {
				// Create the list with the alphabet.
				$list = "<li class=\"letterNoLink\">" + nextChar + "</li>";
			}
			$("#alphabet").append($list);
		}
	} else {
		$message = "No author with co-authors were found in the years and publication type selected.";
		$("#alphabet").append($message);
	}
}
/**
 * Populate the level one of collaboration
 */
function populateCollaborationLevelOne() {
	// Get all the authors.
	$collectionAuthors = $xml.find('author');
	// Loop for each author
	$collectionAuthors
			.each(function(index, element) {
				$authorName = $(element).children("name").text();
				$numberCoAuthors = $(element).children("number").text();
				// Clean the author's name for an id
				$authorId = cleanString($authorName);
				// Create the header 2
				$header = "<h2><span class=\"accordionEmphasis\">["
						+ $authorName
						+ "]</span><span class=\"accordionStrong\">["
						+ $numberCoAuthors
						+ "] co-authors</span>"
						+ " <span><img src='images/collab.png' alt='Collaboration' onclick=\"prepareCollaborationJsonRequest(\'"
						+ $authorName + "\')\"></img></span></h2><div id=\'"
						+ $authorId + "\'/>";
				$("#nestedAccordion").append($header);
				// Validate
				if ($numberCoAuthors > 0) {
					// Take the sibling Id of Header 2
					$coAuthorsDivs = $("#" + $authorId);
					// Create header 3 for book
					$collectionCoAuthors = $(element).find("co-author");
					$collectionCoAuthors
							.each(function(innerIndex, innerElement) {
								$idCoAuthor = $authorId + innerIndex;
								$header = "<h3 id=\'"
										+ $idCoAuthor
										+ "'><span>"
										+ $(innerElement).children("name")
												.text()
										+ "</span> ["
										+ $(innerElement).children("number")
												.text() + "] co-authors</h3>";
								$($coAuthorsDivs).append($header);
							});
				}
			});
	// Refresh the headers
	refresAuthorView('h2');
	refresAuthorView('h3');
	spinnerStop();
}
/**
 * Clean all the special characters to use the string as an id in html.
 * 
 * @param completeString
 * @returns
 */
function cleanString(completeString) {
	// Clean white spaces
	completeString = completeString.replace(/\s/g, '');
	// Clean special characters.
	return completeString.replace(/[&\/\\#,+()$~%.'":*?<>{}]/g, '');
};
/**
 * Activate the year list
 */
function activateDeactivateYearFilter() {
	var $checkBokYear = $("#yearFilter");
	if ($checkBokYear.is(":checked")) {
		$("#yearMin").prop("disabled", false);
		$("#yearMax").prop("disabled", false);
	} else {
		$("#yearMin").prop("disabled", "disabled");
		$("#yearMax").prop("disabled", "disabled");
	}
}
/**
 * Activate the publication type list
 */
function activateDeactivatePubTypeFilter() {
	var $checkBokPubType = $("#pubTypeFilter");
	if ($checkBokPubType.is(":checked")) {
		$("#pubType").prop("disabled", false);
	} else {
		$("#pubType").prop("disabled", "disabled");
	}
}
/**
 * Clean the divs for the network diagram
 */
function cleanDivs() {
	$("#container").empty();
	div = "<div id='center-container'>";
	$("#container").append(div);
	element = "<h1 id='titleNetwork' style='display:none;'>Overall Collaboration</h1>";
	$("#center-container").append(element);
	div = "<div id='infovis'>";
	$("#center-container").append(div);
	div = "<div id='log'>";
	$("#container").append(div);
}