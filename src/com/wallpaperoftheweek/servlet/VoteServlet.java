package com.wallpaperoftheweek.servlet;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.*;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.datastore.Blob;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.wallpaperoftheweek.DAO;
import com.wallpaperoftheweek.MyUtil;
import com.wallpaperoftheweek.model.Vote;
import com.wallpaperoftheweek.model.Wallpaper;


@SuppressWarnings("serial")
public class VoteServlet extends HttpServlet {
	
	private static Pattern votePattern = Pattern.compile("^/vote/(\\d+)$");

	private Wallpaper wallpaperFromUrl(String requestURI, DAO dao) {
		Matcher m = votePattern.matcher(requestURI);
		if (m.find()) {
			long id = Long.parseLong(m.group(1));
			return dao.getWallpaper(id);
		}
		return null;
	}
	
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		String requestURI = req.getRequestURI();

		DAO dao = new DAO();
		UserService userService = UserServiceFactory.getUserService();
		
		User user = userService.getCurrentUser();
		if (user == null) {
//			String loginURL = userService.createLoginURL(req.getRequestURI());
//			resp.sendRedirect(loginURL);
			return;
		}
		
		Wallpaper wallpaper = wallpaperFromUrl(requestURI, dao);
		if (wallpaper == null) {
			resp.sendError(404);
			return;
		}
		
//		if (voteAlredyExists(dao, user, wallpaper)) {
//			resp.sendError(401);
//		}
		
		wallpaper.votes++;
		dao.ofy().put(wallpaper);
		
		Vote vote = new Vote();
		vote.userId = user.getUserId();
		vote.wallpaperId = wallpaper.id;
		dao.ofy().put(vote);
		resp.getWriter().write("OK\n" + wallpaper.id + ":" + wallpaper.votes);
	}

	private boolean voteAlredyExists(DAO dao, User user, Wallpaper wallpaper) {
		Vote vote = dao.ofy().query(Vote.class)
			.filter("userId", user.getUserId())
			.filter("wallpaperId", wallpaper.id)
			.get();
		return vote != null;
	}
}
