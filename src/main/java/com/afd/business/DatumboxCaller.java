package com.afd.business;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

@Component(value = "datumboxCaller")
@Scope(value = "prototype")
public class DatumboxCaller {

	@Autowired
	private PropertyReader propertyReader;

	public Map<String, Object> extractKeywords(String text) {
		// String text;
		Map<String, Object> map = new HashMap<>();

		try {
			String urlParamters = "api_key=" + propertyReader.readProperty("datumbox.api.key") + "&n="
					+ propertyReader.readProperty("datumbox.keywordextraction.n") + "&text=" + text;
			String request = propertyReader.readProperty("datumbox.keywordextraction.url");
			URL url = new URL(request);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			InputStream inputStream = postURL(connection, url, urlParamters, request);
			String jsonText = IOUtils.toString(inputStream);

			String n = propertyReader.readProperty("datumbox.keywordextraction.n");
			map = parse(jsonText, n);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return map;
	}

	public InputStream postURL(HttpURLConnection connection, URL url, String urlParameters, String request)
			throws IOException {

		connection.setDoOutput(true);
		connection.setDoInput(true);
		connection.setInstanceFollowRedirects(false);
		connection.setRequestMethod("POST");
		connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
		connection.setRequestProperty("charset", "utf-8");
		connection.setRequestProperty("Content-Length", "" + Integer.toString(urlParameters.getBytes().length));
		connection.setUseCaches(false);

		DataOutputStream wr = null;

		try {
			wr = new DataOutputStream(connection.getOutputStream());
			wr.writeBytes(urlParameters);
			wr.flush();
		} finally {
			wr.close();
		}
		InputStream is = connection.getInputStream();
		return is;
	}

	public Map<String, Object> parse(String jsonText, String n) {
		JsonElement jelement = new JsonParser().parse(jsonText);
		JsonObject jobject = jelement.getAsJsonObject();
		jobject = jobject.getAsJsonObject("output");
		// String result = jobject.get("result").toString();
		jobject = jobject.getAsJsonObject("result");

		Map<String, Object> map = new HashMap<>();
		Map<String, Integer> wordMap = new HashMap<>();
		int totalFrequency = 0;
		for (int i = 1; i <= Integer.parseInt(n); i++) {
			JsonObject jobjectN = jelement.getAsJsonObject();
			jobjectN = jobject.getAsJsonObject(Integer.toString(i));
			Set<Entry<String, JsonElement>> entrySet = jobjectN.entrySet();
			Iterator<Entry<String, JsonElement>> iterator = entrySet.iterator();
			for (Entry<String, JsonElement> entry : entrySet) {
				wordMap.put(entry.getKey(), Integer.parseInt(entry.getValue().toString()));
				if (i == 1) {
					totalFrequency += Integer.parseInt(entry.getValue().toString());
				}
			}
		}
		map.put("wordMap", wordMap);
		map.put("totalFrequency", totalFrequency);
		return map;
	}

	public Map<String, Double> compareDocs(String docName1, String docName2) {
		Map<String, Double> map = new HashMap<>();
		try {
			// get docs file
			String fileLoc = propertyReader.readProperty("file.read.location");
			//String fileLoc = "C:/Users/krpatel/Desktop/Clustering data/Abstracts/";
			File file1 = new File(fileLoc + docName1);
			File file2 = new File(fileLoc + docName2);

			// get docs text
			String file1Text = new String(Files.readAllBytes(file1.toPath()), StandardCharsets.UTF_8);
			String file2Text = new String(Files.readAllBytes(file2.toPath()), StandardCharsets.UTF_8);

			// call api
			String urlParamters = "api_key=" + propertyReader.readProperty("datumbox.api.key") + "&original="
					+ file1Text + "&copy=" + file2Text;
			String request = propertyReader.readProperty("datumbox.documentsimilarity.url");
			URL url = new URL(request);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			InputStream inputStream = postURL(connection, url, urlParamters, request);
			String jsonText = IOUtils.toString(inputStream);

			// parse result
			map = parse(jsonText);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// return result
		return map;
	}

	private Map<String, Double> parse(String jsonText) {
		JsonElement jelement = new JsonParser().parse(jsonText);
		JsonObject jobject = jelement.getAsJsonObject();
		jobject = jobject.getAsJsonObject("output");
		// String result = jobject.get("result").toString();
		jobject = jobject.getAsJsonObject("result");
		
		Set<Entry<String, JsonElement>> entrySet = jobject.entrySet();
		Iterator<Entry<String, JsonElement>> iterator = entrySet.iterator();
		Map<String, Double> map = new HashMap<>();
		for (Entry<String, JsonElement> entry : entrySet) {
			map.put(entry.getKey(), entry.getValue().getAsDouble());
		}
		return map;
	}

	public PropertyReader getPropertyReader() {
		return propertyReader;
	}

	public void setPropertyReader(PropertyReader propertyReader) {
		this.propertyReader = propertyReader;
	}

}
