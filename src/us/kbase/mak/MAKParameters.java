
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
    private Long rounds;
    @JsonProperty("rounds_move_sequences")
    private List<String> roundsMoveSequences;
    @JsonProperty("refine")
    private Long refine;
    @JsonProperty("linkage")
    private java.lang.String linkage;
    @JsonProperty("null_data_path")
    private java.lang.String nullDataPath;
    @JsonProperty("Rcodepath")
    private java.lang.String Rcodepath;
    @JsonProperty("Rdatapath")
    private java.lang.String Rdatapath;
    @JsonProperty("inputs")
    private List<MAKInputData> inputs;
    private Map<java.lang.String, Object> additionalProperties = new HashMap<java.lang.String, Object>();

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
    public Long getRounds() {
        return rounds;
    }

    @JsonProperty("rounds")
    public void setRounds(Long rounds) {
        this.rounds = rounds;
    }

    public MAKParameters withRounds(Long rounds) {
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
    public Long getRefine() {
        return refine;
    }

    @JsonProperty("refine")
    public void setRefine(Long refine) {
        this.refine = refine;
    }

    public MAKParameters withRefine(Long refine) {
        this.refine = refine;
        return this;
    }

    @JsonProperty("linkage")
    public java.lang.String getLinkage() {
        return linkage;
    }

    @JsonProperty("linkage")
    public void setLinkage(java.lang.String linkage) {
        this.linkage = linkage;
    }

    public MAKParameters withLinkage(java.lang.String linkage) {
        this.linkage = linkage;
        return this;
    }

    @JsonProperty("null_data_path")
    public java.lang.String getNullDataPath() {
        return nullDataPath;
    }

    @JsonProperty("null_data_path")
    public void setNullDataPath(java.lang.String nullDataPath) {
        this.nullDataPath = nullDataPath;
    }

    public MAKParameters withNullDataPath(java.lang.String nullDataPath) {
        this.nullDataPath = nullDataPath;
        return this;
    }

    @JsonProperty("Rcodepath")
    public java.lang.String getRcodepath() {
        return Rcodepath;
    }

    @JsonProperty("Rcodepath")
    public void setRcodepath(java.lang.String Rcodepath) {
        this.Rcodepath = Rcodepath;
    }

    public MAKParameters withRcodepath(java.lang.String Rcodepath) {
        this.Rcodepath = Rcodepath;
        return this;
    }

    @JsonProperty("Rdatapath")
    public java.lang.String getRdatapath() {
        return Rdatapath;
    }

    @JsonProperty("Rdatapath")
    public void setRdatapath(java.lang.String Rdatapath) {
        this.Rdatapath = Rdatapath;
    }

    public MAKParameters withRdatapath(java.lang.String Rdatapath) {
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
    public Map<java.lang.String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperties(java.lang.String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    @Override
    public java.lang.String toString() {
        return ((((((((((((((((((((((((("MAKParameters"+" [minRawBiclusterScore=")+ minRawBiclusterScore)+", maxBiclusterOverlap=")+ maxBiclusterOverlap)+", maxEnrichPvalue=")+ maxEnrichPvalue)+", rounds=")+ rounds)+", roundsMoveSequences=")+ roundsMoveSequences)+", refine=")+ refine)+", linkage=")+ linkage)+", nullDataPath=")+ nullDataPath)+", Rcodepath=")+ Rcodepath)+", Rdatapath=")+ Rdatapath)+", inputs=")+ inputs)+", additionalProperties=")+ additionalProperties)+"]");
    }

}