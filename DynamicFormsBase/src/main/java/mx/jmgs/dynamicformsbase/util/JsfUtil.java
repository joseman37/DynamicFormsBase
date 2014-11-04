package mx.jmgs.dynamicformsbase.util;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Locale;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.model.SelectItem;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import mx.jmgs.dynamicformsbase.singleton.JsfConfiguration;


public class JsfUtil {

    public static SelectItem[] getSelectItems(List<?> entities, boolean selectOne) {
        int size = selectOne ? entities.size() + 1 : entities.size();
        SelectItem[] items = new SelectItem[size];
        int i = 0;
        if (selectOne) {
            items[0] = new SelectItem("", "---");
            i++;
        }
        for (Object x : entities) {
            items[i++] = new SelectItem(x, x.toString());
        }
        return items;
    }

    public static boolean isValidationFailed() {
        return FacesContext.getCurrentInstance().isValidationFailed();
    }

    public static void addErrorMessage(Exception ex, String defaultMsg) {
        String msg = ex.getLocalizedMessage();
        if (msg != null && msg.length() > 0) {
            addErrorMessage(msg);
        } else {
            addErrorMessage(defaultMsg);
        }
    }

    public static void addErrorMessages(List<String> messages) {
        for (String message : messages) {
            addErrorMessage(message);
        }
    }

    public static void addErrorMessage(String msg) {
        FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, msg);
        FacesContext.getCurrentInstance().addMessage(null, facesMsg);
    }

    public static void addSuccessMessage(String msg) {
        FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_INFO, msg, msg);
        FacesContext.getCurrentInstance().addMessage("successInfo", facesMsg);
    }

    public static String getRequestParameter(String key) {
        return FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get(key);
    }

    public static Object getObjectFromRequestParameter(String requestParameterName, Converter converter, UIComponent component) {
        String theId = JsfUtil.getRequestParameter(requestParameterName);
        return converter.getAsObject(FacesContext.getCurrentInstance(), component, theId);
    }
    
    public static Locale getLocale() {
    	return FacesContext.getCurrentInstance().getViewRoot().getLocale();
    }
    
    public static void fordward(String url) {
    	FacesContext.getCurrentInstance().responseComplete();
    	if(!url.startsWith("/")) {
			throw new RuntimeException("invalid relative url, it should start with /");
		}
		try {
			FacesContext.getCurrentInstance().getExternalContext().dispatch(url);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
    }
    
    public static void redirect(String url) {
    	redirect(url, true);
    }
    
    public static void redirect(String url, boolean relative) {
    	FacesContext.getCurrentInstance().responseComplete();
    	if(relative){
    		if(!url.startsWith("/")) {
    			throw new RuntimeException("invalid relative url, it should start with /");
    		}
    		//TODO
    		String contextPath = FacesContext.getCurrentInstance().getExternalContext().getContextName();
    		System.out.println("Context path:" + contextPath);
    		
    		contextPath = ((ServletContext)FacesContext.getCurrentInstance().getExternalContext().getContext()).getContextPath();
    		System.out.println("Context path 2:" + contextPath);
    		url = contextPath + url;
    	}
    	
		try {
			FacesContext.getCurrentInstance().getExternalContext().redirect(url);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
    }
    
    @SuppressWarnings("unchecked")
	public static <T>T getRequestAttribute(String attribute) {
    	HttpServletRequest req =(HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
    	return (T)req.getAttribute(attribute);
    }
    
    @SuppressWarnings("unchecked")
    /**
     * Method that takes a bean field of an object or interface given the field's type.
     * @param obj
     * @param getterName
     * @return
     * @throws SecurityException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     */
	public static <T> T callBeanGetter(Object obj,  String getterName) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, IllegalArgumentException {
    	Class<? extends Object> aClass = obj.getClass();
    	
    	//OBJECT FIELDS
    	//java.lang.reflect.Field field = aClass.getDeclaredField(getterName);
    	//Object value = field.get(obj);
    	
    	//METHOD
    	Method method = aClass.getMethod(getterName);
        Object value = method.invoke(obj, (Object[]) null);
    	
    	return (T)value;
    }

    public static enum PersistAction {

        CREATE,
        DELETE,
        UPDATE
    }
}
