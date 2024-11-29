# Service definition with openapi

This service definition is an openapi api implementation for the [dummyjson service](https://github.com/Ovi/DummyJSON/tree/master)
project on GitHub. 

Read the following instructions on how to set up an api project for an openapi generated service.


# Table of Contents
- [service api project name](#service-api-project-name)
- [Api development guidelines](#api-development-guidelines)
- [Create a yaml file](#create-a-yaml-file)
- [Structure of yaml file](#structure-of-yaml-file)
- [sections openapi, info and servers](#sections-openapi-info-and-servers)
- [Section paths](#section-paths)
- [Section security](#section-security)
- [Configure openapi validation](#configure-openapi-validation)
- [Add openapi validation to the build.gradle](#add-openapi-validation-to-the-gradle-build)
- [Configure openapi code generation (Optional)](#configure-openapi-code-generation-(optional))


## service api project name
To make clear from the name of your service api in which service it is implemented it is wise to name the
service api and the implementing project the same. for instance `example-service-api` is the service api that is
implemented in `example-service`. Keep the naming consistent, so you know immediately where the implementation can be found.

When we have an OpenApi yaml specification for an external service implementation, name the service api `example-external-api`.
The name will immediately make clear that the implementation of the service is external and the api is only used to generate
client code to call the external service. 


## Api development guidelines

Microservices development begins with API definition outside the code and ideally involves ample peer-review feedback
to achieve high-quality APIs. API First encompasses a set of quality-related standards and fosters a peer review culture
including a lightweight review procedure.

Goal is to ensure that APIs:
- are easy to understand and learn
- are general and abstracted from specific implementation and use cases
- are robust and easy to use
- have a common look and feel
- follow a consistent RESTful style and syntax
- are consistent with other APIs and the global architecture

See [Zalando restful api guidelines](https://opensource.zalando.com/restful-api-guidelines)for rules to apply to your openapi rest specification 
  
## Create a yaml file
under `src/main/resources` create a new file with extension `.yaml`.
Name the file after the project. i.e. `example-service-api` or `example-external-api`

### Structure of yaml file
See the [openapi specification](https://swagger.io/specification/) to learn how to code an openapi yaml file. 

An openapi yaml file must have the following sections:
- openapi - Version of openapi spec
- info - Api name, description and version
- servers - Basepath of all requests
- paths - Urls the service listens to
- components - Reusable model objects referenced to from the paths
- security - Security headers needed

### sections openapi info and servers
By default the **openapi**, **info** and **servers** section should look like this:

```yaml
openapi: "3.0.2"
info:
  x-api-id: 52a76924-cce8-429b-a95e-fe24e3dc12ab
  x-api-type: external
  title: Example service to retrieve data
  version: "1.0.0"
  description: API Definition for communication with service
  contact:
    name: Developer
    email: developer@company.com
servers:
  - url: /api
```
- `x-api-id`. Generate a unique uuid for your api, you can use [uuid generator](https://www.guidgenerator.com/online-guid-generator.aspx) for instance
- `x-api-type` can have **external** or **internal** for instance, depends on the kind of services there are
- `title` name of your project 
- `version` the version of your yaml spec, this will most likely be the same as the version in the `build.gradle` file
- `description` meaningful description telling where the api is used
- `contact` You could mention your name so people know who to contact
- `servers` this should contain `/api` it will be the first segment path in the url.  

### Section paths
Configure all paths you need under the `paths:` section. A path is an url with rest methods like Get, Put, Post etc. beneath it.
each method should have a tag, summary, and operationId and optionally a requestBody. Always add descriptions these will be visible
in the generated documentation like swagger-ui.

Use default responses and reference the generic Problem object when you are using the generic exception handling from
the service-spring-boot-starter project. This generic exception handling will wrap all exceptions in a Generic Problem object
and return that object to the client. In the api we must tell the client that this Problem object is returned in case of errors.


```yaml
  /examples/{example_id}:
    parameters:
      - $ref: '#/components/parameters/example_id'
    post:
      tags:
        - createExample
      summary: Create a new example
      operationId: createExample
      requestBody:
        description: >
          A Json object containing all fields needed to create a Example
        required: true
        content:
          application/json:
            schema:
              $ref: '#/components/schemas/CreateExample'
      responses:
        '201':
          description: >
            A Json object containing the response after creating an example, this holds at least the newly created id
          content:
            application/json:
              schema:
                $ref: '#/components/schemas/Example'
        default:
          $ref: '#/components/responses/Problem'
```

### section components
The components section contains parameters, responses and schemas which get referenced from the paths section.
make sure all your schemas have example values these are also used in the generated documentation and will make it easy
to quickly test a path with swagger-ui as the example values are prefilled in the example requests.


### Section security
The security schemas are under the components section and contain the request headers which are required when 
calling the service.
By placing them in the api the client will know they must be provided and can validate on it before sending requests.
You can also generate client code from the api with the headers included because they are in the api. 
Security in the api spec is also needed for the generated documentation, swagger-ui will show an Authorize button with 
which you can set values for these headers which will be sent with each request.

under the security section the used security schemas are mentioned to activate the security. Note there is one - in front of
securitytoken and not in front of userid. this means it is an AND relationship. Both schemas apply. if you put an - in front of
userid too it will be an OR relationship, meaning one of the two headers must be in the request header. 

```yaml
  securitySchemes:
    securitytoken:
      type: apiKey
      name: x-security-token
      in: header
      description: Requests should pass a x-security-token header.
    userid:
      type: apiKey
      name: x-user-id
      in: header
      description: Requests should pass a x-user-id header.

  security:
    - securitytoken: []
      userid: []
```

## Configure openapi validation
To make sure the yaml is correct, validate it with the openapi validator. 


### Add openapi validation to the gradle build
To add validation of the openapi yaml file check the `build.gradle` file. below is example task to validate. 

```groovy
task validateOpenapiSpec(type: ValidateTask, dependsOn: ['extractServiceApi']) {
    inputSpec = "$serviceApiDir/$internalApiFilename"
    recommend = true
}
```

## Configure openapi code generation (Optional)
It's not needed to generate code in a service api project. The code will be generated in the service that implements
the api. It might however be useful to check the generated code while developing your api.

