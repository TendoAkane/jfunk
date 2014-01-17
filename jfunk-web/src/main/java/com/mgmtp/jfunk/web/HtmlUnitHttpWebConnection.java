/*
 * Copyright (c) 2014 mgm technology partners GmbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.mgmtp.jfunk.web;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.LayeredSchemeSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.impl.client.AbstractHttpClient;

import com.gargoylesoftware.htmlunit.HttpWebConnection;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebConnection;
import com.mgmtp.jfunk.common.exception.JFunkException;
import com.mgmtp.jfunk.web.ssl.JFunkSSLSocketFactory;

/**
 * This {@link WebConnection} uses its own {@link LayeredSchemeSocketFactory} as the default
 * HttpClient implementation does not work with sites which require a client certificate.
 * 
 */
public class HtmlUnitHttpWebConnection extends HttpWebConnection {

	private final String keyStore;
	private final String keyStorePassword;
	private final String keyStoreType;
	private final String trustStore;
	private final String trustStorePassword;
	private final String trustStoreType;

	public HtmlUnitHttpWebConnection(final WebClient webClient, final HtmlUnitSSLParams sslParams) {
		super(webClient);
		this.keyStore = sslParams.getKeyStore();
		this.keyStorePassword = sslParams.getKeyStorePassword();
		this.keyStoreType = sslParams.getKeyStoreType();
		this.trustStore = sslParams.getTrustStore();
		this.trustStorePassword = sslParams.getTrustStorePassword();
		this.trustStoreType = sslParams.getTrustStoreType();
	}

	@Override
	protected AbstractHttpClient createHttpClient() {
		AbstractHttpClient httpClient = super.createHttpClient();

		URL keyStoreUrl = null;
		URL trustStoreUrl = null;
		try {
			if (StringUtils.isNotBlank(keyStore)) {
				keyStoreUrl = new File(keyStore).toURI().toURL();
			}
			if (StringUtils.isNotBlank(trustStore)) {
				trustStoreUrl = new File(trustStore).toURI().toURL();
			}
		} catch (MalformedURLException e) {
			throw new JFunkException("Could not construct URL from file", e);
		}

		LayeredSchemeSocketFactory socketFactory = new JFunkSSLSocketFactory(keyStoreUrl, keyStorePassword, keyStoreType, trustStoreUrl,
				trustStorePassword, trustStoreType);

		ClientConnectionManager ccm = httpClient.getConnectionManager();
		SchemeRegistry sr = ccm.getSchemeRegistry();
		sr.register(new Scheme("https", 443, socketFactory));

		return httpClient;
	}

	@Override
	protected synchronized AbstractHttpClient getHttpClient() {
		return super.getHttpClient();
	}
}