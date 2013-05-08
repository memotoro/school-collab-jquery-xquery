/**
 * Ready function
 */
$(document).ready(function() {
	buildRequest('getFileNameUsed');
	iterateAlphabet();
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
	// Validate if the header to update is h3
	else if (header == 'h3') {
		$('#nestedAccordion h3').click(
				function() {
					buildRequest('getPublicationByAuthorByType', $(this).attr(
							"authorName"), $(this).attr("id"));
					childDivs.slideUp();
					if ($(this).next().is(':hidden')) {
						$(this).next().slideDown();
					} else {
						$(this).next().slideUp();

					}
				});
	}
	// Validate if the header to update is h4
	else if (header == 'h4') {
		$('#nestedAccordion h4').click(function() {
			grandDivs.slideUp();
			if ($(this).next().is(':hidden')) {
				$(this).next().slideDown();
			} else {
				$(this).next().slideUp();
			}
		});
	}
}
/**
 * Populate Level One Author info.
 */
function populateAuthorsLevelOne() {
	// Clean the nested accordion
	$("#nestedAccordion").empty();
	// Get all the authors.
	$collectionAuthors = $xml.find('author');
	// Loop for each author
	$collectionAuthors.each(function(index, element) {
		// Clean the author's name for an id
		$authorId = cleanString($(element).find("name").text());
		// Create the header 2
		$header = "<h2><span class=\"accordionEmphasis\">["
				+ $(element).find("name").text()
				+ "]</span><span class=\"accordionStrong\">"
				+ $(element).find("totalpublications").text()
				+ "</span> publications</h2><div id=\'" + $authorId + "\'/>";
		$("#nestedAccordion").append($header);
		// Take the sibling Id of Header 2
		$publicationDivs = $("#" + $authorId);
		// Create header 3 for book
		$idAuthorPublication = $authorId + 'book';
		$header = "<h3 id=\'book\' authorName=\'"
				+ $(element).find("name").text()
				+ "\'><span class=\"numberOfBooks\">"
				+ $(element).find("book").text()
				+ "</span> Books</h3><div id=\'" + $idAuthorPublication
				+ "\'/>";
		$publicationDivs.append($header);
		// Create header 3 for article
		$idAuthorPublication = $authorId + 'article';
		$header = "<h3 id=\'article\' authorName=\'"
				+ $(element).find("name").text()
				+ "\'><span class=\"numberOfArticles\">"
				+ $(element).find("article").text()
				+ "</span> Articles</h3><div id=\'" + $idAuthorPublication
				+ "\'/>";
		$publicationDivs.append($header);
		// Create header 3 for inproceeding
		$idAuthorPublication = $authorId + 'inproceedings';
		$header = "<h3 id=\'inproceedings\' authorName=\'"
				+ $(element).find("name").text()
				+ "\'><span class=\"numberOfInproceedings\">"
				+ $(element).find("inproceedings").text()
				+ "</span> Inproceedings</h3><div id=\'" + $idAuthorPublication
				+ "\'/>";
		$publicationDivs.append($header);
		// Create header 3 for incollection
		$idAuthorPublication = $authorId + 'incollection';
		$header = "<h3 id=\'incollection\' authorName=\'"
				+ $(element).find("name").text()
				+ "\'><span class=\"numberOfIncollections\">"
				+ $(element).find("incollection").text()
				+ "</span> Incollections</h3><div id=\'" + $idAuthorPublication
				+ "\'/>";
		$publicationDivs.append($header);
	});
	// Refresh the headers
	refresAuthorView('h2');
	refresAuthorView('h3');
	spinnerStop();
}
/**
 * Populate level two for authors info.
 * 
 * @param authorName
 * @param publicationType
 */
function populateAuthorsLevelTwo(authorName, publicationType) {
	// Clean the author's name to use it as an id
	$authorId = cleanString(authorName);
	// Get the id from the author and book and the sibling div
	$("#" + $authorId + publicationType).empty();
	// Find all the publications by type
	$collectionPublications = $xml.find(publicationType);
	// Loop
	$collectionPublications
			.each(function(index, element) {
				// Get the titles
				$title = $(element).children("title");
				// Create header 4
				$header = "<h4>" + $title.text() + "</h4><div id=\""
						+ $authorId + publicationType + index + "\">";
				$("#" + $authorId + publicationType).append($header);
				populateAuthorsLevelThree($authorId + publicationType + index,
						element);
			});
	// Refresh the headers
	refresAuthorView('h4');
}
/**
 * Populate level three publications info
 * 
 * @param indexId
 * @param innerElement
 */
function populateAuthorsLevelThree(indexId, innerElement) {
	// Get the div publication
	$divBook = $("#" + indexId);
	// Loop for all the nested elements
	$(innerElement).children().each(
			function(index, element) {
				$newElement = "<li>" + $(element).prop("tagName") + " : "
						+ $(element).text() + "</li>";
				$divBook.append($newElement);
			});
}
/**
 * Function to prepare the request
 * 
 * @param firstLetter
 */
function prepareAuthorSummaryRequest(firstLetter) {
	spinnerInit('alphabet');
	buildRequest('getAuthorsSummary', firstLetter);
}

/**
 * Create the alphabet in the page
 */
function iterateAlphabet() {
	// Alphabet
	var str = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	// Loop
	for ( var i = 0; i < str.length; i++) {
		var nextChar = str.charAt(i);
		// Create the list with the alphabet.
		$list = "<li onclick=\"prepareAuthorSummaryRequest('" + nextChar
				+ "')\" class=\"letter\"><a>" + nextChar + "</a></li>";
		$("#alphabet").append($list);
	}
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