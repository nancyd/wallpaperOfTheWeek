<%@page trimDirectiveWhitespaces="true" %>
<%@page contentType="text/html;charset=UTF-8" language="java" %>
<%@page import="java.util.List"%>
<%@page import="java.util.Collections"%>
<%@page import="java.util.Comparator"%>
<%@page import="org.joda.time.DateMidnight"%>
<%@page import="org.joda.time.format.DateTimeFormatter"%>
<%@page import="org.joda.time.format.DateTimeFormat"%>
<%@page import="com.wallpaperoftheweek.DAO"%>
<%@page import="com.wallpaperoftheweek.model.Wallpaper"%>
<%@page import="com.googlecode.objectify.Key"%>
<% DAO dao = new DAO(); %>
<!DOCTYPE html>
<html>
<head>
	<title>Wallpaper of the week</title>
	<style type="text/css">
		img {border: 2px solid transparent}
	</style>
</head>
<body>
<h1>Wallpaper of the week</h1>
<% 
  // Get first day of week
  String firstDayOfWeek;
  {
  	DateMidnight dm = new DateMidnight().withDayOfWeek(1);
  	DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyyMMdd");
  	firstDayOfWeek = fmt.print(dm);
 }
%>
<img src="weekwallpaper/<%=firstDayOfWeek%>_preview.jpg">
<h2>Submissions for next week:</h2>
<p>Wallpaper receiving the most votes will appear on your desktop next Monday if you have the
<a href="javascript:void(0)">software</a> installed
</p>
<h3>By Votes</h3>
	<ol>
		<%
			// Fetch wallpapers with the most votes
			List<Wallpaper> wallpapersByVotes = dao.ofy().query(Wallpaper.class)
				.order("-votes")
				.limit(5)
				.list();
		
			// Shuffle elements with the same number of votes (i.e shuffle then stable sort)
			Collections.shuffle(wallpapersByVotes);
			Collections.sort(wallpapersByVotes, new Comparator<Wallpaper>() {
				public int compare(Wallpaper w1, Wallpaper w2) {
					if (w1.votes < w2.votes)
						return -1;
					if (w1.votes > w2.votes)
						return 1;
					return 0;
				}
			});
			
			// display wallpapers by votes
			for (Wallpaper w : wallpapersByVotes) {
				out.print("<li><a href=\"wallpaper/"+w.id+"\">"+
						  "<img src=\"image/"+w.id+"_thumbnail.jpg\">"+
						  "</a></li>");
			}
		%>
	</ol>
<h3>Random</h3>
<div class="random">
	<%
		// Get 5 random Wallpapers
		for (int i=0; i<5; i++) {
			// get Wallpaper with a "random field" close to some random number
			Key<Wallpaper> randomKey = dao.ofy().query(Wallpaper.class)
				.filter("random <", Math.random())
				.limit(1)
				.getKey();
			if (randomKey == null) {
				// random number too small get largest one instead
				randomKey = dao.ofy().query(Wallpaper.class)
					.order("-random")
					.limit(1)
					.getKey();
			}
			if (randomKey == null) {
				// No wallpapers
				break;
			}
			out.print("<a href=\"wallpaper/"+randomKey.getId()+"\">"+
					  "<img src=\"image/"+randomKey.getId()+"_thumbnail.jpg\">"+
					  "</a>");
		}
	%>
</div>
<a href="/submit">Submit a wallpaper</a><br>
<a href="javascript:void(0)">Install Wallpaper of the Week App</a> (Work in progress)
</body>
</html>