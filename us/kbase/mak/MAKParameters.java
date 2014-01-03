
package us.kbase.mak;

import java.util.ArrayList;
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
 * <p>Original spec-file type: MAKParameters</p>
 * <pre>
 * MAK algorithm and discovery strategy parameters 
 *         float min_raw_bicluster_score - minimum raw bicluster score
 *         float max_bicluster_overlap - maximum allowed bicluster overlap
 *         float max_enrich_pvalue - maximum allowed enrichment p-value
 *     int rounds - number of rounds in discovery strategy
 *     list<string> rounds_move_sequences - 
 *     int refine - refinement y/n 
 *     string linkage - complete, single, mean etc.
 *     string null_data_path - path to null distribution files
 *     string Rcodepath - path to R code (Miner.R)
 *     string Rdatapath - path to Rdata object
 *     list<MAKInputData> inputs - objects for MAK input data
 *         
 *         @optional 
 *         
 *     @searchable ws_subset
 * </pre>
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("com.googlecode.jsonschema2pojo")
@JsonPropertyOrder({
    "min_raw_bicluster_score",
    "max_bicluster_overlap",
    "max_enrich_pvalue",
    "rounds",
    "rounds_move_sequences",
    "refine",
    "linkage",
    "null_data_path",
    "Rcodepath",
    "Rdatapath",
    "inputs"
})
public class MAKParameters {

    @JsonProperty("min_raw_bicluster_score")
    private Double minRawBiclusterScore;
    @JsonProperty("max_bicluster_overlap")
    private Double maxBiclusterOverlap;
    @JsonProperty("max_enrich_pvalue")
    private Double maxEnrichPvalue;
    @JsonProperty("rounds")
    private Integer rounds;
    @JsonProperty("rounds_move_sequences")
    private List<String> roundsMoveSequences = new ArrayList<String>();
    @JsonProperty("refine")
    private Integer refine;
    @JsonProperty("linkage")
    private String linkage;
    @JsonProperty("null_data_path")
    private String nullDataPath;
    @JsonProperty("Rcodepath")
    private String Rcodepath;
    @JsonProperty("Rdatapath")
    private String Rdatapath;
    @JsonProperty("inputs")
    private List<MAKInputData> inputs = new ArrayList<MAKInputData>();
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("min_raw_bicluster_score")
    public Double getMinRawBiclusterScore() {
        return minRawBiclusterScore;
    }

    @JsonProperty("min_raw_bicluster_score")
    public void setMinRawBiclusterScore(Double minRawBiclusterScore) {
        this.minRawBiclusterScore = minRawBiclusterScore;
    }

    public MAKParameters withMinRawBiclusterScore(Double minRawBiclusterScore) {
        this.minRawBiclusterScore = minRawBiclusterScore;
        return this;
    }

    @JsonProperty("max_bicluster_overlap")
    public Double getMaxBiclusterOverlap() {
        return maxBiclusterOverlap;
    }

    @JsonProperty("max_bicluster_overlap")
    public void setMaxBiclusterOverlap(Double maxBiclusterOverlap) {
        this.maxBiclusterOverlap = maxBiclusterOverlap;
    }

    public MAKParameters withMaxBiclusterOverlap(Double maxBiclusterOverlap) {
        this.maxBiclusterOverlap = maxBiclusterOverlap;
        return this;
    }

    @JsonProperty("max_enrich_pvalue")
    public Double getMaxEnrichPvalue() {
        return maxEnrichPvalue;
    }

    @JsonProperty("max_enrich_pvalue")
    public void setMaxEnrichPvalue(Double maxEnrichPvalue) {
        this.maxEnrichPvalue = maxEnrichPvalue;
    }

    public MAKParameters withMaxEnrichPvalue(Double maxEnrichPvalue) {
        this.maxEnrichPvalue = maxEnrichPvalue;
        return this;
    }

    @JsonProperty("rounds")
    public Integer getRounds() {
        return rounds;
    }

    @JsonProperty("rounds")
    public void setRounds(Integer rounds) {
        this.rounds = rounds;
    }

    public MAKParameters withRounds(Integer rounds) {
        this.rounds = rounds;
        return this;
    }

    @JsonProperty("rounds_move_sequences")
    public List<String> getRoundsMoveSequences() {
        return roundsMoveSequences;
    }

    @JsonProperty("rounds_move_sequences")
    public void setRoundsMoveSequences(List<String> roundsMoveSequences) {
        this.roundsMoveSequences = roundsMoveSequences;
    }

    public MAKParameters withRoundsMoveSequences(List<String> roundsMoveSequences) {
        this.roundsMoveSequences = roundsMoveSequences;
        return this;
    }

    @JsonProperty("refine")
    public Integer getRefine() {
        return refine;
    }

    @JsonProperty("refine")
    public void setRefine(Integer refine) {
        this.refine = refine;
    }

    public MAKParameters withRefine(Integer refine) {
        this.refine = refine;
        return this;
    }

    @JsonProperty("linkage")
    public String getLinkage() {
        return linkage;
    }

    @JsonProperty("linkage")
    public void setLinkage(String linkage) {
        this.linkage = linkage;
    }

    public MAKParameters withLinkage(String linkage) {
        this.linkage = linkage;
        return this;
    }

    @JsonProperty("null_data_path")
    public String getNullDataPath() {
        return nullDataPath;
    }

    @JsonProperty("null_data_path")
    public void setNullDataPath(String nullDataPath) {
        this.nullDataPath = nullDataPath;
    }

    public MAKParameters withNullDataPath(String nullDataPath) {
        this.nullDataPath = nullDataPath;
        return this;
    }

    @JsonProperty("Rcodepath")
    public String getRcodepath() {
        return Rcodepath;
    }

    @JsonProperty("Rcodepath")
    public void setRcodepath(String Rcodepath) {
        this.Rcodepath = Rcodepath;
    }

    public MAKParameters withRcodepath(String Rcodepath) {
        this.Rcodepath = Rcodepath;
        return this;
    }

    @JsonProperty("Rdatapath")
    public String getRdatapath() {
        return Rdatapath;
    }

    @JsonProperty("Rdatapath")
    public void setRdatapath(String Rdatapath) {
        this.Rdatapath = Rdatapath;
    }

    public MAKParameters withRdatapath(String Rdatapath) {
        this.Rdatapath = Rdatapath;
        return this;
    }

    @JsonProperty("inputs")
    public List<MAKInputData> getInputs() {
        return inputs;
    }

    @JsonProperty("inputs")
    public void setInputs(List<MAKInputData> inputs) {
        this.inputs = inputs;
    }

    public MAKParameters withInputs(List<MAKInputData> inputs) {
        this.inputs = inputs;
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

}
