<%@page trimDirectiveWhitespaces="true" %>
<%@page contentType="text/html;charset=UTF-8" language="java" %>
<%@page import="com.google.appengine.api.blobstore.BlobstoreServiceFactory"%>
<%@page import="com.google.appengine.api.blobstore.BlobstoreService"%>
<!DOCTYPE html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Submit a wallpaper</title>
</head>
<body>
<h1>Submit a wallpaper</h1>
Submit a wallpaper for next week.

<%BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();%>


<form action="<%= blobstoreService.createUploadUrl("/upload") %>" method="post" enctype="multipart/form-data">
    <input type="file" name="myFile">
    <input type="submit" value="Submit">
</form>
</body>
</html>