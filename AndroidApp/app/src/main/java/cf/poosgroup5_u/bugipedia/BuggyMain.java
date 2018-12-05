package cf.poosgroup5_u.bugipedia;

import android.app.DownloadManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
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
import cf.poosgroup5_u.bugipedia.api.SearchResult;
import cf.poosgroup5_u.bugipedia.api.SearchResultEntry;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class BuggyMain extends AppCompatActivity {

    RecyclerView recyclerView;
    BugAdapter adapter;

    List<BugCard> bugList;
    ExpandableWeightLayout expandableLayout;
    LinearLayout mainLayout;
    //holds each filter
    LinearLayout filterLiny;
    //holds the filterLiny so that it is scrollable

    ScrollView filterScroll;

    //Holds on the filter options selected by the users and is used for the actual query
    ArrayList<SearchField> searchQuery;
    //Used to populate searchQuery with the different types of views eg checkboxes, spinners, etc
    ArrayList<CheckTuple> checkQueries;
    ArrayList<TextTuple> textQueries;
    ArrayList<SpinnerTuple> spinnerQueries;
    MultiSelectionSpinner colorS;

    //Activates the Search query
    Button searchButton;
    Button filterButton;


    Context bugContext;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.buggy_layout);

        expandableLayout = findViewById(R.id.expandableLayout);
        mainLayout = findViewById(R.id.main_layout);
        filterScroll = findViewById(R.id.filterScroll);
        recyclerView = findViewById(R.id.my_recycler);

        final LinearLayoutManager llm = new LinearLayoutManager(this);


        searchButton = findViewById(R.id.searchButton);
        filterButton = findViewById(R.id.filterButton);
        filterButton.setText("Search and Filters");


        bugContext = this;

        //todo remove me
        APICaller.enableDebugLogging(true);


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



        //Activates Search Button
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expandableLayout.toggle();
                //populates searchQuery from selected filters
                makeQuery(spinnerQueries, textQueries, checkQueries, colorS);

                //API call for list of matches
                APICaller.call().search(searchQuery).enqueue(new Callback<SearchResult>() {
                    @Override
                    public void onResponse(Call<SearchResult> call, Response<SearchResult> response) {
                        SearchResult results = response.body();
                        bugList = new ArrayList<>();

                        recyclerView.setHasFixedSize(true);
                        llm.setOrientation(LinearLayoutManager.VERTICAL);
                        recyclerView.setLayoutManager(llm);


                        for(SearchResultEntry thisCard : results.getSearchResults()){
                            BugCard bugger = new BugCard(thisCard.getId(), thisCard.getCommonName(), thisCard.getScientificName(), thisCard.getThumbnailURL());
                            bugList.add(bugger);
                        }

                        adapter = new BugAdapter(bugContext, bugList);
                        recyclerView.setAdapter(adapter);
                    }

                    @Override
                    public void onFailure(Call<SearchResult> call, Throwable t) {

                    }
                });

            }
        });


    }

    public void makeSearchBox(List<SearchField> myFields){

        checkQueries = new ArrayList<>();
        textQueries = new ArrayList<>();
        spinnerQueries = new ArrayList<>();
        colorS = new MultiSelectionSpinner(this);

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
                    dropOptions.add(0, "Any");
                    //if size is two it is actually going to be a checkbox since it is a yes/no dropdown
                    if(dropOptions.size() == 2)
                    {
                        CheckBox yesno = new CheckBox(this);
                        checkQueries.add(new CheckTuple(field.getLabel(), yesno));
                        linyLayout.addView(yesno);
                    }
                    else{
                        Spinner dropDown = new Spinner(this);
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, dropOptions);
                        dropDown.setAdapter(adapter);
                        spinnerQueries.add(new SpinnerTuple(field.getLabel(), dropDown));
                        linyLayout.addView(dropDown);
                    }
                    break;
                }
                case TEXT:{
                    EditText textField = new EditText(this);
                    textField.setSingleLine(true);
                    textQueries.add(new TextTuple(field.getLabel(), textField));
                    linyLayout.addView(textField);
                    break;
                }
                case CHECK:{
                    //Colors is actually a multi spinner dropdown
                    //since it can have multiple colors selected at once
                    List<String> availableColors = field.getOptions();
                    availableColors.add(0, "Any");
                    colorS.setItems(availableColors);
                    colorS.mSelection[0] = true;
                    linyLayout.addView(colorS);
                    break;
                }

            }

            filterLiny.addView(linyLayout);
        }
        filterScroll.addView(filterLiny);

    }



    public void makeQuery(ArrayList<SpinnerTuple> spinnerQueries, ArrayList<TextTuple> textQueries, ArrayList<CheckTuple> checkQueries, MultiSelectionSpinner colorS){
        searchQuery = new ArrayList<>();

        for(SpinnerTuple thisField : spinnerQueries){
            if(!thisField.myView.getSelectedItem().toString().equals("Any"))
            {
                searchQuery.add(new SearchField(thisField.viewLabel, thisField.myView.getSelectedItem().toString()));
            }
        }
        for(TextTuple thisField : textQueries){
            String currentString = thisField.myView.getText().toString();
            if(!currentString.equals(""))
            {
                searchQuery.add(new SearchField(thisField.viewLabel, currentString));
            }

        }
        for(CheckTuple thisField : checkQueries){
            if(thisField.myView.isChecked())
                searchQuery.add(new SearchField(thisField.viewLabel, "Yes"));
            else
                searchQuery.add(new SearchField(thisField.viewLabel, "No"));

        }
        if(!colorS.getSelectedStrings().contains("Any")){
            searchQuery.add(new SearchField("Colors", colorS.getSelectedStrings()));
        }
    }

    private class SpinnerTuple{
        public String viewLabel;
        public Spinner myView;


        public SpinnerTuple(String viewLabel, Spinner myView) {
            this.viewLabel = viewLabel;
            this.myView = myView;
        }
    }

    private class TextTuple{
        public String viewLabel;
        public EditText myView;

        public TextTuple(String viewLabel, EditText myView) {
            this.viewLabel = viewLabel;
            this.myView = myView;
        }
    }

    private class CheckTuple {
        public String viewLabel;
        public CheckBox myView;

        public CheckTuple(String viewLabel, CheckBox myView) {
            this.viewLabel = viewLabel;
            this.myView = myView;
        }
    }


}
