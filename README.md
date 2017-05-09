# befi-bats-gui

## About ##
**BaTS** (Bayesian analysis of tip significance, loosely bacronymised...) is a standalone program for the analysis of phylogenetic data. It asks the question: 'given this distribution of traits on taxa, how likely is this pattern to have arisen by chance?'. The current stable version of this package is v0.9.0 (see below).

It assumes discrete trait values, known for each taxon unambiguously. The phylogenetic relationship between the taxa must be represented as a posterior set of trees, e.g. a collection of trees assumed to correctly sample the posterior distribution of the phylogeny. Usually these will have been inferred from molecular data using [BEAST](https://www.beast2.org/) or [MrBayes](http://mrbayes.sourceforge.net/). 

The general approach is described in [Parker *et al.* (2008)](http://doi.org/10.1016/j.meegid.2007.08.001) ([.pdf](http://evolve.zoo.ox.ac.uk/Evolve/Oliver_Pybus_files/CorrelatingViralPhenotypes.pdf) copy).

**'Befi-BaTS'** is a further development of this approach. It comprises new methods (NTI/NRI, UniFrac, and PD - trait-association statistics that incorporate branch lengths as well as tree topology) and an improved user interface which should be easier to use and interpret. This is currently in alpha at v0.10.1 (see below).

This repository contains code for both.

## Citation ##
To support our work, please cite: 
J Parker, A Rambaut, OG Pybus(2008) Correlating viral phenotypes with phylogeny: accounting for phylogenetic uncertainty. *Infection, Genetics and Evolution* **8**(3):239-246 [doi:10.1016/j.meegid.2007.08.001](http://doi.org/10.1016/j.meegid.2007.08.001) ([.pdf](http://evolve.zoo.ox.ac.uk/Evolve/Oliver_Pybus_files/CorrelatingViralPhenotypes.pdf) copy)

## Installation ##
**Requirements**: This software requires Java. BaTS v0.9.0 (retroactive naming; referred to as 'Version 1' in the 2008 paper) requires J2SE / Java 1.5+. The current (v0.10.1) Befi-BaTS requires Java 1.7+.

**Downloads**: The current BaTS jarfile, manual and example files can be downloaded [here](https://github.com/lonelyjoeparker/befi-bats-gui/releases/tag/0.9) with additional instructions and detail [here](https://github.com/lonelyjoeparker/befi-bats-gui/blob/master/BaTS_beta_build2/README.md).

**Running BaTS**: Please see the [manual](https://github.com/lonelyjoeparker/befi-bats-gui/tree/master/BaTS_beta_build2) for details.

## Help and instructions ##
- See: [this README](https://github.com/lonelyjoeparker/befi-bats-gui/tree/master/BaTS_beta_build2/README.md)
- See: [General guidance on BaTS](http://www.lonelyjoeparker.com/?p=354)
- See: Additional material for BaTS, including [teaching slides](http://evolve.zoo.ox.ac.uk/Evolve/BaTS.html)
- See also: special guidelines on Befi-BaTS and BEAST files: http://www.lonelyjoeparker.com/?page_id=274#beast-note

## FAQs ##

* *How do I know what the 'state_0','state_1' etc in the output of the MC statistic refer to?*
  - These occur in the same order as the input states, so if your inputs were 
```
begin states;
1 black, 
2 red, 
3 black,
4 black,
5 red, 
4 green
...
```

Where the input order (ignoring duplicates) is ```{black, red, green}```, the input states will map to the following MC ouptut statistics:

| Input state | MC statistic |
| ----------- | ------------ |
'black' | state_0
'red' | state_1
'green' | state_2
... | state_(*n*)
  
* *How do I report an error/request a change?*
  - Please use the [issue tracker here](https://github.com/lonelyjoeparker/befi-bats-gui/issues) to report bugs or request changes. **Please** check the existing requests first, including closed ones!
 
 ## About BaTS and Befi-BaTS versions:

BaTS has been under development since 2006. As a result there are multiple versions of the software, and the naming/versioning hasn't been all that consistent...

- 'BaTS' is taken to mean the package for estimating the core topology-based trait association statistics (AI, PS and MC), reported in Parker et al. (2008).
- 'Befi-BaTS' referred to a modification to BaTS (under intermittent development during 2007-2009) which incorporates additional statistics based on tree branch lengths (UniFrac, NTI/NRI, and PD) as well as the existing BaTS statistics. This is currently unpublished. 
- To simplify things, **all previous versions have been [retroactively named](https://github.com/lonelyjoeparker/befi-bats-gui/tree/master/binaries/versions.md)** in (reasonably) close accordance with [Semantic Versioning principles](http://semver.org/). 

### BaTS v0.9.0
The **stable, published, topology-statistics-only version of BaTS** has therefore been designated 'v0.9.0' and can be found [here](https://github.com/lonelyjoeparker/befi-bats-gui/tree/master/binaries/BaTS-current).

### Befi-BaTS / BaTS v0.10.1
The latest **development build** of BaTS, a.k.a. 'Befi-BaTS' is currently at v0.10.1, and can be found [here](https://github.com/lonelyjoeparker/befi-bats-gui/tree/master/binaries/Befi-BaTS-development). Note that this is unpublished and can't be considered trusted/tested.

## Changelog
A changelog, including list of previous versions/names, can be found [here](https://github.com/lonelyjoeparker/befi-bats-gui/tree/master/binaries/versions.md), or [here](https://github.com/lonelyjoeparker/befi-bats-gui/tree/master/binaries/versions.xlsx) in .xlsx format.

