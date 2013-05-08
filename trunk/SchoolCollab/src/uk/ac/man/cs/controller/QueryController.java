package uk.ac.man.cs.controller;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.URL;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.w3c.dom.Document;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import uk.ac.man.cs.data.CreateJson;
import uk.ac.man.cs.data.DataAdapter;

/**
 * Servlet to control all the queries request
 * 
 * @author memotoro
 */
@WebServlet("/QueryController")
public class QueryController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public static final String DTD_DBLP = "http://dblp.uni-trier.de/xml/dblp.dtd";

	/**
	 * Get method
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		try {
			// DataAdapter adapter = new DataAdapter();
			DataAdapter adapter = new DataAdapter(getLatestFilePath());
			// Query Parameter
			String query = (String) request.getParameter("query");
			// Validate each type of query requested
			// Types Summary
			if (query.equals("getTypesSummary")) {
				this.prepareResponse(response, adapter.getTypesSummary());
			}
			// Publication By Type
			else if (query.equals("getPublicationByType")) {
				// Publication Type
				String publicationType = (String) request
						.getParameter("publicationType");
				this.prepareResponse(response,
						adapter.getPublicationsByType(publicationType));
			}
			// Authors Summary
			else if (query.equals("getAuthorsSummary")) {
				String firstLetter = (String) request
						.getParameter("firstLetter");
				this.prepareResponse(response,
						adapter.getAuthorsSummary(firstLetter));
			}
			// Publications By Author And Type
			else if (query.equals("getPublicationByAuthorByType")) {
				// Parameters author name and publication type
				String authorName = (String) request.getParameter("authorName");
				String publicationType = (String) request
						.getParameter("publicationType");
				this.prepareResponse(response, adapter
						.getPublicationsByAuthorByType(authorName,
								publicationType));
			}
			// Summary year
			else if (query.equals("getYearSummary")) {
				this.prepareResponse(response, adapter.getYearSummary());
			}
			// Publications by Year and Types
			else if (query.equals("getPublicationsByYearByType")) {
				// Parameters year and publication
				String year = (String) request.getParameter("year");
				String publicationType = (String) request
						.getParameter("publicationType");
				this.prepareResponse(response, adapter
						.getPublicationsByYearByType(year, publicationType));
			}
			// Statistics operations
			// Average of Publications and Authors
			else if (query.equals("getStatsPublicationsAuthors")) {
				// Call the method
				this.prepareResponse(response,
						adapter.getStatsPublicationsAuthors());
			}
			// Average of Publications and Authors
			else if (query.equals("getStatsByYears")) {
				// Call the method
				this.prepareResponse(response, adapter.getStatsByYears());
			}
			// Collaboration's request
			else if (query
					.equals("getAuthorNameInitialLettersByPubtypeOrByYear")) {
				// Parameters
				String yearMin = (String) request.getParameter("yearMin");
				String yearMax = (String) request.getParameter("yearMax");
				String publicationType = (String) request
						.getParameter("publicationType");
				// Call the adapter
				this.prepareResponse(response, adapter
						.getAuthorNameInitialLettersByPubtypeOrByYear(
								publicationType, yearMin, yearMax));
			} else if (query.equals("getCoauthorResponseByPubTypeByYearFilter")) {
				// Parameters
				String firstLetter = (String) request
						.getParameter("firstLetter");
				String yearMin = (String) request.getParameter("yearMin");
				String yearMax = (String) request.getParameter("yearMax");
				String publicationType = (String) request
						.getParameter("publicationType");
				// Call the adapter
				this.prepareResponse(response, adapter
						.getCoauthorResponseByPubTypeByYearFilter(firstLetter,
								publicationType, yearMin, yearMax));
			}
			// Get the Response for Years Range
			else if (query.equals("getResponseYearsRange")) {
				this.prepareResponse(response, adapter.getResponseYearsRange());
			}
			// Get the file of the name used
			else if (query.equals("getFileNameUsed")) {
				StringBuffer fileName = new StringBuffer();
				fileName.append("<response>");
				fileName.append("<filename>");
				String path = this.getLatestFilePath();
				fileName.append(path.substring(path.lastIndexOf(File.separator) + 1));
				fileName.append("</filename>");
				fileName.append("</response>");
				this.prepareResponse(response, fileName.toString());
			}
			// Get the JSon object by author name
			else if (query.equals("getCollaborationByAuthorNameJson")) {
				// Parameters
				String authorName = (String) request.getParameter("authorName");
				// Call the json converter
				this.prepareJsonResponse(response, CreateJson.getJsonOfAuthor(
						authorName, getLatestFilePath()));
			}
			// Default branch
			else {
				throw new Exception("invalid operation");
			}
		} catch (Exception ex) {
			String error = "<response><error>" + ex.getMessage()
					+ "</error></response>";
			prepareResponse(response, error);
			ex.printStackTrace();
		}
	}

	/**
	 * Post method
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		// Create buffer response
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append("<response>");
		boolean isMultipartContent = ServletFileUpload
				.isMultipartContent(request);
		// Validate multi-part content
		if (!isMultipartContent) {
			stringBuffer.append("<error>");
			stringBuffer.append("No Multipart Content");
			stringBuffer.append("</error>");
			stringBuffer.append("</response>");
			prepareResponse(response, stringBuffer.toString());
			return;
		}
		// Create file item factory
		FileItemFactory factory = new DiskFileItemFactory();
		ServletFileUpload upload = new ServletFileUpload(factory);
		try {
			// Get the list of the files uploaded
			List<FileItem> fields = upload.parseRequest(request);
			Iterator<FileItem> it = fields.iterator();
			// If there is no file
			if (!it.hasNext()) {
				stringBuffer.append("<error>");
				stringBuffer.append("No Files Found");
				stringBuffer.append("</error>");
				stringBuffer.append("</response>");
				prepareResponse(response, stringBuffer.toString());
				return;
			}
			// Iterate the files
			if (it.hasNext()) {
				FileItem fileItem = it.next();
				String fileName = fileItem.getName();
				Long maxSize = 100000000L;
				// File extension
				if (!fileName.endsWith(".xml")) {
					stringBuffer.append("<error>");
					stringBuffer.append("Is not an XML file");
					stringBuffer.append("</error>");
					stringBuffer.append("</response>");
					prepareResponse(response, stringBuffer.toString());
					return;
				}
				// File size
				else if (fileItem.getSize() > maxSize) {
					stringBuffer.append("<error>");
					stringBuffer.append("The file is bigger than " + maxSize
							/ 1000000 + " MB. ");
					stringBuffer.append("</error>");
					stringBuffer.append("</response>");
					prepareResponse(response, stringBuffer.toString());
					return;
				} else {
					// Create the document
					Document validatedDoc = validationProcess(fileItem);
					// Validation process
					if (validatedDoc == null) {
						stringBuffer.append("<error>");
						stringBuffer.append("The XML file is not valid.");
						stringBuffer.append("</error>");
						stringBuffer.append("</response>");
						prepareResponse(response, stringBuffer.toString());
						return;
					}
					// Flush the file
					String xml_file_name = getXMLFolderPath()
							+ fileItem.getName();
					File file = new File(xml_file_name);
					// fileItem.write(file);
					TransformerFactory transformerFactory = TransformerFactory
							.newInstance();
					Transformer transformer = transformerFactory
							.newTransformer();
					DOMSource source = new DOMSource(validatedDoc);
					StreamResult result = new StreamResult(file);
					transformer.transform(source, result);
					// / Prepare the file upload
					stringBuffer.append("<upload>");
					stringBuffer.append("Upload Successful");
					stringBuffer.append("</upload>");
					stringBuffer.append("</response>");
					prepareResponse(response, stringBuffer.toString());
				}
			}
		} catch (Exception ex) {
			String error = "<response><error>" + ex.getMessage()
					+ "</error></response>";
			prepareResponse(response, error);
			ex.printStackTrace();
		}
	}

	/**
	 * Method that prepares the response of the servlet
	 * 
	 * @param response
	 *            HttpServletReponse to dispatch
	 * @param queryResult
	 *            String with the XML response
	 * @throws ServletException
	 * @throws IOException
	 */
	private void prepareResponse(HttpServletResponse response,
			String queryResult) throws IOException {
		// Buffer to write
		BufferedOutputStream bs = null;
		// Buffer with the response
		response.setContentType("text/xml");
		response.setCharacterEncoding("UTF-8");
		try {
			bs = new BufferedOutputStream(response.getOutputStream());
			bs.write(queryResult.getBytes());
		} finally {
			// Close the resources
			bs.flush();
			bs.close();
		}
	}

	/**
	 * Method to prepare response Json object
	 * 
	 * @param response
	 * @param queryResult
	 * @throws IOException
	 */
	private void prepareJsonResponse(HttpServletResponse response,
			String queryResult) throws IOException {
		// Buffer to write
		BufferedOutputStream bs = null;
		// Buffer with the response
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		try {
			bs = new BufferedOutputStream(response.getOutputStream());
			bs.write(queryResult.getBytes());
		} finally {
			// Close the resources
			bs.flush();
			bs.close();
		}
	}

	/**
	 * Get the folder path
	 * 
	 * @return
	 */
	private String getXMLFolderPath() {
		return getServletContext().getRealPath("") + File.separator + "dblpxml"
				+ File.separator;
	}

	/**
	 * Get the latest file uploaded in the folder
	 * 
	 * @return
	 */
	private String getLatestFilePath() {
		// Get path
		String xml_path = getXMLFolderPath();
		// Create file
		File fl = new File(xml_path);
		// Create file filter
		File[] files = fl.listFiles(new FileFilter() {
			public boolean accept(File file) {
				return file.isFile();
			}
		});
		long lastMod = Long.MIN_VALUE;
		File choise = null;
		// Iterate the files listed
		for (File file : files) {
			if (file.lastModified() > lastMod) {
				choise = file;
				lastMod = file.lastModified();
			}
		}
		// Return the last file uploaded path
		return xml_path + choise.getName();
	}

	/**
	 * Method to validate the XML file against the DTD
	 * 
	 * @param file
	 *            FileItem
	 * @return boolean
	 */
	private Document validationProcess(FileItem file) {
		// Document Builder
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder db;
		Document newDoc = null;
		try {
			factory.setValidating(true);
			db = factory.newDocumentBuilder();
			db.setErrorHandler(new org.xml.sax.ErrorHandler() {
				// Ignore the fatal errors
				public void fatalError(SAXParseException exception)
						throws SAXException {
					throw exception;
				}

				// Validation errors
				public void error(SAXParseException e) throws SAXParseException {
					if (!(e.getMessage().contains("DOCTYPE") || e.getMessage()
							.contains("no grammar found"))) {
						System.out.println(e.getMessage());
						throw e;
					}
				}

				// Show warnings
				public void warning(SAXParseException err)
						throws SAXParseException {
					System.out.println(err.getMessage());
				}
			});
			db.setEntityResolver(new EntityResolver() {
				@Override
				public InputSource resolveEntity(String publicId,
						String systemId) throws SAXException, IOException {
					if (!systemId.isEmpty() || !publicId.isEmpty()) {
						InputStream inputStream = new URL(DTD_DBLP)
								.openStream();
						InputSource inputSource = new InputSource(inputStream);
						return inputSource;
					} else {
						return null;
					}
				}
			});
			// Create a document with the input stream
			Document doc = db.parse(file.getInputStream());
			// Dom source
			DOMSource source = new DOMSource(doc);
			TransformerFactory tf = TransformerFactory.newInstance();
			Transformer transformer = tf.newTransformer();
			// Set the DTD file
			transformer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, DTD_DBLP);
			StringWriter writer = new StringWriter();
			StreamResult result = new StreamResult(writer);
			transformer.transform(source, result);
			// Validate against the DTD
			newDoc = db.parse(new InputSource(new StringReader(writer
					.toString())));
		} catch (Exception e) {
			System.out.println(e.getMessage());
			newDoc = null;
		}
		// Return validation
		return newDoc;
	}
}
