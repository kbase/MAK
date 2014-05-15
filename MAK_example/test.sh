#!/bin/bash
java -Djava.library.path=/kb/dev_container/modules/MAK/so DataMining.RunMiner -param yeast_TF_inter_feat_2to200_001_parameters.txt -debug 1 &> out
