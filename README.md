Web application that reads an xml file and present information using JQuery components. The queries are performed using an XQuery third-part library. The server side uses that library to execute specific queries and return results to the view. The server side is developed using a Java Servlet. The servlet is in charge of identifying the query based on the http parameters received, calls a delegate to execute the third-party xquery libraries and prepares the appropriate response to the view. The responses are dispatched in XML and JSON format to the view.

The view uses JQuery components. All the request from the view uses AJAX calls to send the request to the servlet in the server and process the XML or JSON response. Once the response is received, the JavaScript function display the information combining HTML and JQuery components.