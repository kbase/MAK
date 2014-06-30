
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
 * <p>Original spec-file type: MAKBiclusterSet</p>
 * <pre>
 * Bicluster set 
 *         string id - id
 *         string time_stamp - time stamp for the results
 *         string version - MAK version
 *         int number - number of biclusters in set
 *         int min_genes - min genes for bicluster in set
 *         int max_genes - max genes for bicluster in set
 *         int min_conditions - max genes for bicluster in set
 *         int max_conditions - max conditions for bicluster in set
 *         string taxon - NCBI taxonomy id
 *         string bicluster_type - type of bicluster (determined by source data, e.g. expression, fitness, metagenomic, metabolite, integrated)                
 *         list<MAKBicluster> biclusters - biclusters
 *         map<string, string> id_index - map of bicluster ids to array indices (translates between kb id and ws id)
 *         
 * @searchable ws_subset id taxon
 * </pre>
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("com.googlecode.jsonschema2pojo")
@JsonPropertyOrder({
    "id",
    "time_stamp",
    "version",
    "number",
    "min_genes",
    "max_genes",
    "min_conditions",
    "max_conditions",
    "taxon",
    "bicluster_type",
    "biclusters",
    "id_index"
})
public class MAKBiclusterSet {

    @JsonProperty("id")
    private java.lang.String id;
    @JsonProperty("time_stamp")
    private java.lang.String timeStamp;
    @JsonProperty("version")
    private java.lang.String version;
    @JsonProperty("number")
    private Long number;
    @JsonProperty("min_genes")
    private Long minGenes;
    @JsonProperty("max_genes")
    private Long maxGenes;
    @JsonProperty("min_conditions")
    private Long minConditions;
    @JsonProperty("max_conditions")
    private Long maxConditions;
    @JsonProperty("taxon")
    private java.lang.String taxon;
    @JsonProperty("bicluster_type")
    private java.lang.String biclusterType;
    @JsonProperty("biclusters")
    private List<MAKBicluster> biclusters;
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

    public MAKBiclusterSet withId(java.lang.String id) {
        this.id = id;
        return this;
    }

    @JsonProperty("time_stamp")
    public java.lang.String getTimeStamp() {
        return timeStamp;
    }

    @JsonProperty("time_stamp")
    public void setTimeStamp(java.lang.String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public MAKBiclusterSet withTimeStamp(java.lang.String timeStamp) {
        this.timeStamp = timeStamp;
        return this;
    }

    @JsonProperty("version")
    public java.lang.String getVersion() {
        return version;
    }

    @JsonProperty("version")
    public void setVersion(java.lang.String version) {
        this.version = version;
    }

    public MAKBiclusterSet withVersion(java.lang.String version) {
        this.version = version;
        return this;
    }

    @JsonProperty("number")
    public Long getNumber() {
        return number;
    }

    @JsonProperty("number")
    public void setNumber(Long number) {
        this.number = number;
    }

    public MAKBiclusterSet withNumber(Long number) {
        this.number = number;
        return this;
    }

    @JsonProperty("min_genes")
    public Long getMinGenes() {
        return minGenes;
    }

    @JsonProperty("min_genes")
    public void setMinGenes(Long minGenes) {
        this.minGenes = minGenes;
    }

    public MAKBiclusterSet withMinGenes(Long minGenes) {
        this.minGenes = minGenes;
        return this;
    }

    @JsonProperty("max_genes")
    public Long getMaxGenes() {
        return maxGenes;
    }

    @JsonProperty("max_genes")
    public void setMaxGenes(Long maxGenes) {
        this.maxGenes = maxGenes;
    }

    public MAKBiclusterSet withMaxGenes(Long maxGenes) {
        this.maxGenes = maxGenes;
        return this;
    }

    @JsonProperty("min_conditions")
    public Long getMinConditions() {
        return minConditions;
    }

    @JsonProperty("min_conditions")
    public void setMinConditions(Long minConditions) {
        this.minConditions = minConditions;
    }

    public MAKBiclusterSet withMinConditions(Long minConditions) {
        this.minConditions = minConditions;
        return this;
    }

    @JsonProperty("max_conditions")
    public Long getMaxConditions() {
        return maxConditions;
    }

    @JsonProperty("max_conditions")
    public void setMaxConditions(Long maxConditions) {
        this.maxConditions = maxConditions;
    }

    public MAKBiclusterSet withMaxConditions(Long maxConditions) {
        this.maxConditions = maxConditions;
        return this;
    }

    @JsonProperty("taxon")
    public java.lang.String getTaxon() {
        return taxon;
    }

    @JsonProperty("taxon")
    public void setTaxon(java.lang.String taxon) {
        this.taxon = taxon;
    }

    public MAKBiclusterSet withTaxon(java.lang.String taxon) {
        this.taxon = taxon;
        return this;
    }

    @JsonProperty("bicluster_type")
    public java.lang.String getBiclusterType() {
        return biclusterType;
    }

    @JsonProperty("bicluster_type")
    public void setBiclusterType(java.lang.String biclusterType) {
        this.biclusterType = biclusterType;
    }

    public MAKBiclusterSet withBiclusterType(java.lang.String biclusterType) {
        this.biclusterType = biclusterType;
        return this;
    }

    @JsonProperty("biclusters")
    public List<MAKBicluster> getBiclusters() {
        return biclusters;
    }

    @JsonProperty("biclusters")
    public void setBiclusters(List<MAKBicluster> biclusters) {
        this.biclusters = biclusters;
    }

    public MAKBiclusterSet withBiclusters(List<MAKBicluster> biclusters) {
        this.biclusters = biclusters;
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

    public MAKBiclusterSet withIdIndex(Map<String, String> idIndex) {
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
        return ((((((((((((((((((((((((((("MAKBiclusterSet"+" [id=")+ id)+", timeStamp=")+ timeStamp)+", version=")+ version)+", number=")+ number)+", minGenes=")+ minGenes)+", maxGenes=")+ maxGenes)+", minConditions=")+ minConditions)+", maxConditions=")+ maxConditions)+", taxon=")+ taxon)+", biclusterType=")+ biclusterType)+", biclusters=")+ biclusters)+", idIndex=")+ idIndex)+", additionalProperties=")+ additionalProperties)+"]");
    }

}
