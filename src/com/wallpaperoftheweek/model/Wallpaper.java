package com.wallpaperoftheweek.model;

import javax.persistence.Id;

import com.google.appengine.api.blobstore.BlobKey;

public class Wallpaper {
    public @Id Long id;
    public BlobKey blobKey;
    public int votes = 0;
    
    // Random number used for ordering on the home page 
    public double random = Math.random();
	public byte[] saltedUserId;
}
