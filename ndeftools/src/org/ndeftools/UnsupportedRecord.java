/***************************************************************************
 * 
 * This file is part of the 'NDEF Tools for Android' project at
 * http://code.google.com/p/ndef-tools-for-android/
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 ****************************************************************************/

package org.ndeftools;

import java.util.Arrays;

import android.nfc.NdefRecord;

/**
 * A record which is not supported by this system and thus is handled on a byte-buffer level.
 * 
 * @author Thomas Rorvik Skjolberg (skjolber@gmail.com)
 * 
 */

public class UnsupportedRecord extends Record {

	public static UnsupportedRecord parse(NdefRecord ndefRecord) {
		return new UnsupportedRecord(ndefRecord.getTnf(), ndefRecord.getType(), ndefRecord.getId(), ndefRecord.getPayload());
	}
	
	private short tnf;
	
	/** An identifier that indicates the type of the payload. This specification supports URIs
	  *	[RFC 3986], MIME media type constructs [RFC 2616], as well as an NFC-specific
	  *	record type as type identifiers. 
	  */
	private byte[] type;
	
	/** The application data carried within an NDEF record. */
	private byte[] payload;

	public UnsupportedRecord(short tnf, byte[] type, byte[] id, byte[] payload) {
		this.tnf = tnf;
		this.type = type;
		this.id = id;
		this.payload = payload;
	}
	
	public UnsupportedRecord(NdefRecord record) {
		this(record.getTnf(), record.getType(), record.getId(), record.getPayload());
	}

	public short getTnf() {
		return tnf;
	}

	public void setTnf(short tnf) {
		this.tnf = tnf;
	}

	public byte[] getType() {
		return type;
	}

	public void setType(byte[] type) {
		this.type = type;
	}

	public byte[] getPayload() {
		return payload;
	}

	public void setPayload(byte[] payload) {
		this.payload = payload;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Arrays.hashCode(payload);
		result = prime * result + tnf;
		result = prime * result + Arrays.hashCode(type);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		UnsupportedRecord other = (UnsupportedRecord) obj;
		if (!Arrays.equals(payload, other.payload))
			return false;
		if (tnf != other.tnf)
			return false;
		if (!Arrays.equals(type, other.type))
			return false;
		return true;
	}

	@Override
	public NdefRecord getNdefRecord() {
		return new NdefRecord(tnf, type != null ? type : EMPTY, id != null ? id : EMPTY, payload != null ? payload : EMPTY);
	}

}
