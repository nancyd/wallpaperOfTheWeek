<%@page trimDirectiveWhitespaces="true" %>
<%@page contentType="text/html;charset=UTF-8" language="java" %>
<%@page import="java.util.regex.Pattern"%>
<%@page import="java.util.regex.Matcher"%>
<%@page import="com.googlecode.objectify.Key"%>
<%@page import="com.googlecode.objectify.NotFoundException"%>
<%@page import="com.wallpaperoftheweek.DAO"%>
<%@page import="com.wallpaperoftheweek.model.Wallpaper"%>
<%! Pattern idPattern = Pattern.compile("^/wallpaper/(\\d+)$"); %>
<% 
DAO dao = new DAO();
// Ensure that id was supplied and is valid
Wallpaper wallpaper = null;
int count = 0;
{
	Matcher m = idPattern.matcher(request.getRequestURI());
	if (m.find()) {
		long id = Long.parseLong(m.group(1));
		try {
			wallpaper = dao.ofy().get(new Key<Wallpaper>(Wallpaper.class, id));
		} catch (NotFoundException e) {}
	}
}
if (wallpaper == null) {
	response.sendError(404);
	return;
}
%>
<!DOCTYPE html>
<html>
<head>
<title>Wallpaper View</title>
</head>
<body>

<div>
<img src="/image/<%=wallpaper.id%>_preview.jpg">
</div>


<h3><%=wallpaper.votes%> Votes</h3>
<p>
<a href="/vote/up">up-vote</a><br>
<a href="/vote/down">down-vote</a>
</p>

</body>
</html>