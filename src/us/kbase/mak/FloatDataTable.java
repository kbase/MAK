
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
 * <p>Original spec-file type: FloatDataTable</p>
 * <pre>
 * Represents data for a single data table, convention is biological features on y-axis and samples etc. on x
 * string id - identifier for data table
 * string name - name or title to display in a plot etc.
 * list<string> row_ids -
 * list<string> row_labels -
 * list<string> row_groups - group labels for row
 * list<string> column_ids -
 * list<string> column_labels -
 * list<string> column_groups - group labels for columns
 * list<list<float>> data -
 * </pre>
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("com.googlecode.jsonschema2pojo")
@JsonPropertyOrder({
    "id",
    "name",
    "row_ids",
    "row_labels",
    "row_groups",
    "column_ids",
    "column_labels",
    "column_groups",
    "data"
})
public class FloatDataTable {

    @JsonProperty("id")
    private java.lang.String id;
    @JsonProperty("name")
    private java.lang.String name;
    @JsonProperty("row_ids")
    private List<String> rowIds;
    @JsonProperty("row_labels")
    private List<String> rowLabels;
    @JsonProperty("row_groups")
    private List<String> rowGroups;
    @JsonProperty("column_ids")
    private List<String> columnIds;
    @JsonProperty("column_labels")
    private List<String> columnLabels;
    @JsonProperty("column_groups")
    private List<String> columnGroups;
    @JsonProperty("data")
    private List<List<Double>> data;
    private Map<java.lang.String, Object> additionalProperties = new HashMap<java.lang.String, Object>();

    @JsonProperty("id")
    public java.lang.String getId() {
        return id;
    }

    @JsonProperty("id")
    public void setId(java.lang.String id) {
        this.id = id;
    }

    public FloatDataTable withId(java.lang.String id) {
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

    public FloatDataTable withName(java.lang.String name) {
        this.name = name;
        return this;
    }

    @JsonProperty("row_ids")
    public List<String> getRowIds() {
        return rowIds;
    }

    @JsonProperty("row_ids")
    public void setRowIds(List<String> rowIds) {
        this.rowIds = rowIds;
    }

    public FloatDataTable withRowIds(List<String> rowIds) {
        this.rowIds = rowIds;
        return this;
    }

    @JsonProperty("row_labels")
    public List<String> getRowLabels() {
        return rowLabels;
    }

    @JsonProperty("row_labels")
    public void setRowLabels(List<String> rowLabels) {
        this.rowLabels = rowLabels;
    }

    public FloatDataTable withRowLabels(List<String> rowLabels) {
        this.rowLabels = rowLabels;
        return this;
    }

    @JsonProperty("row_groups")
    public List<String> getRowGroups() {
        return rowGroups;
    }

    @JsonProperty("row_groups")
    public void setRowGroups(List<String> rowGroups) {
        this.rowGroups = rowGroups;
    }

    public FloatDataTable withRowGroups(List<String> rowGroups) {
        this.rowGroups = rowGroups;
        return this;
    }

    @JsonProperty("column_ids")
    public List<String> getColumnIds() {
        return columnIds;
    }

    @JsonProperty("column_ids")
    public void setColumnIds(List<String> columnIds) {
        this.columnIds = columnIds;
    }

    public FloatDataTable withColumnIds(List<String> columnIds) {
        this.columnIds = columnIds;
        return this;
    }

    @JsonProperty("column_labels")
    public List<String> getColumnLabels() {
        return columnLabels;
    }

    @JsonProperty("column_labels")
    public void setColumnLabels(List<String> columnLabels) {
        this.columnLabels = columnLabels;
    }

    public FloatDataTable withColumnLabels(List<String> columnLabels) {
        this.columnLabels = columnLabels;
        return this;
    }

    @JsonProperty("column_groups")
    public List<String> getColumnGroups() {
        return columnGroups;
    }

    @JsonProperty("column_groups")
    public void setColumnGroups(List<String> columnGroups) {
        this.columnGroups = columnGroups;
    }

    public FloatDataTable withColumnGroups(List<String> columnGroups) {
        this.columnGroups = columnGroups;
        return this;
    }

    @JsonProperty("data")
    public List<List<Double>> getData() {
        return data;
    }

    @JsonProperty("data")
    public void setData(List<List<Double>> data) {
        this.data = data;
    }

    public FloatDataTable withData(List<List<Double>> data) {
        this.data = data;
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
        return ((((((((((((((((((((("FloatDataTable"+" [id=")+ id)+", name=")+ name)+", rowIds=")+ rowIds)+", rowLabels=")+ rowLabels)+", rowGroups=")+ rowGroups)+", columnIds=")+ columnIds)+", columnLabels=")+ columnLabels)+", columnGroups=")+ columnGroups)+", data=")+ data)+", additionalProperties=")+ additionalProperties)+"]");
    }

}
