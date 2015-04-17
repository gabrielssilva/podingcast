package gabrielssilva.podingcast.parser;

import org.json.JSONException;
import org.json.JSONObject;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.Stack;


public class SaxHandler extends DefaultHandler {

    private Stack<JSONObject> objects;
    private String stringContent;

    public SaxHandler() { }

    @Override
    public void startDocument() throws SAXException {
        this.objects = new Stack<>();
        this.stringContent = "";

        JSONObject documentRoot = new JSONObject();
        this.objects.push(documentRoot);
    }

    @Override
    public void startElement(String uri, String localName, String name, Attributes attributes)
            throws SAXException {
        JSONObject jsonObject = new JSONObject();

        try {
            getAttributes(jsonObject, attributes);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        this.objects.push(jsonObject);

    }

    @Override
    public void characters(char[] chars, int start, int length) throws SAXException {
        this.stringContent += new String(chars, start, length).replace("\n", "").replace("\t", "");
    }

    @Override
    public void endElement(String uri, String localName, String name) throws SAXException {
        JSONObject currentObject = this.objects.pop();
        JSONObject beforeObject = this.objects.pop();

        try {
            if (!this.stringContent.isEmpty()) {
                currentObject.accumulate(name, this.stringContent);
                this.stringContent = "";
            }

            if (currentObject.length() > 1) {
                beforeObject.accumulate(name, currentObject);
            } else if (currentObject.length() == 1) {
                String key = currentObject.keys().next();
                Object value = currentObject.get(key);
                beforeObject.accumulate(name, value);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        this.objects.push(beforeObject);
    }

    public JSONObject getJson() {
        return this.objects.peek();
    }


    private void getAttributes(JSONObject jsonObject, Attributes attributes) throws JSONException {
        for (int i=0; i<attributes.getLength(); i++) {
            String key = attributes.getQName(i);
            String content =  attributes.getValue(i);
            jsonObject.put(key, content);
        }
    }
}
