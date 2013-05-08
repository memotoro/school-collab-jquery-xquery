package uk.ac.man.cs.data;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Create JSON object utility
 * 
 */
public class CreateJson {

	public static void main(String[] args) throws Exception {
		getJsonOfAuthor("Stefano Ceri", "dblp_curated_sample.xml");
	}

	/**
	 * Method to retrieve the Json object from author name with collaboration
	 * 
	 * @param authorName
	 * @param filePath
	 * @return
	 * @throws Exception
	 */
	public static String getJsonOfAuthor(String authorName, String filePath)
			throws Exception {

		XQueryUtil query = new XQueryUtil(filePath);
		JSONObject jsonObj = new JSONObject();
		try {
			JSONObject data = new JSONObject();
			data.put("band", "1");
			data.put("relation", "M");
			// data.put("data", band);
			// System.out.println(data.toString());
			int id = 0;

			jsonObj.put("name", authorName); // center node.
			jsonObj.put("id", id++);
			JSONArray jsonArrOfAuthors = new JSONArray();
			List<String> coauthorsList = new ArrayList<String>();
			coauthorsList = query.getCoauthorsListByAuthorName(authorName);
			// System.out.println(coauthorsList.toString());
			for (int i = 0; i < coauthorsList.size(); i++) {
				JSONObject jsonObjCoauthor = new JSONObject();
				JSONArray jsonArrOfCoauthors = new JSONArray();
				String coauthor = coauthorsList.get(i);
				if (i >= 30) { // if more than 30 coauthors, only show first 30
								// coauthors and use a number to instead other
								// coauthors
					jsonObjCoauthor.put("name", coauthorsList.size() - i - 1
							+ "more coauthors");
					jsonObjCoauthor.put("id", id++);
					jsonObjCoauthor.put("data", data);
					jsonObjCoauthor.put("children", jsonArrOfCoauthors);
					jsonArrOfAuthors.put(jsonObjCoauthor);
					break;
				} else {
					jsonObjCoauthor.put("name", coauthor);
				}
				jsonObjCoauthor.put("id", id++);
				List<String> cocoauthorsList = new ArrayList<String>();
				cocoauthorsList = query.getCoauthorsListByAuthorName(coauthor);
				cocoauthorsList.remove(authorName);
				if (cocoauthorsList.size() < 20) {// cocoauthors < 20
													// then give
					// 3rd level information
					for (String cocoauthor : cocoauthorsList) {
						JSONObject jsonObjCocoauthor = new JSONObject();
						jsonObjCocoauthor.put("name", cocoauthor);
						jsonObjCocoauthor.put("id", id++);
						JSONArray emptyArray = new JSONArray();
						jsonObjCocoauthor.put("data", data);
						jsonObjCocoauthor.put("children", emptyArray);

						// get a cocoauthor and put him into coauthor's list
						jsonArrOfCoauthors.put(jsonObjCocoauthor);
					}
				} else { // else only give out the number of cocoauthors
					JSONObject jsonObjCocoauthor = new JSONObject();
					jsonObjCocoauthor.put("name", cocoauthorsList.size()
							+ "more coauthors");
					jsonObjCocoauthor.put("id", id++);
					JSONArray emptyArray = new JSONArray();
					jsonObjCocoauthor.put("data", data);
					jsonObjCocoauthor.put("children", emptyArray);

					// get a cocoauthor and put him into coauthor's list
					jsonArrOfCoauthors.put(jsonObjCocoauthor);
				}
				// get a coauthor and put him into author's list
				jsonObjCoauthor.put("data", data);
				jsonObjCoauthor.put("children", jsonArrOfCoauthors);
				jsonArrOfAuthors.put(jsonObjCoauthor);
			}
			jsonObj.put("children", jsonArrOfAuthors);

		} catch (JSONException e) {
			e.printStackTrace();
		}
		return jsonObj.toString();
	}
}