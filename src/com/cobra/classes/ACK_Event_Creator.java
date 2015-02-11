package com.cobra.classes;

import com.cobra.appClass;
import com.cobra.api.Cobra;

public class ACK_Event_Creator {

	public static Boolean HaveEvent(int flag_Battery, int flag_Module,
			int flag_Firecues) {
		if (flag_Battery == 1 || flag_Module == 1 || flag_Firecues == 1) {
			return true;
		}
		return false;
	}

	public static int getEvent(int d, int m, int f) {
		if (d == 1 && m == 1 && f == 1) {
				return Cobra.EVENT_ACK_DEVICE_MODULE_FIRECUE_DATA_CHANGE;

		} else if (d == 1 && m == 1 && f != 1) {
				return Cobra.EVENT_ACK_DEVICE_MODULE_DATA_CHANGE;

		} else if (d == 1 && m != 1 && f == 1) {
				return Cobra.EVENT_ACK_DEVICE_FIRECUE_DATA_CHANGE;

		} else if (d == 1 && m != 1 && f != 1) {
				return Cobra.EVENT_ACK_DEVICE_DATA_CHANGE;

		} else if (d != 1 && m == 1 && f == 1) {
				return Cobra.EVENT_ACK_MODULE_FIRECUE_DATA_CHANGE;
		} else if (d != 1 && m == 1 && f != 1) {
				return Cobra.EVENT_ACK_MODULE_DATA_CHANGE;

		} else if (d != 1 && m != 1 && f == 1) {
				return Cobra.EVENT_ACK_FIRECUE_DATA_CHANGE;
		}
		return 0;
	}
	
//	public static int getEvent(int d, int m, int f) {
//		if (d == 1 && m == 1 && f == 1) {
//			if (appClass.getFlagDeviceUpate() == 0
//					&& appClass.getFlagModuleUpate() == 0
//					&& appClass.getFlagFireCuesUpate() == 0)
//				return Cobra.EVENT_ACK_DEVICE_MODULE_FIRECUE_DATA_CHANGE;
//
//			else if (appClass.getFlagDeviceUpate() == 0
//					&& appClass.getFlagModuleUpate() == 0
//					&& appClass.getFlagFireCuesUpate() != 0)
//				return Cobra.EVENT_ACK_DEVICE_MODULE_DATA_CHANGE;
//
//			else if (appClass.getFlagDeviceUpate() == 0
//					&& appClass.getFlagModuleUpate() != 0
//					&& appClass.getFlagFireCuesUpate() == 0)
//				return Cobra.EVENT_ACK_DEVICE_FIRECUE_DATA_CHANGE;
//
//			else if (appClass.getFlagDeviceUpate() == 0
//					&& appClass.getFlagModuleUpate() != 0
//					&& appClass.getFlagFireCuesUpate() != 0)
//				return Cobra.EVENT_ACK_DEVICE_DATA_CHANGE;
//
//			else if (appClass.getFlagDeviceUpate() != 0
//					&& appClass.getFlagModuleUpate() == 0
//					&& appClass.getFlagFireCuesUpate() == 0)
//				return Cobra.EVENT_ACK_MODULE_FIRECUE_DATA_CHANGE;
//
//			else if (appClass.getFlagDeviceUpate() != 0
//					&& appClass.getFlagModuleUpate() == 0
//					&& appClass.getFlagFireCuesUpate() != 0)
//				return Cobra.EVENT_ACK_MODULE_DATA_CHANGE;
//
//			else if (appClass.getFlagDeviceUpate() != 0
//					&& appClass.getFlagModuleUpate() != 0
//					&& appClass.getFlagFireCuesUpate() == 0)
//				return Cobra.EVENT_ACK_FIRECUE_DATA_CHANGE;
//
//			else
//				return 0;
//
//		} else if (d == 1 && m == 1 && f != 1) {
//			if (appClass.getFlagDeviceUpate() == 0
//					&& appClass.getFlagModuleUpate() == 0)
//				return Cobra.EVENT_ACK_DEVICE_MODULE_DATA_CHANGE;
//
//			else if (appClass.getFlagDeviceUpate() == 0
//					&& appClass.getFlagModuleUpate() != 0)
//				return Cobra.EVENT_ACK_DEVICE_DATA_CHANGE;
//
//			else if (appClass.getFlagDeviceUpate() != 0
//					&& appClass.getFlagModuleUpate() == 0)
//				return Cobra.EVENT_ACK_MODULE_DATA_CHANGE;
//
//			else
//				return 0;
//
//		} else if (d == 1 && m != 1 && f == 1) {
//			if (appClass.getFlagDeviceUpate() == 0
//					&& appClass.getFlagFireCuesUpate() == 0)
//				return Cobra.EVENT_ACK_DEVICE_FIRECUE_DATA_CHANGE;
//
//			else if (appClass.getFlagDeviceUpate() == 0
//					&& appClass.getFlagFireCuesUpate() != 0)
//				return Cobra.EVENT_ACK_DEVICE_DATA_CHANGE;
//
//			else if (appClass.getFlagDeviceUpate() != 0
//					&& appClass.getFlagFireCuesUpate() == 0)
//				return Cobra.EVENT_ACK_FIRECUE_DATA_CHANGE;
//
//			else
//				return 0;
//
//		} else if (d == 1 && m != 1 && f != 1) {
//			if (appClass.getFlagDeviceUpate() == 0)
//				return Cobra.EVENT_ACK_DEVICE_DATA_CHANGE;
//
//		} else if (d != 1 && m == 1 && f == 1) {
//			if (appClass.getFlagModuleUpate() == 0
//					&& appClass.getFlagFireCuesUpate() == 0)
//				return Cobra.EVENT_ACK_MODULE_FIRECUE_DATA_CHANGE;
//
//			else if (appClass.getFlagModuleUpate() == 0
//					&& appClass.getFlagFireCuesUpate() != 0)
//				return Cobra.EVENT_ACK_MODULE_DATA_CHANGE;
//
//			else if (appClass.getFlagModuleUpate() != 0
//					&& appClass.getFlagFireCuesUpate() == 0)
//				return Cobra.EVENT_ACK_FIRECUE_DATA_CHANGE;
//
//			else
//				return 0;
//
//		} else if (d != 1 && m == 1 && f != 1) {
//			if (appClass.getFlagModuleUpate() == 0)
//				return Cobra.EVENT_ACK_MODULE_DATA_CHANGE;
//
//		} else if (d != 1 && m != 1 && f == 1) {
//			if (appClass.getFlagFireCuesUpate() == 0)
//				return Cobra.EVENT_ACK_FIRECUE_DATA_CHANGE;
//
//		}
//		return 0;
//	}

}
