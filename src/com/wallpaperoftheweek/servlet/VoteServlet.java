package com.wallpaperoftheweek.servlet;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.*;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.NotFoundException;
import com.wallpaperoftheweek.DAO;
import com.wallpaperoftheweek.model.ScaledDown;
import com.wallpaperoftheweek.model.Vote;
import com.wallpaperoftheweek.model.Wallpaper;
import com.wallpaperoftheweek.model.Vote.VoteType;


@SuppressWarnings("serial")
public class VoteServlet extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		String requestURI = req.getRequestURI();

		DAO dao = new DAO();
		
		Vote.VoteType voteType = null;
		
		if (requestURI == "/vote/up") {
			voteType = VoteType.VOTE_UP;
		} else if (requestURI == "/vote/down") {
			voteType = VoteType.VOTE_DOWN;
		}
		
		if (voteType == VoteType.VOTE_UP) { 
		} else {
			resp.setStatus(404);
			resp.setContentType("text/plain");
			resp.getWriter().println("Image not found.");
		}
	}
}
