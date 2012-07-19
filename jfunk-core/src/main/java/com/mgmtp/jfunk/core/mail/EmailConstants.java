package com.mgmtp.jfunk.core.mail;

/**
 * @author rnaegele
 * @version $Id$
 */
public final class EmailConstants {

	/*
	 * Mail properties
	 */
	public static final String MAIL_PROTOCOL = "mail.protocol";
	public static final String MAIL_PROTOCOL_IMAP = "imap";
	public static final String MAIL_PROTOCOL_IMAPS = "imaps";
	public static final String MAIL_PROTOCOL_POP3 = "pop3";
	public static final String MAIL_DEBUG = "mail.debug";
	public static final String MAIL_USER = "mail.user";
	public static final String MAIL_PASSWORD = "mail.password";
	public static final String MAIL_ADDRESS = "mail.address";
	public static final String MAIL_ACCOUNT = "mail.account";
	public static final String MAIL_TRANSPORT_PROTOCOL = "mail.transport.protocol";
	public static final String MAIL_SMTP_HOST = "mail.smtp.host";
	public static final String MAIL_SMTP_PORT = "mail.smtp.port";
	public static final String MAIL_SMTP_AUTH = "mail.smtp.auth";
	public static final String MAIL_SMTP_STARTTLS_ENABLE = "mail.smtp.starttls.enable";

	// IMAP
	public static final String MAIL_IMAP_HOST = "mail.imap.host";
	public static final String MAIL_IMAP_PORT = "mail.imap.port";
	public static final String MAIL_IMAP_SOCKET_FACTORY_CLASS = "mail.imap.socketFactory.class";
	public static final String MAIL_IMAP_SOCKET_FACTORY_FALLBACK = "mail.imap.socketFactory.fallback";
	public static final String MAIL_IMAP_SOCKET_FACTORY_PORT = "mail.imap.socketFactory.port";
	public static final String MAIL_IMAP_FOLDER = "mail.imap.folder";

	// IMAPS
	public static final String MAIL_IMAPS_HOST = "mail.imaps.host";
	public static final String MAIL_IMAPS_PORT = "mail.imaps.port";
	public static final String MAIL_IMAPS_SOCKET_FACTORY_CLASS = "mail.imaps.socketFactory.class";
	public static final String MAIL_IMAPS_SOCKET_FACTORY_FALLBACK = "mail.imaps.socketFactory.fallback";
	public static final String MAIL_IMAPS_SOCKET_FACTORY_PORT = "mail.imaps.socketFactory.port";
	public static final String MAIL_IMAPS_FOLDER = "mail.imaps.folder";

	// POP3
	public static final String MAIL_POP3_HOST = "mail.pop3.host";
	public static final String MAIL_POP3_PORT = "mail.pop3.port";
	public static final String MAIL_POP3_SOCKET_FACTORY_CLASS = "mail.pop3.socketFactory.class";
	public static final String MAIL_POP3_SOCKET_FACTORY_FALLBACK = "mail.pop3.socketFactory.fallback";
	public static final String MAIL_POP3_SOCKET_FACTORY_PORT = "mail.pop3.socketFactory.port";

	public static final String MAIL_STARTUP_DELETE_ALL = "mail.startup.deleteAll";
	public static final String MAIL_SUBJECT_REGEX = "mail.subject.regex";
	public static final String MAIL_BODY_REGEX = "mail.body.regex";
	public static final String MAIL_SLEEP = "mail.sleep";
	public static final String MAIL_TIMEOUT = "mail.timeout";
	public static final String MAIL_CHECK_MAXIMAL = "mail.check.maximal";

	public static final String REPORT_MAIL_RECIPIENTS = "report_email.recipients";

	public static final String TESTING_EMAIL_ID = "testing.email.id";

	private EmailConstants() {
		// don't allow instantiation
	}
}