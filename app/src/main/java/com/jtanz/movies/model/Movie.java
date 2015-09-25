package com.jtanz.movies.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by jeff on 8/27/15.
 */
public class Movie implements Parcelable {

    private String adult, backdrop_path, genre_ids[], id, original_language, original_title, overview, release_date, poster_path, popularity, title, video, vote_average, vote_count;

    public String getAdult(){ return adult; }
    public String getBackdrop_path(){ return backdrop_path; }
    public String[] getGenre_ids(){ return genre_ids; }
    public String getId(){ return id; }
    public String getOriginal_language(){ return original_language; }
    public String getOriginal_title(){ return original_title; }
    public String getOverview(){ return overview; }
    public String getRelease_date(){ return release_date; }
    public String getPoster_path(){ return poster_path; }
    public String getPopularity(){ return popularity; }
    public String getTitle(){ return title; }
    public String getVideo(){ return video; }
    public String getVote_average(){ return vote_average; }
    public String getVote_count(){ return vote_count; }


    public void setAdult(String adult) { this.adult = adult; }
    public void setBackdrop_path(String backdrop_path) { this.backdrop_path = backdrop_path; }
    public void setGenre_ids(String genre_ids[]) { this.genre_ids = genre_ids; }
    public void setId(String id) { this.id = id; }
    public void setOriginal_language(String original_language) { this.original_language = original_language; }
    public void setOriginal_title(String original_title) { this.original_title = original_title; }
    public void setOverview(String overview) { this.overview = overview; }
    public void setRelease_date(String release_date) { this.release_date = release_date; }
    public void setPoster_path(String poster_path) { this.poster_path = poster_path; }
    public void setPopularity(String popularity) { this.popularity = popularity; }
    public void setTitle(String title) { this.title = title; }
    public void setVideo(String video) { this.video = video; }
    public void setVote_average(String vote_average) { this.vote_average = vote_average; }
    public void setVote_count(String vote_count) { this.vote_count = vote_count; }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.adult);
        dest.writeString(this.backdrop_path);
        dest.writeStringArray(this.genre_ids);
        dest.writeString(this.id);
        dest.writeString(this.original_language);
        dest.writeString(this.original_title);
        dest.writeString(this.overview);
        dest.writeString(this.release_date);
        dest.writeString(this.poster_path);
        dest.writeString(this.popularity);
        dest.writeString(this.title);
        dest.writeString(this.video);
        dest.writeString(this.vote_average);
        dest.writeString(this.vote_count);
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        public Movie createFromParcel(Parcel in){
            Movie movie = new Movie();
            movie.adult = in.readString();
            movie.backdrop_path = in.readString();
            movie.genre_ids = in.createStringArray();
            movie.id = in.readString();
            movie.original_language = in.readString();
            movie.original_title = in.readString();
            movie.overview = in.readString();
            movie.release_date = in.readString();
            movie.poster_path = in.readString();
            movie.popularity = in.readString();
            movie.title = in.readString();
            movie.video = in.readString();
            movie.vote_average = in.readString();
            movie.vote_count = in.readString();

            return movie;
        }

        public Movie[] newArray(int size){
            return new Movie[size];
        }
    };



}
