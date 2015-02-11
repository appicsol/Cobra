package com.cobra.interfaces;

public interface CustomListViewListener {
	public void ModuleDraggedInToTheList(int DraggedModule,int ListIndex);
	public void NewBucket(int DraggedModule);
	public void ModuleDraggedOutFromList(int DraggedModule);
}
