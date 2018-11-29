package cf.poosgroup5_u.bugipedia;

import android.app.DownloadManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import com.github.aakira.expandablelayout.ExpandableWeightLayout;

import java.util.ArrayList;
import java.util.List;

import cf.poosgroup5_u.bugipedia.api.APICaller;
import cf.poosgroup5_u.bugipedia.api.SearchField;
import cf.poosgroup5_u.bugipedia.api.SearchFieldResult;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class BuggyMain extends AppCompatActivity {

    private RecyclerView recyclerView;
    private BugAdapter adapter;

    List<BugCard> bugList;
    ExpandableWeightLayout expandableLayout;
    LinearLayout mainLayout;
    //holds each filter
    LinearLayout filterLiny;
    //holds the filterLiny so that it is scrollable
    ScrollView filterScroll;


    ArrayList<SearchField> searchQuery;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.buggy_layout);

        expandableLayout = findViewById(R.id.expandableLayout);
        mainLayout = findViewById(R.id.main_layout);
        filterScroll = findViewById(R.id.filterScroll);


        final Button filterButton = new Button(this);
        filterButton.setText("Search and Filters");
        mainLayout.addView(filterButton);





        //Fetches available filters
        APICaller.call().getSearchFields().enqueue(new Callback<SearchFieldResult>() {
            @Override
            public void onResponse(Call<SearchFieldResult> call, Response<SearchFieldResult> response) {
                if(response.isSuccessful()){
                    //What happens if query succeeds
                    SearchFieldResult myResults = response.body();

                    List<SearchField> myFields = myResults.getFields();

                    //Creates filter labels and views and adds them to the expandable layout
                    makeSearchBox(myFields);

                    //toggles the expandable search box
                    filterButton.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            expandableLayout.toggle();
                        }
                    });

                }
            }

            @Override
            public void onFailure(Call<SearchFieldResult> call, Throwable t) {
                //What happens if the query fails
            }
        });


        /*

        ArrayList<SearchField> searchQuery = new ArrayList<>();

        APICaller.call().search(searchQuery).enqueue(new Callback<SearchResult>() {
            @Override
            public void onResponse(Call<SearchResult> call, Response<SearchResult> response) {

            }

            @Override
            public void onFailure(Call<SearchResult> call, Throwable t) {

            }
        });*/


         //bugList = new ArrayList<>();
         //recyclerView.setHasFixedSize(true);
         //recyclerView.setLayoutManager(new LinearLayoutManager(this));

         /*add data to the buglist here, probably gonna need to loop through all available query results
        bugList.add(BugCard ...)

        for(BugCard thisCard : sizeofqueryresult){
            buglist.add(
                    new BugCard(insert queried info here)
            )

        }*/



    }

    public void makeSearchBox(List<SearchField> myFields){

        ArrayList<QueryTuple> queries = new ArrayList<>();
        MultiSelectionSpinner colorS = new MultiSelectionSpinner(this);

        filterLiny = new LinearLayout(this);
        filterLiny.setOrientation(LinearLayout.VERTICAL);
        for(SearchField field : myFields)
        {
            LinearLayout linyLayout = new LinearLayout(this);
            linyLayout.setOrientation(LinearLayout.HORIZONTAL);

            TextView textLabel = new TextView(this);
            textLabel.setText(field.getLabel());
            linyLayout.addView(textLabel);

            switch(field.getType()){
                case DROP:{
                    List<String> dropOptions = field.getOptions();
                    //if size is two it is actualy going to be a checkbox
                    if(dropOptions.size() == 2)
                    {
                        CheckBox yesno = new CheckBox(this);
                        myCheckies.add(yesno);
                        linyLayout.addView(yesno);
                    }
                    else{
                        Spinner dropDown = new Spinner(this);
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, dropOptions);
                        dropDown.setAdapter(adapter);
                        myDropies.add(dropDown);
                        linyLayout.addView(dropDown);
                    }
                    break;
                }
                case TEXT:{
                    EditText textField = new EditText(this);
                    textField.setSingleLine(true);
                    queries.add(new QueryTuple(field.getLabel(), textField));
                    linyLayout.addView(textField);
                    break;
                }
                case CHECK:{
                    //Colors is actually a multi spinner dropdown
                    //for colors since it can have multiple colors
                    List<String> availableColors = field.getOptions();
                    colorS.setItems(availableColors);
                    linyLayout.addView(colorS);
                    break;
                }

            }

            filterLiny.addView(linyLayout);
        }
        Button searchButton = new Button(this);
        searchButton.setText("SEARCH");
        filterLiny.addView(searchButton);
        filterScroll.addView(filterLiny);
        makeQuery(queries, colorS);

    }



    public void makeQuery(ArrayList<QueryTuple> queryables, MultiSelectionSpinner colorS){
        List<String> selectColors = colorS.getSelectedStrings();
    }

    private class QueryTuple{
        public String viewLabel;
        public View myView;


        public QueryTuple(String viewLabel, View myView) {
            this.viewLabel = viewLabel;
            this.myView = myView;
        }
    }



}
