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
import com.wallpaperoftheweek.model.Wallpaper;


@SuppressWarnings("serial")
public class Image extends HttpServlet {
	private BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
	
	private static Pattern imagePattern = Pattern.compile("^/image/(\\d+).jpg$");
	private static Pattern previewPattern = Pattern.compile("^/image/(\\d+)_preview.jpg$");
	private static Pattern thumbnailPattern = Pattern.compile("^/image/(\\d+)_thumbnail.jpg$");
	private static Pattern weekPreviewPattern = Pattern.compile("^/image/weekwallpaper/(\\d{8})_preview\\.jpg$");

	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		String requestURI = req.getRequestURI();

		DAO dao = new DAO();
		
		// Serve image
		try {
			Matcher m = imagePattern.matcher(requestURI);
			if (m.find()) {
				long id = Long.parseLong(m.group(1));
				Wallpaper wallpaper = dao.ofy().get(new Key<Wallpaper>(Wallpaper.class, id));
				blobstoreService.serve(wallpaper.blobKey, resp);
				return;
			}
		} catch (NotFoundException e) {
			// Empty catch. Error reported bellow
		}

		// Serve preview image
		{
			Matcher m = previewPattern.matcher(requestURI);
			if (m.find()) {
				long id = Long.parseLong(m.group(1));
				ScaledDown scaledDown = dao.ofy().query(ScaledDown.class)
						.filter("size =", ScaledDown.Size.SIZE_PREVIEW)
						.filter("wallpaperId =", id)
						.get();
				if (scaledDown != null) {
					resp.setContentType("image/jpeg");
					resp.getOutputStream().write(scaledDown.data);
					return;
				}
			}
		}
		
		// Serve thumbnail
		{
			Matcher m = thumbnailPattern.matcher(requestURI);
			if (m.find()) {
				long id = Long.parseLong(m.group(1));
				ScaledDown scaledDown = dao.ofy().query(ScaledDown.class)
						.filter("size =", ScaledDown.Size.SIZE_THUMB)
						.filter("wallpaperId =", id)
						.get();
				if (scaledDown != null) {
					resp.setContentType("image/jpeg");
					resp.getOutputStream().write(scaledDown.data);
					return;
				}
			}
		}
		
		{
			Matcher m = weekPreviewPattern.matcher(requestURI);
			if (m.find()) {
				BlobKey blobKey = new BlobKey(m.group(1));
				blobstoreService.serve(blobKey, resp);
				return;
			}
		}
		
		resp.setStatus(404);
		resp.setContentType("text/plain");
		resp.getWriter().println("Image not found.");
	}
}
