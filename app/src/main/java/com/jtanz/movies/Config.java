package com.jtanz.movies;

/**
 * Created by jeff on 8/27/15.
 */
public class Config {

    //TODO Replace <API_KEY> with your movie db API Key
    //https://www.themoviedb.org/documentation/api

    public final static String MOVIE_API_KEY = "<API_KEY>";

    //public final static String IMAGE_SIZE = "w185";
    public final static String IMAGE_SIZE = "w500";
    public final static String IMAGE_URL = " http://image.tmdb.org/t/p/";

    public final static String BASE_URL = "http://api.themoviedb.org/3/discover/movie?";
    public final static String SORT_POPULARITY = "sort_by=popularity.desc&";
    public final static String SORT_RATING = "sort_by=vote_average.desc&";

}
