package uk.ac.man.cs.data;

/**
 * Class that provides the services exposed in the xquery
 * 
 * @author memotoro
 * 
 */
public class DataAdapter {
	/**
	 * XQueryUtil
	 */
	private XQueryUtil xqueryUtil;

	/**
	 * Constructor
	 * 
	 * @param filePath
	 */
	public DataAdapter(String filePath) throws Exception {
		this.xqueryUtil = new XQueryUtil(filePath);
	}

	/**
	 * Method to get the types summary information
	 * 
	 * @return String xml query's result
	 */
	public String getTypesSummary() throws Exception {
		return this.xqueryUtil.getTypesSummary();
	}

	/**
	 * Method to get all the publications by Type
	 * 
	 * @param publicationType
	 *            Enum publication type
	 * @return String xml query's result
	 */
	public String getPublicationsByType(String publicationType)
			throws Exception {
		return this.xqueryUtil.getPublicationsByType(PublicationType
				.valueOf(publicationType));
	}

	/**
	 * Method o get the authors summary information
	 * 
	 * @param firstLetter
	 * @return String xml query's result
	 */
	public String getAuthorsSummary(String firstLetter) throws Exception {
		return this.xqueryUtil.getAuthorsSummary(firstLetter);
	}

	/**
	 * Method to get the all publications filtered by author name and type
	 * 
	 * @param authorName
	 *            String author's name
	 * @param publicationType
	 *            Enum publication type
	 * @return String xml query's result
	 */
	public String getPublicationsByAuthorByType(String authorName,
			String publicationType) throws Exception {
		return this.xqueryUtil.getPublicationsByAuthorByType(authorName,
				PublicationType.valueOf(publicationType));
	}

	/**
	 * Method to get the summary information by year
	 * 
	 * @return String xml query's result
	 */
	public String getYearSummary() throws Exception {
		return this.xqueryUtil.getYearSummary();
	}

	/**
	 * Method to get all the publication filterd by year and type
	 * 
	 * @param year
	 *            Strint year
	 * @param publicationType
	 *            Enum publication type
	 * @return String xml query's result
	 */
	public String getPublicationsByYearByType(String year,
			String publicationType) throws Exception {
		return this.xqueryUtil.getPublicationsByYearByType(year,
				PublicationType.valueOf(publicationType));
	}

	/**
	 * Method to get the statistics of publications and authors
	 * 
	 * @return String with the response
	 * @throws Exception
	 */
	public String getStatsPublicationsAuthors() throws Exception {
		return this.xqueryUtil.getResponseXMLStatsPublicationsAuthors();
	}

	/**
	 * Method to get the statistics in years
	 * 
	 * @return String with the response
	 * @throws Exception
	 */
	public String getStatsByYears() throws Exception {
		return this.xqueryUtil.getResponseXMLStatsByYears();
	}

	/**
	 * Method to get the initial letters of the authors who write in years and
	 * pubtype
	 * 
	 * @param pubtype
	 *            Publication Type
	 * @param years
	 *            String... years
	 * @return String response
	 * @throws Exception
	 */
	public String getAuthorNameInitialLettersByPubtypeOrByYear(
			String publicationType, String yearMin, String yearMax)
			throws Exception {
		PublicationType pubtype = null;
		if (!publicationType.isEmpty()) {
			pubtype = PublicationType.valueOf(publicationType);
		}
		if (yearMin.isEmpty()) {
			yearMin = null;
		}
		if (yearMax.isEmpty()) {
			yearMax = null;
		}
		return this.xqueryUtil.getAuthorNameInitialLettersByPubtypeOrByYear(
				pubtype, yearMin, yearMax);
	}

	/**
	 * Method to get the Coauthors response by author name
	 * 
	 * @param authorName
	 *            String author name
	 * @return String response
	 * @throws Exception
	 */
	public String getCoauthorResponseByAuthor_name(String authorName)
			throws Exception {
		return this.xqueryUtil.getCoauthorResponseByAuthorName(authorName);
	}

	/**
	 * Method to get the Coauthor response by year and type filter
	 * 
	 * @param firstLetter
	 * @param pubtype
	 * @param year
	 * @return
	 * @throws Exception
	 */
	public String getCoauthorResponseByPubTypeByYearFilter(String firstLetter,
			String publicationType, String yearMin, String yearMax)
			throws Exception {
		PublicationType pubtype = null;
		if (!publicationType.isEmpty()) {
			pubtype = PublicationType.valueOf(publicationType);
		}
		if (yearMin.isEmpty()) {
			yearMin = null;
		}
		if (yearMax.isEmpty()) {
			yearMax = null;
		}
		return this.xqueryUtil.getCoauthorResponseByPubTypeByYearFilter(
				firstLetter, pubtype, yearMin, yearMax);
	}

	/**
	 * Get the list of years in the file
	 * 
	 * @return String with the response
	 * @throws Exception
	 */
	public String getResponseYearsRange() throws Exception {
		return this.xqueryUtil.getResponseYearsRange();
	}
}
