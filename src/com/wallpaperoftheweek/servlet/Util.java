package com.wallpaperoftheweek.servlet;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Util {
	public static byte[] saltedHash(String str) {
		try {
			MessageDigest md = MessageDigest.getInstance("SHA1");
			return md.digest(("jfkioewjo3898"+str).getBytes("UTF-8"));
		} catch (NoSuchAlgorithmException e) {
			throw new Error(e);
		} catch (UnsupportedEncodingException e) {
			throw new Error(e);
		}
	}
}
