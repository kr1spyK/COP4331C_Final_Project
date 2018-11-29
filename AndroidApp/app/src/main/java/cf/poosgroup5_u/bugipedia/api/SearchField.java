package cf.poosgroup5_u.bugipedia.api;

import android.support.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * A SearchField which can be used to filter a search result from the server. <br/>
 * A Filter may or may not hve options depending on the the type of the field.<br/><br/>
 * If a SearchField is created by a constructor, the Type will not be specifiable, only SearchField
 * objects obtained from the Server (Decoded from JSON) will have a Type
 * @see FieldType
 */


public class SearchField {

    private SearchField(){
        //default no-arg constructor to only be used by GSON for deserialization from internet.
    }

    /**
     * Creates a Search Field that's interactable with the Search API. Uses a Simple (Label : Value) Pair <br/>
     * This is ideal for labels that only have 1 value for responses (i.e. Textbox fields, Dropbox fields) <br/>
     *  Usage:<br/><br/>
     *  <i><code>SearchField nameSearch = new SearchField("name", "FooBar");</code></i>
     * @param label  The label of the search filter
     * @param value The value which corresponds as an answer to the label.
     */
    public SearchField(String label, String value){
        this.label = label;
        options = new ArrayList<>();
        options.add(value);
    }

    /**
     * Creates a Search Field thats interactable with the Search API. Uses a Simple (Label : Options) one to many relationship.<br/>
     * This is ideal for labels that contain multiple values to check aganist when searching (i.e.  Multiple checkbox values)<br/>
     *  Usage:<br/><br/>
     *  <i><code>ArrayList<String> values = new ArrayList(values); <br/>
     *  </String>SearchField nameSearch = new SearchField("name", values );</code></i>
     * @param label  The label of the search filter
     * @param options The values which corresponds as answers to the label.
     */

    public SearchField(String label, List<String> options){
        this.label = label;
        this.options = options;
    }

    @SerializedName("label")
    @Expose(serialize = false)
    private String label;
    @SerializedName("type")
    @Expose(serialize = false)
    private FieldType type = null;
    @SerializedName("options")
    @Expose(serialize = false)
    private List<String> options = null;

    public String getLabel() {
        return label;
    }

    /**
     * Obtains the type of Search Field as defined from an API call.<br/>
     * Will return Null if this SearchField was created manually.
     * @return
     */
    public FieldType getType() {
        return type;
    }

    public List<String> getOptions() {
        return options;
    }


    @NonNull
    @Override
    public String toString() {
        return getLabel() + " : " +  getType();
    }
}