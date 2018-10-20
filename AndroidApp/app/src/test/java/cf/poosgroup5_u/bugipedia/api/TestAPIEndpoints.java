package cf.poosgroup5_u.bugipedia.api;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import retrofit2.Response;


public class TestAPIEndpoints {

    APIEndpoints api;

    public static final String TEST_USERNAME = "testUser";
    public static final String TEST_PASSWORD = "testPassword";


    @Before
    public void setup(){

        api = TestAPICaller.getCaller().create(APIEndpoints.class);

    }

    @Test
    public void testRegister() throws IOException {

        Response<Result> apiCall = api.register(new RegisterUser(TEST_USERNAME, TEST_PASSWORD)).execute();
        if (apiCall.body() == null) {
            Assert.fail(apiCall.errorBody().string());
        }

        Result res = apiCall.body();
        Assert.assertTrue(res.getErrorMessage(), res.wasSuccessful());
    }

    @Test
    public void testLogin() throws IOException {
        Response<LoginResult> apiCall = api.login(new Login(TEST_USERNAME, TEST_PASSWORD)).execute();

        Assert.assertTrue(apiCall.toString(), apiCall.isSuccessful());

        System.out.println("Login SessionID: " + apiCall.body().getSessionID());
    }
}
