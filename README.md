# cachedhttpclient

This is small Quarkus Service for calling an external resource, but caching the response.

## Usecase
Many internal Service are calling an external and/or rate limited service

## How to use
Adding the external resource URL into "baseURL" in the env. 
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