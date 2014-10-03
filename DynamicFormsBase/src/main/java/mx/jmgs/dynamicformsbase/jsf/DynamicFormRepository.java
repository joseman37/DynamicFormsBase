package mx.jmgs.dynamicformsbase.jsf;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import org.apache.commons.io.IOUtils;
import org.xml.sax.SAXException;

/**
 *
 * @author mgonzalez
 */
@ManagedBean(name = "dynamicFormRepository")
@ApplicationScoped
public class DynamicFormRepository {

    private static List<String> formList = null;

    private static Map<String, DynamicForm> forms = null;

    public List<String> getFormList() throws IOException {
        if (formList == null) {
            formList = IOUtils.readLines(DynamicFormRepository.class.getClassLoader().getResourceAsStream("META-INF/forms"));
        }
        return formList;
    }

    public Map<String, DynamicForm> getForms() throws IOException {
        if (forms == null) {
            forms = new HashMap<>();
            InputStream is;
            for (String formName : getFormList()) {
                try {
                    is = DynamicFormRepository.class.getClassLoader().
                            getResourceAsStream("META-INF/forms/" + formName);
                    DynamicForm form = readXmlForm(is);
                    forms.put(formName, form);
                    is.close();
                } catch (JAXBException | SAXException ex) {
                    Logger.getLogger(DynamicFormRepository.class.getName()).log(Level.SEVERE, null, ex);
                    throw new RuntimeException("Error while reading dynamic forms spec data", ex);
                }
            }
        }
        return forms;
    }
    
    private DynamicForm readXmlForm(InputStream is) throws JAXBException, SAXException {
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
        
        // this will create Java object - country from the XML file
        DynamicForm form = (DynamicForm) jaxbUnmarshaller.unmarshal(is);
        return form;
    }

}
