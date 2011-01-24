package com.wallpaperoftheweek;

import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.helper.DAOBase;
import com.wallpaperoftheweek.model.ScaledDown;
import com.wallpaperoftheweek.model.Vote;
import com.wallpaperoftheweek.model.Wallpaper;

public class DAO extends DAOBase {
	static {
		ObjectifyService.register(Wallpaper.class);
		ObjectifyService.register(ScaledDown.class);
		ObjectifyService.register(Vote.class);
	}
	
	public Wallpaper getWallpaper(long id) {
		return ofy().get(Wallpaper.class, id);
	}
}
