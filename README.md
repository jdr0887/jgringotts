JGringotts
==========

store passwords in encrypted derby database

Build
=====

mvn clean install

Run
===

java -XX:-UseSplitVerifier -Dderby.system.home=$HOME/.jgringotts/derby -jar jgringotts-1.0-SNAPSHOT-jar-with-dependencies.jar 
