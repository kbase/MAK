/* 
	Module MAK version 1.0
	This module provides access to MAK biclustering results.

	@author marcin
*/

module MAK {
		
	/* Represents KBase gene identifier
		@id external
	*/
	typedef string gene_id;

	/* Represents WS expression data sample identifier
		id ws KBaseExpression.ExpressionSample
	*/
	typedef string expression_sample_ws_ref;

	/* Represents WS expression data series identifier
		@id ws KBaseExpression.ExpressionSeries
	*/
	typedef string expression_series_ws_ref;

	/* Represents WS genome identifier
		@id ws KBaseGenomes.Genome
	*/
	typedef string genome_ws_ref;

	/* Represents WS network identifier
		@id ws Networks.InteractionSet
	*/
	typedef string network_ws_ref;

	/* Represents WS MAK bicluster identifier
		id subws MAK.MAKBicluster
	*/
	typedef string MABicluster_id;

	/* Represents WS MAKBiclusterSet identifier
		id subws MAK.MAKBiclusterSet
	*/
	typedef string MAK_network_id;

	/* Represents WS MAK run result identifier
		id ws MAK.MAKRunResult
	*/
	typedef string MAK_run_result_id;
	
		
	/* MAK dataset source 
	
		string taxon - NCBI taxonomy id
		string genome_id - kbase id of genome
		string id - kbase id
		string data_type - type of data: "expression", "fitness"
		string description - description
		string dataPath - path to data
	    int num_rows - number of rows
	    int num_columns - number of columns
	     	     
		@searchable ws_subset taxon id description
	*/
	typedef structure {	   
		string taxon;	 
		string genome_id;
		string id;
		string data_type;
		string description;	
		string dataPath;	
		int num_rows;
		int num_cols;		
  	} MAKInputData;  
	
		/* Represents a particular data point from gene expression data set
		string gene_id - KBase gene identifier
		float expression_value - relative expression value 
	*/
	typedef structure{
		string gene_id;
		float expression_value;
	} ExpressionDataPoint;
	
	/* ExpressionDataSample represents set of expression data
		string id - identifier of data set
		string description - description of data set`
		list<ExpressionDataPoint> points - data points
	*/
	typedef structure{
		string id;
		string description;
		list<ExpressionDataPoint> points;
	} ExpressionDataSample;

	/* ExpressionDataSeries represents collection of expression data samples
		string id - identifier of the collection
		list<ExpressionDataSample> samples - data sets
	*/
	typedef structure{
		string id;
		list<ExpressionDataSample> samples;
	} ExpressionDataSeries;
	
	
	/* MAK algorithm and discovery strategy parameters 
		string taxon - taxonomy id
		string genome_id - kbase genome id
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
	    expression_series_ws_ref series_ref - reference to ExpressionSeries
	    list<MAKInputData> inputs - objects for MAK input data
		
		@optional 
		
    	@searchable ws_subset
	*/
  	typedef structure {  
  		string taxon;
  		string genome_id;
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
		expression_series_ws_ref series_ref;
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
  	} MAKBiclusterSet; 
  	

	/* Represents data from a single run of MAK
	string id - identifier of MAK run
	string start_time - start time of MAK run
	string finish_time - end time of MAK run
	MAKParameters parameters - run parameters
	list<MAKBiclusterSet> sets;
	*/
	typedef structure{
		string id;
		string start_time;
		string finish_time;
		MAKParameters parameters;	
		list<MAKBiclusterSet> sets;
	} MAKResult;
  	
	/*	Starts MAK server job and returns job ID of the run
	string ws_id - workspace id
	string kbgid - kbase genome id kbgid
	string job_id - identifier of MAK job
	*/
	funcdef runall_MAK_job_from_ws(string ws_id, string kbgid, string data_type) returns(string MAK_job_id) authentication required;
	
	/*	Starts MAK server job and returns job ID of the run
	string ws_id - workspace id
	tring kbgid - kbase genome id kbgid
	MAKBicluster makb - starting point bicluster
	string job_id - identifier of MAK job
	*/
	funcdef runsingle_MAK_job_from_ws(string ws_id, string kbgid, string data_type, list<string> geneids) returns(string MAK_job_id) authentication required;
	
	/*	Starts MAK server job for searching precomputed biclusters and returns job ID of the run
	string ws_id - workspace id
	string kbgid - kbase genome id kbgid
	list<string> geneids - list of kb gene ids
	string job_id - identifier of MAK job
	*/
	funcdef search_MAK_results_from_ws(string ws_id, string kbgid, string data_type, list<string> geneids) returns(MAKBiclusterSet mbs) authentication required;
	
	
};
