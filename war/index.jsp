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
     <link type="text/css" href="css/pepper-grinder/jquery-ui-1.8.9.custom.css" rel="Stylesheet" />	
	 <script type="text/javascript" src="js/jquery/jquery-1.4.4.js"></script>          
	<script type="text/javascript" src="js/jquery-ui-1.8.9.custom.min.js"></script>
     <script type="text/javascript">                                      
     $(document).ready(function(){
    	 	// Tabs
    	 	$('#tabs').tabs();
    	 	
    	 	// upvotes
    	 	$(".upvote").click(function(e) {
	    	 	var targetId = e.target.id
	    	 	targetId = targetId.replace("upvote-", "votes-")
	    	 	var oldval = Number($("." + targetId).html());
    	 		$("." + targetId).html(oldval + 1);
    	 		
    	 	});
       });

     
     
     </script>                                                               
		<style type="text/css">
			/*demo page css*/
			body{ font: 62.5% "Trebuchet MS", sans-serif; margin: 50px;}
			.demoHeaders { margin-top: 2em; }
			#dialog_link {padding: .4em 1em .4em 20px;text-decoration: none;position: relative;}
			#dialog_link span.ui-icon {margin: 0 5px 0 0;position: absolute;left: .2em;top: 50%;margin-top: -8px;}
			ul#icons {margin: 0; padding: 0;}
			ul#icons li {margin: 2px; position: relative; padding: 4px 0; cursor: pointer; float: left;  list-style: none;}
			ul#icons span.ui-icon {float: left; margin: 0 4px;}
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


		<h2 class="demoHeaders">Submissions for next week</h2>
		<div id="tabs" style="height: 270px">
			<ul>
				<li><a href="#tabs-1">By votes</a></li>
				<li><a href="#tabs-2">Random</a></li>
			</ul>
			<div id="tabs-1">
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
				out.print("<div style=\"float:left\"> <span id=\"upvote-" + w.id + "\" class=\"upvote upvote-" + w.id + " ui-icon ui-icon-triangle-1-n\"></span><br />" +
						  "<span class=\"votes-" + w.id + "\">" + w.votes + "</span><br />votes</div>" +
				 		  "<div style=\"float:left\">" +
				          "<a href=\"wallpaper/"+w.id+"\">"+
						  "<img src=\"image/"+w.id+"_thumbnail.jpg\">"+
						  "</a></div>");
			}
		%>
			</div>
			<div id="tabs-2">
				<%
		// Get 5 random Wallpapers
		for (int i=0; i<5; i++) {
			// get Wallpaper with a "random field" close to some random number
			Wallpaper w = dao.ofy().query(Wallpaper.class)
				.filter("random <", Math.random())
				.limit(1).get();
			if (w == null) {
				// random number too small get largest one instead
				w = dao.ofy().query(Wallpaper.class)
					.order("-random")
					.limit(1)
					.get();
			}
			if (w == null) {
				// No wallpapers
				break;
			}
			out.print("<div style=\"float:left\"> <span id=\"upvote-" + w.id + "\" class=\"upvote upvote-" + w.id + " ui-icon ui-icon-triangle-1-n\"></span><br />" +
					  "<span class=\"votes-" + w.id + "\">" + w.votes + "</span><br />votes</div>" +
			 		  "<div style=\"float:left\">" +
			          "<a href=\"wallpaper/"+w.id+"\">"+
					  "<img src=\"image/"+w.id+"_thumbnail.jpg\">"+
					  "</a></div>");
		}
	%>
			
		</div>
		</div>

<p>Wallpaper receiving the most votes will appear on your desktop next Monday if you have the
<a href="javascript:void(0)">software</a> installed
</p>

<a href="/submit">Submit a wallpaper</a><br>
<a href="javascript:void(0)">Install Wallpaper of the Week App</a> (Work in progress)
</body>
</html>