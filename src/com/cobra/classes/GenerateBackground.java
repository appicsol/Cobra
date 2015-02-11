package com.cobra.classes;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.view.Display;

public class GenerateBackground {

	/**
	 * all Background Function create  Round Rectangular Shape
	 * insideBorder : inside area of this rectangle 
	 * border : border of rectangle
	 * outsideBorder : outside area the rectangle
	 * these functions actually create three shapes (outside area, inside area and border) and return merged drawable of all three shapes. 
	 */
	
	/**
	 * 
	 * @param activity
	 * @return
	 * this function returns rectangular shape with random inside color. 
	 */
	public static LayerDrawable Background(Activity activity) {

		int temp = 5;
		float[] InnerCornerWidth = new float[] { 20 - temp, 20 - temp,
				20 - temp, 20 - temp, 20 - temp, 20 - temp, 20 - temp,
				20 - temp };
		ShapeDrawable insideBorder = new ShapeDrawable(new RoundRectShape(
				InnerCornerWidth, null, null));
		insideBorder.getPaint().setColor(
				Color.rgb(GenerateRandomColor.GetRandomColor(),
						GenerateRandomColor.GetRandomColor(),
						GenerateRandomColor.GetRandomColor()));
		insideBorder.getPaint().setStyle(Paint.Style.FILL_AND_STROKE);

		float[] OuterCornerWidth = new float[] { 20, 20, 20, 20, 20, 20, 20, 20 };
		ShapeDrawable border = new ShapeDrawable(new RoundRectShape(
				OuterCornerWidth, null, null));
		border.getPaint().setColor(Color.rgb(0, 0, 0));
		int padding = (int) GetLength(activity, 0.45f, 'h');
		border.setPadding(padding, padding, padding, padding);

		ShapeDrawable outsideBorder = new ShapeDrawable(new RoundRectShape(
				OuterCornerWidth, null, null));
		outsideBorder.getPaint().setColor(Color.argb(0, 255, 255, 255));
		outsideBorder.setPadding(padding, padding, padding, padding);

		Drawable[] d = { outsideBorder, border, insideBorder };
		LayerDrawable composite1 = new LayerDrawable(d);
		return composite1;
	}

	/**
	 * 
	 * @param DoPadding allow you to set inside area padding of your choice by passing true in it and
	 * by giving padding percentage values under  @param paddingPercentage
	 * if you set @param DoPadding false then it will set padding itself.
 	 * @return
	 */
	public static LayerDrawable Background(Activity activity,
			int[] insideColor, Boolean DoPadding, float paddingPercentage) {

		int temp = 5;
		float[] InnerCornerWidth = new float[] { 20 - temp, 20 - temp,
				20 - temp, 20 - temp, 20 - temp, 20 - temp, 20 - temp,
				20 - temp };
		ShapeDrawable insideBorder = new ShapeDrawable(new RoundRectShape(
				InnerCornerWidth, null, null));
		insideBorder.getPaint().setColor(
				Color.rgb(insideColor[0], insideColor[1], insideColor[2]));
		insideBorder.getPaint().setStyle(Paint.Style.FILL_AND_STROKE);
		int padding1 = (int) GetLength(activity,0.45f , 'w');
		if (DoPadding)
			insideBorder.setPadding(padding1 * 5, padding1, padding1 * 5,
					padding1);
		else{
			padding1 = (int) GetLength(activity,paddingPercentage , 'w');
			insideBorder.setPadding(padding1, padding1, padding1, padding1);
		}
		float[] OuterCornerWidth = new float[] { 20, 20, 20, 20, 20, 20, 20, 20 };
		ShapeDrawable border = new ShapeDrawable(new RoundRectShape(
				OuterCornerWidth, null, null));
		border.getPaint().setColor(Color.rgb(0, 0, 0));
		int padding = (int) GetLength(activity, 0.45f, 'h');
		border.setPadding(padding, padding, padding, padding);

		ShapeDrawable outsideBorder = new ShapeDrawable(new RoundRectShape(
				OuterCornerWidth, null, null));
		outsideBorder.getPaint().setColor(Color.argb(0, 255, 255, 255));
		outsideBorder.setPadding(padding, padding, padding, padding);

		Drawable[] d = { outsideBorder, border, insideBorder };
		LayerDrawable composite1 = new LayerDrawable(d);
		return composite1;
	}

	public static LayerDrawable Background(Activity activity,
			int[] insideColor, Boolean DoPadding, float leftpadding, float rightpadding, float toppadding, float bottompadding) {

		int temp = 5;
		float[] InnerCornerWidth = new float[] { 20 - temp, 20 - temp,
				20 - temp, 20 - temp, 20 - temp, 20 - temp, 20 - temp,
				20 - temp };
		ShapeDrawable insideBorder = new ShapeDrawable(new RoundRectShape(
				InnerCornerWidth, null, null));
		insideBorder.getPaint().setColor(
				Color.rgb(insideColor[0], insideColor[1], insideColor[2]));
		insideBorder.getPaint().setStyle(Paint.Style.FILL_AND_STROKE);
		int padding1 = (int) GetLength(activity,0.45f , 'w');
		int padding2=0;
		int padding3=0;
		int padding4=0;
		if (DoPadding)
			insideBorder.setPadding(padding1 * 5, padding1, padding1 * 5,
					padding1);
		else{
			padding1 = (int) GetLength(activity,leftpadding , 'w');
			padding2 = (int) GetLength(activity,toppadding , 'w');
			padding3 = (int) GetLength(activity,rightpadding , 'w');
			padding4 = (int) GetLength(activity,bottompadding , 'w');
			insideBorder.setPadding(padding1, padding2, padding3, padding4);
		}
		float[] OuterCornerWidth = new float[] { 20, 20, 20, 20, 20, 20, 20, 20 };
		ShapeDrawable border = new ShapeDrawable(new RoundRectShape(
				OuterCornerWidth, null, null));
		border.getPaint().setColor(Color.rgb(0, 0, 0));
		int padding = (int) GetLength(activity, 0.45f, 'h');
		border.setPadding(padding, padding, padding, padding);

		ShapeDrawable outsideBorder = new ShapeDrawable(new RoundRectShape(
				OuterCornerWidth, null, null));
		outsideBorder.getPaint().setColor(Color.argb(0, 255, 255, 255));
		outsideBorder.setPadding(padding, padding, padding, padding);

		Drawable[] d = { outsideBorder, border, insideBorder };
		LayerDrawable composite1 = new LayerDrawable(d);
		return composite1;
	}
	
	public static LayerDrawable Background(Activity activity,
			int[] insideColor,int[] borderColor,int alpha, Boolean DoPadding, float paddingPercentage) {

		int temp = 5;
		float[] InnerCornerWidth = new float[] { 20 - temp, 20 - temp,
				20 - temp, 20 - temp, 20 - temp, 20 - temp, 20 - temp,
				20 - temp };
		ShapeDrawable insideBorder = new ShapeDrawable(new RoundRectShape(
				InnerCornerWidth, null, null));
		insideBorder.getPaint().setColor(
				Color.argb(alpha,insideColor[0], insideColor[1], insideColor[2]));
		insideBorder.getPaint().setStyle(Paint.Style.FILL_AND_STROKE);
		int padding1 = (int) GetLength(activity,0.45f , 'w');
		if (DoPadding)
			insideBorder.setPadding(padding1 * 5, padding1, padding1 * 5,
					padding1);
		else{
			padding1 = (int) GetLength(activity,paddingPercentage , 'w');
			insideBorder.setPadding(padding1, padding1, padding1, padding1);
		}
		float[] OuterCornerWidth = new float[] { 20, 20, 20, 20, 20, 20, 20, 20 };
		ShapeDrawable border = new ShapeDrawable(new RoundRectShape(
				OuterCornerWidth, null, null));
		border.getPaint().setColor(Color.argb(alpha, borderColor[0], borderColor[1], borderColor[2]));
		int padding = (int) GetLength(activity, 0.45f, 'h');
		border.setPadding(padding, padding, padding, padding);

		ShapeDrawable outsideBorder = new ShapeDrawable(new RoundRectShape(
				OuterCornerWidth, null, null));
		outsideBorder.getPaint().setColor(Color.argb(0, 255, 255, 255));
		outsideBorder.setPadding(padding, padding, padding, padding);

		Drawable[] d = { outsideBorder, border, insideBorder };
		LayerDrawable composite1 = new LayerDrawable(d);
		return composite1;
	}
	
	public static LayerDrawable Background(Activity activity, int InSideColor,
			int Radius_RightSide) {
		int temp = 5;
		float[] borderShape1 = new float[] { 0, 0, Radius_RightSide - temp,
				Radius_RightSide - temp, Radius_RightSide - temp,
				Radius_RightSide - temp, 0, 0 };

		float[] borderShape = new float[] { 0, 0, Radius_RightSide,
				Radius_RightSide, Radius_RightSide, Radius_RightSide, 0, 0 };

		ShapeDrawable insideBorder = new ShapeDrawable(new RoundRectShape(
				borderShape1, null, null));
		insideBorder.getPaint().setColor(
				Color.rgb(InSideColor, InSideColor, InSideColor));
		insideBorder.getPaint().setStyle(Paint.Style.FILL_AND_STROKE);
		int padding1 = (int) GetLength(activity, 0.45f, 'h');
		insideBorder.setPadding(padding1 * 5, padding1, padding1 * 5, padding1);

		ShapeDrawable border = new ShapeDrawable(new RoundRectShape(
				borderShape, null, null));
		border.getPaint().setColor(Color.rgb(0, 0, 0));
		int padding = (int) GetLength(activity, 0.45f, 'h');
		border.setPadding(padding, padding, padding, padding);

		ShapeDrawable outsideBorder = new ShapeDrawable(new RoundRectShape(
				borderShape, null, null));
		outsideBorder.getPaint().setColor(Color.argb(0, 255, 255, 255));
		outsideBorder.setPadding(padding, padding, padding, padding);

		Drawable[] d = { outsideBorder, border, insideBorder };
		LayerDrawable composite1 = new LayerDrawable(d);
		return composite1;
	}

	public static LayerDrawable Background(Activity activity,
			int[] InSideColor, int Radius_RightSide) {
		int temp = 5;
		float[] borderShape1 = new float[] { 0, 0, Radius_RightSide - temp,
				Radius_RightSide - temp, Radius_RightSide - temp,
				Radius_RightSide - temp, 0, 0 };

		float[] borderShape = new float[] { 0, 0, Radius_RightSide,
				Radius_RightSide, Radius_RightSide, Radius_RightSide, 0, 0 };

		ShapeDrawable insideBorder = new ShapeDrawable(new RoundRectShape(
				borderShape1, null, null));
		insideBorder.getPaint().setColor(
				Color.rgb(InSideColor[0], InSideColor[1], InSideColor[2]));
		insideBorder.getPaint().setStyle(Paint.Style.FILL_AND_STROKE);
		int padding1 = (int) GetLength(activity, 0.45f, 'h');
		insideBorder.setPadding(padding1 * 5, padding1, padding1 * 5, padding1);

		ShapeDrawable border = new ShapeDrawable(new RoundRectShape(
				borderShape, null, null));
		border.getPaint().setColor(Color.rgb(0, 0, 0));
		int padding = (int) GetLength(activity, 0.45f, 'h');
		border.setPadding(padding, padding, padding, padding);

		ShapeDrawable outsideBorder = new ShapeDrawable(new RoundRectShape(
				borderShape, null, null));
		outsideBorder.getPaint().setColor(Color.argb(0, 255, 255, 255));
		outsideBorder.setPadding(padding, padding, padding, padding);

		Drawable[] d = { outsideBorder, border, insideBorder };
		LayerDrawable composite1 = new LayerDrawable(d);
		return composite1;
	}

	private static float GetLength(Activity activity, float percent,
			char dimensions) {
		Display display = activity.getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		int screen_width = size.x;
		int screen_height = size.y;
		if (dimensions == 'w') {
			float temp = percent / 100 * screen_width;
			return temp;
		} else {
			float temp = percent / 100 * screen_height;
			return temp;
		}
	}
}
