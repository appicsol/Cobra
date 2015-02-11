package com.cobra.api;

import java.util.*;

class CobraMessage {

	public static byte[] createMsg(int command, HashMap<String, Byte> data)
			throws Exception {
		byte[] msg = new byte[CobraCommands.MAX_MESSAGE_BYTES];
		int size = 1;

		msg[0] = (byte) command;

		switch (command) {
		case CobraCommands.VCOM_SET_FIRE_MODE:
			break;

		case CobraCommands.VCOM_OWN_ACK_FLAG_STATUS:

			msg[1] = data.get("device").byteValue();
			msg[2] = data.get("module").byteValue();
			msg[3] = data.get("scripts").byteValue();
			size = 4;
			break;

		case CobraCommands.VCOM_REQ_NEXT_EVENT_DATA:

			msg[1] = data.get("data").byteValue();
			msg[2] = data.get("firsteventbyte").byteValue();
			msg[3] = data.get("secondeventbyte").byteValue();
			size = 4;
			break;

		case CobraCommands.VCOM_REQ_NEXT_SCRIPT_DATA:
			msg[1] = data.get("data").byteValue();
			size = 2;
			break;

		case CobraCommands.VCOM_REQ_QUEUED_MODULE_DATA:

			msg[1] = data.get("firstbyte").byteValue();
			msg[2] = data.get("secondbyte").byteValue();
			size = 3;
			break;

		case CobraCommands.VCOM_REQ_QUEUED_FIREDCUES_DATA:
			msg[1] = data.get("data").byteValue();
			size = 2;
			break;

		case CobraCommands.VCOM_REQ_DEVICE_STATUS:
			break;

		case CobraCommands.VCOM_REQ_MODULE_DATA:
			// msg[1] = data.get("data").byteValue();
			// size=2;
			break;

		case CobraCommands.VCOM_REQ_FIREDCUES_DATA:
			msg[1] = data.get("channel").byteValue();
			size = 2;
			break;

		case CobraCommands.VCOM_ACKNOWLEDGE_STATUS_CHANGE:
			msg[1] = data.get("first").byteValue();
			if (!Cobra.IsSingleData) {
				msg[2] = data.get("second").byteValue();
				msg[3] = data.get("third").byteValue();
				size = 4;
			} else
				size = 2;
			break;

		case CobraCommands.VCOM_SET_TEST_MODE:
			break;

		case CobraCommands.VCOM_SET_CHANNEL:
			msg[1] = data.get("channel").byteValue();
			size = 2;
			break;

		case CobraCommands.VCOM_SET_DEVICE_REBOOT:
			break;

		case CobraCommands.VCOM_REQ_DEVICE_INFO:
			break;

		case 98:
			break;

		case CobraCommands.VCOM_STATUS_PINGBACK:
			break;

		case CobraCommands.VCOM_REQ_MODULE_DATA_REFRESH:
			break;

		case CobraCommands.VCOM_SET_DUMMY_MODE:
			break;
		case CobraCommands.VCOM_REQ_STEP_NEXT:
			break;

		case CobraCommands.VCOM_REQ_FIRE_CUES:
			msg[1] = data.get("cueList0").byteValue();
			msg[2] = data.get("cueList1").byteValue();
			msg[3] = data.get("cueList2").byteValue();
			size = 4;
			break;

		case CobraCommands.VCOM_REQ_PLAY_SCRIPT:
			msg[1] = data.get("scriptIndex").byteValue();
			msg[2] = data.get("startPaused").byteValue();
			size = 3;
			break;

		case CobraCommands.VCOM_REQ_PAUSE_SCRIPT:
			break;

		case CobraCommands.VCOM_REQ_RESUME_SCRIPT:
			break;

		case CobraCommands.VCOM_REQ_STOP_SCRIPT:
			break;

		case CobraCommands.VCOM_REQ_JUMPTO_TIME:
			msg[1] = data.get("jumpTimeIndex0").byteValue();
			msg[2] = data.get("jumpTimeIndex1").byteValue();
			msg[3] = data.get("jumpTimeIndex2").byteValue();
			msg[4] = data.get("jumpTimeIndex3").byteValue();
			size = 5;
			break;

		case CobraCommands.VCOM_REQ_JUMPTO_EVENT:
			msg[1] = data.get("jumpEventIndex0").byteValue();
			msg[2] = data.get("jumpEventIndex1").byteValue();
			size = 3;
			break;

		default:
			throw new Exception("Invalid or non-implemented command.");

		}

		msg = Arrays.copyOf(msg, size + 1);

		msg[size] = calculateChecksum(Arrays.copyOfRange(msg, 0, size));

		return msg;
	}

	public static int verifyMessage(byte[] msg) {
		byte checksum;

		checksum = calculateChecksum(Arrays.copyOfRange(msg, 0, msg.length - 1));
		if (checksum != msg[msg.length - 1]) {
			return 0;
		}

		return msg[0];
	}

	public static byte calculateChecksum(byte[] msg) {
		byte checksum = 0;

		for (int i = 0; i < msg.length; i++)
			checksum += msg[i];

		return checksum;
	}

}
