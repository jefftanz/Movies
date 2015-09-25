
package com.jtanz.movies.ui;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.transition.Slide;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jtanz.movies.Constants;
import com.jtanz.movies.R;
import com.jtanz.movies.model.Movie;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;

public class MovieDetailActivity extends AppCompatActivity {
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private Movie mMovie;

    public static void navigate(AppCompatActivity activity, View transitionImage, Movie movie) {
        Intent intent = new Intent(activity, MovieDetailActivity.class);
        String imgUrl = "http://image.tmdb.org/t/p/w500" + movie.getPoster_path();

        intent.putExtra(Constants.Parameter.BANNER_IMAGE, imgUrl);
        intent.putExtra(Constants.Parameter.MOVIE, movie);

        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(activity, transitionImage, Constants.Parameter.BANNER_IMAGE);
        ActivityCompat.startActivityForResult(activity, intent, Constants.RequestCode.ACTIVITY_CREATE, options.toBundle());
    }

    @SuppressWarnings("ConstantConditions")
    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initActivityTransitions();
        setContentView(R.layout.activity_movie_detail);

        ViewCompat.setTransitionName(findViewById(R.id.app_bar_layout), Constants.Parameter.BANNER_IMAGE);
        supportPostponeEnterTransition();

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mMovie = getIntent().getParcelableExtra(Constants.Parameter.MOVIE);

        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitle(mMovie.getTitle());
        collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(android.R.color.transparent));

        final ImageView image = (ImageView) findViewById(R.id.image_ActivityDetail);
        Picasso.with(this).load(getIntent().getStringExtra(Constants.Parameter.BANNER_IMAGE)).into(image, new Callback() {
            @Override
            public void onSuccess() {
                Bitmap bitmap = ((BitmapDrawable) image.getDrawable()).getBitmap();
                Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
                    public void onGenerated(Palette palette) {
                        applyPalette(palette);
                    }
                });
            }

            @Override
            public void onError() {

            }
        });

        ((TextView) findViewById(R.id.title_MovieDetailActivity)).setText(mMovie.getTitle());
        ((TextView) findViewById(R.id.plot_MovieDetailActivity)).setText(mMovie.getOverview());
        ((TextView) findViewById(R.id.releaseDateDetail_MovieDetailActivity)).setText(mMovie.getRelease_date());
        ((TextView) findViewById(R.id.ratingDetail_MovieDetailActivity)).setText(mMovie.getVote_average());

        DecimalFormat percentageFormat = new DecimalFormat("0.00");
        String finalPercentage = percentageFormat.format(Double.valueOf(mMovie.getPopularity()));
        ((TextView) findViewById(R.id.popularityDetail_MovieDetailActivity)).setText(finalPercentage);

        ((TextView) findViewById(R.id.voteDetail_MovieDetailActivity)).setText(mMovie.getVote_count());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.empty, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    finishAfterTransition();
                } else {
                    finish();
                }
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void initActivityTransitions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Slide transition = new Slide();
            transition.excludeTarget(android.R.id.statusBarBackground, true);
            getWindow().setEnterTransition(transition);
            getWindow().setReturnTransition(transition);
        }
    }

    private void applyPalette(Palette palette) {
        int primaryDark = getResources().getColor(R.color.primary_dark);
        int primary = getResources().getColor(R.color.primary);
        collapsingToolbarLayout.setContentScrimColor(palette.getMutedColor(primary));
        collapsingToolbarLayout.setStatusBarScrimColor(palette.getDarkMutedColor(primaryDark));
        updateBackground((FloatingActionButton) findViewById(R.id.fab), palette);
        supportStartPostponedEnterTransition();
    }

    private void updateBackground(FloatingActionButton fab, Palette palette) {
        int lightVibrantColor = palette.getLightVibrantColor(getResources().getColor(android.R.color.white));
        int vibrantColor = palette.getVibrantColor(getResources().getColor(R.color.accent));

        fab.setRippleColor(lightVibrantColor);
        fab.setBackgroundTintList(ColorStateList.valueOf(vibrantColor));
    }
}
