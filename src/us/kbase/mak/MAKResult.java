
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
 * <p>Original spec-file type: MAKResult</p>
 * <pre>
 * Represents data from a single run of MAK
 * string id - identifier of MAK run
 * string start_time - start time of MAK run
 * string finish_time - end time of MAK run
 * MAKParameters parameters - run parameters
 * list<MAKBiclusterSet> sets;
 * </pre>
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("com.googlecode.jsonschema2pojo")
@JsonPropertyOrder({
    "id",
    "start_time",
    "finish_time",
    "parameters",
    "sets"
})
public class MAKResult {

    @JsonProperty("id")
    private String id;
    @JsonProperty("start_time")
    private String startTime;
    @JsonProperty("finish_time")
    private String finishTime;
    /**
     * <p>Original spec-file type: MAKParameters</p>
     * <pre>
     * MAK algorithm and discovery strategy parameters 
     * string taxon - taxonomy id
     * string genome_id - kbase genome id
     * float min_raw_bicluster_score - minimum raw bicluster score
     * float max_bicluster_overlap - maximum allowed bicluster overlap
     * float max_enrich_pvalue - maximum allowed enrichment p-value
     *             int rounds - number of rounds in discovery strategy
     *             list<string> rounds_move_sequences - 
     *             int refine - refinement y/n 
     *             string linkage - complete, single, mean etc.
     *             string null_data_path - path to null distribution files
     *             string Rcodepath - path to R code (Miner.R)
     *             string Rdatapath - path to Rdata object
     *             expression_series_ws_ref series_ref - reference to ExpressionSeries
     *             list<MAKInputData> inputs - objects for MAK input data
     * @optional 
     *             @searchable ws_subset
     * </pre>
     * 
     */
    @JsonProperty("parameters")
    private MAKParameters parameters;
    @JsonProperty("sets")
    private List<MAKBiclusterSet> sets;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("id")
    public String getId() {
        return id;
    }

    @JsonProperty("id")
    public void setId(String id) {
        this.id = id;
    }

    public MAKResult withId(String id) {
        this.id = id;
        return this;
    }

    @JsonProperty("start_time")
    public String getStartTime() {
        return startTime;
    }

    @JsonProperty("start_time")
    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public MAKResult withStartTime(String startTime) {
        this.startTime = startTime;
        return this;
    }

    @JsonProperty("finish_time")
    public String getFinishTime() {
        return finishTime;
    }

    @JsonProperty("finish_time")
    public void setFinishTime(String finishTime) {
        this.finishTime = finishTime;
    }

    public MAKResult withFinishTime(String finishTime) {
        this.finishTime = finishTime;
        return this;
    }

    /**
     * <p>Original spec-file type: MAKParameters</p>
     * <pre>
     * MAK algorithm and discovery strategy parameters 
     * string taxon - taxonomy id
     * string genome_id - kbase genome id
     * float min_raw_bicluster_score - minimum raw bicluster score
     * float max_bicluster_overlap - maximum allowed bicluster overlap
     * float max_enrich_pvalue - maximum allowed enrichment p-value
     *             int rounds - number of rounds in discovery strategy
     *             list<string> rounds_move_sequences - 
     *             int refine - refinement y/n 
     *             string linkage - complete, single, mean etc.
     *             string null_data_path - path to null distribution files
     *             string Rcodepath - path to R code (Miner.R)
     *             string Rdatapath - path to Rdata object
     *             expression_series_ws_ref series_ref - reference to ExpressionSeries
     *             list<MAKInputData> inputs - objects for MAK input data
     * @optional 
     *             @searchable ws_subset
     * </pre>
     * 
     */
    @JsonProperty("parameters")
    public MAKParameters getParameters() {
        return parameters;
    }

    /**
     * <p>Original spec-file type: MAKParameters</p>
     * <pre>
     * MAK algorithm and discovery strategy parameters 
     * string taxon - taxonomy id
     * string genome_id - kbase genome id
     * float min_raw_bicluster_score - minimum raw bicluster score
     * float max_bicluster_overlap - maximum allowed bicluster overlap
     * float max_enrich_pvalue - maximum allowed enrichment p-value
     *             int rounds - number of rounds in discovery strategy
     *             list<string> rounds_move_sequences - 
     *             int refine - refinement y/n 
     *             string linkage - complete, single, mean etc.
     *             string null_data_path - path to null distribution files
     *             string Rcodepath - path to R code (Miner.R)
     *             string Rdatapath - path to Rdata object
     *             expression_series_ws_ref series_ref - reference to ExpressionSeries
     *             list<MAKInputData> inputs - objects for MAK input data
     * @optional 
     *             @searchable ws_subset
     * </pre>
     * 
     */
    @JsonProperty("parameters")
    public void setParameters(MAKParameters parameters) {
        this.parameters = parameters;
    }

    public MAKResult withParameters(MAKParameters parameters) {
        this.parameters = parameters;
        return this;
    }

    @JsonProperty("sets")
    public List<MAKBiclusterSet> getSets() {
        return sets;
    }

    @JsonProperty("sets")
    public void setSets(List<MAKBiclusterSet> sets) {
        this.sets = sets;
    }

    public MAKResult withSets(List<MAKBiclusterSet> sets) {
        this.sets = sets;
        return this;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperties(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    @Override
    public String toString() {
        return ((((((((((((("MAKResult"+" [id=")+ id)+", startTime=")+ startTime)+", finishTime=")+ finishTime)+", parameters=")+ parameters)+", sets=")+ sets)+", additionalProperties=")+ additionalProperties)+"]");
    }

}
