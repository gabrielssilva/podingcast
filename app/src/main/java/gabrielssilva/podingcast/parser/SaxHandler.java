package gabrielssilva.podingcast.parser;

import org.apache.http.client.methods.HttpGet;
import org.json.JSONException;
import org.json.JSONObject;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.Stack;


public class SaxHandler extends DefaultHandler {

    private final static String LIMITED_TAG = "item";
    private final static int NO_LIMIT = -1;

    private Stack<JSONObject> objects;
    private Stack<String> tags;
    private String stringContent;
    private int maxItems;

    private HttpGet httpOperation;

    public SaxHandler(HttpGet httpOperation) {
        this(NO_LIMIT, httpOperation);
    }

    public SaxHandler(int maxItems, HttpGet httpOperation) {
        this.maxItems = maxItems;
        this.httpOperation = httpOperation;
    }

    @Override
    public void startDocument() throws SAXException {
        this.objects = new Stack<>();
        this.tags = new Stack<>();
        this.stringContent = "";

        JSONObject documentRoot = new JSONObject();
        this.objects.push(documentRoot);
    }

    @Override
    public void startElement(String uri, String localName, String name, Attributes attributes)
            throws SAXException {

        checkStopCondition(name);
        JSONObject jsonObject = new JSONObject();
        this.tags.push(name);

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
        this.tags.pop();

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
        if (this.objects != null) {
            return this.objects.peek();
        } else {
            return null;
        }
    }


    private void getAttributes(JSONObject jsonObject, Attributes attributes) throws JSONException {
        for (int i=0; i<attributes.getLength(); i++) {
            String key = attributes.getQName(i);
            String content =  attributes.getValue(i);
            jsonObject.put(key, content);
        }
    }

    private void closeJson() throws SAXException {
        while (this.tags.size() > 0) {
            String tag = this.tags.peek();
            endElement("", tag, tag);
        }
    }

    private void checkStopCondition(String tagName) throws SAXException {
        if ((this.maxItems != NO_LIMIT) && (tagName.equalsIgnoreCase(LIMITED_TAG))) {
            if (this.maxItems > 0) {
                this.maxItems = this.maxItems - 1;
            } else {
                closeJson();
                this.httpOperation.abort();
                throw new AllItemsParsedSaxException();
            }
        }
    }

    public class AllItemsParsedSaxException extends SAXException { }
}
