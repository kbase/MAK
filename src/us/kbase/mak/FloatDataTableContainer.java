
package us.kbase.mak;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;


/**
 * <p>Original spec-file type: FloatDataTableContainer</p>
 * <pre>
 * Represents a list of data tables
 * string id - identifier for container
 * string name - name or title to display in a plot etc.
 * list<FloatDataTable> - data array
 * map<string, string> id_index - map of table ids to array positions
 * @optional id
 * @optional name
 * @optional id_index
 * </pre>
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("com.googlecode.jsonschema2pojo")
@JsonPropertyOrder({
    "id",
    "name",
    "setdata",
    "id_index"
})
public class FloatDataTableContainer {

    @JsonProperty("id")
    private java.lang.String id;
    @JsonProperty("name")
    private java.lang.String name;
    @JsonProperty("setdata")
    private List<FloatDataTable> setdata;
    @JsonProperty("id_index")
    private Map<String, String> idIndex;
    private Map<java.lang.String, Object> additionalProperties = new HashMap<java.lang.String, Object>();

    @JsonProperty("id")
    public java.lang.String getId() {
        return id;
    }

    @JsonProperty("id")
    public void setId(java.lang.String id) {
        this.id = id;
    }

    public FloatDataTableContainer withId(java.lang.String id) {
        this.id = id;
        return this;
    }

    @JsonProperty("name")
    public java.lang.String getName() {
        return name;
    }

    @JsonProperty("name")
    public void setName(java.lang.String name) {
        this.name = name;
    }

    public FloatDataTableContainer withName(java.lang.String name) {
        this.name = name;
        return this;
    }

    @JsonProperty("setdata")
    public List<FloatDataTable> getSetdata() {
        return setdata;
    }

    @JsonProperty("setdata")
    public void setSetdata(List<FloatDataTable> setdata) {
        this.setdata = setdata;
    }

    public FloatDataTableContainer withSetdata(List<FloatDataTable> setdata) {
        this.setdata = setdata;
        return this;
    }

    @JsonProperty("id_index")
    public Map<String, String> getIdIndex() {
        return idIndex;
    }

    @JsonProperty("id_index")
    public void setIdIndex(Map<String, String> idIndex) {
        this.idIndex = idIndex;
    }

    public FloatDataTableContainer withIdIndex(Map<String, String> idIndex) {
        this.idIndex = idIndex;
        return this;
    }

    @JsonAnyGetter
    public Map<java.lang.String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperties(java.lang.String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    @Override
    public java.lang.String toString() {
        return ((((((((((("FloatDataTableContainer"+" [id=")+ id)+", name=")+ name)+", setdata=")+ setdata)+", idIndex=")+ idIndex)+", additionalProperties=")+ additionalProperties)+"]");
    }

}
