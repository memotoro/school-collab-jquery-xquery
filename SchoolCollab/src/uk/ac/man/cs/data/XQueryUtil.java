package uk.ac.man.cs.data;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.xml.xquery.XQConnection;
import javax.xml.xquery.XQDataSource;
import javax.xml.xquery.XQException;
import javax.xml.xquery.XQExpression;
import javax.xml.xquery.XQSequence;

import net.sf.saxon.xqj.SaxonXQDataSource;

/**
 * Class that execute the xquery sentences.
 * 
 * @author memotoro
 * 
 */
public class XQueryUtil {
	public static void main(String[] args) throws Exception {
		/*
		 * There are 4 kinds of publications: article: refers to a Journal or
		 * Magazine Article inproceedings: refers to a publication in a
		 * Conference or Workshop Proceedings incollection: refers to a chapter
		 * in a collection (book) book: refers to a Book
		 */
	}

	/**
	 * String with the xml file name
	 */
	String xml_file_name = "dblp_curated_sample.xml";

	public XQueryUtil(String filepath) {
		this.xml_file_name = filepath;
	}

	/**
	 * Method to execute a query with the Xquery engine
	 * 
	 * @param query
	 *            String with the Xquery syntax
	 * @return String xml query's result
	 */
	private String executeQuery(String query) throws Exception {
		// Result
		String result = null;
		try {
			// Create the connection to the xquery engine
			XQDataSource ds = new SaxonXQDataSource();
			XQConnection conn = ds.getConnection();
			XQExpression exp = conn.createExpression();
			// Execute the query
			XQSequence seq = exp.executeQuery(query.toString());
			// Get the string value of the xml sequence result
			result = seq.getSequenceAsString(null);
			// Close the sequence
			seq.close();
		} catch (XQException xqe) {
			// Print the trace
			xqe.printStackTrace();
		}
		// Return the result
		return result;
	}

	private Double formatNumber(Double value) throws Exception {
		DecimalFormat decimalFormat = new DecimalFormat("#0.00");
		return Double.parseDouble(decimalFormat.format(value));
	}

	/**
	 * Method o get the authors summary information
	 * 
	 * @param firstLetter
	 * @return String xml query's result
	 */
	public String getAuthorsSummary(String firstLetter) throws Exception {
		// Create the buffer
		StringBuffer query = new StringBuffer();
		// Set the document
		query.append("let $nodes := doc(\"" + xml_file_name + "\")/dblp ");
		query.append("return ");
		query.append("for $authorName in fn:distinct-values($nodes//author[fn:lower-case(fn:substring(. ,1, 1)) = fn:lower-case(\"");
		query.append(firstLetter);
		query.append("\")]) ");
		query.append("return ");
		query.append("<author>");
		query.append("<name>{$authorName}</name>");
		query.append("<totalpublications>{fn:count($nodes//*[author=$authorName])}</totalpublications>");
		query.append("<publications>");
		for (PublicationType publicationType : PublicationType.values()) {
			query.append("<");
			query.append(publicationType.toString());
			query.append(">");
			query.append("{fn:count($nodes//");
			query.append(publicationType.toString());
			query.append("[author=$authorName])}");
			query.append("</");
			query.append(publicationType.toString());
			query.append(">");
		}
		query.append("</publications>");
		query.append("</author>");
		// Create the buffer
		StringBuffer result = new StringBuffer();
		// Set additional tags to the response
		result.append("<response>");
		result.append(executeQuery(query.toString()));
		result.append("</response>");
		// Return the query result
		return result.toString();
	}

	/**
	 * Method to calculate the average of authors by publication type
	 * 
	 * @param publicationType
	 * @return Double
	 * @throws Exception
	 */
	public Double getAverageAuthorsByPublicationType() throws Exception {
		StringBuffer query = new StringBuffer();
		// Set the document
		query.append("let $nodes := doc(\"" + xml_file_name + "\")/dblp ");
		query.append("let $average := avg(");
		query.append("for $individualPub in $nodes/*");
		query.append(" return ");
		query.append("fn:count($individualPub/author)");
		query.append(")");
		query.append("return $average");
		// Return the result of the query
		return formatNumber(new Double(this.executeQuery(query.toString())));
	}

	/**
	 * Method to calculate the average of authors by publication type
	 * 
	 * @param publicationType
	 * @return Double
	 * @throws Exception
	 */
	public Double getAverageAuthorsByPublicationType(
			PublicationType publicationType) throws Exception {
		StringBuffer query = new StringBuffer();
		// Set the document
		query.append("let $nodes := doc(\"" + xml_file_name + "\")/dblp ");
		query.append("let $average := avg(");
		query.append("for $individualPub in $nodes//");
		query.append(publicationType.toString());
		query.append(" return ");
		query.append("fn:count($individualPub/author)");
		query.append(")");
		query.append("return $average");
		// Return the result of the query
		String result = this.executeQuery(query.toString());
		if (result.equals("")) {
			return 0D;
		} else {
			return formatNumber(new Double(result));
		}
	}

	public double getAverageAuthorsPerPubByYear(int year)
			throws NumberFormatException, Exception {
		StringBuffer query = new StringBuffer();
		// Set the document
		query.append("let $nodes := doc(\"" + xml_file_name + "\")/dblp ");
		query.append("let $average := avg(");
		query.append("for $individualPub in $nodes//*[year='" + year + "']");
		query.append(" return ");
		query.append("fn:count($individualPub/author)");
		query.append(")");
		query.append("return $average");
		// Return the result of the query
		return formatNumber(new Double(this.executeQuery(query.toString())));
	}

	public double getAverageNumberOfAuthorsPerPublicationByPubtype(
			PublicationType pubtype) throws NumberFormatException, Exception {
		if (getTotalNumberOfPubByType(pubtype) == 0)
			return 0.0;
		return formatNumber((double) getTotalNumberOfAuthorsByPubType(pubtype)
				/ getTotalNumberOfPubByType(pubtype));
	}

	public Double getAverageNumberOfAuthorsPerPublicationOverall()
			throws NumberFormatException, Exception {
		if (this.getTotalNumberOfPub() == 0)
			return 0.0;
		return formatNumber((double) getTotalNumberOfAuthors()
				/ getTotalNumberOfPub());
	}

	public double getAverageNumberOfPublicationsPerAuthor() throws Exception {
		if (this.getTotalNumberOfAuthorsWithoutDuplication() == 0)
			return 0.0;
		return formatNumber((double) getTotalNumberOfAuthors()
				/ getTotalNumberOfAuthorsWithoutDuplication());
	}

	public double getAverageNumberOfPublicationsPerAuthorByPubtype(
			PublicationType pubtype) throws NumberFormatException, Exception {
		if (this.getTotalNumberOfAuthorsWithoutDuplicationByPubtype(pubtype) == 0)
			return 0.0;
		return formatNumber((double) getTotalNumberOfAuthorsByPubType(pubtype)
				/ getTotalNumberOfAuthorsWithoutDuplication());
	}

	public double getAveragePubsPerAuthorOverallByYear(int year)
			throws NumberFormatException, Exception {
		return (double) this.getTotalAuthorsByYear(year)
				/ getTotalDistinctAuthorsByYear(year);
	}

	public double getAveragePubsPerAuthorOverallByYearByPubtype(int year,
			PublicationType pubtype) throws Exception {
		double x = getTotalDistinctAuthorsByYearByPubtype(year, pubtype);
		if (x == 0.0) {
			return 0.0;
		}
		return (double) getTotalAuthorsByYearByPubtype(year, pubtype) / x;
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
			PublicationType publicationType) throws Exception {
		// Create the buffer
		StringBuffer query = new StringBuffer();
		// Set the document
		query.append("let $nodes := doc(\"" + xml_file_name + "\")/dblp ");
		query.append("return ");
		query.append("<publication>");
		query.append("{$nodes//");
		query.append(publicationType.toString());
		query.append("[author=\"");
		query.append(authorName);
		query.append("\"]}");
		query.append("</publication>");
		// Create the buffer
		StringBuffer result = new StringBuffer();
		// Set additional tags to the response
		result.append("<response>");
		result.append(this.executeQuery(query.toString()));
		result.append("</response>");
		// Return the query result
		return result.toString();
	}

	/**
	 * Method to get all the publications by Type
	 * 
	 * @param publicationType
	 *            Enum publication type
	 * @return String xml query's result
	 */
	public String getPublicationsByType(PublicationType publicationType)
			throws Exception {
		// Create the buffer
		StringBuffer query = new StringBuffer();
		// Set the document
		query.append("let $nodes := doc(\"" + xml_file_name + "\")/dblp ");
		query.append("return ");
		query.append("<response>");
		query.append("<publication>");
		query.append("{$nodes//");
		query.append(publicationType.toString());
		query.append("}");
		query.append("</publication>");
		query.append("</response>");
		// Return the query result
		return this.executeQuery(query.toString());
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
			PublicationType publicationType) throws Exception {
		// Create the buffer
		StringBuffer query = new StringBuffer();
		// Set the document
		query.append("let $nodes := doc(\"" + xml_file_name + "\")/dblp ");
		query.append("return ");
		query.append("<response>");
		query.append("<publication>");
		query.append("{$nodes//");
		query.append(publicationType.toString());
		query.append("[year=\"");
		query.append(year);
		query.append("\"]}");
		query.append("</publication>");
		query.append("</response>");
		// Return the query result
		return this.executeQuery(query.toString());
	}

	/**
	 * Method to get the statistics of publications and authors
	 * 
	 * @return String with the response
	 * @throws Exception
	 */
	public String getResponseXMLStatsPublicationsAuthors() throws Exception {
		StringBuffer response = new StringBuffer();
		response.append("<response>");
		// Overall
		response.append("<overall>");
		// Publication Per author
		response.append("<pub_per_author>");
		// Mean
		response.append("<mean_pub_per_author>");
		response.append(this.getAverageNumberOfPublicationsPerAuthor());
		response.append("</mean_pub_per_author>");
		// Median
		response.append("<median_pub_per_author>");
		response.append(this.getMedianPubsPerAuthor());
		response.append("</median_pub_per_author>");
		// Mode
		response.append("<mode_pub_per_author>");
		response.append(this.getModePubsPerAuthor());
		response.append("</mode_pub_per_author>");
		response.append("</pub_per_author>");
		// Author per publication
		response.append("<author_per_pub>");
		// Mean
		response.append("<mean_author_per_pub>");
		response.append(this.getAverageAuthorsByPublicationType());
		response.append("</mean_author_per_pub>");
		// Median
		response.append("<median_author_per_pub>");
		response.append(this.getMedianOfAuthorsPerPublication());
		response.append("</median_author_per_pub>");
		// Mode
		response.append("<mode_author_per_pub>");
		response.append(this.getModeOfAuthorsPerPublication());
		response.append("</mode_author_per_pub>");
		response.append("</author_per_pub>");
		response.append("</overall>");
		for (PublicationType publicationType : PublicationType.values()) {
			// Publication type
			response.append("<");
			response.append(publicationType.toString());
			response.append(">");
			// Publication per author
			response.append("<pub_per_author>");
			// Mean
			response.append("<mean_pub_per_author>");
			response.append(this
					.getAverageNumberOfPublicationsPerAuthorByPubtype(publicationType));
			response.append("</mean_pub_per_author>");
			// Median
			response.append("<median_pub_per_author>");
			response.append(this
					.getMedianPubsPerAuthorByPubtype(publicationType));
			response.append("</median_pub_per_author>");
			// Mode
			response.append("<mode_pub_per_author>");
			response.append(this.getModePubsPerAuthorByPubtype(publicationType));
			response.append("</mode_pub_per_author>");
			response.append("</pub_per_author>");
			// Author per publication
			response.append("<author_per_pub>");
			// Mean
			response.append("<mean_author_per_pub>");
			response.append(this
					.getAverageAuthorsByPublicationType(publicationType));
			response.append("</mean_author_per_pub>");
			// Median
			response.append("<median_author_per_pub>");
			response.append(this
					.getMedianOfAuthorsPerPublicationByPubType(publicationType));
			response.append("</median_author_per_pub>");
			// Mode
			response.append("<mode_author_per_pub>");
			response.append(this
					.getModeOfAuthorsPerPublicationByPubType(publicationType));
			response.append("</mode_author_per_pub>");
			response.append("</author_per_pub>");
			response.append("</");
			response.append(publicationType.toString());
			response.append(">");
		}
		response.append("</response>");
		// Return response.
		return response.toString();
	}

	/**
	 * Method to get the statistics by years
	 * 
	 * @return String with the response
	 * @throws Exception
	 */
	public String getResponseXMLStatsByYears() throws Exception {
		StringBuffer response = new StringBuffer();
		response.append("<response>");
		// Overall
		response.append("<overall>");
		// Publication Per author
		response.append("<pub_in_year>");
		// Mean
		response.append("<mean_pub_in_year>");
		response.append(this.getAveragePublicationsInYearsOverall());
		response.append("</mean_pub_in_year>");
		// Median
		response.append("<median_pub_in_year>");
		response.append(this.getMedianPublicationsInYears());
		response.append("</median_pub_in_year>");
		// Mode
		response.append("<mode_pub_in_year>");
		response.append(this.getModePublicationsInYears());
		response.append("</mode_pub_in_year>");
		response.append("</pub_in_year>");
		// Author per publication
		response.append("<author_in_year>");
		// Mean
		response.append("<mean_author_in_year>");
		response.append(this.getAverageNumberOfAuthorsWhoWrotePublications());
		response.append("</mean_author_in_year>");
		// Median
		response.append("<median_author_in_year>");
		response.append(this.getMedianNumberOfAuthorsWhoWrotePublications());
		response.append("</median_author_in_year>");
		// Mode
		response.append("<mode_author_in_year>");
		response.append(this.getModeOfAuthorsWhoWrotePublications());
		response.append("</mode_author_in_year>");
		response.append("</author_in_year>");
		response.append("</overall>");
		for (PublicationType publicationType : PublicationType.values()) {
			// Publication type
			response.append("<");
			response.append(publicationType.toString());
			response.append(">");
			// Publication per author
			response.append("<pub_in_year>");
			// Mean
			response.append("<mean_pub_in_year>");
			response.append(this
					.getAveragePublicationsInYearsByPubtype(publicationType));
			response.append("</mean_pub_in_year>");
			// Median
			response.append("<median_pub_in_year>");
			response.append(this
					.getMedianPublicationsInYearsByPubtype(publicationType));
			response.append("</median_pub_in_year>");
			// Mode
			response.append("<mode_pub_in_year>");
			response.append(this
					.getModePublicationsInYearByPubtype(publicationType));
			response.append("</mode_pub_in_year>");
			response.append("</pub_in_year>");
			// Author per publication
			response.append("<author_in_year>");
			// Mean
			response.append("<mean_author_in_year>");
			response.append(this
					.getAverageNumberOfAuthorsWhoWrotePublicationsByPubType(publicationType));
			response.append("</mean_author_in_year>");
			// Median
			response.append("<median_author_in_year>");
			response.append(this
					.getMedianOfAuthorsWhoWrotePublicationsByPubType(publicationType));
			response.append("</median_author_in_year>");
			// Mode
			response.append("<mode_author_in_year>");
			response.append(this
					.getModeOfAuthorsWhoWrotePublicationsByPubType(publicationType));
			response.append("</mode_author_in_year>");
			response.append("</author_in_year>");
			response.append("</");
			response.append(publicationType.toString());
			response.append(">");
		}
		response.append("</response>");
		// Return response.
		return response.toString();
	}

	/**
	 * Method to calculate the total amount of authors by publication type
	 * 
	 * @param publicationType
	 * @return Integer
	 * @throws Exception
	 */
	public Integer getTotalAmountOfAuthorByPublicationType(
			PublicationType publicationType) throws Exception {
		StringBuffer query = new StringBuffer();
		// Set the document
		query.append("let $nodes := doc(\"" + xml_file_name + "\")/dblp ");
		query.append("return ");
		query.append("fn:count($nodes//");
		query.append(publicationType.toString());
		query.append("/author)");
		// Return the result of the query
		return new Integer(this.executeQuery(query.toString()));
	}

	/**
	 * Method to calculate the total amount of publications by type
	 * 
	 * @param publicationType
	 * @return Integer
	 * @throws Exception
	 */
	public Integer getTotalAmountOfPublicationsByPublicationType(
			PublicationType publicationType) throws Exception {
		StringBuffer query = new StringBuffer();
		// Set the document
		query.append("let $nodes := doc(\"" + xml_file_name + "\")/dblp ");
		query.append("return ");
		query.append("fn:count($nodes//");
		query.append(publicationType.toString());
		query.append(")");
		// Return the result of the query
		return new Integer(this.executeQuery(query.toString()));
	}

	public int getTotalAuthorsByYear(int year) throws NumberFormatException,
			Exception {
		// Create the buffer
		StringBuffer query = new StringBuffer();
		// Set the document
		query.append("let $nodes := doc(\"" + xml_file_name + "\")/dblp ");
		query.append("for $x in $nodes ");
		query.append("return count ");
		query.append("(for $y in $x/*[year = '" + year + "']/author return 1) ");
		// Return the query result
		return Integer.parseInt(this.executeQuery(query.toString()));
	}

	public int getTotalAuthorsByYearByPubtype(int year, PublicationType pubtype)
			throws Exception {
		// Create the buffer
		StringBuffer query = new StringBuffer();
		// Set the document
		query.append("let $nodes := doc(\"" + xml_file_name + "\")/dblp ");
		query.append("let $a := fn:count( ");
		query.append("for $individualPub in $nodes/");
		query.append(pubtype.toString());
		query.append("[year='");
		query.append(year);
		query.append("']/author ");
		query.append("return $individualPub");
		query.append(") return $a ");
		return Integer.parseInt((this.executeQuery(query.toString())));
	}

	public double getTotalDistinctAuthorsByYear(int year)
			throws NumberFormatException, Exception {
		// Create the buffer
		StringBuffer query = new StringBuffer();
		// Set the document
		query.append("let $nodes := doc(\"" + xml_file_name + "\")/dblp ");
		query.append("for $x in $nodes ");
		query.append("return count ");
		query.append("(for $y in fn:distinct-values($x/*[year = '" + year
				+ "']/author) return 1) ");
		// Return the query result
		return Integer.parseInt(this.executeQuery(query.toString()));
	}

	public int getTotalDistinctAuthorsByYearByPubtype(int year,
			PublicationType pubtype) throws Exception {
		// Create the buffer
		StringBuffer query = new StringBuffer();
		// Set the document
		query.append("let $nodes := doc(\"" + xml_file_name + "\")/dblp ");
		query.append("let $b := fn:count(");
		query.append("for $individualPub in fn:distinct-values($nodes/");
		query.append(pubtype.toString());
		query.append("[year='");
		query.append(year);
		query.append("']/author)");
		query.append("return $individualPub ");
		query.append(") return $b ");
		return Integer.parseInt(this.executeQuery(query.toString()));
	}

	public int getTotalNumberOfAuthors() throws Exception {
		// Create the buffer
		StringBuffer query = new StringBuffer();
		// Set the document
		query.append("let $nodes := doc(\"" + xml_file_name + "\")/dblp ");
		query.append("for $x in $nodes ");
		query.append("return count ");
		query.append("(for $y in $x/*/author return 1) ");
		// Return the query result
		return Integer.parseInt(this.executeQuery(query.toString()));
	}

	public int getTotalNumberOfAuthorsByPubType(PublicationType pubtype)
			throws NumberFormatException, Exception {
		// Create the buffer
		StringBuffer query = new StringBuffer();
		// Set the document
		query.append("let $nodes := doc(\"" + xml_file_name + "\")/dblp ");
		query.append("for $x in $nodes ");
		query.append("return count ");
		query.append("(for $y in $x/" + pubtype.toString()
				+ "/author return 1) ");
		// Return the query result
		return Integer.parseInt(this.executeQuery(query.toString()));
	}

	public int getTotalNumberOfAuthorsWithoutDuplication()
			throws NumberFormatException, Exception {
		// Create the buffer
		StringBuffer query = new StringBuffer();
		// Set the document
		query.append("let $nodes := doc(\"" + xml_file_name + "\")/dblp ");
		query.append("for $x in $nodes ");
		query.append("return count ");
		query.append("(for $y in fn:distinct-values($x/*/author) return 1) ");
		// Return the query result
		return Integer.parseInt(this.executeQuery(query.toString()));
	}

	public List<String> getAuthorsListWithoutDuplication() throws Exception {
		// Create the buffer
		StringBuffer query = new StringBuffer();
		// Set the document
		query.append("let $nodes := doc(\"" + xml_file_name + "\")/dblp ");
		query.append("for $x in $nodes ");
		query.append("let $authors := fn:distinct-values($x//author) ");
		query.append("let $j := (");
		query.append("for $author in $authors ");
		query.append(" return ");
		query.append(" fn:concat($author,\',\') ");
		query.append(") ");
		query.append("return $j");
		String[] strings = this.executeQuery(query.toString()).split(",");
		ArrayList<String> ints = new ArrayList<String>();
		for (int i = 0; i < strings.length; i++) {
			ints.add(strings[i].trim());
		}
		return ints;
	}

	public int getTotalNumberOfAuthorsWithoutDuplicationByPubtype(
			PublicationType pubtype) throws NumberFormatException, Exception {
		// Create the buffer
		StringBuffer query = new StringBuffer();
		// Set the document
		query.append("let $nodes := doc(\"" + xml_file_name + "\")/dblp ");
		query.append("for $x in $nodes ");
		query.append("return count ");
		query.append("(for $y in fn:distinct-values($x/" + pubtype.toString()
				+ "/author) return 1) ");
		// Return the query result
		return Integer.parseInt(this.executeQuery(query.toString()));
	}

	public int getTotalNumberOfPub() throws NumberFormatException, Exception {
		// Create the buffer
		StringBuffer query = new StringBuffer();
		// Set the document
		query.append("let $nodes := doc(\"" + xml_file_name + "\")/dblp ");
		query.append("for $x in $nodes ");
		query.append("return count ");
		query.append("(for $y in $x/* return 1) ");
		// Return the query result
		return Integer.parseInt(this.executeQuery(query.toString()));
	}

	public int getTotalNumberOfPubByType(PublicationType pubtype)
			throws NumberFormatException, Exception {
		StringBuffer query = new StringBuffer();
		// Set the document
		query.append("let $nodes := doc(\"" + xml_file_name + "\")/dblp ");
		query.append("for $x in $nodes ");
		query.append("return count ");
		query.append("(for $y in $x/" + pubtype.toString() + " return 1) ");
		// Return the query result
		return Integer.parseInt(this.executeQuery(query.toString()));
	}

	public double getTotalPublicationsByYear(int year)
			throws NumberFormatException, Exception {
		// Create the buffer
		StringBuffer query = new StringBuffer();
		// Set the document
		query.append("let $nodes := doc(\"" + xml_file_name + "\")/dblp ");
		query.append("for $x in $nodes ");
		query.append("return count ");
		query.append("(for $y in $x/* where $y/year = '" + year
				+ "' return 1) ");
		// Return the query result
		return Integer.parseInt(this.executeQuery(query.toString()));
	}

	/**
	 * Method to get the types summary information
	 * 
	 * @return String xml query's result
	 */
	public String getTypesSummary() throws Exception {
		// Create the buffer
		StringBuffer query = new StringBuffer();
		// Set the document
		query.append("let $nodes := doc(\"" + xml_file_name + "\")/dblp ");
		query.append("return ");
		query.append("<response>");
		query.append("<totalPublications>");
		query.append("{fn:count($nodes/*)}");
		query.append("</totalPublications>");
		query.append("<totalAuthors>");
		query.append("{fn:count(fn:distinct-values($nodes//author))}");
		query.append("</totalAuthors>");
		for (PublicationType publicationType : PublicationType.values()) {
			query.append("<");
			query.append(publicationType.toString());
			query.append(">");
			query.append("<amount>");
			query.append("{count($nodes//");
			query.append(publicationType.toString());
			query.append(")}");
			query.append("</amount>");
			query.append("<authors>");
			query.append("{fn:count(fn:distinct-values($nodes//");
			query.append(publicationType.toString());
			query.append("/author))}");
			query.append("</authors>");
			query.append("</");
			query.append(publicationType.toString());
			query.append(">");
		}
		query.append("</response>");
		// Return the query result
		return this.executeQuery(query.toString());
	}

	/**
	 * Method to get the summary information by year
	 * 
	 * @return String xml query's result
	 */
	public String getYearSummary() throws Exception {
		// Create the buffer
		StringBuffer query = new StringBuffer();
		// Set the document
		query.append("let $nodes := doc(\"" + xml_file_name + "\")/dblp ");
		query.append("for $year in fn:distinct-values($nodes//*/year) ");
		for (PublicationType publicationType : PublicationType.values()) {
			query.append("let $");
			query.append(publicationType.toString());
			query.append(" := $nodes//");
			query.append(publicationType.toString());
			query.append("[year=$year] ");
		}
		query.append("order by $year descending ");
		query.append("return ");
		query.append("<year>");
		query.append("<value>{$year}</value>");
		query.append("<totalAmountPublications>{fn:count($nodes//*[year=$year])}</totalAmountPublications>");
		query.append("<totalAmountAuthors>{fn:count(fn:distinct-values($nodes//author[../year=$year]))}</totalAmountAuthors>");
		query.append("<publication>");
		for (PublicationType publicationType : PublicationType.values()) {
			query.append("<");
			query.append(publicationType.toString());
			query.append(">");
			query.append("<totalNumberPublications>{fn:count($");
			query.append(publicationType.toString());
			query.append(")}</totalNumberPublications>");
			query.append("<totalNumberAuthors>{fn:count(fn:distinct-values($");
			query.append(publicationType.toString());
			query.append("/author))}</totalNumberAuthors>");
			query.append("</");
			query.append(publicationType.toString());
			query.append(">");
		}
		query.append("</publication>");
		query.append("</year>");
		// Create the buffer
		StringBuffer result = new StringBuffer();
		// Set additional tags to the response
		result.append("<response>");
		result.append(this.executeQuery(query.toString()));
		result.append("</response>");
		// Return the query result
		return result.toString();
	}

	public double getAveragePublicationsInYearsOverall() throws Exception {
		// Create the buffer
		StringBuffer query = new StringBuffer();
		// Set the document
		query.append("let $nodes := doc(\"" + xml_file_name + "\")/dblp ");
		query.append("let $years := fn:distinct-values($nodes/*/year) ");
		query.append("let $sorted := (for $i in $years order by $i return $i) ");
		query.append("let $minYear := fn:min($sorted) cast as xs:integer ");
		query.append("let $maxYear := fn:max($sorted) cast as xs:integer ");
		query.append("let $j := avg( ");
		query.append(" for $i in ($minYear to $maxYear) ");
		query.append("return fn:count($nodes/*[year=$i]) ");
		query.append(") ");
		query.append("return $j ");
		// Return the query result
		return formatNumber(Double.parseDouble(this.executeQuery(query
				.toString())));
	}

	public double getAveragePublicationsInYearsByPubtype(PublicationType pubtype)
			throws Exception {
		// Create the buffer
		StringBuffer query = new StringBuffer();
		// Set the document
		query.append("let $nodes := doc(\"" + xml_file_name + "\")/dblp ");
		query.append("let $years := fn:distinct-values($nodes/*/year) ");
		query.append("let $sorted := (for $i in $years order by $i return $i) ");
		query.append("let $minYear := fn:min($sorted) cast as xs:integer ");
		query.append("let $maxYear := fn:max($sorted) cast as xs:integer ");
		query.append("let $j := avg( ");
		query.append(" for $i in ($minYear to $maxYear) ");
		query.append("return fn:count($nodes/");
		query.append(pubtype);
		query.append("[year=$i]) ");
		query.append(") ");
		query.append("return $j ");
		// Return the query result
		return formatNumber(Double.parseDouble(this.executeQuery(query
				.toString())));
	}

	public List<Integer> getModeOfAuthorsWhoWrotePublicationsByPubType(
			PublicationType publicationType) throws Exception {
		List<Integer> numberOfAuthors = new ArrayList<Integer>();

		for (int year = 1980; year <= 2013; year++) {
			numberOfAuthors.add((int) this
					.getTotalDistinctAuthorsByYearByPubtype(year,
							publicationType));
		}
		return getModeOfList(numberOfAuthors);
	}

	private List<Integer> getSamplePublicationsInYears() throws Exception {
		// Create the buffer
		StringBuffer query = new StringBuffer();
		// Set the document
		query.append("let $nodes := doc(\"" + xml_file_name + "\")/dblp ");
		query.append("let $years := fn:distinct-values($nodes/*/year) ");
		query.append("let $sorted := (for $i in $years order by $i return $i) ");
		query.append("let $minYear := fn:min($sorted) cast as xs:integer ");
		query.append("let $maxYear := fn:max($sorted) cast as xs:integer ");
		query.append("let $j := ( ");
		query.append(" for $i in ($minYear to $maxYear) ");
		query.append("return fn:concat(fn:string(fn:count($nodes/*");
		query.append("[year=$i])),',') ) ");
		query.append("return $j ");
		// Return the query result
		String[] strings = this.executeQuery(query.toString()).split(",");
		ArrayList<Integer> ints = new ArrayList<Integer>();
		for (int i = 0; i < strings.length; i++) {
			ints.add(Integer.parseInt(strings[i].trim()));
		}
		return ints;
	}

	public double getMedianPublicationsInYears() throws Exception {
		return getMedianOfList(getSamplePublicationsInYears());
	}

	public List<Integer> getModePublicationsInYears() throws Exception {
		return getModeOfList(getSamplePublicationsInYears());
	}

	private List<Integer> getSamplePublicationsInYearsByType(
			PublicationType pubtype) throws Exception {
		// Create the buffer
		StringBuffer query = new StringBuffer();
		// Set the document
		query.append("let $nodes := doc(\"" + xml_file_name + "\")/dblp ");
		query.append("let $years := fn:distinct-values($nodes/*/year) ");
		query.append("let $sorted := (for $i in $years order by $i return $i) ");
		query.append("let $minYear := fn:min($sorted) cast as xs:integer ");
		query.append("let $maxYear := fn:max($sorted) cast as xs:integer ");
		query.append("let $j := ( ");
		query.append(" for $i in ($minYear to $maxYear) ");
		query.append("return fn:concat(fn:string(fn:count($nodes/");
		query.append(pubtype.toString());
		query.append("[year=$i])),',') ) ");
		query.append("return $j ");
		// Return the query result
		String[] strings = this.executeQuery(query.toString()).split(",");
		ArrayList<Integer> ints = new ArrayList<Integer>();
		for (int i = 0; i < strings.length; i++) {
			ints.add(Integer.parseInt(strings[i].trim()));
		}
		return ints;
	}

	public double getMedianPublicationsInYearsByPubtype(PublicationType pubtype)
			throws Exception {
		return getMedianOfList(getSamplePublicationsInYearsByType(pubtype));
	}

	public List<Integer> getModePublicationsInYearByPubtype(
			PublicationType pubtype) throws Exception {
		return getModeOfList(getSamplePublicationsInYearsByType(pubtype));
	}

	public double getAverageNumberOfAuthorsWhoWrotePublications()
			throws Exception {
		List<Integer> numberOfAuthors = new ArrayList<Integer>();

		for (int year = 1980; year <= 2013; year++) {
			numberOfAuthors.add((int) this.getTotalDistinctAuthorsByYear(year));
		}
		return formatNumber(getAverageOfList(numberOfAuthors));
	}

	public double getAverageNumberOfAuthorsWhoWrotePublicationsByPubType(
			PublicationType publicationType) throws Exception {
		List<Integer> numberOfAuthors = new ArrayList<Integer>();
		for (int year = 1980; year <= 2013; year++) {
			numberOfAuthors.add((int) this
					.getTotalDistinctAuthorsByYearByPubtype(year,
							publicationType));
		}
		return formatNumber(getAverageOfList(numberOfAuthors));
	}

	public double getMedianNumberOfAuthorsWhoWrotePublications()
			throws Exception {
		List<Integer> numberOfAuthors = new ArrayList<Integer>();

		for (int year = 1980; year <= 2013; year++) {
			numberOfAuthors.add((int) this.getTotalDistinctAuthorsByYear(year));
		}
		return formatNumber(getMedianOfList(numberOfAuthors));
	}

	public double getMedianOfAuthorsWhoWrotePublicationsByPubType(
			PublicationType publicationType) throws Exception {
		List<Integer> numberOfAuthors = new ArrayList<Integer>();

		for (int year = 1980; year <= 2013; year++) {
			numberOfAuthors.add((int) this
					.getTotalDistinctAuthorsByYearByPubtype(year,
							publicationType));
		}
		return formatNumber(getMedianOfList(numberOfAuthors));
	}

	public List<Integer> getModeOfAuthorsWhoWrotePublications()
			throws Exception {
		List<Integer> numberOfAuthors = new ArrayList<Integer>();

		for (int year = 1980; year <= 2013; year++) {
			numberOfAuthors.add((int) this.getTotalDistinctAuthorsByYear(year));
		}
		return getModeOfList(numberOfAuthors);
	}

	public double getAverageOfList(List<Integer> list) {
		double sum = 0;
		for (Integer i : list) {
			sum += i;
		}
		return sum / list.size();
	}

	public double getMedianOfList(List<Integer> list) {
		Collections.sort(list);
		if (list.size() % 2 == 1)
			return list.get((list.size() + 1) / 2 - 1);
		else {
			int size = list.size();
			if (size == 0) {
				return 0;
			} else {
				double lower = list.get(size / 2 - 1);
				double upper = list.get(size / 2);
				return (lower + upper) / 2.0;
			}

		}
	}

	public List<Integer> getModeOfList(List<Integer> list) {
		ArrayList<Integer> most = new ArrayList<Integer>();

		int num = 0;
		int count = 0;

		for (int i = 0; i < list.size(); i++) {
			int cur_num = list.get(i);
			int cur_count = 1;

			for (int i2 = 0; i2 < list.size(); i2++) {
				if (list.get(i2) == cur_num)
					cur_count++;
			}
			if (cur_count > count) {
				num = cur_num;
				count = cur_count;
				most.clear(); // clear list..found higher.
				most.add(num); // found another
			} else if (cur_count == count) {
				// same high as other number...but add both for now..
				// check to see if its already on the list
				boolean flag = false;
				for (int i3 = 0; i3 < most.size(); i3++) {
					if (most.get(i3) == cur_num) {
						flag = true; // duplicate
					}
				}
				if (flag == false) {
					most.add(cur_num); // found another
				}
			}
		}
		return most;
	}

	private List<Integer> getAuthorsPerPublication() throws Exception {
		// Create the buffer
		StringBuffer query = new StringBuffer();
		// Set the document
		query.append(" let $nodes := doc(\"" + xml_file_name + "\")/dblp ");
		query.append(" let $pubs := $nodes/* ");
		query.append(" let $j := ( ");
		query.append(" for $i in $pubs");
		query.append(" return fn:concat(fn:string(fn:count($i/author");
		query.append(" )),',') ) ");
		query.append(" return $j ");
		// Return the query result
		String[] strings = this.executeQuery(query.toString()).split(",");
		ArrayList<Integer> ints = new ArrayList<Integer>();
		for (int i = 0; i < strings.length - 1; i++) {
			ints.add(Integer.parseInt(strings[i].trim()));
		}
		return ints;
	}

	private List<Integer> getAuthorsPerPublicationByPubType(
			PublicationType publicationType) throws Exception {
		// Create the buffer
		StringBuffer query = new StringBuffer();
		// Set the document
		query.append(" let $nodes := doc(\"" + xml_file_name + "\")/dblp ");
		query.append(" let $pubs := $nodes/");
		query.append(publicationType.toString());
		query.append(" let $j := ( ");
		query.append(" for $i in $pubs");
		query.append(" return fn:concat(fn:string(fn:count($i/author");
		query.append(" )),',') ) ");
		query.append(" return $j ");
		// Return the query result
		String[] strings = this.executeQuery(query.toString()).split(",");
		ArrayList<Integer> ints = new ArrayList<Integer>();
		for (int i = 0; i < strings.length - 1; i++) {
			ints.add(Integer.parseInt(strings[i].trim()));
		}
		return ints;
	}

	public double getMedianOfAuthorsPerPublication() throws Exception {
		List<Integer> numberOfAuthorsPerPublication = new ArrayList<Integer>();
		numberOfAuthorsPerPublication = this.getAuthorsPerPublication();
		return this.getMedianOfList(numberOfAuthorsPerPublication);
	}

	public double getMedianOfAuthorsPerPublicationByPubType(
			PublicationType publicationType) throws Exception {
		List<Integer> numberOfAuthorsPerPublication = new ArrayList<Integer>();
		numberOfAuthorsPerPublication = this
				.getAuthorsPerPublicationByPubType(publicationType);
		return this.getMedianOfList(numberOfAuthorsPerPublication);
	}

	public List<Integer> getModeOfAuthorsPerPublication() throws Exception {
		List<Integer> numberOfAuthorsPerPublication = new ArrayList<Integer>();
		numberOfAuthorsPerPublication = this.getAuthorsPerPublication();
		return this.getModeOfList(numberOfAuthorsPerPublication);
	}

	public List<Integer> getModeOfAuthorsPerPublicationByPubType(
			PublicationType publicationType) throws Exception {
		List<Integer> numberOfAuthorsPerPublication = new ArrayList<Integer>();
		numberOfAuthorsPerPublication = this
				.getAuthorsPerPublicationByPubType(publicationType);
		return this.getModeOfList(numberOfAuthorsPerPublication);
	}

	private List<Integer> getSamplePubsPerAuthor() throws Exception {
		// Create the buffer
		StringBuffer query = new StringBuffer();
		// Set the document
		query.append("let $nodes := doc(\"" + xml_file_name + "\")/dblp ");
		query.append("let $authors := fn:distinct-values($nodes/*/author) ");
		query.append("let $j := ( ");
		query.append(" for $i in $authors ");
		query.append("return fn:concat(fn:string(fn:count($nodes/*[author = $i]");
		query.append(")),',') ) ");
		query.append("return $j ");
		// Return the query result
		String[] strings = this.executeQuery(query.toString()).split(",");
		ArrayList<Integer> ints = new ArrayList<Integer>();
		for (int i = 0; i < strings.length - 1; i++) {
			ints.add(Integer.parseInt(strings[i].trim()));
		}
		return ints;
	}

	private List<Integer> getSamplePubsPerAuthorByType(
			PublicationType publicationType) throws Exception {
		// Create the buffer
		StringBuffer query = new StringBuffer();
		// Set the document
		query.append("let $nodes := doc(\"" + xml_file_name + "\")/dblp ");
		query.append("let $authors := fn:distinct-values($nodes/*/author) ");
		query.append("let $j := ( ");
		query.append(" for $i in $authors ");
		query.append("return fn:concat(fn:string(fn:count($nodes/");
		query.append(publicationType.toString());
		query.append("[author = $i]");
		query.append(")),',') ) ");
		query.append("return $j ");
		// Return the query result
		String[] strings = this.executeQuery(query.toString()).split(",");
		ArrayList<Integer> ints = new ArrayList<Integer>();
		for (int i = 0; i < strings.length - 1; i++) {
			ints.add(Integer.parseInt(strings[i].trim()));
		}
		return ints;
	}

	public double getMedianPubsPerAuthor() throws Exception {
		return getMedianOfList(getSamplePubsPerAuthor());
	}

	public List<Integer> getModePubsPerAuthor() throws Exception {
		return getModeOfList(getSamplePubsPerAuthor());
	}

	public double getMedianPubsPerAuthorByPubtype(PublicationType pubtype)
			throws Exception {
		return getMedianOfList(getSamplePubsPerAuthorByType(pubtype));
	}

	public List<Integer> getModePubsPerAuthorByPubtype(PublicationType pubtype)
			throws Exception {
		return getModeOfList(getSamplePubsPerAuthorByType(pubtype));
	}

	public int numberOfPubsByTwoAuthors(String author1, String author2)
			throws Exception {
		// Create the buffer
		StringBuffer query = new StringBuffer();
		// Set the document
		query.append("let $nodes := doc(\"" + xml_file_name + "\")/dblp ");
		query.append("let $pubs := $nodes//*[author = \"");
		query.append(author1);
		query.append("\"] ");
		query.append("return fn:count( ");
		query.append("for $count in $pubs ");
		query.append("where $count/author = \"");
		query.append(author2);
		query.append("\" ");
		query.append("return 1   ");
		query.append(") ");

		return Integer.parseInt(this.executeQuery(query.toString()));

	}

	/*
	 * ---------------------------------------- -------------- Sprint 3rd
	 * -------------- ----------------------------------------
	 */

	public int getNumberOfCoauthorByAuthorName(String author_name)
			throws NumberFormatException, Exception {
		// Create the buffer
		StringBuffer query = new StringBuffer();
		// Set the document
		query.append(" let $node := doc(\"" + xml_file_name + "\")/dblp ");
		query.append(" let $publications := $node//*[author=\"");
		query.append(author_name);
		query.append("\"] ");
		query.append(" return fn:count($publications/author)-fn:count($publications) ");
		// Return the query result
		return Integer.parseInt(this.executeQuery(query.toString()));
	}

	public String getCoauthorResponseByAuthorName(String authorName)
			throws Exception {
		// Create the buffer
		StringBuffer query = new StringBuffer();
		// Set the document
		query.append("let $node := doc(\"" + xml_file_name + "\")/dblp ");
		query.append("let $authorName := \"");
		query.append(authorName);
		query.append("\"  ");
		query.append("let $publications := $node//*[author=$authorName] ");
		query.append("return  ");
		query.append("<response> ");
		query.append("<author> ");
		query.append("<name>{$authorName}</name> ");
		query.append("<number>{fn:count($publications/author)-fn:count($publications)}</number> ");
		query.append("<co-authors>{ ");
		query.append("let $coauthors := fn:distinct-values($publications/author) ");
		query.append("let $publicationsCoAuthor := $node//*[author=$authorName] ");
		query.append("for $coauthor in $coauthors ");
		query.append("where $coauthor != $authorName ");
		query.append("return  ");
		query.append("<co-author> ");
		query.append("<name>{$coauthor}</name> ");
		query.append("<number>{fn:count($publicationsCoAuthor/author)-fn:count($publicationsCoAuthor)}</number> ");
		query.append("</co-author> ");
		query.append("}        ");
		query.append("</co-authors> ");
		query.append("</author> ");
		query.append("</response> ");
		// Return the query result
		return this.executeQuery(query.toString());
	}

	public String getCoauthorResponseByPubTypeByYearFilter(String first_letter,
			PublicationType pubtype, String... years) throws Exception {
		// Create the buffer
		StringBuffer query = new StringBuffer();
		// Set the document
		query.append("let $nodes := doc(\"" + xml_file_name + "\")/dblp ");
		query.append("let $first_letter := '");
		query.append(first_letter);
		query.append("' ");
		query.append("let $authors := fn:distinct-values($nodes//author[fn:lower-case(fn:substring(. ,1, 1)) = fn:lower-case($first_letter)]) ");
		if (years[0] != null && years[1] == null) {
			query.append("let $yearMin := fn:number(" + years[0] + ") ");
			query.append("let $yearMax := fn:number(" + years[0] + ") ");
		} else if (years[0] != null && years[1] != null) {
			query.append("let $yearMin := fn:number(" + years[0] + ") ");
			query.append("let $yearMax := fn:number(" + years[1] + ") ");
		} else {
			query.append("let $yearMin := fn:number(0) ");
			query.append("let $yearMax := fn:number(5000) ");
		}
		if (pubtype != null) {
			query.append("let $totalPublications := $nodes//");
			query.append(pubtype);
			query.append("[year[fn:number(.) >= $yearMin and fn:number(.) <= $yearMax] and author[fn:lower-case(fn:substring(. ,1, 1)) = fn:lower-case($first_letter)]] ");
		} else {
			query.append("let $totalPublications := $nodes//*[year[fn:number(.) >= $yearMin and fn:number(.) <= $yearMax] and author[fn:lower-case(fn:substring(. ,1, 1)) = fn:lower-case($first_letter)]] ");
		}
		query.append("return  ");
		query.append("<response>");
		if (pubtype != null) {
			query.append("<");
			query.append(pubtype);
			query.append(">");
		}
		query.append("{ ");
		query.append("if(fn:count($totalPublications) > 0) then ( ");
		query.append("for $authorName in $authors ");
		if (pubtype != null) { // filter by pubtype
			query.append("let $publications := $nodes//");
			query.append(pubtype);
			query.append("[year[fn:number(.) >= $yearMin and fn:number(.) <= $yearMax] and author=$authorName] ");
		} else {
			query.append("let $publications := $nodes//*[year[fn:number(.) >= $yearMin and fn:number(.) <= $yearMax] and author=$authorName] ");
		}
		query.append("let $authorNumber := fn:count(fn:distinct-values($publications/author))-1 ");
		query.append("order by $authorName ascending ");
		query.append("return ");
		query.append("<author> ");
		query.append("<name>{$authorName}</name> ");
		query.append("<number>{if($authorNumber>=0) then ($authorNumber) else (0)}</number> ");
		query.append("<co-authors>{ ");
		query.append("let $coauthors := fn:distinct-values($publications/author) ");
		query.append("for $coauthor in $coauthors ");
		if (pubtype != null) { // filter by pubtype
			query.append("let $publicationsCoAuthor := $nodes//");
			query.append(pubtype);
			query.append("[year[fn:number(.) >= $yearMin and fn:number(.) <= $yearMax] and author=$coauthor] ");
		} else {
			query.append("let $publicationsCoAuthor := $nodes//*[year[fn:number(.) >= $yearMin and fn:number(.) <= $yearMax] and author=$coauthor] ");
		}
		query.append("let $coAuthorNumber := fn:count(fn:distinct-values($publicationsCoAuthor/author))-1 ");
		query.append("where $coauthor != $authorName ");
		query.append("order by $coauthor  ");
		query.append("return  ");
		query.append("<co-author> ");
		query.append("<name>{$coauthor}</name> ");
		query.append("<number>{if($coAuthorNumber>=0) then ($coAuthorNumber) else (0)}</number> ");
		query.append("</co-author> ");
		query.append("}         ");
		query.append("</co-authors> ");
		query.append("</author> ");
		query.append(")else( ");
		query.append("<year>");
		query.append("<from>{$yearMin}</from> ");
		query.append("<to>{$yearMax}</to> ");
		query.append("<number_of_publications>{fn:count($totalPublications)}</number_of_publications> ");
		query.append("</year> ");
		query.append(") ");
		query.append("}");
		if (pubtype != null) {
			query.append("</");
			query.append(pubtype);
			query.append(">");
		}
		query.append("</response> ");
		// Return the query result
		return this.executeQuery(query.toString());
	}

	public String getAuthorNameInitialLettersByPubtypeOrByYear(
			PublicationType pubtype, String... years) throws Exception {
		// Create the buffer
		StringBuffer query = new StringBuffer();
		// Set the document
		query.append("let $nodes := doc(\"" + xml_file_name + "\")/dblp ");
		if (years[0] != null && years[1] == null) {
			query.append("let $yearMin := fn:number(" + years[0] + ") ");
			query.append("let $yearMax := fn:number(" + years[0] + ") ");
		} else if (years.length == 2 && years[0] != null && years[1] != null) {
			query.append("let $yearMin := fn:number(" + years[0] + ") ");
			query.append("let $yearMax := fn:number(" + years[1] + ") ");
		} else {
			query.append("let $yearMin := fn:number(0) ");
			query.append("let $yearMax := fn:number(5000) ");
		}
		if (pubtype == null) {
			query.append("let $totalPublications := $nodes//*[year[fn:number(.) >= $yearMin and fn:number(.) <= $yearMax]] ");
		} else {
			query.append("let $totalPublications := $nodes//");
			query.append(pubtype);
			query.append("[year[fn:number(.) >= $yearMin and fn:number(.) <= $yearMax]] ");
		}
		query.append("let $listLetters := ( ");
		query.append("    for $author in fn:distinct-values($totalPublications/author) ");
		query.append("    order by $author ascending ");
		query.append("    return fn:upper-case(fn:substring($author ,1, 1)) ");
		query.append(") ");
		query.append("let $listInitialLetters := fn:distinct-values($listLetters) ");
		query.append("return      ");
		query.append("<response>{ ");
		query.append("    for $initialLeter in $listInitialLetters ");
		query.append("    return ");
		query.append("        <initialLetter>{$initialLeter}</initialLetter> ");
		query.append("}</response> ");
		// Return the query result
		return this.executeQuery(query.toString());
	}

	public List<String> getCoauthorsListByAuthorName(String authorName)
			throws Exception {
		// Create the buffer
		StringBuffer query = new StringBuffer();
		// Set the document
		query.append("let $node := doc(\"" + xml_file_name + "\")/dblp ");
		query.append("let $authorName := \"");
		query.append(authorName);
		query.append("\"  ");
		query.append("let $publications := $node//*[author=$authorName] ");
		query.append("return  ");
		query.append("let $coauthors := fn:distinct-values($publications/author) ");
		query.append("let $publicationsCoAuthor := $node//*[author=$authorName] ");
		query.append("for $coauthor in $coauthors ");
		query.append("where $coauthor != $authorName ");
		query.append("return  ");
		query.append("fn:concat($coauthor,\',\') ");

		String[] strings = this.executeQuery(query.toString()).split(",");
		ArrayList<String> ints = new ArrayList<String>();
		for (int i = 0; i < strings.length; i++) {
			ints.add(strings[i].trim());
		}
		return ints;
	}

	public List<Integer> getYearsRange() throws Exception {
		// Create the buffer
		StringBuffer query = new StringBuffer();
		// Set the document
		query.append("let $nodes := doc(\"" + xml_file_name + "\")/dblp ");
		query.append("let $years := fn:distinct-values($nodes/*/year) ");
		query.append("let $sorted := (for $i in $years order by $i return fn:concat($i,',')) ");
		query.append("return $sorted");
		String[] strings = this.executeQuery(query.toString()).split(",");
		List<Integer> ints = new ArrayList<Integer>();
		for (int i = 0; i < strings.length; i++) {
			ints.add(Integer.parseInt(strings[i].trim()));
		}
		return ints;
	}

	/**
	 * Get the list of years in the file
	 * 
	 * @return String with the response
	 * @throws Exception
	 */
	public String getResponseYearsRange() throws Exception {
		// Create the buffer
		StringBuffer response = new StringBuffer();
		response.append("<response>");
		response.append("<years>");
		List<Integer> years = getYearsRange();
		for (Integer year : years) {
			response.append("<year>");
			response.append(year);
			response.append("</year>");
		}
		response.append("</years>");
		response.append("</response>");
		return response.toString();
	}
}