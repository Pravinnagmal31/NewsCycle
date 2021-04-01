package com.pravin.newscyclejava;

import androidx.appcompat.app.AppCompatActivity;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;

import com.google.gson.Gson;
import com.pravin.newscyclejava.API.APIClient;
import com.pravin.newscyclejava.API.Models.Article;
import com.pravin.newscyclejava.API.Models.Root;
import com.pravin.newscyclejava.Adapters.NewsAdapter;
import com.pravin.newscyclejava.Adapters.NewsOnclickListener;
import com.pravin.newscyclejava.Adapters.SpinnerAdapter;
import com.pravin.newscyclejava.Model.SpinnerModel;
import com.pravin.newscyclejava.Utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements NewsOnclickListener {

    RecyclerView recyclerView;
    NewsAdapter newsAdapter;
    Spinner countrySpinner, categorySpinner;
    SpinnerAdapter countrySpinnerAdapter,categorySpinnerAdapter;

    JSONArray countriesJson;
    ProgressDialog progressDialog;

    String lastSelectedCountry = "in", lastSelectedCategory="general";

    String[] categories = new String []{"Business", "Entertainment","General", "Health", "Science", "Sports", "Technology"};

    ArrayList<SpinnerModel> countryModels = new ArrayList<>();
    ArrayList<SpinnerModel> categoryModels = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        countrySpinner = findViewById(R.id.sCountry);
        categorySpinner = findViewById(R.id.sCategory);


        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        initCountriesList();
        initCategoryList();
        addCountrySpinner();
        addCategorySpinner();
        getNews();

        //default general
        categorySpinner.setSelection(2);

        //default india
        countrySpinner.setSelection(23);

        newsAdapter =new NewsAdapter(this,this);
        recyclerView.setAdapter(newsAdapter);


    }



    private void initCategoryList() {

        for (String category : categories) {
            categoryModels.add(new SpinnerModel(category, ""));
        }
    }

    void getNews(){

        if(progressDialog==null){
            progressDialog = new ProgressDialog(this);
        }
        progressDialog.setTitle("Getting news");
        progressDialog.setMessage("loading "+((SpinnerModel)categorySpinner.getSelectedItem()).getSpinnerString()+" news from "+((SpinnerModel)countrySpinner.getSelectedItem()).getSpinnerString());
        progressDialog.show();
        Log.d("creation","ok");

        Call<Root> call = APIClient.getApiClient().getNews(lastSelectedCountry,lastSelectedCategory,"f9d5f54177ad4bf2a59f242372e8ee41");

        call.enqueue(new Callback<Root>() {
            @Override
            public void onResponse(Call<Root> call, Response<Root> response) {

                Root root = response.body();

                Log.d("res",root.status);
                Log.d("articles",root.articles.size()+" "+new Gson().toJson(root.articles.get(0)));
                progressDialog.dismiss();
                newsAdapter.updateNews(response.body().articles);
                Log.d("after dismiss",root.status);

            }

            @Override
            public void onFailure(Call<Root> call, Throwable t) {
                progressDialog.dismiss();
                Log.e("responseError",t.getLocalizedMessage());
            }
        });

    }

    @Override
    public void OnItemClickListener(Article article) {

        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
        CustomTabsIntent customTabsIntent = builder.build();
        customTabsIntent.launchUrl(this, Uri.parse(article.url));

    }

    private void initCountriesList()
    {

        try {
            countriesJson = new JSONArray(new Utils().getJsonFromAssets(this,"countries.json"));

            if (countriesJson != null) {
                for (int i=0;i<countriesJson.length();i++){
                    countryModels.add(new SpinnerModel(countriesJson.getJSONObject(i).getString("country"),countriesJson.getJSONObject(i).getString("domain")));
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    private void addCountrySpinner(){
        // we pass our item list and context to our Adapter.
        countrySpinnerAdapter = new SpinnerAdapter(this, countryModels);
        countrySpinner.setAdapter(countrySpinnerAdapter);

        countrySpinner.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent,
                                               View view, int position, long id)
                    {
                        lastSelectedCountry = countryModels.get(position).getDomain();
                        getNews();
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> parent)
                    {
                    }
                });
    }

    private void addCategorySpinner() {
        categorySpinnerAdapter = new SpinnerAdapter(this, categoryModels);
        categorySpinner.setAdapter(categorySpinnerAdapter);

        categorySpinner.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent,
                                               View view, int position, long id)
                    {
                        lastSelectedCategory = categoryModels.get(position).getSpinnerString().toLowerCase();
                        getNews();
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> parent)
                    {
                    }
                });

    }

}