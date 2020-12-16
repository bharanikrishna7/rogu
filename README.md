<img align="right" src="logos/rogu.jpg" height="170px" width="170px" style="padding-left: 20px"/>
# ROGU
Modern database query and access library for Scala/Java

The aim of Rogu is to abstract | automate all teh steps after generating connection till retrieving execution results. This includes:
* Prepare Statement
* Execute Query | Operation
* Retrieve Resultsets
* Parse Results to Scala | Java class.

The most appropriate use case for this library | framework would be to quickly query and retrieve results parsed as scala | java objects. 

## OFFICIALLY SUPPORTED DATABASES
Any jdbc-connector which generates java.sql.Connection type of object should be natively supported since we are not using any libraries (or) high level objects (or) APIs.
Currently we support:
* All Databases which support jdbc connections
* Connection Pool support (which use jdbc connections. ex: HikariCP and BoneCP)

## WHEN TO USE AND AVOID
This library is very useful when performing small to medium level of requests or jobs. The major use case of this is directly get results as Scala (or) Java native objects.
### SHOULD USE
* Small to medium level of jobs or tasks.
* Require retrieving data directly as scala or java objects.
* Resultsets are not massive. For this purpose please use other solution(s).
* Freequently used database operations in Service or Application. 

### SHOULD [NOT] USE
* Heavy compute tasks or jobs. Can perform but it's not officially supported.
* Retrieving huge result sets. This will cause JVM to hog a lot of memory and eventually the JVM will be out of heap space.