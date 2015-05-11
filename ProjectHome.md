Befi-BaTS is a program for the analyisis of phylogenetic data:

Befi-BaTS provides a method by which the degree to which phenotypic traits seen in a phylogeny are associated with ancestry are correlated. In other words, where a set of character states are seen on the tip of a phylogenetic tree, is any given taxon more likely to share a character state with a sister taxon than we would expect due to chance?

This problem has been posed in a variety of contexts over the last three decades, particularly molecular epidemiology and phylogeography. A number of approaches have been developed over the years, of which the method of Slatkin & Maddison (1989) is perhaps the best known.

Befi-BaTS uses two established statistics (the Association Index, AI (Wang et al., 2001), and Fitch parsimony score, PS) as well as a third statistic (maximum exclusive single-state clade size, MC) introduced by us in the BaTS citation, where the merits of each of these are discussed. Befi-BaTS 0.1.1 includes additional statistics that include branch length as well as tree topology. What sets Befi-BaTS aside from previous methods, however, is that we incorporate uncertainty arising from phylogenetic error into the analysis through a Bayesian framework. While other many other methods obtain a null distribution for significance testing through tip character randomization, they rely on a single tree upon which phylogeny-trait association is measured for any observed or expected set of tip characters.

To improve on this basic approach we use posterior sets of trees (PSTs), obtained through earlier Bayesian MCMC analysis of the data, that integrate over all likely phylogenies and incorporate the phylogenetic uncertainty arising from the data. Although a Bayesian MCMC analysis is therefore a precondition to using Befi-BaTS, we do not feel that this is likely to deter potential users since these analyses are increasingly common.

Author: Joe Parker
Befi-BaTS development sponsored by Kitson Consulting Ltd.
BaTS development supported by Oxford University and the Natural Environment Research Council (UK)