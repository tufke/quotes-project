# quotes-service

This service implements an API with which you can maintain quotes.

Read the following instructions to get an understanding of the service.


[[_TOC_]]


## The assignment
Your assignment is to build a web service. The main API endpoint of your service should
display a random quote, retrieved from a remote source (for example
https://dummyjson.com/quotes or https://zenquotes.io/api/random). Every request should
respond with another random quote. All other requirements are up to you. Be as creative as
you like, impress us! In general try to approach this assignment as a production project
(within reason).

I decided to go for a Springboot service with a rest api and json payload. The database is an in memory hsql database and
hibernate is used for persistence. To make the domain a little more complicated than just a Quote table it was made 
possible to rate a quote. A logged-in user can rate a quote only once and can update or delete his own quotes and ratings. 

the possible ratings are:
- agree
- neutral
- disagree

The random quote can be requested by calling the getRandomQuoteService.

There is a scheduler in the service that runs right after server startup and every day at 4 AM, in the morning. 
Every time the scheduler runs a call is done to the dummyjson service to retrieve all quotes they have listed. 
These quotes are added to the database if they are not yet added. 

Apart from the scheduler it is also possible to add new quotes manually into the database. 

There is no frontend implemented, I focussed on setting up a decent scalable and maintainable backend with as much 
code generation as possible to keep things simple and clean. All boilerplate code is in the build folder.

code generation is done using:
- openapi
- mapstruct
- lombok

There are unit tests available. they are not complete yet, I focussed on showing different type of tests not on completeness.
Some will load the full context, others will use mockito. 

There is also a mutation test task available. in a CI/CD pipeline I would also add things like Sonarqube and dependency check
to assure code quality. 


### Testing the assignment
To run this project you have to follow these steps:

make sure you have these tools installed on your machine:
- Git
- Gradle
- Java 17

Make sure your proxy (if you are behind one) is configured in global gradle properties.

open a console or use eclipse or intellij.

clone the 2 git repositories for this assignment:
- git clone https://github.com/tufke/spring-boot-starter.git
- git clone https://github.com/tufke/quotes-project.git

build and publish the projects: gradlew publishToMavenLocal
1. from the root of the spring-boot-starter project
2. from the root of the dummyjson-external-api folder
3. from the root of the quotes-service-api folder
4. from the root of the quotes-service folder

The 3 submodules in the quotes-project are configured as composite projects meaning they could also be placed in their
own git repositories. 

to run the service: gradlew bootrun
- from the root of the quotes-service folder

This will start a server at port 11000. open a browser and navigate to:
- http://localhost:11000/quotes-service

you will get redirected to the swagger-ui page that was included during the build of the service 
available on /quotes-service/swagger-ui/index.html

<img src="images/swagger-ui.png" alt="Swagger-ui" width="500">

in the top right corner you can click on the authorize button to enter needed request headers that should be available
in every rest call. 


<img src="images/authorize.png" alt="Swagger-ui Authorize" width="500">

enter a securitytoken (anything will do for now) and enter an userid. Do not forget to push both `authorize` buttons


<img src="images/get-random-quote.png" alt="Swagger-ui GetRandomQuote" width="500">

open GetRandomQuote and click on the `Try it out` button. You will see a big blue bar to execute a call to the getRandomQuote 
endpoint. When you scroll down you will see some possible responses. the first one is an example of the success response 
we want to see. press the blue execute bar.


<img src="images/get-random-quote-result.png" alt="Swagger-ui GetRandomQuoteResult" width="500">

After executing the getRadomQuote call you will see the json response in the black box. 


## Functional summary
This service serves the quotes domain. Other services in the landscape 
could be a user-service to maintain user information and an authentication-service to log in a user. 

The quotes domain currently consists of 2 objects: Quote and Rating. 

```text
  QUOTE
    1      A Quote has zero or more Ratings
    |       
    |
   0.n     A Rating belongs to 1 Quote
  RATING
```

The api has service operations to apply typical crud methods to this domain:
- [CreateQuoteService](src%2Fmain%2Fjava%2Fnl%2Fkabisa%2Fservice%2Fquotes%2Frest%2Fimpl%2FCreateQuoteService.java)
- [CreateRatingService.java](src%2Fmain%2Fjava%2Fnl%2Fkabisa%2Fservice%2Fquotes%2Frest%2Fimpl%2FCreateRatingService.java)
- [DeleteQuoteByIdService.java](src%2Fmain%2Fjava%2Fnl%2Fkabisa%2Fservice%2Fquotes%2Frest%2Fimpl%2FDeleteQuoteByIdService.java)
- [DeleteRatingByIdService.java](src%2Fmain%2Fjava%2Fnl%2Fkabisa%2Fservice%2Fquotes%2Frest%2Fimpl%2FDeleteRatingByIdService.java)
- [GetQuoteByIdService.java](src%2Fmain%2Fjava%2Fnl%2Fkabisa%2Fservice%2Fquotes%2Frest%2Fimpl%2FGetQuoteByIdService.java)
- [GetQuotesService.java](src%2Fmain%2Fjava%2Fnl%2Fkabisa%2Fservice%2Fquotes%2Frest%2Fimpl%2FGetQuotesService.java)
- [GetRandomQuoteService.java](src%2Fmain%2Fjava%2Fnl%2Fkabisa%2Fservice%2Fquotes%2Frest%2Fimpl%2FGetRandomQuoteService.java)
- [GetRatingByIdService.java](src%2Fmain%2Fjava%2Fnl%2Fkabisa%2Fservice%2Fquotes%2Frest%2Fimpl%2FGetRatingByIdService.java)
- [GetRatingsByQuoteIdService.java](src%2Fmain%2Fjava%2Fnl%2Fkabisa%2Fservice%2Fquotes%2Frest%2Fimpl%2FGetRatingsByQuoteIdService.java)
- [UpdateQuoteByIdService.java](src%2Fmain%2Fjava%2Fnl%2Fkabisa%2Fservice%2Fquotes%2Frest%2Fimpl%2FUpdateQuoteByIdService.java)
- [UpdateRatingByIdService.java](src%2Fmain%2Fjava%2Fnl%2Fkabisa%2Fservice%2Fquotes%2Frest%2Fimpl%2FUpdateRatingByIdService.java)


## Generic configuration in spring boot starters
Every service needs to set up things like security, logging, exception handling and serialization and deserialization config.
This could be done in every service over and over again, but usually it is convenient to arrange these solutions centrally,
so they can be maintained and delivered once after which the services can do life cycle management and increase the
framework version.

This service was set up as if it is part of a company platform. All version management and architectural requirements
are secured in this platform. The services have a dependency on this platform and only have to focus on implementing
functional requirements. 

## Generating code from apis
The api of this service is specified using openapi in project `quotes-service-api`. All boilerplate code such as controllers and
model objects are generated with openapi and placed in the `openapi` folder in the `build` directory.

To make a call to the dummyjson web server and retrieve quotes a client is generated. The api of this dummyjson service 
is specified using openapi in project `dummyjson-external-api`

Below is the directory structure and overview of important generated classes

```text
    - build
        - openapi
            - dummyjson-external-api
                - src/main/java/nl/kabisa/service/quotes/dummyjson/client
                    - api
                        - getQuotesApi
                    - application
                        - ApiClient
                    - model
                        - GetQuotesResultDummyApi
                        - QuoteDummyApi
            - internal-api
                - src/main/java/nl/kabisa/service/quotes
                    - rest
                        - api
                            - CreateQuoteApi
                            - CreateQuoteApiController
                            - CreateQuoteApiDelegate
                            <etc>
                        - configuration
                            - HomeController
                            - SpringDocConfiguration
                        - model
                            - Problem
                            - Quote
                            - Rating
                            <etc>
            - service-api
                - dummyjson-external-api
                - quotes-service-api
```

All api specifications needed in this service are extracted to the `service-api` folder. The code for the api of your 
service is generated in the `internal-api` folder. The client code to call other services is generated in a folder 
named after the openapi spec openapi generates it from.

### service-api
This folder contains all openapi specification yaml files needed in the service. They are extracted with the extractServiceApi
gradle task. This task takes all dependencies in the serviceApi scope and copies the yaml files from them to the `service-api` 
folder

### client folders
all generated client code is placed in a folder named after the openapi yaml it was generated from. each client will be placed in 
its own folder. 

- `basepackage.client.api`
  - getQuotesApi, represents the api of the resource to be called. To make a call autowire this class and call method. 
    This class must be configured as a bean in a springboot application
- `basepackage.client.application`
  - ApiClient, classes from the `basepackage.client.api` package use this class to make the actual call to the resource.
    this class can be configured as a bean in a springboot application to configure connection properties and request parameters.
    If request specific headers are needed, like userid and securityToken, make sure the bean has prototype scoope. 
- `basepackage.client.model`
  - The components the resources in openapi yaml use as input and output

### internal-api
The internal api is generated in the `internal-api` 

- `basepackage.rest.api`
  - For every resource in the `openapi.yml` a interface, controller and delegate is generated
- `baspackage.rest.configuration`
  - HomeController, forwards request to root context to `swagger-ui` page
  - SpringDocConfiguration, configuration needed for swagger-ui to work correctly
- `basepackage.rest.model`
  - The components the resources in openapi yaml use as input and output

## Implementing the apis

To start implementing an api you need to implement the generated delegate from the `build.openapi.internal-api.basepackage.rest.api`
folder. All Classes that implement generated delegates are in `basepackage.rest.impl` package. They all override 
the interface methods from the delegate. By default, the delegate will return a http status `501 NOT IMPLEMENTED` if
there is no implementation of the delegate found. By overriding the delegate we can give it an implementation and return 
a suitable http status and body. 

## security

This service is not really secured yet. To start with there are 2 request headers needed to call this service.
`x-security-token` and `x-user-id`. The only check currently is that they should be present and contain a value. 
This was done for simplicity of this project. 

The 2 request headers are checked in the RequestInterceptor in `basepackage.interceptor`. If not present 
a client exception is thrown. If they are present they will be put in the application context on the thread local.
see `basepackage.context`. 

Since `spring-security` is on the class path this can later be used to log in a user and pass a role and userid. 
This could be put in a JWT token and passed as security token to the service. 

Flow would be something like this:
1. User Login: The client sends a POST request to /api/auth/login with username and password. permitAll is set for endpoint.
2. JWT Generation: If the credentials are valid, the server generates a JWT token and returns it to the client.
3. Token Usage: The client includes the JWT token in the Authorization header of subsequent requests to access protected endpoints.
4. Token Validation: The service has a JwtAuthenticationFilter that intercepts requests, extracts the token, validates it, 
   and sets the authentication in the application context.

## Mapping to and from api model

In the api implementing classes there is a need to map input objects to the database model objects and also the inverse
way from the database model back to the api classes. 

mapping is a tedious task and error-prone, therefor MapStruct is used to generate this mapping code from an interface
using annotations. Rule of thumb is that only one site of the relation between 2 objects is mapped to avoid cyclic 
errors. Although there are solutions to avoid this it is much friendlier to think about how you want to use the data 
and only map what you need on the site where you want it to be available. The Quote object has a List of ratings for
instance but the Rating does not have a Quote mapped. 

## Database model

The database model is mapped on both sites. Hibernate retrieves the relations lazily avoiding cyclic problems. 
Each entity has getters, setters and an add method to make sure the relations are always in sync. It does not matter
if you add a Rating to a quote by adding it to the rating list in the Quote entity or by just saving the Rating.

Lombok is used with care in the entity classes to avoid problems with the toString, equals and hashcode. 
See the javadoc in those classes. 


## Dummyjson client configuration

Openapi generates the java client that can be used to retrieve quotes from Dummyjson.com. This client must be
configured as a bean if you want to be able to autowire it in your code. To do so you have to at least configure
the generated <resourcename>Api class as a bean. This class uses the generated ApiClient class internally for the actual call
to the external resource. Usually it is required to configure the connection setting in this client. To do so, also 
configure the ApiClient as a bean and let the <resourcename>Api bean use this ApiClient bean. If you have to set 
request specific parameters like userid and securitytoken make sure the bean is in prototype scope.
