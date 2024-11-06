package com.example.StockManagement.util;

import org.json.JSONObject;
import org.json.XML;

public class XMLToJSONConverter {

    /**
     * Converts XML string to JSON string.
     *
     * @param xml XML string to be converted
     * @return JSON string representation of the XML
     */
    public static String convertXMLToJSON(String xml) {
        JSONObject json = XML.toJSONObject(xml);
        return json.toString(4);  // Pretty print with indentation
    }
}

