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
        Parameter newParm = new Parameter();
        newParm.setType(ParameterEnum.PROMOTIONALDISCOUNT);
        newParm.setPercentage(21);
        HttpEntity<Parameter> entity = new HttpEntity<>(newParm, httpHeaders);
        httpHeaders.setContentType(MediaType.APPLICATION_JSON_UTF8);
        ResponseEntity<Parameter> responseEntity = testRestTemplate.postForEntity(createURLWithPort(BASE_URI + "/"), entity, Parameter.class);
        System.out.println("responseEntity.getBody = " + responseEntity.getBody());
        System.out.println("HttpStatus = " + responseEntity.getStatusCode().toString());

        assertThat(responseEntity.getBody()).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(responseEntity.getBody().getType()).isEqualTo(ParameterEnum.PROMOTIONALDISCOUNT);
        Long newId = responseEntity.getBody().getId();

        entity = new HttpEntity<>(null, httpHeaders);
        ResponseEntity<String> response = testRestTemplate.exchange(createURLWithPort(BASE_URI + "/findall"),
        HttpMethod.GET, entity, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        System.out.println("response = " + response.getBody());
        String expected = "[{\"id\":1,\"type\":\"PROFITMARGIN\",\"percentage\":30},{\"id\":2,\"type\":\"PREMIUMCUSTOMER\",\"percentage\":10},{\"id\":3,\"type\":\"DAMAGEDISCOUNT\",\"percentage\":20},{\"id\":4,\"type\":\"PROMOTIONALDISCOUNT\",\"percentage\":21}]";
        JSONAssert.assertEquals(expected, response.getBody(), false);

        ResponseEntity<Iterable> iterableResponseEntity = testRestTemplate.getForEntity(createURLWithPort(BASE_URI + "/findall"), Iterable.class);
        System.out.println("HttpStatus = " + iterableResponseEntity.getStatusCode().toString());
        assertThat(iterableResponseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat((List) iterableResponseEntity.getBody()).size().isEqualTo(4);

        //find Parameter with type=PROFITMARGIN that exists
        ResponseEntity<Parameter> responseEntityOK = testRestTemplate.getForEntity(createURLWithPort(BASE_URI + "/" + newId), Parameter.class);
        System.out.println("HttpStatus = " + responseEntityOK.getStatusCode().toString());
        assertThat(responseEntityOK.getBody()).isNotNull();
        assertThat(responseEntityOK.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntityOK.getBody().getType()).isEqualTo(ParameterEnum.PROMOTIONALDISCOUNT);

        //delete Parameter with id=1 that exists
        entity = new HttpEntity<>(null, httpHeaders);
        ResponseEntity<String> responseDelete = testRestTemplate.exchange(createURLWithPort(BASE_URI + "/1"),
        HttpMethod.DELETE, entity, String.class);
        System.out.println("HttpStatus = " + responseDelete.getStatusCode().toString());
        assertThat(responseDelete.getStatusCode()).isEqualTo(HttpStatus.OK);

        //try to lookup Parameter with type=PROFITMARGIN which is deleted
        ResponseEntity<Parameter> responseEntityFind = testRestTemplate.getForEntity(createURLWithPort(BASE_URI + "/1"), Parameter.class);
        System.out.println("HttpStatus = " + responseEntityFind.getStatusCode().toString());
//        assertThat(responseEntityFind.getBody()).isNull();
        assertThat(responseEntityFind.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    private String createURLWithPort(String uri) {
        String uriString = "http://localhost:" + port + uri;
        System.out.println(uriString);
        return uriString;
    }
}