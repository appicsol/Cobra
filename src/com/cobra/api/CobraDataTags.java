package com.cobra.api;

import java.util.ArrayList;

import com.cobra.appClass;
import com.cobra.api.Cobra.ModuleType;

// TODO: Auto-generated Javadoc
/**
 * The Class CobraDataTags.
 */
public class CobraDataTags {

	/** The Constant TAG. */
	public static final String TAG = "com.cobra.api.CobraDataTags";

	/**
	 * The Version class holds version information.
	 */
	public static class Version {
		/** The major number. */
		public final int major;
		/** The minor number. */
		public final int minor;
		/** The revision number. */
		public final int revision;
		/** The sub-revision. */
		public final char subrevision;

		/**
		 * Instantiates a new version.
		 * 
		 * @param major
		 *            the major version
		 * @param minor
		 *            the minor version
		 * @param revision
		 *            the revision
		 * @param subrevision
		 *            the subrevision NOTWORKING
		 */
		public Version(int major, int minor, int revision, char subrevision) {
			this.major = major;
			this.minor = minor;
			this.revision = revision;
			this.subrevision = subrevision;
		}

		/**
		 * Instantiates a new version.
		 * 
		 * @param version
		 *            the version
		 */
		public Version(Version version) {
			this(version.major, version.minor, version.revision,
					version.subrevision);
		}
	}

	/**
	 * The Class ModuleDataTag which holds information pertaining to a module on
	 * the network.
	 */
	public static class ModuleDataTag {
		/**
		 * The armed status of the module. Only valid if {@link #onNetwork}
		 * value for the same module is true.
		 * 
		 * @return true, if armed. <br>
		 *         false, otherwise.
		 * 
		 * */
		public Boolean armed;

		/**
		 * The key position of the module. Only valid if {@link #onNetwork}
		 * value for the same module is true.
		 * 
		 * @return true, the last acknowledged report from the given module
		 *         showed its key position. <br>
		 *         false, otherwise.
		 * 
		 * */
		public Boolean keyPos;

		/**
		 * The current channel of this module, if the module has reported itself
		 * to the device. Valid channel values are from 0 to 99. Only valid if
		 * {@link #onNetwork} value for the same module is true.
		 * 
		 **/
		public int currentChannel;

		/**
		 * Provides the last measured battery levels of the given module, if the
		 * module has reported itself to the device. {@link #batteryLevel1}
		 * value is the low voltage (logic) power battery source reading. Valid
		 * values are 0-100 which gives an approximate state of charge
		 * indication in percent.
		 * 
		 * Only valid if {@link #onNetwork} value for the same module is true.
		 * 
		 **/
		public int batteryLevel1;

		/**
		 * Provides the last measured battery levels of the given module, if the
		 * module has reported itself to the device. {@link #batteryLevel2}
		 * value is the high voltage (cue firing) power battery source reading.
		 * Valid values are 0-100 which gives an approximate state of charge
		 * indication in percent.
		 * 
		 * Only valid if {@link #onNetwork} value for the same module is true.
		 * 
		 **/
		public int batteryLevel2;

		/**
		 * Provides the last measured link quality of the given module in dB
		 * (decibels), if the module has reported itself to the device.
		 * 
		 * Only valid if {@link #onNetwork} value for the same module is true.
		 * 
		 **/
		public int linkQuality;

		/**
		 * Is a 32-bit field that contains cue continuity information for each
		 * cue of a firing module. The bits are arranged with bit 0 representing
		 * cue 1, and bit 17 representing cue 18. A set (1) bit in a certain
		 * position indicates that the respective cue output has measured a low
		 * resistance device attached to the terminals.
		 * 
		 * Only valid if {@link #onNetwork} value for the same module is true.
		 * 
		 **/
		public int testResults;

		/**
	     * 
	     * 
	     **/
		public ModuleType modType;

		/**
		 * The module ID/address.
		 * 
		 * Only valid if {@link #onNetwork} value for the same module is true.
		 * 
		 **/
		public int modID;

		/**
		 * Instantiates a new module data tag object.
		 * 
		 * @param modID
		 *            the mod id
		 * @param channel
		 *            the current channel.
		 * @param battLevel
		 *            the batteryLevel.
		 * @param linkQual
		 *            the link quality.
		 * @param testRes
		 *            the continuity test results.
		 */
		public ModuleDataTag(int modID, int channel, int battLevel,
				int linkQual, int testRes, ModuleType type) {
			this.modID = modID;
			this.armed = ((channel & 0xFF) >> 7) == 1;
			this.keyPos = ((linkQual & 0xFF) >> 7) == 0;
			this.currentChannel = (byte) (channel & 0x7F); // mask off MSB (MSB
															// = Armed status)
			this.batteryLevel1 = (byte) (battLevel & 0x0F);
			this.batteryLevel2 = (byte) (battLevel >> 4);
			this.linkQuality = (byte) (linkQual & 0x7F);
			this.testResults = testRes;
			this.modType = type;
		}

		/**
		 * Copy constructor. Instantiates a new ModuleDataTag by copying the
		 * argument ModuleDataTag.
		 * 
		 * @param tag
		 *            the tag
		 */
		public ModuleDataTag(ModuleDataTag tag) {
			this.modID = tag.modID;
			this.armed = tag.armed;
			this.keyPos = tag.keyPos;
			this.currentChannel = tag.currentChannel;
			this.batteryLevel1 = tag.batteryLevel1;
			this.batteryLevel2 = tag.batteryLevel2;
			this.linkQuality = tag.linkQuality;
			this.testResults = tag.testResults;
			this.modType = tag.modType;
		}
	}

	/**
	 * The Class ChannelDataTag object that holds information pertaining to each
	 * valid channel.
	 */
	public static class ChannelDataTag {
		/**
		 * Is a 32-bit unsigned member which contains information on which cues
		 * have been fired since last power-up. The bits are arranged with bit 0
		 * representing cue 1, and bit 17 representing cue 18. A set (1) bit in
		 * a certain position indicates that the respective cue has been fired,
		 * either manually or from a script. On reset of the Cobra device, these
		 * bits are all cleared.
		 * 
		 * 
		 **/
		public long firedCues;

		/**
		 * Is a 32-bit unsigned member which contains information on which cues
		 * are listed in any of the stored scripts. The bits are arranged with
		 * bit 0 representing cue 1, and bit 17 representing cue 18. A set (1)
		 * bit in a certain position indicates that the respective cue is
		 * included in one or more of the scripts that are currently loaded, if
		 * any.
		 * 
		 * 
		 **/
		public long scriptCues;

		public int channel;

		/**
		 * Instantiates a new channel data tag.
		 * 
		 * @param firedCues
		 *            the fired cues
		 * @param scriptCues
		 *            the script cues
		 */
		public ChannelDataTag(int channel, long firedCues, long scriptCues) {
			this.channel = channel;
			this.firedCues = firedCues;
			this.scriptCues = scriptCues;
		}

		/**
		 * Copy constructor. Instantiates a new channel data tag.
		 * 
		 * @param tag
		 *            the tag
		 */
		public ChannelDataTag(ChannelDataTag tag) {
			this.firedCues = tag.firedCues;
			this.scriptCues = tag.scriptCues;
		}
	}

	/**
	 * The Class ScriptIndexTag object that holds information pertaining to each
	 * valid script.
	 */
	public static class ScriptIndexTag {

		/** The trigger button1. */
		public int triggerButton1;

		/** The trigger button2. */
		public int triggerButton2;

		/** The trigger channel. */
		public int triggerChannel;

		/** The end channel. */
		public int endChannel;
		/**
		 * Is the value of time-discrete events stored in the script. This value
		 * includes step events, and only counts one event for overlapping fires
		 * (fires on multiple cues or channels) occurring at the same time.
		 * 
		 **/
		public int numEvents;

		/** The audiobox enable. */
		// public Boolean audioboxEnable;
		/** NOT IMPLEMENTED. The audio filename. */
		public String audioFilename;

		/** The script name. */
		public String scriptName;

		/** The script id. */
		public int scriptID;

		public long scriptLength;

		/**
		 * Instantiates a new script index tag.
		 * 
		 * @param scriptID
		 *            the script id
		 * @param b1
		 *            the b1
		 * @param b2
		 *            the b2
		 * @param chan
		 *            the chan
		 * @param endChan
		 *            the end chan
		 * @param events
		 *            the events
		 * @param audioEn
		 *            the audio en
		 * @param audioFile
		 *            the audio file
		 * @param scriptName
		 *            the script name
		 */
		public ScriptIndexTag(int scriptID, int b1, int b2, int chan,
				int endChan, int events, long scriptLength, String audioFile,
				String scriptName) {
			this.scriptID = scriptID;
			this.triggerButton1 = b1;
			this.triggerButton2 = b2;
			this.triggerChannel = chan;
			this.endChannel = endChan;
			this.numEvents = events;
			this.scriptLength = scriptLength;
			// this.audioboxEnable = audioEn;
			this.audioFilename = audioFile;
			this.scriptName = scriptName;
		}

		/**
		 * Copy constructor. Instantiates a new script index tag.
		 * 
		 * @param tag
		 *            the tag
		 */
		public ScriptIndexTag(ScriptIndexTag tag) {
			this.scriptID = tag.scriptID;
			this.triggerButton1 = tag.triggerButton1;
			this.triggerButton2 = tag.triggerButton2;
			this.triggerChannel = tag.triggerChannel;
			this.endChannel = tag.endChannel;
			this.numEvents = tag.numEvents;
			// this.audioboxEnable = tag.audioboxEnable;
			this.scriptLength = tag.scriptLength;
			this.audioFilename = tag.audioFilename;
			this.scriptName = tag.scriptName;
		}
	}

	/**
	 * The Class EventDataTag containing event data.
	 */
	public static class EventDataTag {

		/** The script index. */
		public int scriptIndex;

		/** The event index. */
		public int eventIndex;

		/** The channel. */
		public int channel;

		/** The time index. */
		public long timeIndex;

		/** The cue list. */
		public long cueList;

		/** The shifted time index. */
		public long shiftedTimeIndex = 0;

		/** The event description. */
		public String eventDescription;

		/**
		 * Instantiates a new event data tag.
		 * 
		 * @param scriptIndex
		 *            the script index
		 * @param event
		 *            the event index
		 * @param timeIndex
		 *            the time index
		 * @param channel
		 *            the channel
		 * @param cueList
		 *            the cue list
		 * @param eventDesc
		 *            the event description
		 */
		public EventDataTag(int scriptIndex, int event, long timeIndex,
				int channel, long cueList, String eventDesc) {
			this.scriptIndex = scriptIndex;
			this.eventIndex = event;
			this.timeIndex = timeIndex;
			this.channel = channel;
			this.cueList = cueList;
			this.eventDescription = eventDesc;
		}

		/**
		 * Instantiates a new event data tag.
		 * 
		 * @param tag
		 *            the tag
		 */
		public EventDataTag(EventDataTag tag) {
			this.scriptIndex = tag.scriptIndex;
			this.eventIndex = tag.eventIndex;
			this.timeIndex = tag.timeIndex;
			this.channel = tag.channel;
			this.cueList = tag.cueList;
			this.shiftedTimeIndex = tag.shiftedTimeIndex;
			this.eventDescription = tag.eventDescription;
		}

		/**
		 * Sets the corrected time index.
		 * 
		 * @param time
		 *            the new corrected time index
		 */
		public void setCorrectedTimeIndex(long time) {
			shiftedTimeIndex = time;
		}
	}

	/**
	 * The Class ScriptPingTag.
	 */
	public static class ScriptPingTag {

		/** The script index. */
		public int scriptIndex;

		/** The event index. */
		public int eventIndex;

		/** The elapsed time. */
		public long elapsedTime;

		public int index;

		/**
		 * Instantiates a new script ping tag.
		 * 
		 * @param elapsedTime
		 *            the elapsed time
		 * @param scriptIndex
		 *            the script index
		 * @param eventIndex
		 *            the event index
		 */
		public ScriptPingTag(int index, long elapsedTime, int scriptIndex,
				int eventIndex) {
			this.index = index;
			this.scriptIndex = scriptIndex;
			this.eventIndex = eventIndex;
			this.elapsedTime = elapsedTime;
		}

		/**
		 * Instantiates a new script ping tag.
		 * 
		 * @param tag
		 *            the tag
		 */
		public ScriptPingTag(ScriptPingTag tag) {
			this.index = tag.index;
			this.scriptIndex = tag.scriptIndex;
			this.eventIndex = tag.eventIndex;
			this.elapsedTime = tag.elapsedTime;
		}
	}

	/**
	 * The Class MessageDataTag.
	 */
	protected static class MessageDataTag {

		/** The raw msg. */
		final int[] rawMsg;

		/** The command code. */
		final int commandCode;

		/** The total length. */
		final int totalLength;

		/** The msg number. */
		final int msgNumber;

		/** The total msgs. */
		final int totalMsgs;

		/** The data. */
		final int[] data;

		/** The checksum. */
		final int checksum;

		/** The check sum pass. */
		final boolean checkSumPass;

		/**
		 * Instantiates a new message data tag using the raw message data.
		 * 
		 * @param msg
		 *            the msg
		 */
		protected MessageDataTag(int[] msg) {
			rawMsg = msg;
			commandCode = msg[0];
			totalLength = msg[1];
			msgNumber = msg[2];
			totalMsgs = msg[3];
			data = new int[totalLength - 5];
			System.arraycopy(msg, 4, data, 0, totalLength - 5);
			checksum = msg[totalLength - 1];

			int tempChk = 0;
			for (int i = 0; i < totalLength - 1; i++)
				tempChk += msg[i];

			checkSumPass = ((tempChk & 0xFF) == checksum);
		}

		/**
		 * Gets the msg hex.
		 * 
		 * @return the msg hex
		 */
		String getMsgHex() {
			return SerialDriver.getHex(data);
		}
	}

	public static class ModuleLightsHandler {
		/** The module channel. */
		private int channel;

		/** cueStringList this is for highlighted cues **/
		private ArrayList<String> cueStringList;

		public ArrayList<String> getCueStringList() {
			return cueStringList;
		}

		public void setCueStringList(ArrayList<String> cueStringList) {
			this.cueStringList = cueStringList;
		}

		/** module red lights on specific channel */
		private long cueList = 0;

		public void setChannel(int channel) {
			this.channel = channel;
		}

		public int getChannel() {
			return this.channel;
		}

		public long getCueList() {
			return this.cueList;
		}

		public void setCueList(long cueList) {
			this.cueList = cueList;
		}

		public int eventIndex = -1;

		public ModuleLightsHandler(int channel, int eventIndex, long cueList,
				ArrayList<String> cueListString) {
			this.channel = channel;
			this.cueList = cueList;
			this.cueStringList = cueListString;
			this.eventIndex = eventIndex;
		}

		public void setThisCue(String thisCue) {
			if (thisCue == null)
				return;
			if (cueStringList == null)
				cueStringList = new ArrayList<String>();
			for (int i = 0; i < cueStringList.size(); i++) {
				if (cueStringList.get(i) != null
						&& cueStringList.get(i).equals(thisCue)) {
					return;
				}
			}
			cueStringList.add(thisCue);

		}
	}
}