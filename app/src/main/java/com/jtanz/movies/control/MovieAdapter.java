
package com.jtanz.movies.control;

import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.jtanz.movies.R;
import com.jtanz.movies.model.Movie;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> implements View.OnClickListener {

    private List<Movie> mMovieData;
    private OnItemClickListener onItemClickListener;
    private Context mContext;

    public MovieAdapter(Context context, List<Movie> movieData) {

        this.mMovieData = movieData;
        this.mContext = context;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_movie, parent, false);
        v.setOnClickListener(this);
        return new ViewHolder(v);
    }

    @Override public void onBindViewHolder(ViewHolder holder, int position) {
        Movie movie = mMovieData.get(position);
        holder.mImageView.setImageBitmap(null);

        String posterPath = movie.getPoster_path();
        //String imgUrl = Config.IMAGE_URL + Config.IMAGE_SIZE + posterPath;

        //imgUrl = "http://image.tmdb.org/t/p/w500/7SGGUiTE6oc2fh9MjIk5M00dsQd.jpg";
        //          http://image.tmdb.org/t/p/w185/kqjL17yufvn9OVLyXYpvtyrFfak.jpg
        //http://lorempixel.com/500/500/animals/" + i

        Picasso.with(holder.mImageView.getContext()).load("http://image.tmdb.org/t/p/w500" + posterPath).into(holder.mImageView);

        //Picasso.with(mContext).load("").centerCrop()
        //.fit().centerCrop()
        //Picasso.fit().centerCrop();

        //Picasso.with(holder.mImageView.getContext()).load(item.getImage()).into(holder.mImageView);

        //holder.mTextView.setText(movie.getTitle());

        holder.itemView.setTag(movie);
    }

    @Override public int getItemCount() {
        return mMovieData.size();
    }

    @Override public void onClick(final View v) {
        // Give some time to the ripple to finish the effect
        if (onItemClickListener != null) {
            new Handler().postDelayed(new Runnable() {
                @Override public void run() {
                    onItemClickListener.onItemClick(v, (Movie) v.getTag());
                }
            }, 200);
        }
    }

    protected static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView mImageView;
        //public TextView mTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            mImageView = (ImageView) itemView.findViewById(R.id.imgView_ItemRecycler2);
        }
    }

    public interface OnItemClickListener {

        void onItemClick(View view, Movie movie);

    }
}
