package com.cobra.api;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EventListener;
import java.util.EventObject;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Semaphore;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbConstants;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbEndpoint;
import android.hardware.usb.UsbInterface;
import android.hardware.usb.UsbManager;
import android.hardware.usb.UsbRequest;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import com.cobra.appClass;
import com.cobra.interfaces.OnEventChange;

public class SerialDriver {
	protected static String TAG;

	public final static int SERIAL_CODE_NODEVICE = 0x1;
	public final static int SERIAL_CODE_NOPERM = 0x2;

	private UsbManager manager;
	private UsbDeviceConnection connection;
	private UsbDevice device;
	private UsbEndpoint recvEP, sendEP;
	private UsbInterface dataIface;
	private UsbRequest recvRequest;

	private final List<ISerialPortWListener> listenerList;

	private static Thread recvThread;

	private static Thread sendThread;
	private ByteBuffer readBuffer;

	private static ConcurrentLinkedQueue<byte[]> writeBuffer;
	private static Semaphore semWrite;
	private static boolean isOpen = false;
	public appClass globV;
	static Context context;

	public SerialDriver(Context context, ISerialPortWListener listener) {
		this.context = context;
		TAG = SerialDriver.class.getName();
		// Cobra.unregisterListener = unRegisterListener;

		appClass.onEventChange = onEventChange;
		this.listenerList = new ArrayList<ISerialPortWListener>();
		this.addEventListener(listener);
		this.manager = (UsbManager) context
				.getSystemService(Context.USB_SERVICE);
		this.readBuffer = ByteBuffer.allocate(50);
		device = SerialProbe.probeForDevice(context);
		SerialDriver.writeBuffer = new ConcurrentLinkedQueue<byte[]>();
		SerialDriver.semWrite = new Semaphore(0);

		Log.i(TAG, "Serial Init Complete");
	}

	public int open() {
		if (device == null) {
			return SERIAL_CODE_NODEVICE;
		}

		if (this.manager.hasPermission(device) == false) {
			return SERIAL_CODE_NOPERM;
		}

		connection = this.manager.openDevice(device);
		if (!connection.claimInterface(device.getInterface(0), true))
			Log.e(TAG, "Could not claim control interface");

		// initialize CDC
		int ret = 0;
		int requestType = UsbConstants.USB_TYPE_CLASS | 0x01;
		ret = connection.controlTransfer(requestType, 0x22, 0x0, 0, null, 0,
				1000);
		int baudRate = 115200;
		byte[] msg = { (byte) (baudRate & 0xff),
				(byte) ((baudRate >> 8) & 0xff),
				(byte) ((baudRate >> 16) & 0xff),
				(byte) ((baudRate >> 24) & 0xff), 0, 0, (byte) 0x08 };
		// ArrayList<UsbEndpoint> allEndPoints = getAllEndPoints(this.device);
		// for (int i = 0; i < allEndPoints.size(); i++) {
		// int rep = this.connection.bulkTransfer(allEndPoints.get(i), msg,
		// msg.length, 1000);
		// rep += 0;
		// }

		// baud 921600; 8-N-1
		ret = connection.controlTransfer(UsbConstants.USB_TYPE_CLASS | 0x01,
				0x20, 0, 0, msg, msg.length, 1000);
		// set DTR
		ret = connection.controlTransfer(UsbConstants.USB_TYPE_CLASS | 0x01,
				0x22, 0x1, 0, null, 0, 1000);

		if (ret < 0) {
			Log.e(TAG,
					"USB Control Transfer Error: Failed to initialize device!");
			return ret;
		}

		this.dataIface = device.getInterface(1);
		Log.i(TAG, "DataEP Count: " + this.dataIface.getEndpointCount());

		if (!connection.claimInterface(dataIface, true))
			Log.e(TAG, "Could not claim data interface");

		this.recvEP = this.dataIface.getEndpoint(0);
		Log.i(TAG,
				"RECVDir: "
						+ ((this.recvEP.getDirection() == UsbConstants.USB_DIR_IN) ? "IN"
								: "ERR"));
		this.sendEP = this.dataIface.getEndpoint(1);
		Log.i(TAG,
				"SENDDir: "
						+ ((this.sendEP.getDirection() == UsbConstants.USB_DIR_OUT) ? "OUT"
								: "ERR"));

		this.recvRequest = new UsbRequest();
		if (!this.recvRequest.initialize(connection, this.recvEP))
			Log.e(TAG, "USB Request init failed!");

		SerialDriver.recvThread = new Thread(reader);
		recvThread.start();

		SerialDriver.sendThread = new Thread(writer);
		sendThread.start();

		SerialDriver.isOpen = true;

		return 0;
	}

	private ArrayList<UsbEndpoint> getAllEndPoints(UsbDevice device) {
		ArrayList<UsbEndpoint> allEnds = new ArrayList<UsbEndpoint>();
		for (int i = 0; i < device.getInterfaceCount(); i++) {
			for (int j = 0; j < device.getInterface(i).getEndpointCount(); j++) {
				UsbEndpoint endPoint = device.getInterface(i).getEndpoint(j);
				allEnds.add(endPoint);
			}

		}
		return allEnds;
	}

	public static String getHex(byte[] data) {
		StringBuilder str = new StringBuilder();
		for (int i = 0; i < data.length; i++) {
			str.append(String.format("%02X ", (data[i] & 0x0FF)));
		}
		return str.toString();
	}

	public static String getHex(int[] data) {
		StringBuilder str = new StringBuilder();
		for (int i = 0; i < data.length; i++) {
			str.append(String.format("%02X ", data[i]));
		}
		return str.toString();
	}

	public static void write(byte[] msg) {
		Boolean result = writeBuffer.add(msg);

		semWrite.release();
	}

	private Runnable writer = new Runnable() {
		@Override
		public void run() {
			while (true) {
				try {
					semWrite.tryAcquire();
					// semWrite.acquire();
				} catch (Exception e) {
					// TODO: handle exception
					int i = 0;
					i++;
				}

				if (sendThread.isInterrupted()) {
					return;
				}
				// if(appClass.BreakExecutor)
				// return;

				byte[] msg = writeBuffer.poll();
				if (msg == null) {
					Log.e(TAG, "Bad token! No message in buffer!");
					continue;
				}
				String message = "";
				for (int i = 0; i < msg.length; i++) {
					message += "  " + (msg[i] & 0xFF);// +
														// Integer.toHexString(msg[i]);
				}

				Spanned text;
				if (appClass.IsCommandPrompt_DECIMAL)
					text = Html
							.fromHtml("<b>--------Writing----------:</b><br>"
									+ message + "<br>");
				else
					text = Html
							.fromHtml("<b>--------Writing----------:</b><br>"
									+ getHex(msg) + "<br>");
				Cobra.fireCommandEventLister(text);
				// TestCommands.writing="--> Writing: "+message+"\n ";
				// appClass.createSerialportLogOnDevice(false, message);
				try {
					int len = connection.bulkTransfer(sendEP, msg, msg.length,
							0);
					if (message.contains(""
							+ CobraCommands.VCOM_REQ_QUEUED_MODULE_DATA)) {
						appClass.setAN106Logs(System.currentTimeMillis()
								+ " Command is written: " + message);
					}
					// appClass.createSerialportLogOnDevice(false,
					// "RESPONSE AFTER SENDING TO 18R2: " + len);

					if (len != msg.length)
						Log.e(TAG, "Write error: ML-" + msg.length + " L-"
								+ len);
					Log.i(TAG, String.format("WRITE_MSG -- W:%d Msg: %s",
							msg.length, getHex(msg)));
				} catch (NullPointerException e) {

				} catch (Exception e) {

				}
			}
		}
	};
	//
	protected Runnable reader = new Runnable() {
		@Override
		public void run() {
			try {
				Log.i(TAG, "Serial Recv task initialized.");
				while (true) {
					int length;

					if (recvRequest == null || connection == null
							|| recvThread.isInterrupted()) {
						return;
					}
					recvRequest.queue(readBuffer, 50);
					UsbRequest reqResult = connection.requestWait();
					// if(appClass.BreakExecutor)
					// return;

					if (reqResult == recvRequest) {
						byte[] read = readBuffer.array();
						length = read[1] & 0xFF;

						if (length < 5) {
							Log.e(TAG,
									"MSG ERROR TOO SHORT: "
											+ getHex(Arrays.copyOfRange(read,
													0, length)));
							continue;
						}

						read = Arrays.copyOfRange(read, 0, length);
						String hex = getHex(read);
						Log.v(TAG, String.format(
								"READ_MSG RAW -- R:%d Msg: %s", read.length,
								hex));
						appClass.IsConnectionEstablished = true;
						fireSerialPortWEvent(new SerialPortWEvent(read));
						// }

					} else if (reqResult == null) {
						// Log.wtf(TAG, "Null?!?");
					}
				}
			} catch (NullPointerException e) {

			} catch (Exception e) {
			}
		}
	};

	protected static byte calculateChecksum(byte[] msg) {
		byte checksum = 0;

		for (int i = 0; i < msg.length; i++)
			checksum += msg[i];

		return checksum;
	}

	OnEventChange onEventChange = new OnEventChange() {
		@Override
		public void onEventChange() {
			// TODO Auto-generated method stub
			try {
				Log.i("com.cobra.SerialDriver", "Close");
				close();
			} catch (Exception e) {
				Log.i("UnregisterException", "" + e.getMessage());
			}
		}
	};

	// UnRegisterListeners unRegisterListener = new UnRegisterListeners() {
	// @Override
	// public void Unregister() {
	// // TODO Auto-generated method stub
	//
	// }
	// };

	public void close() {
		Log.i(TAG, "Disconnecting");
		if (sendThread != null)
			sendThread.interrupt();
		if (recvThread != null)
			recvThread.interrupt();
		// try {
		// sendThread.join();
		// recvThread.join();
		//
		// } catch (InterruptedException e) {
		// Log.e(TAG, e.getMessage());
		// }

		if (recvRequest != null) {
			recvRequest.cancel();
			Log.i("com.cobra.SerialDriver",
					"Cancelled Reciever Pending Requests");
			recvRequest.close();
			Log.i("com.cobra.SerialDriver", "Closed Reciever Request");
		}

		int ret = 0;

		// reset CDC
		if (connection != null)
			ret = connection.controlTransfer(
					UsbConstants.USB_TYPE_CLASS | 0x01, 0x22, 0x0, 0, null, 0,
					1000);
		Log.i("com.cobra.SerialDriver",
				"USBDeviceConnection Control Transfered");
		if (ret < 0)
			Log.e(TAG, "USB Control Transfer Error: Failed to reset device!");

		if (connection != null)
			connection.close();

		if(connection !=null)
		if (!connection.releaseInterface(device.getInterface(0)))
			Log.e(TAG, "USB failed to release interface0");
		if(connection !=null)
		if (!connection.releaseInterface(device.getInterface(1)))
			Log.e(TAG, "USB failed to release interface1");

		Log.i("com.cobra.SerialDriver", "Closed USBDeviceConnection");
		manager = null;
		Log.i("manager", "NULL");
		recvEP = null;
		Log.i("recvEP", "NULL");
		sendEP = null;
		Log.i("sendEP", "NULL");
		dataIface = null;
		Log.i("dataIface", "NULL");
		device = null;
		Log.i("device", "NULL");

		isOpen = false;
	}

	public static boolean isOpen() {
		return isOpen;
	}

	public class SerialPortWEvent extends EventObject {
		private static final long serialVersionUID = 1L;
		private byte[] data;

		public SerialPortWEvent(byte[] data) {
			super(data);
			this.data = data;
		}

		public byte[] getSerialData() {
			return this.data;
		}
	}

	public static interface ISerialPortWListener extends EventListener {
		public void onSerialRead(SerialPortWEvent event);
	}

	public void addEventListener(ISerialPortWListener listener) {
		this.listenerList.add(listener);
	}

	public void removeEventListener(ISerialPortWListener listener) {
		this.listenerList.remove(listener);
	}

	protected void fireSerialPortWEvent(SerialPortWEvent event) {
		for (ISerialPortWListener l : this.listenerList)
			l.onSerialRead(event);
	}

	public static class SerialProbe {
		private static final String TAG = "SerialTerm.SerialProbe.Class";
		private static final String ACTION_USB_PERMISSION = "com.cobra.api.serialprobe.USB_PERMISSION";

		private static final int VID = 9476;
		private static final int PID = 768;

		public static final int NO_DEVICE = 0x1;
		public static final int NO_PERMISSION = 0x2;

		private static final List<SerialProbeListener> listenerList = new ArrayList<SerialProbeListener>(); // TODO:
																											// Removed
																											// ;

		public static int requestPermission(Context context,
				SerialProbeListener listener) {
			int perm;

			perm = checkPermission(context);
			if (perm != NO_PERMISSION)
				return perm;

			listenerList.add(listener);
			UsbManager manager = (UsbManager) context
					.getSystemService(Context.USB_SERVICE);
			UsbDevice device = probeForDevice(context);

			PendingIntent mPermissionIntent = PendingIntent.getBroadcast(
					context, 0, new Intent(ACTION_USB_PERMISSION), 0);
			IntentFilter filter = new IntentFilter(ACTION_USB_PERMISSION);
			context.registerReceiver(mUsbReceiver, filter);

			manager.requestPermission(device, mPermissionIntent);

			return 0;
		}

		private static final BroadcastReceiver mUsbReceiver = new BroadcastReceiver() {
			public void onReceive(Context context, Intent intent) {
				String action = intent.getAction();
				if (ACTION_USB_PERMISSION.equals(action)) {
					synchronized (this) {
						UsbDevice device = (UsbDevice) intent
								.getParcelableExtra(UsbManager.EXTRA_DEVICE);

						if (device == null) {
							Log.d(TAG, "Device is null");
							return;
						}

						boolean perm = intent.getBooleanExtra(
								UsbManager.EXTRA_PERMISSION_GRANTED, false);
						// context.unregisterReceiver(mUsbReceiver); Not testes,
						// prevents the receiver from leaking
						fireSerialProbeEvent(new SerialProbeEvent(this, device,
								perm));
					}
				}
			}
		};

		public static UsbDevice probeForDevice(Context context) {
			UsbManager manager = (UsbManager) context
					.getSystemService(Context.USB_SERVICE);
			List<UsbDevice> devices = new ArrayList<UsbDevice>(manager
					.getDeviceList().values());

			for (UsbDevice d : devices) {
				if (d.getVendorId() == VID && d.getProductId() == PID)
					return d;
			}

			return null;
		}

		public static int checkPermission(Context context) {
			UsbManager manager = (UsbManager) context
					.getSystemService(Context.USB_SERVICE);
			UsbDevice device = probeForDevice(context);
			if (device == null) {
				return NO_DEVICE;
			} else if (!manager.hasPermission(device)) {
				return NO_PERMISSION;
			}
			return 0;
		}

		public static class SerialProbeEvent extends EventObject {
			private static final long serialVersionUID = 1L;
			private boolean hasPermission = false;
			private UsbDevice device;

			public SerialProbeEvent(Object source, UsbDevice device,
					boolean hasPermission) {
				super(source);
				this.device = device;
				this.hasPermission = hasPermission;
			}

			public UsbDevice getUsbDevice() {
				return this.device;
			}

			public boolean hasPermission() {
				return this.hasPermission;
			}
		}

		public static void clearSerialProbeListenerList() {
			try {
				listenerList.clear();
			} catch (Exception e) {
			}
		}

		public static interface SerialProbeListener extends EventListener {
			public void onPermissionRequestComplete(SerialProbeEvent event);
		}

		private static void fireSerialProbeEvent(SerialProbeEvent event) {
			for (SerialProbeListener l : listenerList)
				l.onPermissionRequestComplete(event);
		}

	}
}