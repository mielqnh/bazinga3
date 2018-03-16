package be.mielnoelanders.bazinga.restcontroller;

import be.mielnoelanders.bazinga.BazingaApplication;
import be.mielnoelanders.bazinga.domain.Parameter;
import be.mielnoelanders.bazinga.domain.ParameterEnum;
import org.json.JSONException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.assertj.core.api.Java6Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = BazingaApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)

public class ParameterEndPointIT {

    private static final String BASE_URI = "/api/parm";

    private TestRestTemplate testRestTemplate;

    private HttpHeaders httpHeaders;

    @LocalServerPort
    private int port;

    @Before
    public void init() {
        testRestTemplate = new TestRestTemplate();
        httpHeaders = new HttpHeaders();
    }

    @Test
    public void crudParameterTest() throws JSONException {
        //create Parameter instance
        Parameter newParm = new Parameter();
        newParm.setType(ParameterEnum.PROMOTIONALDISCOUNT);
        newParm.setPercentage(21);

        // testAddOne()
        HttpEntity<Parameter> entityAddOne = new HttpEntity<>(newParm, httpHeaders);
        httpHeaders.setContentType(MediaType.APPLICATION_JSON_UTF8);
        ResponseEntity<Parameter> responseEntityAddOne = testRestTemplate.postForEntity(createURLWithPort(BASE_URI + "/"), entityAddOne, Parameter.class);

        System.out.println("responseEntity.getBody()) = " + responseEntityAddOne.getBody());
        System.out.println("responseEntity.getStatusCode()) = " + responseEntityAddOne.getStatusCode());
        assertThat(responseEntityAddOne.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(responseEntityAddOne.getBody().getType()).isEqualTo(ParameterEnum.PROMOTIONALDISCOUNT);
        Long newId = responseEntityAddOne.getBody().getId();

        // test findall() String
        HttpEntity<Parameter> entity = new HttpEntity<>(null, httpHeaders);
        ResponseEntity<String> response = testRestTemplate.exchange(createURLWithPort(BASE_URI + "/findall"),HttpMethod.GET, entity, String.class);

        System.out.println("responseEntity.getBody()) = " + response.getBody());
        System.out.println("responseEntity.getStatusCode()) = " + response.getStatusCode());
        assertThat(response.getStatusCode()).isEqualTo( HttpStatus.OK);
        String expected = "[{\"id\":1,\"type\":\"PROFITMARGIN\",\"percentage\":30},{\"id\":2,\"type\":\"PREMIUMCUSTOMER\",\"percentage\":10},{\"id\":3,\"type\":\"DAMAGEDISCOUNT\",\"percentage\":20},{\"id\":4,\"type\":\"PROMOTIONALDISCOUNT\",\"percentage\":21}]";
        JSONAssert.assertEquals(expected, response.getBody(), false);

        // test findall() Iterable
        ResponseEntity<Iterable> iterableResponseEntity = testRestTemplate.getForEntity(createURLWithPort(BASE_URI + "/findall"), Iterable.class);

        System.out.println("responseEntity.getBody()) = " + iterableResponseEntity.getBody());
        System.out.println("responseEntity.getStatusCode()) = " + iterableResponseEntity.getStatusCode());
        assertThat(iterableResponseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat((List) iterableResponseEntity.getBody()).size().isEqualTo(4);

        //findById that exists
        ResponseEntity<Parameter> responseEntityOK = testRestTemplate.getForEntity(createURLWithPort(BASE_URI + "/" + newId), Parameter.class);

        System.out.println("responseEntity.getBody()) = " + responseEntityOK.getBody());
        System.out.println("responseEntity.getStatusCode()) = " + responseEntityOK.getStatusCode());
        assertThat(responseEntityOK.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntityOK.getBody().getType()).isEqualTo(ParameterEnum.PROMOTIONALDISCOUNT);

        //deleteOneById with id=1 that exists
        entity = new HttpEntity<>(null, httpHeaders);
        ResponseEntity<Boolean> responseDelete = testRestTemplate.exchange(createURLWithPort(BASE_URI + "/1"),HttpMethod.DELETE, entity, Boolean.class);

        System.out.println("responseEntity.getBody()) = " + responseDelete.getBody());
        System.out.println("responseEntity.getStatusCode()) = " + responseDelete.getStatusCode());
        assertThat(responseDelete.getStatusCode()).isEqualTo(HttpStatus.OK);

        //findById which is deleted
        ResponseEntity<Parameter> responseEntityFind = testRestTemplate.getForEntity(createURLWithPort(BASE_URI + "/1"), Parameter.class);

        System.out.println("responseEntity.getBody()) = " + responseEntityFind.getBody());
        System.out.println("responseEntity.getStatusCode()) = " + responseEntityFind.getStatusCode());
        assertThat(responseEntityFind.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

//    private void checkBodyAndHttpStatusResponseEntity(ResponseEntity responseEntity, int responseBodyValue, HttpStatus httpStatus) {
//        System.out.println("responseEntity.getBody()) = " + responseEntity.getBody());
//        System.out.println("responseEntity.getStatusCode()) = " + responseEntity.getStatusCode());
//        if (responseBodyValue == 0) {
//            assertThat(responseEntity.getBody()).isNull();
//        } else {
//            assertThat(responseEntity.getBody()).isNotNull();
//        }
//        assertThat(responseEntity.getStatusCode()).isEqualTo(httpStatus);
//    }

    private String createURLWithPort(String uri) {
        String uriString = "http://localhost:" + port + uri;
        System.out.println(uriString);
        return uriString;
    }
}