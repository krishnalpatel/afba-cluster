<%@page import="com.afd.pojo.TfidfTemp"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>TF-IDF</title>
</head>
<body>
	<h1 align="center">TF-IDF LIST</h1>
	<br>
	<%
		Map<String, Object> model = (Map<String,Object>) request.getAttribute("model");
		List<TfidfTemp> tfidfTempList = (List<TfidfTemp>) model.get("tfidfTempList");
	%>
	
	<table border="1" align="center">
		<tr><td>Id</td><td>Word</td><td>TF-IDF</td><td>DocId</td></tr>
		
		<%for(TfidfTemp tfidfTemp : tfidfTempList){ %>
		<tr>
			<td><%=tfidfTemp.getId() %></td>
			<td><%=tfidfTemp.getWord() %></td>
			<td><% out.print(tfidfTemp.getTfidf()); %></td>
			<td><%=tfidfTemp.getDocId() %></td>
		</tr>
		<%} %>
	</table>
	
	<br/>
	<br/>
	<a href="index.jsp">Index</a>	
	
</body>
</html>