## README

# BaTS - version 0.9 beta
 
Joe Parker, 

Dept. Biodiversity Informatics & Spatial Analysis

Royal Botanic Gardens, Kew

Richmond UK

joe.parker@kew.org

*Old address: Dept. Zoology, Oxford UK, joe.parker@zoo.ox.ac.uk*

---

### Contents:

1. Version
2. Package contents
3. Instructions
4. Copyright
5. Input
6. Usage
7. Running the example data
8. Getting further help

---

### 1. Version
This is the first publicly-available release of BaTS (a beta release) but does not contain a GUI interface or some other I/O refinements. As such this is designated the 0.9 release.

### 2. Package contents
This package contains the core java package, BaTS_beta.jar, as well as this readme and example files

### 3. Instructions
To run BaTS you will need Java version 1.5.0 or higher, also known as 'J2SE 5.0'. For full instructions, including advice on how to install Java 1.5.0 on Mac OS X, please refer to the manual.

### 4. Copyright
The programmer asserts the right to be identified as author of this work, the v.0.9 release of BaTS. 
BaTS v0.9 is released under GNU Lesser General Public License, Version 3. See http://www.gnu.org/licenses/lgpl.html

### 5. Input
BaTS takes a modified nexus format (please also see [this note on formatting](http://lonelyjoeparker.com/wp/?page_id=274#beast-note)). Instead of the 'translate' or 'taxa' list, there is a 'states' block containing the names of tips _as they appear in the treefile_ and states, e.g.:

```
#NEXUS
begin states;
1 Brasil,
2 Brasil,
3 China,
4 Brasil,
5 China,
6 Brasil,
end;

begin trees;
...
```

### 6. Usage
BaTS runs in either 'single' or 'batch' mode. Single mode outputs a detailed analysis on a single trees file, while batch mode analyses and returns summary statistics based on a whole directory.

e.g. `user:% java -jar BaTS_beta_build2.jar [batch|single] [file|or dir] [null replicates] [max states]`

### 7. Running the example data
Try running the [example.trees](https://github.com/lonelyjoeparker/befi-bats-gui/blob/master/BaTS_beta_build2/example.trees) test data - this should complete quickly on most computers with 10 replicates. Execute with this command:
```
java -jar befi-BaTS_0-2.jar single example.trees 10 7
```
Which should produce output similar to:
```
Running a single analysis on /Applications/Phylogenetics/BaTS/BaTSpackage/BaTS_beta/example.trees with 10 null replicates and 7 maximum discrete traits.
obs vals: 15
analysing... 30 trees, with 7 states
analysing observed (using obs state data)
Stat order:
	Tree size
	Internal branch size
	AI
	PS
	UniFrac
	NT (combined, distance-based)
	NR (combined, distance-based)
	PD (combined)
	MC (states 1..n)

observed mean	lower 95% CI	upper 95% CU	null mean	lower 95% CI	upper 95% CI	significance
1.14453125	0.910027801990509	1.465386152267456	1.14453125	1.14453125	1.14453125	0.0	
0.7513406276702881	0.5633496046066284	1.008514404296875	0.7513406872749329	0.7513406276702881	0.7513406276702881	1.0
1.5555052757263184	1.1128818988800049	2.160350799560547	12.053610801696777	11.542703628540039	12.780768394470215	0.0
25.733333587646484	22.0	29.0	101.51666259765625	99.23332977294922	105.53333282470703	0.0	
0.5337199568748474	0.43090277910232544	0.6257830262184143	0.022999044507741928	0.008916184306144714	0.0505472831428051	0.10000002384185791	
0.14438751339912415	0.11481654644012451	0.19503583014011383	0.4027658998966217	0.37492889165878296	0.4395662248134613	0.0
7.236220836639404	5.183211326599121	10.466057777404785	24.53736114501953	24.305871963500977	25.065155029296875	0.0
2.0876755714416504	1.4199613332748413	3.0974924564361572	4.125097751617432	4.060738563537598	4.233194828033447	0.0
12.633333206176758	9.0	16.0	1.6600000858306885	1.399999976158142	2.1666667461395264	0.10000002384185791	
19.0	19.0	19.0	1.683333396911621	1.399999976158142	2.3333332538604736	0.10000002384185791	
12.666666984558105	12.0	13.0	1.5533332824707031	1.2999999523162842	2.0	0.10000002384185791	
8.566666603088379	3.0	11.0	1.683333158493042	1.3666666746139526	2.1666667461395264	0.10000002384185791	
11.0	11.0	11.0	1.6600000858306885	1.2666666507720947	2.1666667461395264	0.10000002384185791	
3.433333396911621	2.0	6.0	1.6033332347869873	1.2999999523162842	2.066666603088379	0.10000002384185791	
5.066666603088379	5.0	6.0	1.3766666650772095	1.1333333253860474	1.9666666984558105	0.10000002384185791	
done

	Done.
	Process took 0.01875 minutes (3.1250002E-4 hours)
```

### 8. Getting further help
*(Please also see notes at http://lonelyjoeparker.com/wp/?page_id=274 and http://lonelyjoeparker.com/wp/?p=1255)*

If you are having issues running BaTS please follow the steps below (in all communication **please** make sure you include a description of your operating system, BaTS, and Java version;  and paste in a trace file containing the *exact* command used to run BaTS, any output and errors shown, and the input .trees file header plus a few trees (not thousands). This will greatly help spot issues): 

1. Check you have the [newest version](https://github.com/lonelyjoeparker/befi-bats-gui)
2. Check you have Java SE v1.5 installed
3. Try running the [example.trees](https://github.com/lonelyjoeparker/befi-bats-gui/blob/master/BaTS_beta_build2/example.trees) test data - see above.
4. Check the formatting of your input file, paying particular attention to [non-standard characters and whitespace](http://lonelyjoeparker.com/wp/?page_id=274#beast-note).
5. If your problem still isn't fixed, or seems weird (runs but with odd errors) please [submit a new issue here](https://github.com/lonelyjoeparker/befi-bats-gui/issues). 
6. If none of the above steps work, please [email the author](mailto:joe.parker@kew.org) for more queries. 
