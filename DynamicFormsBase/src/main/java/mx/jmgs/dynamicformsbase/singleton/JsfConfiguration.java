package mx.jmgs.dynamicformsbase.singleton;

/**
 * 
 * JSF configuration properties.
 * 
 * @author jgonzalez
 *
 */
public class JsfConfiguration {
	
	public static final String INDEX_URL = "/index.xhtml";
	
	public static final String FORM_URL = "/dynamicForm/form.xhtml";
	
	public static final String DYNAMIC_URL = "/dynamicForm/form";
	
	private static JsfConfiguration instance = new JsfConfiguration();
	
	/**
	 * private constructor.
	 */
	private JsfConfiguration() {
		
	}
	
	public static JsfConfiguration getInstace(){
		return instance;
	}

}
