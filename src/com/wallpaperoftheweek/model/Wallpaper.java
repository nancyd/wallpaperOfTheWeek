package com.wallpaperoftheweek.model;

import javax.persistence.Id;

import org.joda.time.Instant;

import com.google.appengine.api.blobstore.BlobKey;

public class Wallpaper {
    public @Id Long id;
    public BlobKey blobKey;
    public int votes = 0;
    public String userId;
    public long addedOnMilis;
    
    // Random number used for ordering on the home page 
    public double random = Math.random();
    
	public Instant getAddedOn() {
		return new Instant(addedOnMilis);
	}

	public void setAddedOn(Instant addedOn) {
		this.addedOnMilis = addedOn.getMillis();
	}

}
