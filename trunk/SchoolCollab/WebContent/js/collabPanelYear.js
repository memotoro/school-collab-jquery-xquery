/**
 * Ready function
 */
$(document).ready(function() {
	buildRequest('getFileNameUsed');
	spinnerInit('statsTable');
	buildRequest('getYearSummary');
	buildRequest('getStatsByYears');
});
/**
 * Refresh year view.
 */
function refresYearView(header) {
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
					buildRequest('getPublicationsByYearByType', $(this).attr(
							"year"), $(this).attr("id"));
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
 * Populate level one year info.
 */
function populateYearsLevelOne() {
	// Clean nested accordion
	$("#nestedAccordion").empty();
	// Get the years
	$collectionYears = $xml.find('year');
	// Loop
	$collectionYears
			.each(function(index, element) {
				// Create header 2
				$header = "<h2><span class=\"accordionEmphasis\">["
						+ $(element).find("value").text()
						+ "] </span><span class=\"accordionStrong\">"
						+ $(element).find("totalAmountPublications").text()
						+ "</span> Publications by <span class=\"accordionStrong\">"
						+ $(element).find("totalAmountAuthors").text()
						+ "</span> authors</h2><div id=\'"
						+ $(element).find("value").text() + "\'/>";
				$("#nestedAccordion").append($header);
				// Get the year sibling div
				$publicationDivs = $("#" + $(element).find("value").text());
				// Create header 3 for book
				$idYearPublication = $(element).find("value").text() + "book";
				$header = "<h3 year=\'"
						+ $(element).find("value").text()
						+ "\' id='book'><span class=\"numberOfBooks\">"
						+ $(element).find("book").find(
								"totalNumberPublications").text()
						+ "</span> Books by <span class=\"numberOfAuthors\">"
						+ $(element).find("book").find("totalNumberAuthors")
								.text() + "</span> Authors</h3><div id=\'"
						+ $idYearPublication + "'/>";
				$publicationDivs.append($header);
				// Create header 3 for article
				$idYearPublication = $(element).find("value").text()
						+ "article";
				$header = "<h3 year=\'"
						+ $(element).find("value").text()
						+ "\' id='article'><span class=\"numberOfArticles\">"
						+ $(element).find("article").find(
								"totalNumberPublications").text()
						+ "</span> Articles by <span class=\"numberOfAuthors\">"
						+ $(element).find("article").find("totalNumberAuthors")
								.text() + "</span> Authors</h3><div id=\'"
						+ $idYearPublication + "'/>";
				$publicationDivs.append($header);
				// Create header 3 for inproceedings
				$idYearPublication = $(element).find("value").text()
						+ "inproceedings";
				$header = "<h3 year=\'"
						+ $(element).find("value").text()
						+ "\' id='inproceedings'><span class=\"numberOfInproceedings\">"
						+ $(element).find("inproceedings").find(
								"totalNumberPublications").text()
						+ "</span> Inproceedings <span class=\"numberOfAuthors\">"
						+ +$(element).find("inproceedings").find(
								"totalNumberAuthors").text()
						+ "</span> Authors</h3><div id=\'" + $idYearPublication
						+ "'/>";
				$publicationDivs.append($header);
				// Create header 3 for incollection
				$idYearPublication = $(element).find("value").text()
						+ "incollection";
				$header = "<h3 year=\'"
						+ $(element).find("value").text()
						+ "\' id='incollection'><span class=\"numberOfIncollections\">"
						+ $(element).find("incollection").find(
								"totalNumberPublications").text()
						+ "</span> Incollections <span class=\"numberOfAuthors\">"
						+ +$(element).find("incollection").find(
								"totalNumberAuthors").text()
						+ "</span> Authors</h3><div id=\'" + $idYearPublication
						+ "'/>";
				$publicationDivs.append($header);
			});
	// Refresh the headers.
	refresYearView('h2');
	refresYearView('h3');
}
/**
 * Populate level two year info.
 */
function populateYearsLevelTwo(year, publicationType) {
	// Get the publication type by year
	$("#" + year + publicationType).empty();
	// Get all the publications
	$collectionPublications = $xml.find(publicationType);
	// Loop
	$collectionPublications.each(function(index, element) {
		// Create header 4
		$title = $(element).children("title");
		$header = "<h4>" + $title.text() + " by "
				+ $(element).children("author").size()
				+ " authors</h4><div id=\"" + year + publicationType + index
				+ "\">";
		$("#" + year + publicationType).append($header);
		populateYearLevelThree(year + publicationType + index, element);
	});
	// Refresh the header
	refresYearView('h4');
}
/**
 * Populate level three publications info
 * 
 * @param indexId
 * @param innerElement
 */
function populateYearLevelThree(indexId, innerElement) {
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
 * Function to populate the year's stats
 */
function populateYearsStatisticTableLevelOne() {
	// Get overall values
	$overall = $xml.find("overall");
	// Pub Per Author
	$meanPubInYear = $overall.find("mean_pub_in_year").text();
	$medianPubInYear = $overall.find("median_pub_in_year").text();
	$modePubInYear = $overall.find("mode_pub_in_year").text();
	$("#overallMeanPubInYear").html($meanPubInYear);
	$("#overallMedianPubInYear").html($medianPubInYear);
	$("#overallModePubInYear").html($modePubInYear);
	// Author Per Publication
	$meanAuthorInYear = $overall.find("mean_author_in_year").text();
	$medianAuthorInYear = $overall.find("median_author_in_year").text();
	$modeAuthorInYear = $overall.find("mode_author_in_year").text();
	$("#overallMeanAuthorInYear").html($meanAuthorInYear);
	$("#overallMedianAuthorInYear").html($medianAuthorInYear);
	$("#overallModeAuthorInYear").html($modeAuthorInYear);
	// Get books
	$book = $xml.find("book");
	// Pub Per Author
	$meanPubInYear = $book.find("mean_pub_in_year").text();
	$medianPubInYear = $book.find("median_pub_in_year").text();
	$modePubInYear = $book.find("mode_pub_in_year").text();
	$("#bookMeanPubInYear").html($meanPubInYear);
	$("#bookMedianPubInYear").html($medianPubInYear);
	$("#bookModePubInYear").html($modePubInYear);
	// Author Per Publication
	$meanAuthorInYear = $book.find("mean_author_in_year").text();
	$medianAuthorInYear = $book.find("median_author_in_year").text();
	$modeAuthorInYear = $book.find("mode_author_in_year").text();
	$("#bookMeanAuthorInYear").html($meanAuthorInYear);
	$("#bookMedianAuthorInYear").html($medianAuthorInYear);
	$("#bookModeAuthorInYear").html($modeAuthorInYear);
	// Get article
	$article = $xml.find("article");
	// Pub Per Author
	$meanPubInYear = $article.find("mean_pub_in_year").text();
	$medianPubInYear = $article.find("median_pub_in_year").text();
	$modePubInYear = $article.find("mode_pub_in_year").text();
	$("#articleMeanPubInYear").html($meanPubInYear);
	$("#articleMedianPubInYear").html($medianPubInYear);
	$("#articleModePubInYear").html($modePubInYear);
	// Author Per Publication
	$meanAuthorInYear = $article.find("mean_author_in_year").text();
	$medianAuthorInYear = $article.find("median_author_in_year").text();
	$modeAuthorInYear = $article.find("mode_author_in_year").text();
	$("#articleMeanAuthorInYear").html($meanAuthorInYear);
	$("#articleMedianAuthorInYear").html($medianAuthorInYear);
	$("#articleModeAuthorInYear").html($modeAuthorInYear);
	// Get Inproceedings
	$inproceedings = $xml.find("inproceedings");
	// Pub Per Author
	$meanPubInYear = $inproceedings.find("mean_pub_in_year").text();
	$medianPubInYear = $inproceedings.find("median_pub_in_year").text();
	$modePubInYear = $inproceedings.find("mode_pub_in_year").text();
	$("#inproceedingsMeanPubInYear").html($meanPubInYear);
	$("#inproceedingsMedianPubInYear").html($medianPubInYear);
	$("#inproceedingsModePubInYear").html($modePubInYear);
	// Author Per Publication
	$meanAuthorInYear = $inproceedings.find("mean_author_in_year").text();
	$medianAuthorInYear = $inproceedings.find("median_author_in_year").text();
	$modeAuthorInYear = $inproceedings.find("mode_author_in_year").text();
	$("#inproceedingsMeanAuthorInYear").html($meanAuthorInYear);
	$("#inproceedingsMedianAuthorInYear").html($medianAuthorInYear);
	$("#inproceedingsModeAuthorInYear").html($modeAuthorInYear);
	// Get incollection
	$incollection = $xml.find("incollection");
	// Pub Per Author
	$meanPubInYear = $incollection.find("mean_pub_in_year").text();
	$medianPubInYear = $incollection.find("median_pub_in_year").text();
	$modePubInYear = $incollection.find("mode_pub_in_year").text();
	$("#incollectionMeanPubInYear").html($meanPubInYear);
	$("#incollectionMedianPubInYear").html($medianPubInYear);
	$("#incollectionModePubInYear").html($modePubInYear);
	// Author Per Publication
	$meanAuthorInYear = $incollection.find("mean_author_in_year").text();
	$medianAuthorInYear = $incollection.find("median_author_in_year").text();
	$modeAuthorInYear = $incollection.find("mode_author_in_year").text();
	$("#incollectionMeanAuthorInYear").html($meanAuthorInYear);
	$("#incollectionMedianAuthorInYear").html($medianAuthorInYear);
	$("#incollectionModeAuthorInYear").html($modeAuthorInYear);
	// Call the function to stop the spinner
	spinnerStop();
}