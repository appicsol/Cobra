package com.cobra.classes;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Random;

import com.cobra.R;

public class MyResources {

	public static final int[] BRANDS = new int[] {};

	public static final String[] BRAND_NAMES = new String[] { "adidas",
			"adobe", "allianz", "aol", "audi", "billabong", "canon" };

	public static String[] getRandomStringForBrand(String brandName) {

		ArrayList<String> brandList = new ArrayList<String>();
		for (int i = 0; i < brandList.size(); i++) {
			String letter=""+brandName.charAt(i);
			String letterInUpperCase=letter.toUpperCase(Locale.ENGLISH);
			brandList.add(letterInUpperCase);
		}

		String[] randomArray = new String[12];
		int remainingRandomString = randomArray.length - brandList.size();
		
		Random rand = new Random();
		while (brandList.size() > 0) {
			int index = rand.nextInt(brandList.size());
			int indexOfNewArray = rand.nextInt(randomArray.length);

			if (randomArray[indexOfNewArray] == null) {
				randomArray[indexOfNewArray] = brandList.get(index);
				brandList.remove(index);
			}
		}

		//filling remaining random array indexs with random alphabets
		for(int i=0;i<randomArray.length;i++){
			int index = rand.nextInt(alphabets.length);
			if(randomArray[i]==null){
				randomArray[i]=alphabets[index];
			}
		}
		
		return randomArray;
	}

	private static String[] alphabets = new String[] { "A", "B", "C", "D", "E", "F",
			"G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S",
			"T", "U", "V", "W", "X", "Y", "Z" };
}
