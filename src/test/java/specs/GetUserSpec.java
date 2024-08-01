package specs;

import io.restassured.specification.RequestSpecification;

import static helpers.CustomAllureListener.withCustomTemplates;
import static io.restassured.RestAssured.with;
import static io.restassured.http.ContentType.JSON;

public class GetUserSpec {
    public static RequestSpecification getUserSpec = with()
            .filter(withCustomTemplates())
            .log().uri()
            .log().headers()
            .contentType(JSON);
}
