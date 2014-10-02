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
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.commons.io.IOUtils;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 *
 * @author mgonzalez
 */
@ManagedBean(name = "dynamicFormRepository")
@ApplicationScoped
public class DynamicFormRepository {

    private static List<String> formList = null;

    private static Map<String, Document> forms = null;

    public List<String> getFormList() throws IOException {
        if (formList == null) {
            formList = IOUtils.readLines(DynamicFormRepository.class.getClassLoader().getResourceAsStream("META-INF/forms"));
        }
        return formList;
    }

    public Map<String, Document> getForms() throws IOException {
        if (forms == null) {
            forms = new HashMap<>();
            InputStream is;
            for (String form : getFormList()) {
                is = DynamicFormRepository.class.getClassLoader().
                        getResourceAsStream("META-INF/forms/" + form);
                Document xml = readXml(is);
                forms.put(form, xml);
                is.close();
            }
        }
        return forms;
    }
    
    private Document readXml(InputStream is) {
        if(is == null) {
            throw new RuntimeException("couldn't find the dynamic form definition");
        }
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        DocumentBuilder builder;
        try {
            builder = factory.newDocumentBuilder();
            Document doc = builder.parse(is);
            //TODO validar esquema
            return doc;
        } catch (ParserConfigurationException | SAXException | IOException ex) {
            Logger.getLogger(DynamicFormRepository.class.getName()).log(Level.SEVERE, null, ex);
            throw new RuntimeException("Error while reading dynamic forms spec data", ex);
        } 
            
    }

}
