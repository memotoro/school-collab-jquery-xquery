/**
 * Ready function.
 */
$(document).ready(function() {
	buildRequest('getFileNameUsed');
	spinnerInit('statsTable');
	buildRequest('getTypesSummary');
	buildRequest('getStatsPublicationsAuthors');
});
/**
 * Refresh publication view.
 */
function refresPublicationView(header) {
	var parentDivs = $('#nestedAccordion div'), childDivs = $(
			'#nestedAccordion h3').siblings('div');
	grandDivs = $('#nestedAccordion h4').siblings('div');
	// Validate if the header to update is h2
	if (header == 'h2') {
		$('#nestedAccordion h2').click(function() {
			buildRequest('getPublicationByType', $(this).attr("id"));
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
		$('#nestedAccordion h3').click(function() {
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
 * Populate level one publication info.
 */
function populatePublicationsLevelOne() {
	// Get the amount
	$totalAmountPublications = $xml.find('totalPublications').text();
	$('#totalNumberPublications').html($totalAmountPublications);
	$('#totalNumberPublications').addClass('accordionEmphasis');
	// Get the book amount.
	$bookAmount = $xml.find('book').find('amount').text();
	$bookAuthors = $xml.find('book').find('authors').text();
	$header = "<span class=\'accordionEmphasis\'>Books [" + $bookAmount
			+ "]</span>";
	$('#book').append($header);
	// Get the articles amount
	$articleAmount = $xml.find('article').find('amount').text();
	$articleAuthors = $xml.find('article').find('authors').text();
	$header = "<span class=\'accordionEmphasis\'>Articles [" + $articleAmount
			+ "]</span>";
	$('#article').append($header);
	// Get the inproceeding amount
	$inproceedingsAmount = $xml.find('inproceedings').find('amount').text();
	$inproceedingsAuthors = $xml.find('inproceedings').find('authors').text();
	$header = "<span class=\'accordionEmphasis\'>Inproceedings ["
			+ $inproceedingsAmount + "]</span>";
	$('#inproceedings').append($header);
	// Get the incollection amount
	$incollectionsAmount = $xml.find('incollection').find('amount').text();
	$incollectionsAuthors = $xml.find('incollection').find('authors').text();
	$header = "<span class=\'accordionEmphasis\'>Incollections ["
			+ $incollectionsAmount + "]</span>";
	$('#incollection').append($header);
	// Call the function to populate table level 1
	populatePublicationsAuthorsStatsTableLevelOne();
	// Refresh the header
	refresPublicationView('h2');
}
/**
 * Populate level two publications info
 */
function populatePublicationsLevelTwo(publicationType) {
	// Get the publication type sibling div
	$("#" + publicationType + "Info").empty();
	// Get all the publications
	$collectionPublications = $xml.find(publicationType);
	// Loop
	$collectionPublications.each(function(index, element) {
		// Create header 3
		$title = $(element).children("title");
		$header = "<h3>" + $title.text() + "</h3><div id=\"" + publicationType
				+ index + "\">";
		// Take the header 3 sibling div
		$("#" + publicationType + "Info").append($header);
		// Loop
		$(element).children().each(
				function(innerIndex, innerElement) {
					// Populate level three
					populatePublicationsLevelThree(publicationType + index,
							innerElement);
				});
	});
	// Refresh the header
	refresPublicationView('h3');
}
/**
 * Populate level three publications info
 * 
 * @param indexId
 * @param innerElement
 */
function populatePublicationsLevelThree(indexId, innerElement) {
	// Get the div publication
	$divBook = $("#" + indexId);
	// Loop for all the nested elements
	$(innerElement).each(
			function(index, element) {
				$newElement = "<li>" + $(element).prop("tagName") + " : "
						+ $(element).text() + "</li>";
				$divBook.append($newElement);
			});
}
/**
 * Function that populates the table one.
 */
function populatePublicationsAuthorsStatsTableLevelOne() {
	$totalAmountPublications = $xml.find('totalPublications').text();
	$totalAmountAuthors = $xml.find('totalAuthors').text();
	$("#overallPub").html($totalAmountPublications);
	$("#overallAuth").html($totalAmountAuthors);
	// Get the books amount
	$bookAmount = $xml.find('book').find('amount').text();
	$bookAuthors = $xml.find('book').find('authors').text();
	$("#bookPub").html($bookAmount);
	$("#bookAuth").html($bookAuthors);
	// Get the articles amount
	$articleAmount = $xml.find('article').find('amount').text();
	$articleAuthors = $xml.find('article').find('authors').text();
	$("#articlePub").html($articleAmount);
	$("#articleAuth").html($articleAuthors);
	// Get the inproceeding amount
	$inproceedingsAmount = $xml.find('inproceedings').find('amount').text();
	$inproceedingsAuthors = $xml.find('inproceedings').find('authors').text();
	$("#inproceedingsPub").html($inproceedingsAmount);
	$("#inproceedingsAuth").html($inproceedingsAuthors);
	// Get the incollection amount
	$incollectionsAmount = $xml.find('incollection').find('amount').text();
	$incollectionsAuthors = $xml.find('incollection').find('authors').text();
	$("#incollectionPub").html($incollectionsAmount);
	$("#incollectionAuth").html($incollectionsAuthors);
}
/**
 * Function that populates the second level two.
 */
function populatePublicationsAuthorsStatsTableLevelTwo() {
	// Get overall values
	$overall = $xml.find("overall");
	// Pub Per Author
	$meanPubPerAuthor = $overall.find("mean_pub_per_author").text();
	$medianPubPerAuthor = $overall.find("median_pub_per_author").text();
	$modePubPerAuthor = $overall.find("mode_pub_per_author").text();
	$("#overallMeanPubPerAuth").html($meanPubPerAuthor);
	$("#overallMedianPubPerAuth").html($medianPubPerAuthor);
	$("#overallModePubPerAuth").html($modePubPerAuthor);
	// Author Per Publication
	$meanAuthorPerPub = $overall.find("mean_author_per_pub").text();
	$medianAuthorPerPub = $overall.find("median_author_per_pub").text();
	$modeAuthorPerPub = $overall.find("mode_author_per_pub").text();
	$("#overallMeanAuthPerPub").html($meanAuthorPerPub);
	$("#overallMedianAuthPerPub").html($medianAuthorPerPub);
	$("#overallModeAuthPerPub").html($modeAuthorPerPub);
	// Get books
	$book = $xml.find("book");
	// Pub Per Author
	$meanPubPerAuthor = $book.find("mean_pub_per_author").text();
	$medianPubPerAuthor = $book.find("median_pub_per_author").text();
	$modePubPerAuthor = $book.find("mode_pub_per_author").text();
	$("#bookMeanPubPerAuth").html($meanPubPerAuthor);
	$("#bookMedianPubPerAuth").html($medianPubPerAuthor);
	$("#bookModePubPerAuth").html($modePubPerAuthor);
	// Author Per Publication
	$meanAuthorPerPub = $book.find("mean_author_per_pub").text();
	$medianAuthorPerPub = $book.find("median_author_per_pub").text();
	$modeAuthorPerPub = $book.find("mode_author_per_pub").text();
	$("#bookMeanAuthPerPub").html($meanAuthorPerPub);
	$("#bookMedianAuthPerPub").html($medianAuthorPerPub);
	$("#bookModeAuthPerPub").html($modeAuthorPerPub);
	// Get article
	$article = $xml.find("article");
	// Pub Per Author
	$meanPubPerAuthor = $article.find("mean_pub_per_author").text();
	$medianPubPerAuthor = $article.find("median_pub_per_author").text();
	$modePubPerAuthor = $article.find("mode_pub_per_author").text();
	$("#articleMeanPubPerAuth").html($meanPubPerAuthor);
	$("#articleMedianPubPerAuth").html($medianPubPerAuthor);
	$("#articleModePubPerAuth").html($modePubPerAuthor);
	// Author Per Publication
	$meanAuthorPerPub = $article.find("mean_author_per_pub").text();
	$medianAuthorPerPub = $article.find("median_author_per_pub").text();
	$modeAuthorPerPub = $article.find("mode_author_per_pub").text();
	$("#articleMeanAuthPerPub").html($meanAuthorPerPub);
	$("#articleMedianAuthPerPub").html($medianAuthorPerPub);
	$("#articleModeAuthPerPub").html($modeAuthorPerPub);
	// Get Inproceedings
	$inproceedings = $xml.find("inproceedings");
	// Pub Per Author
	$meanPubPerAuthor = $inproceedings.find("mean_pub_per_author").text();
	$medianPubPerAuthor = $inproceedings.find("median_pub_per_author").text();
	$modePubPerAuthor = $inproceedings.find("mode_pub_per_author").text();
	$("#inproceedingsMeanPubPerAuth").html($meanPubPerAuthor);
	$("#inproceedingsMedianPubPerAuth").html($medianPubPerAuthor);
	$("#inproceedingsModePubPerAuth").html($modePubPerAuthor);
	// Author Per Publication
	$meanAuthorPerPub = $inproceedings.find("mean_author_per_pub").text();
	$medianAuthorPerPub = $inproceedings.find("median_author_per_pub").text();
	$modeAuthorPerPub = $inproceedings.find("mode_author_per_pub").text();
	$("#inproceedingsMeanAuthPerPub").html($meanAuthorPerPub);
	$("#inproceedingsMedianAuthPerPub").html($medianAuthorPerPub);
	$("#inproceedingsModeAuthPerPub").html($modeAuthorPerPub);
	// Get incollection
	$incollection = $xml.find("incollection");
	// Pub Per Author
	$meanPubPerAuthor = $incollection.find("mean_pub_per_author").text();
	$medianPubPerAuthor = $incollection.find("median_pub_per_author").text();
	$modePubPerAuthor = $incollection.find("mode_pub_per_author").text();
	$("#incollectionMeanPubPerAuth").html($meanPubPerAuthor);
	$("#incollectionMedianPubPerAuth").html($medianPubPerAuthor);
	$("#incollectionModePubPerAuth").html($modePubPerAuthor);
	// Author Per Publication
	$meanAuthorPerPub = $incollection.find("mean_author_per_pub").text();
	$medianAuthorPerPub = $incollection.find("median_author_per_pub").text();
	$modeAuthorPerPub = $incollection.find("mode_author_per_pub").text();
	$("#incollectionMeanAuthPerPub").html($meanAuthorPerPub);
	$("#incollectionMedianAuthPerPub").html($medianAuthorPerPub);
	$("#incollectionModeAuthPerPub").html($modeAuthorPerPub);
	// Call the function to stop the spinner
	spinnerStop();
}