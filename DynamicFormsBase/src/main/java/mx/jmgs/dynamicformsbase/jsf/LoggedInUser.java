package mx.jmgs.dynamicformsbase.jsf;


import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIOutput;
import javax.faces.event.AjaxBehaviorEvent;

@Named("loggedInUser")
@SessionScoped
public class LoggedInUser implements Serializable {

    private Map<String, String> preferences = null;
    

    public LoggedInUser() {
        preferences = new HashMap<>();
        preferences.put("theme", "bootstrap");
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
