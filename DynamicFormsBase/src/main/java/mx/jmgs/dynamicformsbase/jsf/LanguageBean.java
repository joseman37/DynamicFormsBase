package mx.jmgs.dynamicformsbase.jsf;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ValueChangeEvent;
import javax.faces.context.FacesContext;

@ManagedBean(name = "language")
@SessionScoped
public class LanguageBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private String localeCode;

	private static Map<String, Object> languages;
	
	static {
		languages = new LinkedHashMap<String, Object>();
		languages.put("English", Locale.ENGLISH); // label, value
		languages.put("Español", new Locale("es"));
	}
	
	@PostConstruct
	public void init() {
		localeCode = FacesContext.getCurrentInstance().getViewRoot().getLocale().getDisplayName();
	}

	public Map<String, Object> getCountriesInMap() {
		return languages;
	}

	public String getLocaleCode() {
		return localeCode;
	}

	public void setLocaleCode(String localeCode) {
		this.localeCode = localeCode;
	}

	// value change event listener
	public void countryLocaleCodeChanged(ValueChangeEvent e) {

		String newLocaleValue = e.getNewValue().toString();

		// loop country map to compare the locale code
		for (Map.Entry<String, Object> entry : languages.entrySet()) {

			if (entry.getValue().toString().equals(newLocaleValue)) {

				FacesContext.getCurrentInstance().getViewRoot()
						.setLocale((Locale) entry.getValue());

			}
		}
	}

}
