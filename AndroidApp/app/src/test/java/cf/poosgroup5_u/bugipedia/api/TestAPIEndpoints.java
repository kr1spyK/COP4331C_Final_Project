package cf.poosgroup5_u.bugipedia.api;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class TestAPIEndpoints {


    public static final String TEST_USERNAME = "testUser";
    public static final String TEST_PASSWORD = "testPassword";

    @Before
    public void setup(){
        APICaller.enableDebugLogging(true);
    }

    @Test
    public void testRegister() throws Exception {

        final CompletableFuture<Result> future = new CompletableFuture<>();

        RegisterUser testRegUser = new RegisterUser(TEST_USERNAME, TEST_PASSWORD);

        APICaller.call().register(testRegUser).enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {

                future.complete(response.body());
            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {
                future.completeExceptionally(t);
            }
        });

        Assert.assertTrue(future.get().getErrorMessage(), future.get().wasSuccessful());

    }

    @Test
    public void testLogin() throws Exception {
        final CompletableFuture<Result> future = new CompletableFuture<>();
        APICaller.call().login(new Login(TEST_USERNAME, TEST_PASSWORD)).enqueue(new Callback<LoginResult>() {
            @Override
            public void onResponse(Call<LoginResult> call, Response<LoginResult> response) {
                future.complete(response.body());
            }

            @Override
            public void onFailure(Call<LoginResult> call, Throwable t) {
                future.completeExceptionally(t);
            }
        });


        Assert.assertTrue(future.get().getErrorMessage(), future.get().wasSuccessful());

    }

}
