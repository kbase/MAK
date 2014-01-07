/* 
	Module MAKBiclusterCollection version 1.0
	This module provides access to MAK biclustering results.

	@author marcin
*/

module MAK {
		
		
	/* MAK dataset source 
	
		string taxon - NCBI taxonomy id
		string id - kbase id
		string description - description
		string dataPath - path to data
	    int num_rows - number of rows
	    int num_columns - number of columns
	     	     
		@searchable ws_subset taxon id description
	*/
	typedef structure {	   
		string taxon;	 
		string id;
		string data_type;
		string description;	
		string dataPath;	
		int num_rows;
		int num_cols;		
  	} MAKInputData;  
	
	
	
	/* MAK algorithm and discovery strategy parameters 
	
		float min_raw_bicluster_score - minimum raw bicluster score
		float max_bicluster_overlap - maximum allowed bicluster overlap
		float max_enrich_pvalue - maximum allowed enrichment p-value
	    int rounds - number of rounds in discovery strategy
	    list<string> rounds_move_sequences - 
	    int refine - refinement y/n 
	    string linkage - complete, single, mean etc.
	    string null_data_path - path to null distribution files
	    string Rcodepath - path to R code (Miner.R)
	    string Rdatapath - path to Rdata object
	    list<MAKInputData> inputs - objects for MAK input data
		
		@optional 
		
    	@searchable ws_subset
	*/
  	typedef structure {  	
		float min_raw_bicluster_score;
		float max_bicluster_overlap;
		float max_enrich_pvalue;
		int rounds;		
		list<string> rounds_move_sequences;
		int refine;
		string linkage;
		string null_data_path;
		string Rcodepath;
		string Rdatapath;						
		list<MAKInputData> inputs;		
  	} MAKParameters;  
  
	

	/* Bicluster 
	
		string bicluster_type - type of bicluster (determined by source data, e.g. expression, fitness, metagenomic, metabolite, integrated)
		string bicluster_id - id
		int num_genes - number of genes
		int num_conditions - number of conditions
		list<string> condition_ids - condition ids
		list<string> condition_labels - condition labels
		list<string> gene_ids - gene ids
		list<string> gene_labels - gene labels
		float exp_mean - expression mean
		float exp_mean_crit - expression mean criterion value
		float exp_crit - expression criterion value
		float ppi_crit - PPI criterion value
		float TF_crit - TF criterion value
		float ortho_crit - orthology criterion value
		float full_crit - full criterion value
		float miss_frxn - fraction of missing data
		mapping<string, string> enriched_terms - enriched terms
	
		@searchable ws_subset bicluster_id gene_ids gene_labels condition_ids condition_labels enriched_terms
	*/
   typedef structure {			 
	 string bicluster_id;	
	 int num_genes;
	 int num_conditions;
	 list<string> condition_ids;	
	 list<string> condition_labels;	
	 list<string> gene_ids;	 
	 list<string> gene_labels;	
	 float exp_mean;	
	 float exp_mean_crit;	
	 float exp_crit;
	 float ppi_crit;
	 float TF_crit;
	 float ortho_crit;
	 float full_crit;
	 float miss_frxn;
	 mapping<string, string> enriched_terms;
	} MAKBicluster;
	
	
	
	/* Bicluster set 
	
		string id - id
		string time_stamp - time stamp for the results
		string version - MAK version
		int number - number of biclusters in set
		int min_genes - min genes for bicluster in set
		int max_genes - max genes for bicluster in set
		int min_conditions - max genes for bicluster in set
		int max_conditions - max conditions for bicluster in set
		string taxon - NCBI taxonomy id
		string bicluster_type - type of bicluster (determined by source data, e.g. expression, fitness, metagenomic, metabolite, integrated)		
		list<MAKBicluster> biclusters - biclusters
		MAKParameters mak_param - parameters
		
	@searchable ws_subset id taxon
	*/
	typedef structure {	
	    string id;
		string time_stamp;
		string version;
		int number;
		int min_genes;
		int max_genes;
		int min_conditions;
		int max_conditions;
	 	string taxon;		
	 	string bicluster_type;
		list<MAKBicluster> biclusters;
		MAKParameters mak_param;		
  	} MAKBiclusterSet; 
};
