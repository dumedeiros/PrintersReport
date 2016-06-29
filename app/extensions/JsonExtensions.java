package extensions;

import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import play.templates.JavaExtensions;

import java.time.DayOfWeek;

public class JsonExtensions extends JavaExtensions {


    /**
     * Remove que quotes in a Json Primitive Object
     */
    public static String noQuotes(JsonPrimitive json) {
        return json.toString().replaceAll("\"", "");
    }
}