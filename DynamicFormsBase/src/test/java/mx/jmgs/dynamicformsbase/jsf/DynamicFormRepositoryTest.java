package mx.jmgs.dynamicformsbase.jsf;

import java.io.IOException;
import java.util.Map;
import mx.jmgs.dynamicformsbase.dyna.xml.DynamicForm;
import org.w3c.dom.Document;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author mgonzalez
 */
public class DynamicFormRepositoryTest {

    /**
     * Test of getForms method, of class DynamicFormRepository.
     * @throws java.io.IOException
     */
    @Test
    public void testFormRepository() throws IOException {
        System.out.println("getForms");

        DynamicFormRepository rep = new DynamicFormRepository();
        for (String fileName : rep.getForms().keySet()) {
            DynamicForm doc = rep.getForms().get(fileName);
            System.out.println(fileName + " : " + doc.toString());
        }
    }

}
