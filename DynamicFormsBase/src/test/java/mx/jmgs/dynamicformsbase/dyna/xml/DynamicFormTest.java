package mx.jmgs.dynamicformsbase.dyna.xml;

import java.io.File;
import java.io.IOException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.SchemaOutputResolver;
import javax.xml.transform.Result;
import javax.xml.transform.stream.StreamResult;

import org.junit.Test;

public class DynamicFormTest {

	@Test
	public void generateXmlSchemaFromJAXB() throws JAXBException, IOException {
		JAXBContext jaxbContext = JAXBContext.newInstance(DynamicForm.class);
		SchemaOutputResolver sor = new MySchemaOutputResolver();
		jaxbContext.generateSchema(sor);
	}

	private class MySchemaOutputResolver extends SchemaOutputResolver {

		public Result createOutput(String namespaceURI, String suggestedFileName) throws IOException {
			//File file = new File(suggestedFileName);
			StreamResult result = new StreamResult(System.out);
			result.setSystemId(System.out.toString());
			return result;
		}

	}
	
	
}
