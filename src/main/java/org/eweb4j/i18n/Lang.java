package org.eweb4j.i18n;

import java.util.List;
import java.util.Locale;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eweb4j.config.ConfigConstant;
import org.eweb4j.config.Log;
import org.eweb4j.config.LogFactory;
import org.eweb4j.config.bean.I18N;
import org.eweb4j.mvc.Context;
import org.eweb4j.mvc.MVC;
import org.eweb4j.mvc.MVCCons;


/**
 * EWeb4j 国际化支持
 * @author weiwei
 *
 */
public class Lang {

	private static Log log = LogFactory.getMVCLogger(Lang.class);
	
	private static ThreadLocal<Locale> current = new ThreadLocal<Locale>();
	
	public static Locale get(){
		
		Locale locale = current.get();
		if (locale == null){
			Context ctx = MVC.ctx();
			if (ctx != null){
				HttpServletRequest req = ctx.getRequest();
				HttpServletResponse res = ctx.getResponse();
				if (req != null){
					resolve(req, res);
				}else{
					setDefaultLocale();
				}
			}else{
				setDefaultLocale();
			}
			
			locale = current.get();
		}
		
		return locale;
	}
	
	private static void resolve(HttpServletRequest req, HttpServletResponse res) {
		Cookie[] cookies = req.getCookies();
		if (cookies != null){
			for (Cookie cookie : cookies){
				if (MVCCons.COOKIE_KEY_LOCALE.equals(cookie.getName())){
					String loc = cookie.getValue();
					if (loc == null)
						continue;
					
					if (loc.indexOf("_") > 0){
						String[] locs = loc.split("_");
						if (set(new Locale(locs[0], locs[1]))){
							return ;
						}
					}
					
					if (set(new Locale(loc)))
						return;
				}
			}
		}
		
		Locale locale = req.getLocale();
		
		set(locale);
		
		res.addCookie(new Cookie(MVCCons.COOKIE_KEY_LOCALE, locale.toString()));
	}

	public static boolean set(Locale locale){
		if (I18N.get().contains(locale)){
			log.debug("Locale is set -> " + locale.toString());
			current.set(locale);
			return true;
		}
		
		log.warn("Locale " + locale + " is not defined in your " + ConfigConstant.START_FILE_NAME +" > i18n");
		return false;
	}
	
	public static void clear(){
		current.remove();
	}
	
	public static void change(Locale locale){
		if (get() == null){
			if (set(locale))
				MVC.ctx().getResponse().addCookie(new Cookie(MVCCons.COOKIE_KEY_LOCALE, locale.toString()));
		}else{
			if (!get().equals(locale)){
				if (set(locale))
					MVC.ctx().getResponse().addCookie(new Cookie(MVCCons.COOKIE_KEY_LOCALE, locale.toString()));
			}
		}
	}
	
	private static void setDefaultLocale() {
		List<org.eweb4j.config.bean.Locale> locales = I18N.get().getLocale();
		if (locales == null || locales.isEmpty()){
			set(Locale.getDefault());
		} else{
			set(new Locale(locales.get(0).getLanguage(), locales.get(0).getCountry()));
		}
	}
}
