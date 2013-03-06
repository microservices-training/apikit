
package org.mule.module.apikit.rest.resource.base;

import static com.jayway.restassured.RestAssured.given;

import org.mule.tck.junit4.FunctionalTestCase;
import org.mule.tck.junit4.rule.DynamicPort;
import org.mule.util.IOUtils;

import com.jayway.restassured.RestAssured;

import org.hamcrest.Matchers;
import org.junit.Rule;
import org.junit.Test;

public class BaseResourceSwaggerFunctionalTestCase extends FunctionalTestCase
{

    @Rule
    public DynamicPort serverPort = new DynamicPort("serverPort");

    @Override
    protected void doSetUp() throws Exception
    {
        RestAssured.port = serverPort.getNumber();
        super.doSetUp();
    }

    @Override
    protected String getConfigResources()
    {
        return "org/mule/module/apikit/rest/resource/base/base-functional-config.xml, org/mule/module/apikit/test-flows-config.xml";
    }

    @Test
    public void getHtml() throws Exception
    {
        given().header("Accept", "text/html")
            .expect()
            .response()
            .statusCode(200)
            .contentType("text/html")
            .body(
                Matchers.equalTo(IOUtils.getResourceAsString(
                    "org/mule/module/apikit/rest/expected-index.html", getClass())))
            .when()
            .get("/api");
        given().header("Accept", "text/html")
            .expect()
            .response()
            .statusCode(200)
            .contentType("text/html")
            .body(
                Matchers.equalTo(IOUtils.getResourceAsString(
                    "org/mule/module/apikit/rest/expected-index.html", getClass())))
            .when()
            .get("/api/");
    }

    @Test
    public void getHtmlSwaggerDisabled() throws Exception
    {
        given().header("Accept", "text/html")
            .expect()
            .response()
            .statusCode(404)
            .header("Content-Length", "0")
            .when()
            .get("/apiSwaggerDisabled");
        given().header("Accept", "text/html")
            .expect()
            .response()
            .statusCode(404)
            .header("Content-Length", "0")
            .when()
            .get("/apiSwaggerDisabled/");
    }

    @Test
    public void getResources() throws Exception
    {
        given().header("Accept", "application/x-javascript")
            .expect()
            .response()
            .statusCode(200)
            .contentType("application/x-javascript")
            .body(
                Matchers.equalTo(IOUtils.getResourceAsString(
                    "org/mule/module/apikit/rest/swagger/lib/swagger.js", this.getClass())))
            .when()
            .get("/api/_swagger/lib/swagger.js");
    }

    @Test
    public void getResourcesSwaggerDisabled() throws Exception
    {
        given().header("Accept", "application/x-javascript")
            .expect()
            .response()
            .statusCode(404)
            .header("Content-Length", "0")
            .when()
            .get("/apiSwaggerDisabled/_swagger/lib/swagger.js");
    }

    /*
     * Default behavior is to provide swagger meta-data (json) for all resources/operation including those
     * that are protected with access controls
     */
    @Test
    public void getSwaggerJson() throws Exception
    {
        given().header("Accept", "application/swagger+json")
            .expect()
            .response()
            .statusCode(200)
            .contentType("application/swagger+json")
            .body(
                Matchers.equalTo("{\"apiVersion\":\"1.0\",\"swaggerVersion\":\"1.0\",\"apis\":[{\"path\":\"/leagues\",\"description\":\"\"},{\"path\":\"/teams\",\"description\":\"\"}]}"))
            .when()
            .get("/api");
        given().header("Accept", "application/swagger+json")
            .expect()
            .response()
            .statusCode(200)
            .contentType("application/swagger+json")
            .body(
                Matchers.equalTo("{\"apiVersion\":\"1.0\",\"swaggerVersion\":\"1.0\",\"apis\":[{\"path\":\"/leagues\",\"description\":\"\"},{\"path\":\"/teams\",\"description\":\"\"}]}"))
            .when()
            .get("/api/");
    }

    public void getSwaggerJsonSwaggerDisabled() throws Exception
    {
        given().header("Accept", "application/swagger+json")
            .expect()
            .response()
            .statusCode(404)
            .header("Content-Length", "0")
            .when()
            .get("/apiSwaggerDisabled");
        given().header("Accept", "application/swagger+json")
            .expect()
            .response()
            .statusCode(404)
            .header("Content-Length", "0")
            .when()
            .get("/apiSwaggerDisabled/");
    }

    @Test
    public void getSwaggerJsonProtectedInterface() throws Exception
    {
        given().header("Accept", "application/swagger+json")
            .expect()
            .response()
            .statusCode(401)
            .when()
            .get("/protectedapi");
        given().header("Accept", "application/swagger+json")
            .expect()
            .response()
            .statusCode(401)
            .when()
            .get("/protectedapi/");
    }

}
