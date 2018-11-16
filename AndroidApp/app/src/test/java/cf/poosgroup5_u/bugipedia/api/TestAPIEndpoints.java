package cf.poosgroup5_u.bugipedia.api;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
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

        if (future.get().getErrorMessage() != null && future.get().getErrorMessage().equals("User already registered")){
            System.err.println(future.get().getErrorMessage());
        } else {
            Assert.assertTrue(future.get().getErrorMessage(), future.get().wasSuccessful());
        }
    }

    @Test
    public void testLogin() throws Exception {
        final CompletableFuture<LoginResult> future = new CompletableFuture<>();
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
        APICaller.updateAuthToken(future.get().getSessionID());

    }

    @Test
    public void testGetFilters() throws Exception {
        final CompletableFuture<SearchFieldResult> future = new CompletableFuture<>();
        APICaller.call().getSearchFields().enqueue(new Callback<SearchFieldResult>() {
            @Override
            public void onResponse(Call<SearchFieldResult> call, Response<SearchFieldResult> response) {
                future.complete(response.body());
            }

            @Override
            public void onFailure(Call<SearchFieldResult> call, Throwable t) {
                future.completeExceptionally(t);
            }
        });


        Assert.assertTrue(future.get().getErrorMessage(), future.get().wasSuccessful());
        for (SearchField searchField : future.get().getFields())
            System.out.println(searchField);

    }

    @Test
    public void testSearch() throws Exception {

        /* Raw JSON of Test Search:
            {"Common Name" : "forest", "Colors" : ["black", "red"], "Has Antenna" : "Yes"}

           Should Return 1 entry fo the Eight-Spotted Forester Moth
         */
        ArrayList<SearchField> searchQuery = new ArrayList<>(4);
        searchQuery.add(new SearchField("Common Name", "forest"));
        searchQuery.add(new SearchField("Colors", Arrays.asList("Black", "Red")));
        searchQuery.add(new SearchField("Has Antenna", "Yes"));



        final CompletableFuture<SearchResult> future = new CompletableFuture<>();
        APICaller.call().search(searchQuery).enqueue(new Callback<SearchResult>() {
            @Override
            public void onResponse(Call<SearchResult> call, Response<SearchResult> response) {
                future.complete(response.body());
            }

            @Override
            public void onFailure(Call<SearchResult> call, Throwable t) {
                future.completeExceptionally(t);
            }
        });


        Assert.assertTrue(future.get().getErrorMessage(), future.get().wasSuccessful());
        //check to make sure we got the right type of Moth
        Assert.assertEquals("Wrong Object returned",
                future.get().getSearchResults().get(0).getCommonName(),
                "Eight-Spotted Forester Moth");

    }

    @Test
    public void testAddSighting() throws Exception {
        testLogin();

        final CompletableFuture<Result> future = new CompletableFuture<>();

        Sighting sighting = new Sighting(1, 28.419515,-81.581203);

        APICaller.call().addSighting(sighting).enqueue(new Callback<Result>() {
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
    public void testGetSightings() throws Exception {
        final CompletableFuture<SightingResult> future = new CompletableFuture<>();
        APICaller.call().getSightings(new BugInfo(1)).enqueue(new Callback<SightingResult>() {
            @Override
            public void onResponse(Call<SightingResult> call, Response<SightingResult> response) {
                future.complete(response.body());
            }

            @Override
            public void onFailure(Call<SightingResult> call, Throwable t) {
                future.completeExceptionally(t);
            }
        });


        Assert.assertTrue(future.get().getErrorMessage(), future.get().wasSuccessful());
        for (Sighting sighting : future.get().getSightings())
            System.out.println(sighting);
    }


}
