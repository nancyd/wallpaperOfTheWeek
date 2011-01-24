package com.wallpaperoftheweek.model;

import javax.persistence.Id;

import com.google.appengine.api.blobstore.BlobKey;

public class Vote {
	public @Id Long Id;
    public Long wallpaperId;
    public String userId;
}
