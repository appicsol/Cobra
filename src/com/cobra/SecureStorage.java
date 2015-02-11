package com.cobra;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import android.content.Context;

public class SecureStorage {

	public static void storeString(String encryptionKey, String location, Context context, String data) throws IOException, GeneralSecurityException {		
		FileOutputStream outputStream;
		byte[] encryptedData = encrypt(encryptionKey, data);

	    outputStream = context.openFileOutput(location, Context.MODE_PRIVATE);
	    outputStream.write(encryptedData);
	    outputStream.close();
	}
	
	public static String retrieveString(String encryptionKey, String location, Context context) throws IOException, GeneralSecurityException {
		FileInputStream inputStream = context.openFileInput(location);
		byte[] data = new byte[3]; //TODO increase bytearray size
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();
		int numRead;

		while ((numRead = inputStream.read(data, 0, data.length)) != -1) {
		  buffer.write(data, 0, numRead);
		}

		buffer.flush();
		
		inputStream.close();
		
		return decrypt(encryptionKey, buffer.toByteArray());
	}
	
	private static String decrypt(String encryptionKey, byte[] data) throws GeneralSecurityException{
		Cipher aes = Cipher.getInstance("AES/ECB/PKCS5Padding");
		
		MessageDigest digest = MessageDigest.getInstance("SHA");
		digest.update(encryptionKey.getBytes());
		
		SecretKeySpec key = new SecretKeySpec(digest.digest(), 0, 16, "AES");
		
		aes.init(Cipher.DECRYPT_MODE, key);
		
		return new String(aes.doFinal( data  ));
	}
	
	private static byte[] encrypt(String encryptionKey, String data) throws GeneralSecurityException{
		MessageDigest digest = MessageDigest.getInstance("SHA");
		digest.update(encryptionKey.getBytes());
		
		SecretKeySpec key = new SecretKeySpec(digest.digest(), 0, 16, "AES");
		
		Cipher aes = Cipher.getInstance("AES/ECB/PKCS5Padding");
		aes.init(Cipher.ENCRYPT_MODE, key);
		
		return aes.doFinal(data.getBytes());
	}
}
