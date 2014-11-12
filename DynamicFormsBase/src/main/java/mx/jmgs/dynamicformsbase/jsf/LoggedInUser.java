package mx.jmgs.dynamicformsbase.jsf;


import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIOutput;
import javax.faces.event.AjaxBehaviorEvent;

/**
 * 
 * @author pp
 */
//@Named("loggedInUser")
@ManagedBean(name = "loggedInUser")
@SessionScoped
public class LoggedInUser implements Serializable {

    /**
	 * Helps serialization.
	 */
	private static final long serialVersionUID = 1L;
	
	private Map<String, String> preferences = null;

    public LoggedInUser() {
        preferences = new HashMap<>();
        preferences.put("theme", "aristo");
    }
    
    public Map<String, String> getPreferences() {
        return preferences;
    }

    public void setPreferences(Map<String, String> preferences) {
        this.preferences = preferences;
    }
    
    public void saveTheme(AjaxBehaviorEvent event) {
        String name= (String) ((UIOutput) event.getSource()).getValue();
        System.out.println(name);
        preferences.put("theme", name);
    }

}
