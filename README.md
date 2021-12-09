# Pokemon API 

#### Prerequisites
Please ensure Docker is installed on your machine.

#### How to run
To run the Pokemon API, simply copy-and-paste the following command into your terminal:
```
docker build -t pokemon-api . && docker run -p 8080:8080 pokemon-api
```
This will expose the pokemon-api on your local machine via your Docker host, binded to port 8080, which can then be accessed via <DockerHost>:8080 (your Docker host is normally localhost, unless you're running Docker Machine).

The following two endpoints are available for use:
```
/pokemon-api/{pokemon}
/pokemon-api/translated/{pokemon}
```


#### What would I do differently?
Had this application been required to run on a production environment, we'd of course need to run a number of replicas to ensure high availability (HA), and as a means of enabling rolling deployments. We'd, of course, also need to configure our connection and read timeouts a bit more scientifically (the values assigned are arbitrary, 'sensible' values), and we'd probably want to have our containers orchestrated/managed by some container orchestration tool like Kubernetes.

Dependent on the volume of requests/traffic requirements, we could also look to utilise a more sophisticated caching solution, though I would still be reticent to introduce a distributed caching solution unless performance was a key metric.

I would, of course, also look to introduce some logging framework (i.e. Logback), in alignment with the enterprise standards/architecture, to ensure our logs are persisted in some uniform format, for the consumption of some central logging aggregation tool.

Assuming this API was exposed to the public internet, we would also need to ensure we sanitise inputs appropriately. I would also look to provide some formal API documentation (i.e. OpenAPI), and implementing provider-side contract testing as a means of ensuring the provided contract is accurate/syntactically correct.
