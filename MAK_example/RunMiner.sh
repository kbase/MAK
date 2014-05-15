#!/bin/bash

java -classpath ../MAK.jar:/usr/lib64/R/library/rJava/jri/JRI.jar -Djava.library.path=/usr/lib64/R/lib/:/usr/lib64/R/library/rJava/jri/ DataMining.RunMiner -param yeast_TF_inter_feat_2to200_001_parameters.txt &> RunMiner.out&




