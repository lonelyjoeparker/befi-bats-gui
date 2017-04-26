# befi-bats-gui

## About ##
**BaTS** (Bayesian analysis of tip significance, loosely bacronymised...) is a standalone program for the analysis of phylogenetic data. It asks the question: 'given this distribution of traits on taxa, how likely is this pattern to have arisen by chance?'. It assumes: discrete trait values, known for each taxon unambiguously. The phylogenetic relationship between the taxa must be represented as a posterior set of trees, e.g. a collection of trees assumed to correctly sample the posterior distribution of the phylogeny. Usually these will have been inferred from molecular data using [BEAST]() or [MrBayes](). The general approach is described in [Parker *et al.* (2008)](http://doi.org/10.1016/j.meegid.2007.08.001) [.pdf](http://evolve.zoo.ox.ac.uk/Evolve/Oliver_Pybus_files/CorrelatingViralPhenotypes.pdf).

**'Befi-BaTS'** is a further development of this approach. It comprises new methods (NTI/NRI, UniFrac, and PD - trait-association statistics that incorporate branch lengths as well as tree topology) and an improved user interface which should be easier to use and interpret.

This repository contains code for both.

## Citation ##
To support our work, please cite: J Parker, A Rambaut, OG Pybus(2008) Correlating viral phenotypes with phylogeny: accounting for phylogenetic uncertainty. *Infection, Genetics and Evolution* **8**(3):239-246 [doi:10.1016/j.meegid.2007.08.001](http://doi.org/10.1016/j.meegid.2007.08.001) [.pdf](http://evolve.zoo.ox.ac.uk/Evolve/Oliver_Pybus_files/CorrelatingViralPhenotypes.pdf)

## Installation ##
**Requirements**: This software requires Java. BaTS version 1 (as in the 2008 paper) requires J2SE / Java 1.5+. Befi-BaTS requires Java 1.7+.

**Downloads**: The BaTS jarfile, manual and example files can be downloaded here.

**Running BaTS**: Please see the manual in the BaTS .zip download for details.

## Help and instructions ##
- See: [this README](https://github.com/lonelyjoeparker/befi-bats-gui/tree/master/BaTS_beta_build2/README.md)
- See: [General guidance on BaTS](http://www.lonelyjoeparker.com/?p=354)
- See: Additional material for BaTS, including [teaching slides](http://evolve.zoo.ox.ac.uk/Evolve/BaTS.html)
- See also: special guidelines on Befi-BaTS and BEAST files: http://www.lonelyjoeparker.com/?page_id=274#beast-note

## FAQs ##

* *How do I know what the 'state_0','state_1' etc in the output of the MC statistic refer to?*
  - These will take the same order as the input states, so if your inputs were 'black, red, red, green' (in that order), the input states will map:
    - 'black'=>state_0
    - 'red'=>state_1
    - 'green'=>state_2
    - ... etc
  
* *How do I report an error/request a change?*
 - Please use the [issue tracker here](https://github.com/lonelyjoeparker/befi-bats-gui/issues) to report bugs or request changes. **Please** check the existing requests first, including closed ones!
