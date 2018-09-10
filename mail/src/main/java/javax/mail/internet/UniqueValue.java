/*
 * Copyright (c) 1997, 2018 Oracle and/or its affiliates. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v. 2.0, which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * This Source Code may also be made available under the following Secondary
 * Licenses when the conditions for such availability set forth in the
 * Eclipse Public License v. 2.0 are satisfied: GNU General Public License,
 * version 2 with the GNU Classpath Exception, which is available at
 * https://www.gnu.org/software/classpath/license.html.
 *
 * SPDX-License-Identifier: EPL-2.0 OR GPL-2.0 WITH Classpath-exception-2.0
 */

package javax.mail.internet;

import java.net.*;
import java.util.concurrent.atomic.AtomicInteger;
import javax.mail.Session;

/**
 * This is a utility class that generates unique values. The generated
 * String contains only US-ASCII characters and hence is safe for use
 * in RFC822 headers. <p>
 *
 * This is a package private class.
 *
 * @author John Mani
 * @author Max Spivak
 * @author Bill Shannon
 */

class UniqueValue {
    /**
     * A global unique number, to ensure uniqueness of generated strings.
     */
    private static AtomicInteger id = new AtomicInteger();

    /**
     * Get a unique value for use in a multipart boundary string.
     *
     * This implementation generates it by concatenating a global
     * part number, a newly created object's <code>hashCode()</code>,
     * and the current time (in milliseconds).
     */
    public static String getUniqueBoundaryValue() {
	StringBuilder s = new StringBuilder();
	long hash = s.hashCode();

	// Unique string is ----=_Part_<part>_<hashcode>.<currentTime>
	s.append("----=_Part_").append(id.getAndIncrement()).append("_").
	  append(hash).append('.').
	  append(System.currentTimeMillis());
	return s.toString();
    }

    /**
     * Get a unique value for use in a Message-ID.
     *
     * This implementation generates it by concatenating a newly
     * created object's <code>hashCode()</code>, a global ID
     * (incremented on every use), the current time (in milliseconds),
     * and the host name from this user's local address generated by 
     * <code>InternetAddress.getLocalAddress()</code>.
     * (The host name defaults to "localhost" if
     * <code>getLocalAddress()</code> returns null.)
     *
     * @param ssn Session object used to get the local address
     * @see javax.mail.internet.InternetAddress
     */
    public static String getUniqueMessageIDValue(Session ssn) {
	String suffix = null;

	InternetAddress addr = InternetAddress.getLocalAddress(ssn);
	if (addr != null)
	    suffix = addr.getAddress();
	else {
	    suffix = "javamailuser@localhost"; // worst-case default
	}
	int at = suffix.lastIndexOf('@');
	if (at >= 0)
	    suffix = suffix.substring(at);

	StringBuilder s = new StringBuilder();

	// Unique string is <hashcode>.<id>.<currentTime><suffix>
	s.append(s.hashCode()).append('.').
	  append(id.getAndIncrement()).append('.').
	  append(System.currentTimeMillis()).
	  append(suffix);
	return s.toString();
    }
}
