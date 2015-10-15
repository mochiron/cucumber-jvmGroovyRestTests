package tdpDD

// use this URL for more help: https://softnoise.wordpress.com/2014/01/27/grails-cucumber-with-httpbuilder-remotecontrol/
// https://github.com/gschueler/groovy-rest

import org.apache.log4j.Logger
import cucumber.runtime.*;
import groovy.util.slurpersupport.GPathResult

import static groovyx.net.http.ContentType.*
import static groovyx.net.http.Method.*

import org.apache.http.client.*

import grails.converters.*
import org.codehaus.groovy.grails.web.json.*; // package containing JSONObject, JSONArray,...
import groovyx.net.http.*

import groovy.json.JsonSlurper

this.metaClass.mixin(cucumber.runtime.groovy.Hooks)
this.metaClass.mixin(cucumber.runtime.groovy.EN)

def log = Logger.getLogger(this.class)
def host = 'https://website.cloudhub.io'
def loginPath = '/api/session'
def baseDir = 'src/test/resources/'
def uri = null
def path = ""
def restClient = null
def resp = null
def resourceUrl = ""
def status = 0
def requestBody= []
def requestHeaders= []
def requestFile = null
def token=""

Given(~"I access the resource url \"([^\"]*)\"") { String url ->
  resp = null
  status = null
  method = 'get'
  uri = new URIBuilder(host)
  path = url
  requestHeaders = []
}

Given(~"I post to the resource url \"([^\"]*)\"") { String url ->
  resp = null
  status = null
  method = 'post'
  uri = new URIBuilder(host)
  path = url
  requestHeaders = []
}

Given(~"I provide parameter \"([^\"]*)\" as \"([^\"]*)\"") { String name, String value ->
	uri.addQueryParam name, value
}

Given(~"I provide header \"([^\"]*)\" as \"([^\"]*)\"") { String name, String value ->
  requestHeaders.add(name)
  requestHeaders.add(value)
}

Given(~"I provide a body request file from \"([^\"]*)\"") { String requestPath ->
  requestFile = new File(baseDir + requestPath)
  requestBody = new JsonSlurper().parseText(requestFile.text)
}

Given(~"I'm logged with the username \"([^\"]*)\" and the password \"([^\"]*)\"") { String username, String password ->
  requestHeaders.add('username')
  requestHeaders.add(username)
  requestHeaders.add('signature')
  requestHeaders.add(password)

  try {
    restClient = new RESTClient(uri.toString())
    resp = restClient.post(
      body: [],
      path: loginPath,
      requestContentType: JSON,
      headers: requestHeaders.toSpreadMap()
    )
  } catch (HttpResponseException ex) {
    status = ex.getStatusCode()
  }
  if (resp != null) {
    parsed = resp.data
    token = "bearer "+ parsed.access_token.toString()
    log.warn("token: " +token)
    requestHeaders.add('username')
    requestHeaders.add(username)
    requestHeaders.add('Authorization')
    requestHeaders.add(token)
    requestHeaders.add('Agent-Id')
    requestHeaders.add('client_credentials')

    log.warn(requestHeaders)
  }

}


When(~"I retrieve the results") { ->
  log.warn("HEADERS: "+requestHeaders)
  try {
    restClient = new RESTClient(uri.toString())
    if (method == 'get'){
      restClient.defaultRequestHeaders.'Content-Type' = "application/json"
      resp = restClient.get(
        path: path,
        headers: requestHeaders.toSpreadMap()
      )
    } else {
      //method='post'
      resp = restClient.post(
        body: requestBody,
        path: path,
        requestContentType: JSON,
        headers: requestHeaders.toSpreadMap()
      )
    }
  } catch (HttpResponseException ex) {
		status = ex.getStatusCode()
	}
  if (resp != null) {
    status = resp.status
    assert ( resp.data instanceof net.sf.json.JSON )
    assert resp.data.size() > 0
    parsed = resp.data
  }
}

Then(~"the status code should be (\\d+)") { int expectedStatusCode ->
	assert status == expectedStatusCode
}

Then(~"it should have the field \"(.*)\" containing the value \"(.*)\"") { String field, String value ->
  log.warn("value found: " +parsed."${field}".toString())
	assert parsed."${field}".toString().equals(value)
}
