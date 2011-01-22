package com.wallpaperoftheweek.model;

import javax.persistence.Id;

import com.google.appengine.api.blobstore.BlobKey;

public class Vote {
	public enum VoteType {
		VOTE_UP,
		VOTE_DOWN
	}
	public @Id Long Id;
    public Long wallpaperId;
    public VoteType voteType;
    public byte[] saltedUserId;
}
