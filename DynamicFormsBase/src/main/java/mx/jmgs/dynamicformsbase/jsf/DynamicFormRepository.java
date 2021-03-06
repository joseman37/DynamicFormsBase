package mx.jmgs.dynamicformsbase.jsf;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import mx.jmgs.dynamicformsbase.dyna.xml.DynamicForm;
import mx.jmgs.dynamicformsbase.struct.TemplateStat;

import org.apache.commons.io.IOUtils;
import org.xml.sax.SAXException;

import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;

/**
 *
 * @author mgonzalez
 */
@ManagedBean(name = "dynamicFormRepository")
@ApplicationScoped
public class DynamicFormRepository implements Serializable {

    /**
	 * helps Serialization
	 */
	private static final long serialVersionUID = 1L;

	private static List<String> formList = null;

    private static Map<String, DynamicForm> forms = null;
	
	/**
     * Freemarker Configuration object 
     */
 	private static Configuration cfg;
 	
 	/**
 	 * FreeMarker StringTemplateLoader. Used to load from strings objects not from files.
 	 */
 	private static StringTemplateLoader stringLoader;
 	
 	private static Set<String> templateNames;

    
    static {
    	cfg = new Configuration();
 		stringLoader = new StringTemplateLoader();
 		cfg.setTemplateLoader(stringLoader);
 		
 		//Cofigure Free Marker
 		cfg.setTemplateUpdateDelay(3600); // Disable cache reload
 		templateNames = new HashSet<>();
 		
 		try {
			formList = IOUtils.readLines(DynamicFormRepository.class.getClassLoader().getResourceAsStream("META-INF/forms"));
		} catch (IOException e) {
			throw new RuntimeException("Error while loading dynamic forms");
		}
 		
 		forms = new HashMap<>();
        InputStream is;
        for (String formName : formList) {
            try {
                is = DynamicFormRepository.class.getClassLoader().
                        getResourceAsStream("META-INF/forms/" + formName);
                DynamicForm form = readXmlForm(is);
                forms.put(formName, form);
                is.close();
            } catch (JAXBException | SAXException | IOException ex) {
                Logger.getLogger(DynamicFormRepository.class.getName()).log(Level.SEVERE, null, ex);
                throw new RuntimeException("Error while reading dynamic forms spec data: " + ex.getMessage(), ex);
            }
        }
        
        //TODO
        System.setProperty("org.apache.el.parser.COERCE_TO_ZERO", "false");
        String config = System.getProperty("org.apache.el.parser.COERCE_TO_ZERO");
        System.out.println("org.apache.el.parser.COERCE_TO_ZERO: " + config);
    }
 	
 	/**
 	 * Constructor
 	 */
 	public DynamicFormRepository() {
 		
 	}

    public List<String> getFormList() {
        return formList;
    }

    public Map<String, DynamicForm> getForms() {
        return forms;
    }
    
    private static DynamicForm readXmlForm(InputStream is) throws JAXBException, SAXException {
        if(is == null) {
            throw new RuntimeException("couldn't find the dynamic form definition");
        }
        // create JAXB context and initializing Marshaller
        JAXBContext jaxbContext = JAXBContext.newInstance(DynamicForm.class);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

        //TODO validar esquema. Puede no ser necesario por que las clases JAXB bind corresponden al esquema.
        SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        Schema schema = schemaFactory.newSchema(DynamicFormRepository.class
                .getClassLoader().getResource("META-INF/forms.xsd"));
        jaxbUnmarshaller.setSchema(schema);
        
        // this will create Java object - DynamicForm from the XML file
        // Since the xsd have more than one root element we must use JAXBElement instead of:
        // DynamicForm form = (DynamicForm) jaxbUnmarshaller.unmarshal(is);      
		// DynamicForm form = ((JAXBElement<DynamicForm>)jaxbUnmarshaller.unmarshal(is)).getValue();
        
        DynamicForm form = (DynamicForm) jaxbUnmarshaller.unmarshal(is);
        return form;
    }
    
    /**
     * Retrieves a FreeMarker template to use for localization in the dynamic forms.
     * @throws IOException 
     */
    public Template getTemplate(String templateStr) throws IOException {
    	// Load the template
    	stringLoader.putTemplate(templateStr, templateStr, -1);
    	// the next line could be used too.
    	// stringLoader.putTemplate(templateStr, templateStr,-1);
    	// so the cache reload is disabled, but it is already disabled in the constructor
    	// if this class.
    	
    	//Use the whole template string as its name.
    	Template template  = cfg.getTemplate(templateStr);
    	
    	templateNames.add(templateStr);
    	
		return template;
    	
    }
    
    public List<TemplateStat> getTemplateStatistics() {
    	List<TemplateStat> stats = new ArrayList<>();
    	for(String templateName : templateNames) {
    		Object source = stringLoader.findTemplateSource(templateName);
    		Calendar cal = Calendar.getInstance();
    		cal.setTimeInMillis(stringLoader.getLastModified(source));
    		stats.add(new TemplateStat(templateName, cal.getTime(), source.toString()));
    	}
    	return stats;
    }

}
