package com.mgmtp.jfunk.core.mail;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.inject.Singleton;

import org.apache.log4j.Logger;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mgmtp.jfunk.common.exception.JFunkException;
import com.mgmtp.jfunk.common.util.Configuration;
import com.mgmtp.jfunk.common.util.Disposable;
import com.mgmtp.jfunk.common.util.ExtendedProperties;
import com.mgmtp.jfunk.common.util.Predicates;

/**
 * Default-{@link MailHandler}-Implementation for retrieving mail accounts from the configuration.
 * 
 * @version $Id$
 */
@Singleton
public class DefaultMailHandler implements MailHandler, Disposable {

	private static final String MAIL_ACCOUNT_PREFIX = "mail.account.";

	private final Logger log = Logger.getLogger(getClass());
	private final Map<String, Thread> usedAccounts = Maps.newHashMap();
	private final ReentrantLock lock = new ReentrantLock();
	private final Condition condition = lock.newCondition();

	@Override
	public MailAccount getMailAccount(final Configuration config) {
		String accountId = config.get(EmailConstants.TESTING_EMAIL_ID);

		List<MailAccount> accounts;
		if (accountId != null && accountId.length() > 0) {
			log.info("Trying to use fixed e-mail account: " + accountId);
			String user = config.get("mail.user");
			String password = config.get("mail.password");
			accounts = Lists.newArrayList(new MailAccount(accountId, user, password));
		} else {
			log.info("Trying to get free e-mail account.");
			accounts = getAccounts(config);
			Collections.shuffle(accounts);
		}

		lock.lock();
		try {
			while (true) {
				MailAccount account = null;
				// Try to find a free account.
				for (MailAccount acc : accounts) {
					Thread thread = usedAccounts.get(acc.getAccountId());
					if (thread == null || thread == Thread.currentThread()) {
						// If the account is not used or the current thread already owns it, then use it.
						account = acc;
						break;
					}
				}

				if (account == null) {
					// No free account available. We wait and then start over with the loop.
					log.info("No free e-mail account available. Waiting...");
					condition.await();
				} else {
					// We've found a free account and return it.
					accountId = account.getAccountId();
					log.info("Found free e-mail account: " + accountId);

					usedAccounts.put(accountId, Thread.currentThread());
					config.put(EmailConstants.TESTING_EMAIL_ID, accountId);
					return account;
				}
			}
		} catch (InterruptedException ex) {
			Thread.currentThread().interrupt();
			throw new JFunkException(ex.getMessage(), ex);
		} finally {
			lock.unlock();
		}
	}

	@Override
	public void releaseAllMailAccounts() {
		lock.lock();
		try {
			usedAccounts.clear();
			condition.signalAll();
		} finally {
			lock.unlock();
		}
	}

	/**
	 * Calls {@link #releaseAllMailAccountsForThread()}.
	 */
	@Override
	public void dispose() {
		releaseAllMailAccountsForThread();
	}

	@Override
	public void releaseAllMailAccountsForThread() {
		lock.lock();
		try {
			for (Iterator<Entry<String, Thread>> it = usedAccounts.entrySet().iterator(); it.hasNext();) {
				if (it.next().getValue().equals(Thread.currentThread())) {
					it.remove();
				}
			}
			condition.signalAll();
		} finally {
			lock.unlock();
		}
	}

	@Override
	public void releaseMailAccount(final String accountId) {
		lock.lock();
		try {
			usedAccounts.remove(accountId);
			condition.signal();
		} finally {
			lock.unlock();
		}
	}

	private static List<MailAccount> getAccounts(final ExtendedProperties props) {
		ExtendedProperties properties = props.clone();

		Map<String, String> view = Maps.filterKeys(props, Predicates.startsWithPredicate(MAIL_ACCOUNT_PREFIX));

		Pattern pattern = Pattern.compile("mail\\.account.(.+)(\\.user|\\.password|\\.address)");

		List<MailAccount> accounts = Lists.newArrayList();
		for (String accountKey : view.keySet()) {
			Matcher m = pattern.matcher(accountKey);

			String accountId = m.matches() ? m.group(1) : accountKey.substring(MAIL_ACCOUNT_PREFIX.length());
			properties.put(EmailConstants.TESTING_EMAIL_ID, accountId);

			String user = properties.get(EmailConstants.MAIL_USER);
			String password = properties.get(EmailConstants.MAIL_PASSWORD);

			accounts.add(new MailAccount(accountId, user, password));
		}
		return accounts;
	}
}