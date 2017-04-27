README

BaTS version 0.9 beta

=====================

By Joe Parker, 
Dept. Zoology
Oxford UK
joe.parker@zoo.ox.ac.uk


1. Version
2. Package contents
3. Instructions
4. Copyright


----------------------



1. Version
This is the first publicly-availavle release of BaTS (a beta release) but does not contain a GUI interface or some other I/O refinements. As such this is designated the 0.9 release.


2. Package contents
This package contains the core java package, BaTS_beta.jar, as well as this readme and example files


3. Instructions
To run BaTS you will need Java version 1.5.0 or higher, also known as 'J2SE 5.0'. For full instructions, including advice on how to install Java 1.5.0 on Mac OS X, please refer to the manual.


4. Copyright
The programmer asserts the right to be identified as author of this work, the v.0.9 release of BaTS. 
BaTS v0.9 is released under GNU Lesser General Public License, Version 3. See http://www.gnu.org/licenses/lgpl.html

5. Input
BaTS takes a modified nexus format. Instead of the 'translate' or 'taxa' list, there is a 'states' block containing the names of tips _as they appear in the treefile_ and states:
e.g.:
===
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
===

6. Usage
BaTS runs in either 'single' or 'batch' mode. Single mode outputs a detailed analysis on a single trees file, while batch mode analyses and returns summary statistics based on a whole directory.

e.g. user:% java -jar BaTS_beta_build2.jar [batch|single] [file|or dir] [null replicates] [max states]

Email the author for any queries.
