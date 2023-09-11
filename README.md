# CachedHTTPSclient

This is small Quarkus Service for calling an external resource over HTTPS,
and than caching the response for later use.

## Usecase
Many internal Service are calling an external and/or rate limited service

## How to use
Adding the external resource URL into "baseDomain" and "basePath" in the env. 
https://quarkus.io/guides/config-reference


### Calling the Endpoint with only one Parameter
**Call:**
/rest?parameter=abc
\
**outgoing Call:**
?abc


### Calling the Endpoint with more than one Parameter
**Call:**
/rest?parameter=abc:123:def:456
\
**outgoing Call:**
?abc=123&def=456