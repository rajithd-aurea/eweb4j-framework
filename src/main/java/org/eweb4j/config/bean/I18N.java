package org.eweb4j.config.bean;

import java.util.ArrayList;
import java.util.List;

import org.eweb4j.cache.SingleBeanCache;

public class I18N {
	private List<Locale> locale = new ArrayList<Locale>();

	public List<Locale> getLocale() {
		return locale;
	}

	public void setLocale(List<Locale> locale) {
		this.locale = locale;
	}

	public static I18N get() {
		ConfigBean cb = (ConfigBean) SingleBeanCache.get(ConfigBean.class);
		org.eweb4j.config.bean.I18N i18n = cb.getLocales();

		return i18n;
	}

	public boolean contains(java.util.Locale locale) {
		for (Locale l : this.locale) {
			if (l.getLanguage().equals(l.getLanguage())) {
				return true;
			}
		}

		return false;
	}

	/**
	 * 
	 * @param str
	 *            e.g en | zh_CN
	 * @return
	 */
	public Locale get(String str) {
		for (Locale l : locale) {
			if (str.indexOf("_") > 0 && str.equals(l.toString())) {
				return l;
			} else if (!str.contains("_") && str.equals(l.getLanguage())) {
				return l;
			}
		}

		return locale.get(0);
	}

	@Override
	public String toString() {
		return "I18N [locale=" + locale + "]";
	}
}