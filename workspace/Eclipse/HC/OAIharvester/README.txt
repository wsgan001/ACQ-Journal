
run the command to download CiteSeerx data.

1. cd filepath of OAIharvester.

2. java -classpath .:harvester2.jar:log4j-1.2.12.jar:xalan.jar:xercesImpl.jar:xml-apis.jar ORG.oclc.oai.harvester2.app.RawWrite -out filepath:citeseerx.xml  http://citeseerx.ist.psu.edu/oai2