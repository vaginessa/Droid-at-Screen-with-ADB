package com.ribomation.droidAtScreen.dev;

import com.android.ddmlib.IShellOutputReceiver;
import org.apache.log4j.Logger;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;

public abstract class BinaryDataReceiver implements IShellOutputReceiver {
	private static final Logger LOG = Logger.getLogger(BinaryDataReceiver.class);
	private byte[] totalData = new byte[]{};

	@Override
	public final void addOutput(byte[] data, int offset, int length) {
		if (!this.isCancelled()) {
			if (length < data.length) {
				data = Arrays.copyOfRange(data, 0, length);
			}
			totalData = concatenate(totalData, data);
		}
	}

	@Override
	public final void flush() {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		for (int i = 0; i < totalData.length; i++) {
			if (totalData.length > i + 1 && totalData[i] == 0x0d && totalData[i + 1] == 0x0a) {
				baos.write(0x0a);
				i += 1;
			} else {
				baos.write(totalData[i]);
			}
		}
		byte[] converted = baos.toByteArray();
		try {
			baos.close();
		} catch (IOException e) {
			LOG.error("Error closing ByteArrayOutputStream: " + e.getMessage());
		}
		this.processNewLines(converted);
	}

	@Override
	public boolean isCancelled() {
		return false;
	}

	public byte[] concatenate(byte[] a, byte[] b) {
		int aLen = a.length;
		int bLen = b.length;
		byte[] c = new byte[aLen + bLen];
		System.arraycopy(a, 0, c, 0, aLen);
		System.arraycopy(b, 0, c, aLen, bLen);
		return c;
	}

	public abstract void processNewLines(byte[] var1);
}

