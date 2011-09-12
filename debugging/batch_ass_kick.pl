#! /usr/bin/perl


print "\t$sim $rep\n";

for($i = 0;$i<29;$i++){
	system(`java -Xmx1g -jar Befi-BaTS_v0.1.jar single ./batch_data/E/untitledtext.trees 10 2 > E_0010`);
}


nohup java -Xmx1g -jar Befi-BaTS_v0.1.jar single ./batch_data/E/untitledtext.trees 10 2 > E_0010 &