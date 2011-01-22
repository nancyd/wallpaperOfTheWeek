package com.wallpaperoftheweek.servlet;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.*;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.images.Image;
import com.google.appengine.api.images.ImagesService;
import com.google.appengine.api.images.ImagesServiceFactory;
import com.google.appengine.api.images.Transform;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.wallpaperoftheweek.DAO;
import com.wallpaperoftheweek.model.ScaledDown;
import com.wallpaperoftheweek.model.Wallpaper;
import com.wallpaperoftheweek.model.ScaledDown.Size;

@SuppressWarnings("serial")
public class Upload extends HttpServlet {

	private BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();

	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
        Map<String, BlobKey> blobs = blobstoreService.getUploadedBlobs(req);
        BlobKey blobKey = blobs.get("myFile");

        if (blobKey == null) {
        	resp.sendRedirect("/");
        	return;
        }
        
        DAO dao = new DAO();
        
        UserService userService = UserServiceFactory.getUserService();

        
        // Store image
        Wallpaper wallpaper = new Wallpaper();
        wallpaper.blobKey = blobKey;
        wallpaper.saltedUserId = Util.saltedHash(userService.getCurrentUser().getUserId());
        dao.ofy().put(wallpaper);
        
        // Build scaled down versions
        ImagesService imagesService = ImagesServiceFactory.getImagesService();
        Image image = ImagesServiceFactory.makeImageFromBlob(blobKey);
 
        buildScaledDown(dao, wallpaper, imagesService, image, ScaledDown.Size.SIZE_PREVIEW);
        buildScaledDown(dao, wallpaper, imagesService, image, ScaledDown.Size.SIZE_THUMB);
	}

	private void buildScaledDown(DAO dao, Wallpaper wallpaper, ImagesService imagesService, Image image, Size size) {
		int width, height;
		if (size == Size.SIZE_PREVIEW) {
			width=500; height=500;
		} else { // Size.SIZE_THUMB
			width=200; height=200;
		}

		// scale
        Transform resize = ImagesServiceFactory.makeResize(width, height);
        Image newImage = imagesService.applyTransform(resize, image);
        
        // store
        ScaledDown thumbnailPreview = new ScaledDown();
        thumbnailPreview.wallpaperId = wallpaper.id;
		thumbnailPreview.size = size;
        thumbnailPreview.data = newImage.getImageData();
        dao.ofy().put(thumbnailPreview);
	}
}
