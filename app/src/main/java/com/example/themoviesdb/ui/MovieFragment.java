package com.example.themoviesdb.ui;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.themoviesdb.R;
import com.example.themoviesdb.data.local.entity.MovieEntity;
import com.example.themoviesdb.data.network.Resource;
import com.example.themoviesdb.viewmodel.MovieViewModel;

import java.util.List;

/**
 * A fragment representing a list of Items.
 */
public class MovieFragment extends Fragment {

    private static final String ARG_COLUMN_COUNT = "column-count";
    private int mColumnCount = 2;
    List<MovieEntity> movieList;
    MyMovieRecyclerViewAdapter adapter;

    MovieViewModel movieViewModel;

    public MovieFragment() {
    }

    public static MovieFragment newInstance(int columnCount) {
        MovieFragment fragment = new MovieFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }

        movieViewModel = ViewModelProviders.of(getActivity())
                .get(MovieViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            adapter = new MyMovieRecyclerViewAdapter(
                    getActivity(),movieList
            );
            recyclerView.setAdapter(adapter);

            loadMovies();
        }
        return view;
    }

    private void loadMovies() {
        movieViewModel.getPopularMovies().observe(getActivity(), new Observer<Resource<List<MovieEntity>>>() {
            @Override
            public void onChanged(Resource<List<MovieEntity>> listResource) {
                movieList = listResource.data;
                adapter.setData(movieList);

            }
        });
    }
}