import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

@WebServlet("/webhook")
public class Bot extends HttpServlet {

	private static final long serialVersionUID = 8071426090770097330L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		System.out.println("**************Entering Callback Servlet**************************");

		String data = getBody(request);
		String senderId = null;
		String recipientId;
		String messageText;
		System.out.println(data);
		Object obj;
		try {
			JSONParser parser = new JSONParser();
			obj = parser.parse(data);
			JSONObject jsonObject = (JSONObject) obj;

			JSONArray entryArray = (JSONArray) jsonObject.get("entry");
			JSONObject jsonObject_ = (JSONObject) entryArray.get(0);
			JSONArray arr = (JSONArray) jsonObject_.get("messaging");
			JSONObject messObj = (JSONObject) arr.get(0);

			JSONObject senderObj = (JSONObject) messObj.get("sender");
			senderId = (String) senderObj.get("id");
			JSONObject recpObj = (JSONObject) messObj.get("recipient");
			recipientId = (String) recpObj.get("id");
			JSONObject messageObj = (JSONObject) messObj.get("message");
			messageText = (String) messageObj.get("text");

			System.out.println(senderId + " , " + recipientId + " , " + messageText);

			if (messageText != null && !messageText.isEmpty())
				sendRequest(senderId);

		} catch (Exception e) {
			// e.printStackTrace();
		} finally {

		}
		/*
		 * Map<String, String[]> parametersMap = request.getParameterMap(); if
		 * (parametersMap.size() > 0) { if
		 * (request.getParameter("hub.mode").equals("subscribe")) {
		 * System.out.println("Verify Token: " +
		 * request.getParameter("hub.verify_token"));
		 * System.out.println("Challenge number:" +
		 * request.getParameter("hub.challenge")); String responseToClient =
		 * request.getParameter("hub.challenge");
		 * response.setStatus(HttpServletResponse.SC_OK);
		 * response.getWriter().write(responseToClient);
		 * response.getWriter().flush(); response.getWriter().close();
		 * response.getWriter
		 * ().append("Fetch-Mode").append(request.getParameter("hub.mode"));
		 * response
		 * .getWriter().append("App Verify Token:").append(request.getParameter
		 * ("hub.verify_token"));
		 * response.getWriter().append("App Challenge No")
		 * .append(request.getParameter("hub.challenge")); System.out.println(
		 * "**************Callback Successful**************************"); }
		 * 
		 * //
		 * response.getWriter().append("Served at: ").append(request.getContextPath
		 * ()); } else { System.out.println(
		 * "**************Not an Facebook POST**************************"); }
		 * System.out.println(
		 * "**************Exiting Callback Servlet**************************");
		 */

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

	@SuppressWarnings("unchecked")
	public static void sendRequest(String sender) throws IOException {

		String access_token = "EAASc0Lnwq24BAE0AsH8ddKR6ZBLJ4vEa3OZBRYI876Hwv39Hz6ytUWKYXbAuGiYFblHDanEjGZBGAFU0gcfGhssTd6wv1U2Ni4pq8y03DUpJAQQLV7b5dBzhItDus90ZB1EmdlhnL0tUfWuRu4JTuo7aGXcQ1B8QyhkXZBEJN4AZDZD";

		JSONObject requestObj = new JSONObject();
		JSONObject recObj = new JSONObject();
		JSONObject messageObj = new JSONObject();
		JSONObject attachObj = new JSONObject();
		JSONObject payloadObj = new JSONObject();
		JSONArray elementsArr = new JSONArray();
		JSONObject elementObj = new JSONObject();
		JSONArray buttonsArr = new JSONArray();
		JSONObject buttonObj = new JSONObject();

		recObj.put("id", sender);

		
		
		
		
		buttonObj.put("type", "web_url");
		buttonObj.put("url", "http://www.etisalat.eg");
		buttonObj.put("title", "Subscribe");

		buttonsArr.add(buttonObj);

		
		/* convert this to be dynamic*
		 * read from database
		 * for each record, build new object*/
		//for loop
		
		/*buttonObj.put("type", "web_url");
		buttonObj.put("url", "http://www.etisalat.eg");
		buttonObj.put("title", "Subscribe");

		buttonsArr.add(buttonObj);*/
		
		
		
		elementObj.put("title", "Bonus on recharge");
		elementObj.put("subtitle", "Recharge with 10 L.E and get 10x BOR /n Price : 10 L.E");
		elementObj.put("image_url", "http://www.compareegyptnetworks.com/images/proms/et_rechargeIcon.jpg");
		elementObj.put("buttons", buttonsArr);

		elementsArr.add(elementObj);
		
		
		/***************************************/
		
		
		//elementsArr.add(elementObj);

		payloadObj.put("template_type", "generic");
		payloadObj.put("elements", elementsArr);

		attachObj.put("type", "template");
		attachObj.put("payload", payloadObj);

		messageObj.put("attachment", attachObj);

		requestObj.put("recipient", recObj);
		requestObj.put("message", messageObj);

		CloseableHttpClient httpClient = HttpClientBuilder.create().build();

		try {
			HttpPost request = new HttpPost("https://graph.facebook.com/v2.6/me/messages?access_token=" + access_token);
			StringEntity params = new StringEntity(requestObj.toString());
			request.addHeader("content-type", "application/json");
			request.setEntity(params);
			CloseableHttpResponse response = httpClient.execute(request);
			System.out.println(response.toString());
			// handle response here...
		} catch (Exception ex) {
			ex.printStackTrace();
			// handle exception here
		} finally {
			httpClient.close();
		}

	}

	public static String getBody(HttpServletRequest request) throws IOException {

		String body = null;
		StringBuilder stringBuilder = new StringBuilder();
		BufferedReader bufferedReader = null;

		try {
			InputStream inputStream = request.getInputStream();
			if (inputStream != null) {
				bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
				char[] charBuffer = new char[128];
				int bytesRead = -1;
				while ((bytesRead = bufferedReader.read(charBuffer)) > 0) {
					stringBuilder.append(charBuffer, 0, bytesRead);
				}
			} else {
				stringBuilder.append("");
			}
		} catch (IOException ex) {
			throw ex;
		} finally {
			if (bufferedReader != null) {
				try {
					bufferedReader.close();
				} catch (IOException ex) {
					throw ex;
				}
			}
		}

		body = stringBuilder.toString();
		return body;
	}

}