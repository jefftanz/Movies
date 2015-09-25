package com.jtanz.movies.ui;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jtanz.movies.Config;
import com.jtanz.movies.R;
import com.jtanz.movies.control.MovieAdapter;
import com.jtanz.movies.model.Movie;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class MovieListActivity extends AppCompatActivity implements MovieAdapter.OnItemClickListener {
    private ObjectMapper mObjMapper;
    private ArrayList<Movie> mMovieData;
    private asyncTask_GetMovieData mTaskGetMovieData;
    private ProgressDialog mProgDialog;
    private View content;
    private Context mContext;
    private String mSortBy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_list);

        mContext = this;

        mObjMapper = new ObjectMapper();
        mMovieData = new ArrayList<>();

        mSortBy = Config.SORT_POPULARITY;

        initToolbar();

        content = findViewById(R.id.content);

        displayProgressDialog();
        loadMovieData();
    }

    private void displayProgressDialog(){
        mProgDialog = new ProgressDialog(this);
        mProgDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgDialog.setMessage(getString(R.string.loading));
        mProgDialog.setCancelable(false);
        mProgDialog.show();
    }

    private void loadMovieData(){
        mTaskGetMovieData = new asyncTask_GetMovieData(mSortBy);
        mTaskGetMovieData.execute((Void) null);
    }

    private void loadRecyclerView() {
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        MovieAdapter adapter = new MovieAdapter(mContext, mMovieData);
        adapter.setOnItemClickListener(this);
        recyclerView.setAdapter(adapter);
    }

    private void initToolbar() {
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final ActionBar actionBar = getSupportActionBar();

//        if (actionBar != null) {
//            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);
//            actionBar.setDisplayHomeAsUpEnabled(true);
//        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //drawerLayout.openDrawer(GravityCompat.START);
                return true;
            case R.id.menuItem_Popularity:
                displayProgressDialog();
                mSortBy = Config.SORT_POPULARITY;
                loadMovieData();
                break;
            case R.id.menuItem_Rating:
                displayProgressDialog();
                mSortBy = Config.SORT_RATING;
                loadMovieData();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override public void onItemClick(View view, Movie movie) {
        MovieDetailActivity.navigate(this, view.findViewById(R.id.imgView_ItemRecycler2), movie);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {

        }
    }

    /**
     * Represents an asynchronous task used to retrieve Movie Data
     */
    public class asyncTask_GetMovieData extends AsyncTask<Void, Void, Boolean> {

        private final String mSortBy;

        //Constructor
        asyncTask_GetMovieData(String sortBy) {
            mSortBy = sortBy;
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            getMovieData();
            return true;
        }

        private boolean getMovieData() {
            String result = "";
            String url;

            url = Config.BASE_URL + mSortBy + Config.MOVIE_API_KEY;

            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpGet httpGet = new HttpGet(url);
                HttpResponse response = httpclient.execute(httpGet);
                HttpEntity entity = response.getEntity();
                InputStream is = entity.getContent();

                //convert response to string
                BufferedReader reader = new BufferedReader(new InputStreamReader(is,"iso-8859-1"),8);
                StringBuilder sb = new StringBuilder();
                String line = null;

                while ((line = reader.readLine()) != null) {sb.append(line);}
                is.close();

                result = sb.toString();

                JsonFactory jsonFactory = new JsonFactory();
                Movie movie;
                mMovieData = new ArrayList<>();

                try{
                    JsonParser jsonParser = jsonFactory.createParser(result);
                    jsonParser.nextToken(); // Start Object
                    jsonParser.nextToken(); // Page
                    jsonParser.nextToken(); // Page value
                    jsonParser.nextToken(); // Results
                    jsonParser.nextToken(); // Start Array
                    // and then each time, advance to opening START_OBJECT
                    while(jsonParser.nextToken() == JsonToken.START_OBJECT){
                        movie = mObjMapper.readValue(jsonParser, Movie.class);
                        mMovieData.add(movie);
                    }
                }catch(IOException ex){
                    System.out.println("Something went wrong!");
                    ex.printStackTrace();
                }

                return true;
            } catch(Exception e) {
                //TODO Error communicating to server?
                return false;
            } finally {
                //mHandler.postDelayed(mUpdateTimeTask, 100);
            }
        }

        @Override
        protected void onPostExecute(final Boolean success) {

            if (success) {
                loadRecyclerView();
            } else {
                //TODO error communicating to Server?
            }

            mProgDialog.dismiss();
        }

        @Override
        protected void onCancelled() {
            mProgDialog.dismiss();
        }
    }
}
