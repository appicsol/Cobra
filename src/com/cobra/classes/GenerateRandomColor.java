package com.cobra.classes;

import java.util.Random;

public class GenerateRandomColor {
	static int pre_Range1 = 0;
	static int pre_Range2 = 0;
	static int pre_Range3 = 0;
	static int pre_Range4 = 0;

	public static int GetRandomColor() {
		int minimum = 0;
		int maximum = 128;

		int range = maximum - minimum + 1;

		int range1 = GenerateRandom(pre_Range1, range, minimum)+127;
		pre_Range1 = range1;
		int range2 = GenerateRandom(pre_Range2, range, minimum)+127;
		pre_Range2 = range2;
//		minimum = 150;
//		maximum = 255;
		int range3 = GenerateRandom(pre_Range3, range, minimum)+127;
		pre_Range3 = range3;
		int range4 = GenerateRandom(pre_Range4, range, minimum)+127;
		pre_Range4 = range4;
		return SelectRGB(range1, range2, range3, range4);
	}

	static int tries = 0;

	public static int GenerateRandom(int pre_range, int range, int minimum) {
		Random rand = new Random();
		int temp = rand.nextInt(range) + minimum;
		tries++;
		if (minimum > 149) {
			if (pre_range > (temp - 50) && pre_range < (temp + 50) && tries < 5) {
				return GenerateRandom(pre_range, range, minimum);

			} else {
				if (tries == 5) {
					tries = 0;
					return temp + 50;
				} else {
					return temp;
				}
			}
		} else {
			if (pre_range > (temp - 10) && pre_range < (temp + 10) && tries < 5) {
				return GenerateRandom(pre_range, range, minimum);

			} else {
				if (tries == 5) {
					tries = 0;
					return temp + 10;
				} else {
					return temp;
				}
			}
		}
	}

	static Boolean Is_Range1_Selected = false;
	static Boolean Is_Range2_Selected = false;
	static Boolean Is_Range3_Selected = false;
	static Boolean Is_Range4_Selected = false;
	static int Available_RGB_Range = 4;

	public static int SelectRGB(int range1, int range2, int range3, int range4) {
		Random rand = new Random();
		int position = rand.nextInt(Available_RGB_Range) + 1;
		if (position == 1) {
			if (!Is_Range1_Selected) {
				Is_Range1_Selected = true;
				return range1;
			} else if (!Is_Range2_Selected) {
				Is_Range2_Selected = true;
				return range2;
			} else if (!Is_Range3_Selected) {
				Is_Range3_Selected = true;
				return range3;
			} else {
				Is_Range4_Selected = true;
				return range4;
			}
		} else if (position == 2) {
			if (!Is_Range2_Selected) {
				Is_Range2_Selected = true;
				return range2;
			} else if (!Is_Range3_Selected) {
				Is_Range3_Selected = true;
				return range3;
			} else if (!Is_Range4_Selected) {
				Is_Range4_Selected = true;
				return range4;
			} else {
				Is_Range1_Selected = true;
				return range1;
			}
		} else if (position == 3) {
			if (!Is_Range3_Selected) {
				Is_Range3_Selected = true;
				return range3;
			} else if (!Is_Range4_Selected) {
				Is_Range4_Selected = true;
				return range4;
			} else if (!Is_Range1_Selected) {
				Is_Range1_Selected = true;
				return range1;
			} else {
				Is_Range2_Selected = true;
				return range2;
			}
		} else {
			if (!Is_Range4_Selected) {
				Is_Range4_Selected = true;
				return range4;
			} else if (!Is_Range1_Selected) {
				Is_Range1_Selected = true;
				return range1;
			} else if (!Is_Range2_Selected) {
				Is_Range2_Selected = true;
				return range2;
			} else {
				Is_Range1_Selected = true;
				return range1;
			}
		}

	}

	public static void ClearRGB_Range() {
		Is_Range1_Selected = false;
		Is_Range2_Selected = false;
		Is_Range3_Selected = false;
		Is_Range4_Selected = false;
	}
}
