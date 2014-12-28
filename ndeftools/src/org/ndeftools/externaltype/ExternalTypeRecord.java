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

package org.ndeftools.externaltype;

import java.nio.charset.Charset;
import java.util.Locale;

import org.ndeftools.Record;

import android.annotation.SuppressLint;
import android.nfc.NdefRecord;

/**
 * External type record.<br/><br/>
 * 
 * @author Thomas Rorvik Skjolberg (skjolber@gmail.com)
 *
 */

public abstract class ExternalTypeRecord extends Record {

	public static ExternalTypeRecord parse(NdefRecord ndefRecord) {
		String domainType = new String(ndefRecord.getType(), Charset.forName("UTF-8"));
		
		int colon = domainType.lastIndexOf(':');
		
		String type;
		String domain;
		if(colon == -1) {
			domain = domainType;
			type = null;
		} else {
			domain = domainType.substring(0, colon);
			if(colon + 1 < domainType.length()) {
				type = domainType.substring(colon + 1);
			} else {
				type = "";
			}
		}

		if(domain.equals(AndroidApplicationRecord.DOMAIN) && type.equals(AndroidApplicationRecord.TYPE)) {
			return new AndroidApplicationRecord(ndefRecord.getPayload());
		}
		
		return new GenericExternalTypeRecord(domain, type, ndefRecord.getPayload());
	}
	
	public ExternalTypeRecord() {
	}
	
	/**
	 * Get the domain
	 * 
	 * @return domain
	 */

	public abstract String getDomain();
	
	/**
	 * Get the type (relevant under the domain)
	 * 
	 * @return type
	 */

	public abstract String getType();

	/**
	 * Get the domain type data.
	 * 
	 * @return data bytes
	 */
	
	public abstract byte[] getData();

	@SuppressLint("NewApi")
	@Override
	public NdefRecord getNdefRecord() {
		if (android.os.Build.VERSION.SDK_INT >= 16) {
			return NdefRecord.createExternal(getDomain(), getType(), getData());
		} else {
			return createExternal(getDomain(), getType(), getData());
		}
	}	
	
	@Deprecated
    private NdefRecord createExternal(String domain, String type, byte[] data) {
        if (domain == null) throw new NullPointerException("domain is null");
        if (type == null) throw new NullPointerException("type is null");

        domain = domain.trim().toLowerCase(Locale.US);
        type = type.trim().toLowerCase(Locale.US);

        if (domain.length() == 0) throw new IllegalArgumentException("domain is empty");
        if (type.length() == 0) throw new IllegalArgumentException("type is empty");

        byte[] byteDomain = domain.getBytes(Charset.forName("UTF_8"));
        byte[] byteType = type.getBytes(Charset.forName("UTF_8"));
        byte[] b = new byte[byteDomain.length + 1 + byteType.length];
        System.arraycopy(byteDomain, 0, b, 0, byteDomain.length);
        b[byteDomain.length] = ':';
        System.arraycopy(byteType, 0, b, byteDomain.length + 1, byteType.length);

        // external type id must be empty
        return new NdefRecord(NdefRecord.TNF_EXTERNAL_TYPE, b, EMPTY, data != null ? data : EMPTY);
    }
	
}
