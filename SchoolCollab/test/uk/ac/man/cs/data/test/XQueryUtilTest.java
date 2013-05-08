package uk.ac.man.cs.data.test;

import static org.junit.Assert.*;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import uk.ac.man.cs.data.PublicationType;
import uk.ac.man.cs.data.XQueryUtil;

/**
 * JUnit class for XQueryUtil. Naming convention for Test:
 * [argument]_[functionName]_[expectedResult]
 * 
 * @author Guillermo Toro
 * @author Emmanouil Samatas
 * @author Pengfei Zhang
 */
public class XQueryUtilTest {
	/**
	 * Constants for amounts
	 */
	public static Integer BOOKS;
	public static Integer ARTICLES;
	public static Integer INCOLLECTIONS;
	public static Integer INPROCEEDINGS;
	public static Integer BOOK_AUTHORS;
	public static Integer ARTICLE_AUTHORS;
	public static Integer INCOLLECTIONS_AUTHORS;
	public static Integer INPROCEEDINGS_AUTHORS;

	public static Integer BOOK_AUTHORS_WITHOUT_DUPLICATION;
	public static Integer ARTICLE_AUTHORS_WITHOUT_DUPLICATION;
	public static Integer INCOLLECTIONS_AUTHORS_WITHOUT_DUPLICATION;
	public static Integer INPROCEEDINGS_AUTHORS_WITHOUT_DUPLICATION;

	/**
	 * Xquery util
	 */
	private static XQueryUtil queryUtil;

	@BeforeClass
	public static void initialiseObjects() throws Exception {
		String filepath = XQueryUtilTest.class.getResource(
				"dblp_curated_sample.xml").toString();
		queryUtil = new XQueryUtil(filepath);
		BOOKS = queryUtil
				.getTotalAmountOfPublicationsByPublicationType(PublicationType.book);
		INCOLLECTIONS = queryUtil
				.getTotalAmountOfPublicationsByPublicationType(PublicationType.incollection);
		INPROCEEDINGS = queryUtil
				.getTotalAmountOfPublicationsByPublicationType(PublicationType.inproceedings);
		ARTICLES = queryUtil
				.getTotalAmountOfPublicationsByPublicationType(PublicationType.article);
		BOOK_AUTHORS = queryUtil
				.getTotalAmountOfAuthorByPublicationType(PublicationType.book);
		INCOLLECTIONS_AUTHORS = queryUtil
				.getTotalAmountOfAuthorByPublicationType(PublicationType.incollection);
		INPROCEEDINGS_AUTHORS = queryUtil
				.getTotalAmountOfAuthorByPublicationType(PublicationType.inproceedings);
		ARTICLE_AUTHORS = queryUtil
				.getTotalAmountOfAuthorByPublicationType(PublicationType.article);

		BOOK_AUTHORS_WITHOUT_DUPLICATION = queryUtil
				.getTotalNumberOfAuthorsWithoutDuplicationByPubtype(PublicationType.book);
		INCOLLECTIONS_AUTHORS_WITHOUT_DUPLICATION = queryUtil
				.getTotalNumberOfAuthorsWithoutDuplicationByPubtype(PublicationType.incollection);
		INPROCEEDINGS_AUTHORS_WITHOUT_DUPLICATION = queryUtil
				.getTotalNumberOfAuthorsWithoutDuplicationByPubtype(PublicationType.inproceedings);
		ARTICLE_AUTHORS_WITHOUT_DUPLICATION = queryUtil
				.getTotalNumberOfAuthorsWithoutDuplicationByPubtype(PublicationType.article);
	}

	private Double formatNumber(Double value) throws Exception {
		DecimalFormat decimalFormat = new DecimalFormat("#0.00");
		return Double.parseDouble(decimalFormat.format(value));
	}

	@Test
	public void bookPubType_calculateAverageAuthorsPerPublicationType_ActualAverage()
			throws Exception {
		Double expectedValue = formatNumber(BOOK_AUTHORS.doubleValue() / BOOKS);

		Double actualValue = queryUtil
				.getAverageAuthorsByPublicationType(PublicationType.book);

		assertEquals(expectedValue, actualValue);
	}

	@Test
	public void articlePubType_calculateAverageAuthorsPerPublicationType_ActualAverage()
			throws Exception {
		Double expectedValue = formatNumber(ARTICLE_AUTHORS.doubleValue()
				/ ARTICLES);

		Double actualValue = queryUtil
				.getAverageAuthorsByPublicationType(PublicationType.article);

		assertEquals(expectedValue, actualValue);
	}

	@Test
	public void inproceedingsPubType_calculateAverageAuthorsPerPublicationType_ActualAverage()
			throws Exception {
		Double expectedValue = formatNumber(INPROCEEDINGS_AUTHORS.doubleValue()
				/ INPROCEEDINGS);

		Double actualValue = queryUtil
				.getAverageAuthorsByPublicationType(PublicationType.inproceedings);

		assertEquals(expectedValue, actualValue);
	}

	@Test
	public void incollectionPubType_calculateAverageAuthorsPerPublicationType_ActualAverage()
			throws Exception {
		Double expectedValue = formatNumber(INCOLLECTIONS_AUTHORS.doubleValue()
				/ INCOLLECTIONS);

		Double actualValue = queryUtil
				.getAverageAuthorsByPublicationType(PublicationType.incollection);

		assertEquals(expectedValue, actualValue);
	}

	@Test
	public void overallPubType_calculateAverageAuthorsPerPublicationType_ActualAverage()
			throws Exception {
		Double expectedValue = formatNumber((INCOLLECTIONS_AUTHORS
				.doubleValue()
				+ ARTICLE_AUTHORS.doubleValue()
				+ BOOK_AUTHORS.doubleValue() + INPROCEEDINGS_AUTHORS
					.doubleValue())
				/ (INCOLLECTIONS + ARTICLES + BOOKS + INPROCEEDINGS));

		Double actualValue = queryUtil.getAverageAuthorsByPublicationType();

		assertEquals(expectedValue, actualValue);
	}

	@Test
	public void arrayPubType_calculateAverageAuthorsPerPublicationType_ActualAverage()
			throws Exception {
		List<PublicationType> publicationTypes = new ArrayList<PublicationType>();
		publicationTypes.add(PublicationType.book);
		publicationTypes.add(PublicationType.article);
		// Double expectedValue = (ARTICLE_AUTHORS + BOOK_AUTHORS)
		// / (ARTICLES + BOOKS);

		// Double actualValue = queryUtil.getAverageAuthorsByPublicationType(
		// PublicationType.book, PublicationType.article);
		//
		// assertEquals(expectedValue, actualValue);
	}

	@Test
	public void testGetTotalNumberOfAuthorsReturnTotalAuthors()
			throws Exception {
		String filepath = this.getClass()
				.getResource("test_data_autherPerPubs_1.xml").toString();
		XQueryUtil util = new XQueryUtil(filepath);
		assertEquals(5, util.getTotalNumberOfAuthors());
	}

	@Test
	public void testGetTotalNumberOfAuthorsReturnTotalAuthorsByBook()
			throws NumberFormatException, Exception {
		String filepath = this.getClass()
				.getResource("test_data_autherPerPubs_1.xml").toString();
		XQueryUtil util = new XQueryUtil(filepath);
		assertEquals(5,
				util.getTotalNumberOfAuthorsByPubType(PublicationType.book));
	}

	@Test
	public void testGetTotalNumberOfAuthorsReturnTotalAuthorsByIncollection()
			throws NumberFormatException, Exception {
		String filepath = this.getClass()
				.getResource("test_data_autherPerPubs_2.xml").toString();
		XQueryUtil util = new XQueryUtil(filepath);
		assertEquals(
				2,
				util.getTotalNumberOfAuthorsByPubType(PublicationType.incollection));
	}

	@Test
	public void testGetTotalNumberOfAuthorsReturnTotalAuthorsByArticle()
			throws NumberFormatException, Exception {
		String filepath = this.getClass()
				.getResource("test_data_autherPerPubs_2.xml").toString();
		XQueryUtil util = new XQueryUtil(filepath);
		assertEquals(0,
				util.getTotalNumberOfAuthorsByPubType(PublicationType.article));
	}

	@Test
	public void testGetTotalNumberOfAuthorsReturnTotalAuthorsByInproceedings()
			throws NumberFormatException, Exception {
		String filepath = this.getClass()
				.getResource("test_data_autherPerPubs_2.xml").toString();
		XQueryUtil util = new XQueryUtil(filepath);
		assertEquals(
				0,
				util.getTotalNumberOfAuthorsByPubType(PublicationType.inproceedings));
	}

	@Test
	public void testGetTotalNumberOfPub() throws NumberFormatException,
			Exception {
		String filepath = this.getClass()
				.getResource("test_data_autherPerPubs_2.xml").toString();
		XQueryUtil util = new XQueryUtil(filepath);
		assertEquals(3, util.getTotalNumberOfPub());
	}

	@Test
	public void testGetTotalNumberOfPubByBook() throws NumberFormatException,
			Exception {
		String filepath = this.getClass()
				.getResource("test_data_autherPerPubs_2.xml").toString();
		XQueryUtil util = new XQueryUtil(filepath);
		assertEquals(2, util.getTotalNumberOfPubByType(PublicationType.book));
	}

	@Test
	public void testGetTotalNumberOfPubByIncollection()
			throws NumberFormatException, Exception {
		String filepath = this.getClass()
				.getResource("test_data_autherPerPubs_2.xml").toString();
		XQueryUtil util = new XQueryUtil(filepath);
		assertEquals(1,
				util.getTotalNumberOfPubByType(PublicationType.incollection));
	}

	@Test
	public void testGetTotalNumberOfPubsReturnTotalPubsByInproceedings()
			throws NumberFormatException, Exception {
		String filepath = this.getClass()
				.getResource("test_data_autherPerPubs_2.xml").toString();
		XQueryUtil util = new XQueryUtil(filepath);
		assertEquals(0,
				util.getTotalNumberOfPubByType(PublicationType.inproceedings));
	}

	@Test
	public void testGetAverageNumberOfAuthorsPerPublicationOverall()
			throws NumberFormatException, Exception {
		String filepath = this.getClass()
				.getResource("test_data_autherPerPubs_2.xml").toString();
		XQueryUtil util = new XQueryUtil(filepath);
		assertEquals(2.3,
				util.getAverageNumberOfAuthorsPerPublicationOverall(), 0.1);
	}

	@Test
	public void testGetAverageNumberOfAuthorsPerPublicationOverallIfNoPub()
			throws NumberFormatException, Exception {
		String filepath = this.getClass()
				.getResource("test_data_autherPerPubs_3.xml").toString();
		XQueryUtil util = new XQueryUtil(filepath);
		assertEquals(0.0,
				util.getAverageNumberOfAuthorsPerPublicationOverall(), 0.1);
	}

	@Test
	public void testGetAverageNumberOfAuthorsPerPublicationByBook()
			throws NumberFormatException, Exception {
		String filepath = this.getClass()
				.getResource("test_data_autherPerPubs_2.xml").toString();
		XQueryUtil util = new XQueryUtil(filepath);
		assertEquals(
				2.5,
				util.getAverageNumberOfAuthorsPerPublicationByPubtype(PublicationType.book),
				0.1);
	}

	@Test
	public void testGetAverageNumberOfAuthorsPerPublicationByIncollection()
			throws NumberFormatException, Exception {
		String filepath = this.getClass()
				.getResource("test_data_autherPerPubs_2.xml").toString();
		XQueryUtil util = new XQueryUtil(filepath);
		assertEquals(
				2.0,
				util.getAverageNumberOfAuthorsPerPublicationByPubtype(PublicationType.incollection),
				0.1);
	}

	@Test
	public void testGetAverageNumberOfAuthorsPerPublicationByInproceedings()
			throws NumberFormatException, Exception {
		String filepath = this.getClass()
				.getResource("test_data_autherPerPubs_2.xml").toString();
		XQueryUtil util = new XQueryUtil(filepath);
		assertEquals(
				0.0,
				util.getAverageNumberOfAuthorsPerPublicationByPubtype(PublicationType.inproceedings),
				0.1);
	}

	@Test
	public void testGetAverageNumberOfAuthorsPerPublicationByArticle()
			throws NumberFormatException, Exception {
		String filepath = this.getClass()
				.getResource("test_data_autherPerPubs_2.xml").toString();
		XQueryUtil util = new XQueryUtil(filepath);
		assertEquals(
				0.0,
				util.getAverageNumberOfAuthorsPerPublicationByPubtype(PublicationType.article),
				0.1);
	}

	@Test
	public void testGetAverageNumberOfAuthorsPerPublicationOverallInActualData()
			throws NumberFormatException, Exception {
		String filepath = this.getClass()
				.getResource("dblp_curated_sample.xml").toString();
		XQueryUtil util = new XQueryUtil(filepath);
		assertEquals(
				5.3,
				util.getAverageNumberOfAuthorsPerPublicationByPubtype(PublicationType.article),
				0.1);
	}

	@Test
	public void testGetTotalNumberOfAuthorsReturnTotalAuthorsWithoutDuplication()
			throws Exception {
		String filepath = this.getClass()
				.getResource("test_data_autherPerPubs_1.xml").toString();
		XQueryUtil util = new XQueryUtil(filepath);
		assertEquals(4, util.getTotalNumberOfAuthorsWithoutDuplication());
	}

	@Test
	public void testGetTotalNumberOfAuthorsReturnTotalAuthorsWithoutDuplicationByBook()
			throws Exception {
		String filepath = this.getClass()
				.getResource("test_data_autherPerPubs_2.xml").toString();
		XQueryUtil util = new XQueryUtil(filepath);
		assertEquals(
				4,
				util.getTotalNumberOfAuthorsWithoutDuplicationByPubtype(PublicationType.book));
	}

	@Test
	public void testGetTotalNumberOfAuthorsReturnTotalAuthorsWithoutDuplicationByIncollection()
			throws Exception {
		String filepath = this.getClass()
				.getResource("test_data_autherPerPubs_2.xml").toString();
		XQueryUtil util = new XQueryUtil(filepath);
		assertEquals(
				2,
				util.getTotalNumberOfAuthorsWithoutDuplicationByPubtype(PublicationType.incollection));
	}

	@Test
	public void testGetAverageNumberOfPublicationsPerAuthorOverall()
			throws NumberFormatException, Exception {
		String filepath = this.getClass()
				.getResource("test_data_autherPerPubs_1.xml").toString();
		XQueryUtil util = new XQueryUtil(filepath);
		assertEquals(1.25, util.getAverageNumberOfPublicationsPerAuthor(), 0.1);
	}

	@Test
	public void testGetAverageNumberOfPublicationsPerAuthorByBook()
			throws NumberFormatException, Exception {
		String filepath = this.getClass()
				.getResource("test_data_autherPerPubs_1.xml").toString();
		XQueryUtil util = new XQueryUtil(filepath);
		assertEquals(
				1.25,
				util.getAverageNumberOfPublicationsPerAuthorByPubtype(PublicationType.book),
				0.1);
	}

	@Test
	public void testGetAverageNumberOfPublicationsPerAuthorByIncollection()
			throws NumberFormatException, Exception {
		String filepath = this.getClass()
				.getResource("test_data_autherPerPubs_2.xml").toString();
		XQueryUtil util = new XQueryUtil(filepath);
		assertEquals(
				0.5,
				util.getAverageNumberOfPublicationsPerAuthorByPubtype(PublicationType.incollection),
				0.1);
	}

	@Test
	public void testGetAverageNumberOfPublicationsPerAuthorByInproceedings()
			throws NumberFormatException, Exception {
		String filepath = this.getClass()
				.getResource("test_data_autherPerPubs_2.xml").toString();
		XQueryUtil util = new XQueryUtil(filepath);
		assertEquals(
				0.0,
				util.getAverageNumberOfPublicationsPerAuthorByPubtype(PublicationType.inproceedings),
				0.1);
	}

	@Test
	public void testGetTotalNumberOfPublicationsByYear1997()
			throws NumberFormatException, Exception {
		String filepath = this.getClass()
				.getResource("test_data_autherPerPubs_4.xml").toString();
		XQueryUtil util = new XQueryUtil(filepath);
		assertEquals(3, util.getTotalPublicationsByYear(1997), 0.1);
	}

	@Test
	public void testGetTotalNumberOfPublicationsByYear1992()
			throws NumberFormatException, Exception {
		String filepath = this.getClass()
				.getResource("test_data_autherPerPubs_4.xml").toString();
		XQueryUtil util = new XQueryUtil(filepath);
		assertEquals(1, util.getTotalPublicationsByYear(1992), 0.1);
	}

	@Test
	public void testGetTotalNumberOfAuthorsByYear1992()
			throws NumberFormatException, Exception {
		String filepath = this.getClass()
				.getResource("test_data_autherPerPubs_2.xml").toString();
		XQueryUtil util = new XQueryUtil(filepath);
		assertEquals(3, util.getTotalAuthorsByYear(1992), 0.1);
	}

	@Test
	public void testGetTotalNumberOfDistinctAuthorsByYear1992()
			throws NumberFormatException, Exception {
		String filepath = this.getClass()
				.getResource("test_data_autherPerPubs_4.xml").toString();
		XQueryUtil util = new XQueryUtil(filepath);
		assertEquals(2, util.getTotalDistinctAuthorsByYear(1992), 0.1);
	}

	@Test
	public void testGetTotalNumberOfDistinctAuthorsByYear1999()
			throws NumberFormatException, Exception {
		String filepath = this.getClass()
				.getResource("test_data_autherPerPubs_4.xml").toString();
		XQueryUtil util = new XQueryUtil(filepath);
		assertEquals(2, util.getTotalDistinctAuthorsByYear(1999), 0.1);
	}

	@Test
	public void testGetAverageAuthorsPerPublicationByYear1997()
			throws NumberFormatException, Exception {
		String filepath = this.getClass()
				.getResource("test_data_autherPerPubs_4.xml").toString();
		XQueryUtil util = new XQueryUtil(filepath);
		assertEquals(2.3, util.getAverageAuthorsPerPubByYear(1997), 0.1);
	}

	@Test
	public void testGetAveragePublicationsPerAuthorByYear1997()
			throws NumberFormatException, Exception {
		String filepath = this.getClass()
				.getResource("test_data_autherPerPubs_4.xml").toString();
		XQueryUtil util = new XQueryUtil(filepath);
		assertEquals(1.4, util.getAveragePubsPerAuthorOverallByYear(1997), 0.1);
	}

	@Test
	public void testGetTotalAuthorsBy1997ByBook() throws Exception {
		String filepath = this.getClass()
				.getResource("test_data_autherPerPubs_4.xml").toString();
		XQueryUtil util = new XQueryUtil(filepath);
		assertEquals(
				5,
				util.getTotalAuthorsByYearByPubtype(1997, PublicationType.book),
				0.1);
	}

	@Test
	public void testGetAvgPublicationsInYearsOverall() throws Exception {
		String filepath = this.getClass()
				.getResource("test_data_autherPerPubs_5.xml").toString();
		XQueryUtil util = new XQueryUtil(filepath);
		assertEquals(0.625, util.getAveragePublicationsInYearsOverall(), 0.1);
	}

	@Test
	public void testGetAvgPublicationsInYearsByBook() throws Exception {
		String filepath = this.getClass()
				.getResource("test_data_autherPerPubs_6.xml").toString();
		XQueryUtil util = new XQueryUtil(filepath);
		assertEquals(
				0.375,
				util.getAveragePublicationsInYearsByPubtype(PublicationType.book),
				0.1);
	}

	@Test
	public void testGetAvgPublicationsInYearsByInproceedings() throws Exception {
		String filepath = this.getClass()
				.getResource("test_data_autherPerPubs_7.xml").toString();
		XQueryUtil util = new XQueryUtil(filepath);
		assertEquals(
				0.625,
				util.getAveragePublicationsInYearsByPubtype(PublicationType.inproceedings),
				0.1);
	}

	@Test
	public void testGetMedianPublicationsInYears() throws Exception {
		String filepath = this.getClass()
				.getResource("test_data_autherPerPubs_8.xml").toString();
		XQueryUtil util = new XQueryUtil(filepath);
		assertEquals(1, util.getMedianPublicationsInYears(), 0.1);
	}

	@Test
	public void testGetMedianPublicationsInYearsByBook() throws Exception {
		String filepath = this.getClass()
				.getResource("test_data_autherPerPubs_8.xml").toString();
		XQueryUtil util = new XQueryUtil(filepath);
		assertEquals(
				0.0,
				util.getMedianPublicationsInYearsByPubtype(PublicationType.book),
				0.1);
	}

	@Test
	public void testGetMedianPublicationsInYearsByInproceedings()
			throws Exception {
		String filepath = this.getClass()
				.getResource("test_data_autherPerPubs_8.xml").toString();
		XQueryUtil util = new XQueryUtil(filepath);
		assertEquals(
				1,
				util.getMedianPublicationsInYearsByPubtype(PublicationType.inproceedings),
				0.1);
	}

	@Test
	public void testGetModePublicationsInYears() throws Exception {
		String filepath = this.getClass()
				.getResource("test_data_autherPerPubs_8.xml").toString();
		XQueryUtil util = new XQueryUtil(filepath);
		List<Integer> list = new ArrayList<Integer>();
		list.add(new Integer(1));
		assertEquals(list, util.getModePublicationsInYears());
	}

	@Test
	public void testGetModePublicationsInYearsByBook() throws Exception {
		String filepath = this.getClass()
				.getResource("test_data_autherPerPubs_8.xml").toString();
		XQueryUtil util = new XQueryUtil(filepath);
		List<Integer> list = new ArrayList<Integer>();
		list.add(new Integer(0));
		assertEquals(list,
				util.getModePublicationsInYearByPubtype(PublicationType.book));
	}

	@Test
	public void testGetModePublicationsInYearsByInproceedings()
			throws Exception {
		String filepath = this.getClass()
				.getResource("test_data_autherPerPubs_8.xml").toString();
		XQueryUtil util = new XQueryUtil(filepath);
		List<Integer> list = new ArrayList<Integer>();
		list.add(new Integer(1));
		assertEquals(
				list,
				util.getModePublicationsInYearByPubtype(PublicationType.inproceedings));
	}

	@Test
	public void testGetModePublicationsInYearsReturnAllItems() throws Exception {
		String filepath = this.getClass()
				.getResource("test_data_autherPerPubs_9.xml").toString();
		XQueryUtil util = new XQueryUtil(filepath);
		List<Integer> list = new ArrayList<Integer>();
		list.add(new Integer(1));
		list.add(new Integer(6));
		list.add(new Integer(0));
		list.add(new Integer(3));
		list.add(new Integer(2));
		assertEquals(list, util.getModePublicationsInYears());
	}

	@Test
	public void testGetAverageOfArrayList() throws Exception {
		String filepath = this.getClass()
				.getResource("test_data_autherPerPubs_4.xml").toString();
		XQueryUtil util = new XQueryUtil(filepath);
		ArrayList<Integer> test = new ArrayList<Integer>();
		test.add(1);
		test.add(1);
		test.add(1);
		test.add(1);
		test.add(5);
		assertEquals(1.8, util.getAverageOfList(test), 0.1);
	}

	@Test
	public void testGetAverageNumberOfAuthorsWhoWrotePublications()
			throws Exception {
		String filepath = this.getClass()
				.getResource("dblp_curated_sample.xml").toString();
		XQueryUtil util = new XQueryUtil(filepath);
		assertEquals(70.7,
				util.getAverageNumberOfAuthorsWhoWrotePublications(), 0.1);
	}

	@Test
	public void testGetAverageNumberOfAuthorsWhoWrotePublicationsByPubBook()
			throws Exception {
		String filepath = this.getClass()
				.getResource("dblp_curated_sample.xml").toString();
		XQueryUtil util = new XQueryUtil(filepath);
		assertEquals(
				0.74,
				util.getAverageNumberOfAuthorsWhoWrotePublicationsByPubType(PublicationType.book),
				0.1);
	}

	@Test
	public void testGetMedianOfList() throws Exception {
		String filepath = this.getClass()
				.getResource("test_data_autherPerPubs_4.xml").toString();
		XQueryUtil util = new XQueryUtil(filepath);
		ArrayList<Integer> test = new ArrayList<Integer>();
		test.add(1);
		test.add(1);
		test.add(1);
		test.add(1);
		test.add(5);
		assertEquals(1, util.getMedianOfList(test), 0.1);
	}

	@Test
	public void testGetMedianNumberOfAuthorsWhoWrotePublications()
			throws Exception {
		String filepath = this.getClass()
				.getResource("dblp_curated_sample.xml").toString();
		XQueryUtil util = new XQueryUtil(filepath);
		assertEquals(45.5, util.getMedianNumberOfAuthorsWhoWrotePublications(),
				0.1);
	}

	@Test
	public void testGetMedianOfAuthorsWhoWrotePublicationsByPubType()
			throws Exception {
		String filepath = this.getClass()
				.getResource("dblp_curated_sample.xml").toString();
		XQueryUtil util = new XQueryUtil(filepath);
		assertEquals(
				16.0,
				util.getMedianOfAuthorsWhoWrotePublicationsByPubType(PublicationType.article),
				0.1);
	}

	@Test
	public void testGetModeOfList() throws Exception {
		String filepath = this.getClass()
				.getResource("test_data_autherPerPubs_4.xml").toString();
		XQueryUtil util = new XQueryUtil(filepath);

		ArrayList<Integer> expectedList = new ArrayList<Integer>();
		expectedList.add(1);
		expectedList.add(2);
		expectedList.add(3);

		ArrayList<Integer> test = new ArrayList<Integer>();
		test.add(1);
		test.add(1);
		test.add(2);
		test.add(2);
		test.add(5);
		test.add(3);
		test.add(4);
		test.add(3);
		assertEquals(expectedList, util.getModeOfList(test));
	}

	@Test
	public void testGetModeOfAuthorsWhoWrotePublications() throws Exception {
		String filepath = this.getClass()
				.getResource("dblp_curated_sample.xml").toString();
		XQueryUtil util = new XQueryUtil(filepath);

		ArrayList<Integer> expectedList = new ArrayList<Integer>();
		expectedList.add(3);
		expectedList.add(7);
		expectedList.add(6);
		expectedList.add(25);
		expectedList.add(156);
		assertEquals(expectedList, util.getModeOfAuthorsWhoWrotePublications());
	}

	@Test
	public void testGetModeOfAuthorsWhoWrotePublicationsByPubType()
			throws Exception {
		String filepath = this.getClass()
				.getResource("dblp_curated_sample.xml").toString();
		XQueryUtil util = new XQueryUtil(filepath);

		ArrayList<Integer> expectedList = new ArrayList<Integer>();
		expectedList.add(5);
		assertEquals(
				expectedList,
				util.getModeOfAuthorsWhoWrotePublicationsByPubType(PublicationType.article));
	}

	@Test
	public void testGetMedianPublicationsPerAuthorOverall() throws Exception {
		String filepath = this.getClass()
				.getResource("test_data_autherPerPubs_2.xml").toString();
		XQueryUtil util = new XQueryUtil(filepath);

		assertEquals(1.0, util.getMedianPubsPerAuthor(), 0.1);
	}

	@Test
	public void testGetModePublicationsPerAuthorOverall() throws Exception {
		String filepath = this.getClass()
				.getResource("test_data_autherPerPubs_2.xml").toString();
		XQueryUtil util = new XQueryUtil(filepath);
		ArrayList<Integer> list = new ArrayList<Integer>();
		list.add(1);
		assertEquals(list, util.getModePubsPerAuthor());
	}

	@Test
	public void testGetMedianPublicationsPerAuthorByBookType() throws Exception {
		String filepath = this.getClass()
				.getResource("test_data_autherPerPubs_2.xml").toString();
		XQueryUtil util = new XQueryUtil(filepath);
		assertEquals(1.0,
				util.getMedianPubsPerAuthorByPubtype(PublicationType.book), 0.1);
	}

	@Test
	public void testGetModePublicationsPerAuthorByBookType() throws Exception {
		String filepath = this.getClass()
				.getResource("test_data_autherPerPubs_2.xml").toString();
		XQueryUtil util = new XQueryUtil(filepath);
		ArrayList<Integer> list = new ArrayList<Integer>();
		list.add(1);
		assertEquals(list,
				util.getModePubsPerAuthorByPubtype(PublicationType.book));
	}

	@Test
	public void testGetMedianOfAuthorsPerPublication() throws Exception {
		String filepath = this.getClass()
				.getResource("dblp_curated_sample.xml").toString();
		XQueryUtil util = new XQueryUtil(filepath);
		assertEquals(4.0, util.getMedianOfAuthorsPerPublication(), 0.1);
	}

	@Test
	public void testGetMedianOfAuthorsPerPublicationByPubType()
			throws Exception {
		String filepath = this.getClass()
				.getResource("dblp_curated_sample.xml").toString();
		XQueryUtil util = new XQueryUtil(filepath);
		assertEquals(
				3.0,
				util.getMedianOfAuthorsPerPublicationByPubType(PublicationType.book),
				0.1);
	}

	@Test
	public void testGetModeOfAuthorsPerPublication() throws Exception {
		String filepath = this.getClass()
				.getResource("dblp_curated_sample.xml").toString();
		XQueryUtil util = new XQueryUtil(filepath);
		ArrayList<Integer> expectedList = new ArrayList<Integer>();
		expectedList.add(3);
		assertEquals(expectedList, util.getModeOfAuthorsPerPublication());
	}

	@Test
	public void testGetModeOfAuthorsPerPublicationByPubType() throws Exception {
		String filepath = this.getClass()
				.getResource("dblp_curated_sample.xml").toString();
		XQueryUtil util = new XQueryUtil(filepath);
		ArrayList<Integer> expectedList = new ArrayList<Integer>();
		expectedList.add(3);
		assertEquals(
				expectedList,
				util.getModeOfAuthorsPerPublicationByPubType(PublicationType.article));
	}

	@Test
	public void testGetAuthorsListWithoutDuplication() throws Exception {
		String filepath = this.getClass()
				.getResource("test_data_autherPerPubs_1.xml").toString();
		XQueryUtil util = new XQueryUtil(filepath);
		List<String> list = new ArrayList<String>();
		list.add("Stefano Ceri");
		list.add("Piero Fraternali");
		list.add("Carlo Batini");
		list.add("Shamkant B. Navathe");
		assertEquals(list, util.getAuthorsListWithoutDuplication());
	}

	@Test
	public void testNumberOfPubsByTwoAuthors() throws Exception {
		String filepath = this.getClass()
				.getResource("test_data_autherPerPubs_1.xml").toString();
		XQueryUtil util = new XQueryUtil(filepath);
		assertEquals(1, util.numberOfPubsByTwoAuthors("Stefano Ceri",
				"Piero Fraternali"));
	}

	// Test case for Sprint 3rd
	@Test
	public void testGetNumberOfCoauthorByAuthorName() throws Exception {
		String filepath = this.getClass()
				.getResource("test_data_co-author_10.xml").toString();
		XQueryUtil util = new XQueryUtil(filepath);
		String authorName = "Stefano Ceri";
		assertEquals(3, util.getNumberOfCoauthorByAuthorName(authorName));
	}

	@Test
	public void testGetCoauthorsResponseByAuthor_name() throws Exception {
		String filepath = this.getClass()
				.getResource("test_data_co-author_10.xml").toString();
		XQueryUtil util = new XQueryUtil(filepath);
		// parameter
		String authorName = "Oscar Daz";
		// expected values
		StringBuffer response = new StringBuffer();
		response.append("<response>\n");
		response.append("<author>\n");
		response.append("<name>Oscar Daz</name>\n");
		response.append("<number>1</number>\n");
		response.append("<co-authors>\n");
		response.append("<co-author>\n");
		response.append("<name>Suzanne M. Embury</name>\n");
		response.append("<number>1</number>\n");
		response.append("</co-author>\n");
		response.append("</co-authors>\n");
		response.append("</author>\n");
		response.append("</response>");
		String cleanResponse = response.toString().trim();
		cleanResponse = cleanResponse.replace(" ", "");
		String cleanActualResponse = util.getCoauthorResponseByAuthorName(
				authorName).trim();
		cleanActualResponse = cleanActualResponse.replace(" ", "");
		assertEquals(cleanResponse, cleanActualResponse);
	}

	@Test
	public void testGetCoauthorsResponseByAuthor_name_Refection()
			throws Exception {
		String filepath = this.getClass()
				.getResource("test_data_co-author_10.xml").toString();
		XQueryUtil util = new XQueryUtil(filepath);
		// parameter
		String authorName = "Suzanne M. Embury";
		// expected values
		StringBuffer response = new StringBuffer();
		response.append("<response>\n");
		response.append("<author>\n");
		response.append("<name>Suzanne M. Embury</name>\n");
		response.append("<number>1</number>\n");
		response.append("<co-authors>\n");
		response.append("<co-author>\n");
		response.append("<name>Oscar Daz</name>\n");
		response.append("<number>1</number>\n");
		response.append("</co-author>\n");
		response.append("</co-authors>\n");
		response.append("</author>\n");
		response.append("</response>\n");
		String cleanResponse = response.toString().trim();
		cleanResponse = cleanResponse.replace(" ", "");
		String cleanActualResponse = util.getCoauthorResponseByAuthorName(
				authorName).trim();
		cleanActualResponse = cleanActualResponse.replace(" ", "");
		assertEquals(cleanResponse, cleanActualResponse);
	}

	@Test
	public void testGetCoauthorsResponseByAuthor_name_StartWithS()
			throws Exception {
		String filepath = this.getClass()
				.getResource("test_data_co-author_10.xml").toString();
		XQueryUtil util = new XQueryUtil(filepath);
		// parameter
		String letter = "S";
		// expected values
		StringBuffer response = new StringBuffer();
		response.append("<response>\n");
		response.append("<author>\n");
		response.append("<name>Shamkant B. Navathe</name>\n");
		response.append("<number>2</number>\n");
		response.append("<co-authors>\n");
		response.append("<co-author>\n");
		response.append("<name>Carlo Batini</name>\n");
		response.append("<number>2</number>\n");
		response.append("</co-author>\n");
		response.append("<co-author>\n");
		response.append("<name>Stefano Ceri</name>\n");
		response.append("<number>3</number>\n");
		response.append("</co-author>\n");
		response.append("</co-authors>\n");
		response.append("</author>\n");
		response.append("<author>\n");
		response.append("<name>Stefano Ceri</name>\n");
		response.append("<number>3</number>\n");
		response.append("<co-authors>\n");
		response.append("<co-author>\n");
		response.append("<name>Carlo Batini</name>\n");
		response.append("<number>2</number>\n");
		response.append("</co-author>\n");
		response.append("<co-author>\n");
		response.append("<name>Raghu Ramakrishnan</name>\n");
		response.append("<number>1</number>\n");
		response.append("</co-author>\n");
		response.append("<co-author>\n");
		response.append("<name>Shamkant B. Navathe</name>\n");
		response.append("<number>2</number>\n");
		response.append("</co-author>\n");
		response.append("</co-authors>\n");
		response.append("</author>\n");
		response.append("<author>\n");
		response.append("<name>Suzanne M. Embury</name>\n");
		response.append("<number>1</number>\n");
		response.append("<co-authors>\n");
		response.append("<co-author>\n");
		response.append("<name>Oscar Daz</name>\n");
		response.append("<number>1</number>\n");
		response.append("</co-author>\n");
		response.append("</co-authors>\n");
		response.append("</author>\n");
		response.append("</response>\n");
		String cleanResponse = response.toString().trim();
		cleanResponse = cleanResponse.replace(" ", "");
		String cleanActualResponse = util
				.getCoauthorResponseByPubTypeByYearFilter(letter, null, null,
						null).trim();
		cleanActualResponse = cleanActualResponse.replace(" ", "");
		assertEquals(cleanResponse, cleanActualResponse);
	}

	@Test
	public void testGetCoauthorsResponseByAuthornameStartwithS_byIncollection()
			throws Exception {
		String filepath = this.getClass()
				.getResource("test_data_co-author_bytype_11.xml").toString();
		XQueryUtil util = new XQueryUtil(filepath);
		// parameter
		String letter = "S";
		// expected values
		StringBuffer response = new StringBuffer();
		response.append("<response>\n");
		response.append("<incollection>\n");
		response.append("<author>\n");
		response.append("<name>ShamkantB.Navathe</name>\n");
		response.append("<number>0</number>\n");
		response.append("<co-authors/>\n");
		response.append("</author>\n");
		response.append("<author>\n");
		response.append("<name>Stefano Ceri</name>\n");
		response.append("<number>2</number>\n");
		response.append("<co-authors>\n");
		response.append("<co-author>\n");
		response.append("<name>BarbaraPernici</name>\n");
		response.append("<number>1</number>\n");
		response.append("</co-author>\n");
		response.append("<co-author>\n");
		response.append("<name>Raghu Ramakrishnan</name>\n");
		response.append("<number>1</number>\n");
		response.append("</co-author>\n");
		response.append("</co-authors>\n");
		response.append("</author>\n");
		response.append("</incollection>\n");
		response.append("</response>\n");
		String cleanResponse = response.toString().trim();
		cleanResponse = cleanResponse.replace(" ", "");
		String cleanActualResponse = util
				.getCoauthorResponseByPubTypeByYearFilter(letter,
						PublicationType.incollection, null, null).trim();
		cleanActualResponse = cleanActualResponse.replace(" ", "");
		assertEquals(cleanResponse, cleanActualResponse);
	}

	@Test
	public void testGetCoauthorsResponseByAuthornameStartwithZ_byBook_inDBLP()
			throws Exception {
		String filepath = this.getClass()
				.getResource("dblp_curated_sample.xml").toString();
		XQueryUtil util = new XQueryUtil(filepath);
		// parameter
		String letter = "Z";
		// expected values
		StringBuffer response = new StringBuffer();
		response.append("<response>\n");
		response.append("<book>\n");
		response.append("<author>\n");
		response.append("<name>Z.Meralzsoyoglu</name>\n");
		response.append("<number>0</number>\n");
		response.append("<co-authors/>\n");
		response.append("</author>\n");
		response.append("<author>\n");
		response.append("<name>ZacharyG.Ives</name>\n");
		response.append("<number>2</number>\n");
		response.append("<co-authors>\n");
		response.append("<co-author>\n");
		response.append("<name>AlonY.Halevy</name>\n");
		response.append("<number>2</number>\n");
		response.append("</co-author>\n");
		response.append("<co-author>\n");
		response.append("<name>AnHaiDoan</name>\n");
		response.append("<number>2</number>\n");
		response.append("</co-author>\n");
		response.append("</co-authors>\n");
		response.append("</author>\n");
		response.append("<author>\n");
		response.append("<name>ZhanCui</name>\n");
		response.append("<number>0</number>\n");
		response.append("<co-authors/>\n");
		response.append("</author>\n");
		response.append("<author>\n");
		response.append("<name>ZhuoanJiao</name>\n");
		response.append("<number>0</number>\n");
		response.append("<co-authors/>\n");
		response.append("</author>\n");
		response.append("<author>\n");
		response.append("<name>ZoLacroix</name>\n");
		response.append("<number>0</number>\n");
		response.append("<co-authors/>\n");
		response.append("</author>\n");
		response.append("<author>\n");
		response.append("<name>ZoubidaKedad</name>\n");
		response.append("<number>0</number>\n");
		response.append("<co-authors/>\n");
		response.append("</author>\n");
		response.append("</book>\n");
		response.append("</response>\n");
		String cleanResponse = response.toString().trim();
		cleanResponse = cleanResponse.replace(" ", "");
		String cleanActualResponse = util
				.getCoauthorResponseByPubTypeByYearFilter(letter,
						PublicationType.book, null, null).trim();
		cleanActualResponse = cleanActualResponse.replace(" ", "");
		assertEquals(cleanResponse, cleanActualResponse);
	}

	@Test
	public void testGetCoauthorsResponse_StartwithS_ByAllPubtypes_In1997_1999()
			throws Exception {
		String filepath = this.getClass()
				.getResource("test_data_co-author_bytype_11.xml").toString();
		XQueryUtil util = new XQueryUtil(filepath);
		// parameter
		String letter = "S";
		String yearMin = "1997";
		String yearMax = "1999";
		// expected value
		StringBuffer response = new StringBuffer();
		response.append("<response>\n");
		response.append("<author>\n");
		response.append("<name>ShamkantB.Navathe</name>\n");
		response.append("<number>2</number>\n");
		response.append("<co-authors>\n");
		response.append("<co-author>\n");
		response.append("<name>CarloBatini</name>\n");
		response.append("<number>2</number>\n");
		response.append("</co-author>\n");
		response.append("<co-author>\n");
		response.append("<name>StefanoCeri</name>\n");
		response.append("<number>4</number>\n");
		response.append("</co-author>\n");
		response.append("</co-authors>\n");
		response.append("</author>\n");
		response.append("<author>\n");
		response.append("<name>StefanoCeri</name>\n");
		response.append("<number>4</number>\n");
		response.append("<co-authors>\n");
		response.append("<co-author>\n");
		response.append("<name>BarbaraPernici</name>\n");
		response.append("<number>1</number>\n");
		response.append("</co-author>\n");
		response.append("<co-author>\n");
		response.append("<name>CarloBatini</name>\n");
		response.append("<number>2</number>\n");
		response.append("</co-author>\n");
		response.append("<co-author>\n");
		response.append("<name>RaghuRamakrishnan</name>\n");
		response.append("<number>1</number>\n");
		response.append("</co-author>\n");
		response.append("<co-author>\n");
		response.append("<name>ShamkantB.Navathe</name>\n");
		response.append("<number>2</number>\n");
		response.append("</co-author>\n");
		response.append("</co-authors>\n");
		response.append("</author>\n");
		response.append("</response>\n");
		String cleanResponse = response.toString().trim();
		cleanResponse = cleanResponse.replace(" ", "");
		String cleanActualResponse = util
				.getCoauthorResponseByPubTypeByYearFilter(letter, null,
						yearMin, yearMax).trim();
		cleanActualResponse = cleanActualResponse.replace(" ", "");
		assertEquals(cleanResponse, cleanActualResponse);
	}

	@Test
	public void testGetCoauthorsResponse_StartwithS_WithAllPubtypes_In1997()
			throws Exception {
		String filepath = this.getClass()
				.getResource("test_data_co-author_bytype_11.xml").toString();
		XQueryUtil util = new XQueryUtil(filepath);
		// parameter
		String letter = "S";
		String year = "1997";
		// expected value
		StringBuffer response = new StringBuffer();
		response.append("<response>\n");
		response.append("<author>\n");
		response.append("<name>ShamkantB.Navathe</name>\n");
		response.append("<number>2</number>\n");
		response.append("<co-authors>\n");
		response.append("<co-author>\n");
		response.append("<name>CarloBatini</name>\n");
		response.append("<number>2</number>\n");
		response.append("</co-author>\n");
		response.append("<co-author>\n");
		response.append("<name>StefanoCeri</name>\n");
		response.append("<number>3</number>\n");
		response.append("</co-author>\n");
		response.append("</co-authors>\n");
		response.append("</author>\n");
		response.append("<author>\n");
		response.append("<name>StefanoCeri</name>\n");
		response.append("<number>3</number>\n");
		response.append("<co-authors>\n");
		response.append("<co-author>\n");
		response.append("<name>CarloBatini</name>\n");
		response.append("<number>2</number>\n");
		response.append("</co-author>\n");
		response.append("<co-author>\n");
		response.append("<name>RaghuRamakrishnan</name>\n");
		response.append("<number>1</number>\n");
		response.append("</co-author>\n");
		response.append("<co-author>\n");
		response.append("<name>ShamkantB.Navathe</name>\n");
		response.append("<number>2</number>\n");
		response.append("</co-author>\n");
		response.append("</co-authors>\n");
		response.append("</author>\n");
		response.append("</response>\n");
		String cleanResponse = response.toString().trim();
		cleanResponse = cleanResponse.replace(" ", "");
		String cleanActualResponse = util
				.getCoauthorResponseByPubTypeByYearFilter(letter, null, year,
						null).trim();
		cleanActualResponse = cleanActualResponse.replace(" ", "");
		assertEquals(cleanResponse, cleanActualResponse);
	}

	@Test
	public void testGetCoauthorsResponseByStartwithX_AllPubtypes_InBetween2011_2012_dblp()
			throws Exception {
		String filepath = this.getClass()
				.getResource("dblp_curated_sample.xml").toString();
		XQueryUtil util = new XQueryUtil(filepath);
		// parameter
		String letter = "z";
		String yearMin = "2011";
		String yearMax = "2012";
		// expected value
		StringBuffer response = new StringBuffer();
		// TODO change expected response
		response.append("<response>\n");
		response.append("<author>\n");
		response.append("<name>Z.Meralzsoyoglu</name>\n");
		response.append("<number>0</number>\n");
		response.append("<co-authors/>\n");
		response.append("</author>\n");
		response.append("<author>\n");
		response.append("<name>ZacharyG.Ives</name>\n");
		response.append("<number>2</number>\n");
		response.append("<co-authors>\n");
		response.append("<co-author>\n");
		response.append("<name>AlonY.Halevy</name>\n");
		response.append("<number>31</number>\n");
		response.append("</co-author>\n");
		response.append("<co-author>\n");
		response.append("<name>AnHaiDoan</name>\n");
		response.append("<number>3</number>\n");
		response.append("</co-author>\n");
		response.append("</co-authors>\n");
		response.append("</author>\n");
		response.append("<author>\n");
		response.append("<name>ZhanCui</name>\n");
		response.append("<number>0</number>\n");
		response.append("<co-authors/>\n");
		response.append("</author>\n");
		response.append("<author>\n");
		response.append("<name>ZhuoanJiao</name>\n");
		response.append("<number>0</number>\n");
		response.append("<co-authors/>\n");
		response.append("</author>\n");
		response.append("<author>\n");
		response.append("<name>ZoLacroix</name>\n");
		response.append("<number>0</number>\n");
		response.append("<co-authors/>\n");
		response.append("</author>\n");
		response.append("<author>\n");
		response.append("<name>ZoubidaKedad</name>\n");
		response.append("<number>0</number>\n");
		response.append("<co-authors/>\n");
		response.append("</author>\n");
		response.append("</response>\n");
		String cleanResponse = response.toString().trim();
		cleanResponse = cleanResponse.replace(" ", "");
		String cleanActualResponse = util
				.getCoauthorResponseByPubTypeByYearFilter(letter, null,
						yearMin, yearMax).trim();
		cleanActualResponse = cleanActualResponse.replace(" ", "");
		assertEquals(cleanResponse, cleanActualResponse);
	}

	@Test
	public void testGetCoauthorsResponse_StartwithA_In2014_NoPublications()
			throws Exception {
		String filepath = this.getClass()
				.getResource("dblp_curated_sample.xml").toString();
		XQueryUtil util = new XQueryUtil(filepath);
		// parameter
		String letter = "a";
		String year = "2014";
		// expected value
		StringBuffer response = new StringBuffer();
		// TODO change expected response
		response.append("<response>\n");
		response.append("<year>\n");
		response.append("<from>2014</from>\n");
		response.append("<to>2014</to>\n");
		response.append("<number_of_publications>0</number_of_publications>\n");
		response.append("</year>\n");
		response.append("</response>\n");
		String cleanResponse = response.toString().trim();
		cleanResponse = cleanResponse.replace(" ", "");
		String cleanActualResponse = util
				.getCoauthorResponseByPubTypeByYearFilter(letter, null, year,
						null).trim();
		cleanActualResponse = cleanActualResponse.replace(" ", "");
		assertEquals(cleanResponse, cleanActualResponse);
	}

	@Test
	public void testGetCoauthorsResponse_StartwithX_InAllYears()
			throws Exception {
		String filepath = this.getClass()
				.getResource("dblp_curated_sample.xml").toString();
		XQueryUtil util = new XQueryUtil(filepath);
		// parameter
		String letter = "z";
		// expected value
		StringBuffer response = new StringBuffer();
		// TODO change expected response
		response.append("<response>\n");
		response.append("<author>\n");
		response.append("<name>Z.Meralzsoyoglu</name>\n");
		response.append("<number>6</number>\n");
		response.append("<co-authors>\n");
		response.append("<co-author>\n");
		response.append("<name>HongjunLu</name>\n");
		response.append("<number>6</number>\n");
		response.append("</co-author>\n");
		response.append("<co-author>\n");
		response.append("<name>LeonidA.Kalinichenko</name>\n");
		response.append("<number>6</number>\n");
		response.append("</co-author>\n");
		response.append("<co-author>\n");
		response.append("<name>MasaruKitsuregawa</name>\n");
		response.append("<number>6</number>\n");
		response.append("</co-author>\n");
		response.append("<co-author>\n");
		response.append("<name>RichardT.Snodgrass</name>\n");
		response.append("<number>38</number>\n");
		response.append("</co-author>\n");
		response.append("<co-author>\n");
		response.append("<name>StefanoCeri</name>\n");
		response.append("<number>230</number>\n");
		response.append("</co-author>\n");
		response.append("<co-author>\n");
		response.append("<name>VictorVianu</name>\n");
		response.append("<number>6</number>\n");
		response.append("</co-author>\n");
		response.append("</co-authors>\n");
		response.append("</author>\n");
		response.append("<author>\n");
		response.append("<name>ZacharyG.Ives</name>\n");
		response.append("<number>21</number>\n");
		response.append("<co-authors>\n");
		response.append("<co-author>\n");
		response.append("<name>AlonY.Halevy</name>\n");
		response.append("<number>195</number>\n");
		response.append("</co-author>\n");
		response.append("<co-author>\n");
		response.append("<name>AnHaiDoan</name>\n");
		response.append("<number>38</number>\n");
		response.append("</co-author>\n");
		response.append("<co-author>\n");
		response.append("<name>AnastassiaAilamaki</name>\n");
		response.append("<number>7</number>\n");
		response.append("</co-author>\n");
		response.append("<co-author>\n");
		response.append("<name>DanSuciu</name>\n");
		response.append("<number>13</number>\n");
		response.append("</co-author>\n");
		response.append("<co-author>\n");
		response.append("<name>DanielS.Weld</name>\n");
		response.append("<number>4</number>\n");
		response.append("</co-author>\n");
		response.append("<co-author>\n");
		response.append("<name>DavidJ.DeWitt</name>\n");
		response.append("<number>34</number>\n");
		response.append("</co-author>\n");
		response.append("<co-author>\n");
		response.append("<name>GerhardWeikum</name>\n");
		response.append("<number>57</number>\n");
		response.append("</co-author>\n");
		response.append("<co-author>\n");
		response.append("<name>GeromeMiklau</name>\n");
		response.append("<number>9</number>\n");
		response.append("</co-author>\n");
		response.append("<co-author>\n");
		response.append("<name>IgorTatarinov</name>\n");
		response.append("<number>13</number>\n");
		response.append("</co-author>\n");
		response.append("<co-author>\n");
		response.append("<name>JayantMadhavan</name>\n");
		response.append("<number>65</number>\n");
		response.append("</co-author>\n");
		response.append("<co-author>\n");
		response.append("<name>JenniferWidom</name>\n");
		response.append("<number>35</number>\n");
		response.append("</co-author>\n");
		response.append("<co-author>\n");
		response.append("<name>LukeMcDowell</name>\n");
		response.append("<number>11</number>\n");
		response.append("</co-author>\n");
		response.append("<co-author>\n");
		response.append("<name>MayaRodrig</name>\n");
		response.append("<number>4</number>\n");
		response.append("</co-author>\n");
		response.append("<co-author>\n");
		response.append("<name>MichaelJ.Franklin</name>\n");
		response.append("<number>54</number>\n");
		response.append("</co-author>\n");
		response.append("<co-author>\n");
		response.append("<name>NileshN.Dalvi</name>\n");
		response.append("<number>9</number>\n");
		response.append("</co-author>\n");
		response.append("<co-author>\n");
		response.append("<name>OrenEtzioni</name>\n");
		response.append("<number>11</number>\n");
		response.append("</co-author>\n");
		response.append("<co-author>\n");
		response.append("<name>PeterMork</name>\n");
		response.append("<number>19</number>\n");
		response.append("</co-author>\n");
		response.append("<co-author>\n");
		response.append("<name>PhilipA.Bernstein</name>\n");
		response.append("<number>57</number>\n");
		response.append("</co-author>\n");
		response.append("<co-author>\n");
		response.append("<name>StevenD.Gribble</name>\n");
		response.append("<number>10</number>\n");
		response.append("</co-author>\n");
		response.append("<co-author>\n");
		response.append("<name>XinDong</name>\n");
		response.append("<number>13</number>\n");
		response.append("</co-author>\n");
		response.append("<co-author>\n");
		response.append("<name>YanaKadiyska</name>\n");
		response.append("<number>9</number>\n");
		response.append("</co-author>\n");
		response.append("</co-authors>\n");
		response.append("</author>\n");
		response.append("<author>\n");
		response.append("<name>ZhanCui</name>\n");
		response.append("<number>21</number>\n");
		response.append("<co-authors>\n");
		response.append("<co-author>\n");
		response.append("<name>AlunD.Preece</name>\n");
		response.append("<number>31</number>\n");
		response.append("</co-author>\n");
		response.append("<co-author>\n");
		response.append("<name>AndrewC.Jones</name>\n");
		response.append("<number>34</number>\n");
		response.append("</co-author>\n");
		response.append("<co-author>\n");
		response.append("<name>B.M.Diaz</name>\n");
		response.append("<number>21</number>\n");
		response.append("</co-author>\n");
		response.append("<co-author>\n");
		response.append("<name>DeanM.Jones</name>\n");
		response.append("<number>21</number>\n");
		response.append("</co-author>\n");
		response.append("<co-author>\n");
		response.append("<name>E.W.Lawson</name>\n");
		response.append("<number>21</number>\n");
		response.append("</co-author>\n");
		response.append("<co-author>\n");
		response.append("<name>GrahamJ.L.Kemp</name>\n");
		response.append("<number>22</number>\n");
		response.append("</co-author>\n");
		response.append("<co-author>\n");
		response.append("<name>JianhuaShao</name>\n");
		response.append("<number>26</number>\n");
		response.append("</co-author>\n");
		response.append("<co-author>\n");
		response.append("<name>K.Lunn</name>\n");
		response.append("<number>21</number>\n");
		response.append("</co-author>\n");
		response.append("<co-author>\n");
		response.append("<name>Kit-yingHui</name>\n");
		response.append("<number>21</number>\n");
		response.append("</co-author>\n");
		response.append("<co-author>\n");
		response.append("<name>M.Ashwell</name>\n");
		response.append("<number>21</number>\n");
		response.append("</co-author>\n");
		response.append("<co-author>\n");
		response.append("<name>M.Wiegand</name>\n");
		response.append("<number>21</number>\n");
		response.append("</co-author>\n");
		response.append("<co-author>\n");
		response.append("<name>MartinD.Beer</name>\n");
		response.append("<number>21</number>\n");
		response.append("</co-author>\n");
		response.append("<co-author>\n");
		response.append("<name>MichaelJ.R.Shave</name>\n");
		response.append("<number>21</number>\n");
		response.append("</co-author>\n");
		response.append("<co-author>\n");
		response.append("<name>N.J.Fiddian</name>\n");
		response.append("<number>21</number>\n");
		response.append("</co-author>\n");
		response.append("<co-author>\n");
		response.append("<name>NaderAzarmi</name>\n");
		response.append("<number>21</number>\n");
		response.append("</co-author>\n");
		response.append("<co-author>\n");
		response.append("<name>PepijnR.S.Visser</name>\n");
		response.append("<number>21</number>\n");
		response.append("</co-author>\n");
		response.append("<co-author>\n");
		response.append("<name>PeterM.D.Gray</name>\n");
		response.append("<number>30</number>\n");
		response.append("</co-author>\n");
		response.append("<co-author>\n");
		response.append("<name>PhilippeMarti</name>\n");
		response.append("<number>21</number>\n");
		response.append("</co-author>\n");
		response.append("<co-author>\n");
		response.append("<name>SuzanneM.Embury</name>\n");
		response.append("<number>88</number>\n");
		response.append("</co-author>\n");
		response.append("<co-author>\n");
		response.append("<name>TrevorJ.M.Bench-Capon</name>\n");
		response.append("<number>21</number>\n");
		response.append("</co-author>\n");
		response.append("<co-author>\n");
		response.append("<name>W.A.Gray</name>\n");
		response.append("<number>29</number>\n");
		response.append("</co-author>\n");
		response.append("</co-authors>\n");
		response.append("</author>\n");
		response.append("<author>\n");
		response.append("<name>ZhuoanJiao</name>\n");
		response.append("<number>15</number>\n");
		response.append("<co-authors>\n");
		response.append("<co-author>\n");
		response.append("<name>AndyJ.Keane</name>\n");
		response.append("<number>19</number>\n");
		response.append("</co-author>\n");
		response.append("<co-author>\n");
		response.append("<name>BarryTao</name>\n");
		response.append("<number>15</number>\n");
		response.append("</co-author>\n");
		response.append("<co-author>\n");
		response.append("<name>CaroleA.Goble</name>\n");
		response.append("<number>405</number>\n");
		response.append("</co-author>\n");
		response.append("<co-author>\n");
		response.append("<name>GangXue</name>\n");
		response.append("<number>15</number>\n");
		response.append("</co-author>\n");
		response.append("<co-author>\n");
		response.append("<name>GraemeE.Pound</name>\n");
		response.append("<number>15</number>\n");
		response.append("</co-author>\n");
		response.append("<co-author>\n");
		response.append("<name>JasminL.Wason</name>\n");
		response.append("<number>15</number>\n");
		response.append("</co-author>\n");
		response.append("<co-author>\n");
		response.append("<name>LimingChen</name>\n");
		response.append("<number>20</number>\n");
		response.append("</co-author>\n");
		response.append("<co-author>\n");
		response.append("<name>M.HakkiEres</name>\n");
		response.append("<number>15</number>\n");
		response.append("</co-author>\n");
		response.append("<co-author>\n");
		response.append("<name>MarkScott</name>\n");
		response.append("<number>15</number>\n");
		response.append("</co-author>\n");
		response.append("<co-author>\n");
		response.append("<name>MattJ.Fairman</name>\n");
		response.append("<number>15</number>\n");
		response.append("</co-author>\n");
		response.append("<co-author>\n");
		response.append("<name>MichaelGiles</name>\n");
		response.append("<number>15</number>\n");
		response.append("</co-author>\n");
		response.append("<co-author>\n");
		response.append("<name>MihaiDuta</name>\n");
		response.append("<number>15</number>\n");
		response.append("</co-author>\n");
		response.append("<co-author>\n");
		response.append("<name>NigelShadbolt</name>\n");
		response.append("<number>15</number>\n");
		response.append("</co-author>\n");
		response.append("<co-author>\n");
		response.append("<name>RichardP.Boardman</name>\n");
		response.append("<number>15</number>\n");
		response.append("</co-author>\n");
		response.append("<co-author>\n");
		response.append("<name>SimonJ.Cox</name>\n");
		response.append("<number>20</number>\n");
		response.append("</co-author>\n");
		response.append("</co-authors>\n");
		response.append("</author>\n");
		response.append("<author>\n");
		response.append("<name>ZoLacroix</name>\n");
		response.append("<number>5</number>\n");
		response.append("<co-authors>\n");
		response.append("<co-author>\n");
		response.append("<name>BertramLudscher</name>\n");
		response.append("<number>58</number>\n");
		response.append("</co-author>\n");
		response.append("<co-author>\n");
		response.append("<name>CaroleA.Goble</name>\n");
		response.append("<number>405</number>\n");
		response.append("</co-author>\n");
		response.append("<co-author>\n");
		response.append("<name>OmarBoucelma</name>\n");
		response.append("<number>5</number>\n");
		response.append("</co-author>\n");
		response.append("<co-author>\n");
		response.append("<name>SilvanaCastano</name>\n");
		response.append("<number>5</number>\n");
		response.append("</co-author>\n");
		response.append("<co-author>\n");
		response.append("<name>VanjaJosifovski</name>\n");
		response.append("<number>5</number>\n");
		response.append("</co-author>\n");
		response.append("</co-authors>\n");
		response.append("</author>\n");
		response.append("<author>\n");
		response.append("<name>ZoubidaKedad</name>\n");
		response.append("<number>12</number>\n");
		response.append("<co-authors>\n");
		response.append("<co-author>\n");
		response.append("<name>BatriceFinance</name>\n");
		response.append("<number>12</number>\n");
		response.append("</co-author>\n");
		response.append("<co-author>\n");
		response.append("<name>ChristineCollet</name>\n");
		response.append("<number>12</number>\n");
		response.append("</co-author>\n");
		response.append("<co-author>\n");
		response.append("<name>ChristopheBobineau</name>\n");
		response.append("<number>12</number>\n");
		response.append("</co-author>\n");
		response.append("<co-author>\n");
		response.append("<name>DavidLaurent</name>\n");
		response.append("<number>12</number>\n");
		response.append("</co-author>\n");
		response.append("<co-author>\n");
		response.append("<name>FabriceJouanot</name>\n");
		response.append("<number>12</number>\n");
		response.append("</co-author>\n");
		response.append("<co-author>\n");
		response.append("<name>FarizaTahi</name>\n");
		response.append("<number>12</number>\n");
		response.append("</co-author>\n");
		response.append("<co-author>\n");
		response.append("<name>GennaroBruno</name>\n");
		response.append("<number>12</number>\n");
		response.append("</co-author>\n");
		response.append("<co-author>\n");
		response.append("<name>GenovevaVargas-Solar</name>\n");
		response.append("<number>14</number>\n");
		response.append("</co-author>\n");
		response.append("<co-author>\n");
		response.append("<name>GillesBernot</name>\n");
		response.append("<number>12</number>\n");
		response.append("</co-author>\n");
		response.append("<co-author>\n");
		response.append("<name>KhalidBelhajjame</name>\n");
		response.append("<number>71</number>\n");
		response.append("</co-author>\n");
		response.append("<co-author>\n");
		response.append("<name>Tuyet-TrinhVu</name>\n");
		response.append("<number>12</number>\n");
		response.append("</co-author>\n");
		response.append("<co-author>\n");
		response.append("<name>XiaohuiXue</name>\n");
		response.append("<number>12</number>\n");
		response.append("</co-author>\n");
		response.append("</co-authors>\n");
		response.append("</author>\n");
		response.append("</response>\n");
		String cleanResponse = response.toString().trim();
		cleanResponse = cleanResponse.replace(" ", "");
		String cleanActualResponse = util
				.getCoauthorResponseByPubTypeByYearFilter(letter, null, null,
						null).trim();
		cleanActualResponse = cleanActualResponse.replace(" ", "");
		assertEquals(cleanResponse, cleanActualResponse);
	}

	@Test
	public void testGetAuthorNameInitialLettersByBookIn2012() throws Exception {
		String filepath = this.getClass()
				.getResource("dblp_curated_sample.xml").toString();
		XQueryUtil util = new XQueryUtil(filepath);
		// parameter
		String year = "2012";
		// expected value
		StringBuffer response = new StringBuffer();
		// TODO change expected response
		response.append("<response>\n");
		response.append("<initialLetter>A</initialLetter>\n");
		response.append("<initialLetter>Z</initialLetter>\n");
		response.append("</response>\n");
		String cleanResponse = response.toString().trim();
		cleanResponse = cleanResponse.replace(" ", "");
		String cleanActualResponse = util
				.getAuthorNameInitialLettersByPubtypeOrByYear(
						PublicationType.book, year, null).trim();
		cleanActualResponse = cleanActualResponse.replace(" ", "");
		assertEquals(cleanResponse, cleanActualResponse);
	}

	@Test
	public void testGetAuthorNameInitialLettersInAllYears() throws Exception {
		String filepath = this.getClass()
				.getResource("dblp_curated_sample.xml").toString();
		XQueryUtil util = new XQueryUtil(filepath);
		// expected value
		StringBuffer response = new StringBuffer();
		// TODO change expected response
		response.append("<response>\n");
		response.append("<initialLetter>A</initialLetter>\n");
		response.append("<initialLetter>B</initialLetter>\n");
		response.append("<initialLetter>C</initialLetter>\n");
		response.append("<initialLetter>D</initialLetter>\n");
		response.append("<initialLetter>E</initialLetter>\n");
		response.append("<initialLetter>F</initialLetter>\n");
		response.append("<initialLetter>G</initialLetter>\n");
		response.append("<initialLetter>H</initialLetter>\n");
		response.append("<initialLetter>I</initialLetter>\n");
		response.append("<initialLetter>J</initialLetter>\n");
		response.append("<initialLetter>K</initialLetter>\n");
		response.append("<initialLetter>L</initialLetter>\n");
		response.append("<initialLetter>M</initialLetter>\n");
		response.append("<initialLetter>N</initialLetter>\n");
		response.append("<initialLetter>O</initialLetter>\n");
		response.append("<initialLetter>P</initialLetter>\n");
		response.append("<initialLetter>Q</initialLetter>\n");
		response.append("<initialLetter>R</initialLetter>\n");
		response.append("<initialLetter>S</initialLetter>\n");
		response.append("<initialLetter>T</initialLetter>\n");
		response.append("<initialLetter>U</initialLetter>\n");
		response.append("<initialLetter>V</initialLetter>\n");
		response.append("<initialLetter>W</initialLetter>\n");
		response.append("<initialLetter>X</initialLetter>\n");
		response.append("<initialLetter>Y</initialLetter>\n");
		response.append("<initialLetter>Z</initialLetter>\n");
		response.append("</response>\n");
		String cleanResponse = response.toString().trim();
		cleanResponse = cleanResponse.replace(" ", "");
		String cleanActualResponse = util
				.getAuthorNameInitialLettersByPubtypeOrByYear(null, null, null)
				.trim();
		cleanActualResponse = cleanActualResponse.replace(" ", "");
		assertEquals(cleanResponse, cleanActualResponse);
	}

	@Test
	public void testGetAuthorNameInitialLettersOverallIn2012() throws Exception {
		String filepath = this.getClass()
				.getResource("dblp_curated_sample.xml").toString();
		XQueryUtil util = new XQueryUtil(filepath);
		// parameter
		String year = "2012";
		// expected value
		StringBuffer response = new StringBuffer();
		// TODO change expected response
		response.append("<response>\n");
		response.append("<initialLetter>A</initialLetter>\n");
		response.append("<initialLetter>B</initialLetter>\n");
		response.append("<initialLetter>C</initialLetter>\n");
		response.append("<initialLetter>D</initialLetter>\n");
		response.append("<initialLetter>E</initialLetter>\n");
		response.append("<initialLetter>F</initialLetter>\n");
		response.append("<initialLetter>G</initialLetter>\n");
		response.append("<initialLetter>H</initialLetter>\n");
		response.append("<initialLetter>I</initialLetter>\n");
		response.append("<initialLetter>J</initialLetter>\n");
		response.append("<initialLetter>K</initialLetter>\n");
		response.append("<initialLetter>L</initialLetter>\n");
		response.append("<initialLetter>M</initialLetter>\n");
		response.append("<initialLetter>N</initialLetter>\n");
		response.append("<initialLetter>O</initialLetter>\n");
		response.append("<initialLetter>P</initialLetter>\n");
		response.append("<initialLetter>Q</initialLetter>\n");
		response.append("<initialLetter>R</initialLetter>\n");
		response.append("<initialLetter>S</initialLetter>\n");
		response.append("<initialLetter>T</initialLetter>\n");
		response.append("<initialLetter>U</initialLetter>\n");
		response.append("<initialLetter>W</initialLetter>\n");
		response.append("<initialLetter>Y</initialLetter>\n");
		response.append("<initialLetter>Z</initialLetter>\n");
		response.append("</response>\n");
		String cleanResponse = response.toString().trim();
		cleanResponse = cleanResponse.replace(" ", "");
		String cleanActualResponse = util
				.getAuthorNameInitialLettersByPubtypeOrByYear(null, year, null)
				.trim();
		cleanActualResponse = cleanActualResponse.replace(" ", "");
		assertEquals(cleanResponse, cleanActualResponse);
	}

	@Test
	public void testGetAuthorNameInitialLettersByBookIn2014_NoPublication()
			throws Exception {
		String filepath = this.getClass()
				.getResource("dblp_curated_sample.xml").toString();
		XQueryUtil util = new XQueryUtil(filepath);
		// parameter
		String year = "2014";
		// expected value
		StringBuffer response = new StringBuffer();
		// TODO change expected response
		response.append("<response/>\n");
		String cleanResponse = response.toString().trim();
		cleanResponse = cleanResponse.replace(" ", "");
		String cleanActualResponse = util
				.getAuthorNameInitialLettersByPubtypeOrByYear(
						PublicationType.book, year, null).trim();
		cleanActualResponse = cleanActualResponse.replace(" ", "");
		assertEquals(cleanResponse, cleanActualResponse);
	}

	@Test
	public void testGetCoauthorsListByAuthorNameStefano_Ceri() throws Exception {
		// Stefano Ceri
		String filepath = this.getClass().getResource("test_data_json.xml")
				.toString();
		XQueryUtil util = new XQueryUtil(filepath);
		// parameter
		String authorName = "Stefano Ceri";
		ArrayList<String> expected = new ArrayList<String>();
		expected.add("Piero Fraternali");
		expected.add("Carlo Batini");
		expected.add("Shamkant B. Navathe");
		expected.add("Raghu Ramakrishnan");
		expected.add("Stefano Paraboschi");
		expected.add("Letizia Tanca");
		assertEquals(expected, util.getCoauthorsListByAuthorName(authorName));
	}

	@Test
	public void testGetYearsRange() throws Exception {
		String filepath = this.getClass()
				.getResource("dblp_curated_sample.xml").toString();
		XQueryUtil util = new XQueryUtil(filepath);
		List<Integer> years = new ArrayList<Integer>();
		years.add(1980);
		years.add(1981);
		years.add(1982);
		years.add(1983);
		years.add(1984);
		years.add(1985);
		years.add(1986);
		years.add(1988);
		years.add(1989);
		years.add(1990);
		years.add(1991);
		years.add(1992);
		years.add(1993);
		years.add(1994);
		years.add(1995);
		years.add(1996);
		years.add(1997);
		years.add(1998);
		years.add(1999);
		years.add(2000);
		years.add(2001);
		years.add(2002);
		years.add(2003);
		years.add(2004);
		years.add(2005);
		years.add(2006);
		years.add(2007);
		years.add(2008);
		years.add(2009);
		years.add(2010);
		years.add(2011);
		years.add(2012);
		years.add(2013);
		assertEquals(years, util.getYearsRange());
	}
}