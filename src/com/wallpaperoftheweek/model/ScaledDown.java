package com.wallpaperoftheweek.model;

import javax.persistence.Id;

import com.google.appengine.api.blobstore.BlobKey;

public class ScaledDown {
	public enum Size {
		SIZE_THUMB,
		SIZE_PREVIEW
	}
	public @Id Long Id;
    public Long wallpaperId;
    public Size size;
    public byte[] data;
}
