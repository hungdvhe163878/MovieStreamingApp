package com.example.moviestreamingapp;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class HomeActivity extends AppCompatActivity {
    private static final String TAG = "HomeActivity";

    private RecyclerView moviesRecyclerView;
    private MovieAdapter movieAdapter;
    private List<Movie> movieList;
    private FirebaseFirestore db;
    private EditText searchEditText;
    private Spinner categorySpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        searchEditText = findViewById(R.id.searchView);
        moviesRecyclerView = findViewById(R.id.movies_list);
        categorySpinner = findViewById(R.id.category_spinner);

        moviesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        movieList = new ArrayList<>();
        movieAdapter = new MovieAdapter(this, movieList);
        moviesRecyclerView.setAdapter(movieAdapter);

        db = FirebaseFirestore.getInstance();

        setupSearchEditText();
        setupCategorySpinner();

        loadMovies();
    }

    private void setupSearchEditText() {
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchMovies(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        // Handle enter key press in searchEditText
        searchEditText.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH || (event != null && event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                String query = searchEditText.getText().toString().trim();
                searchMovies(query);
                return true;
            }
            return false;
        });
    }

    private void setupCategorySpinner() {
        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedCategory = categorySpinner.getSelectedItem().toString();
                filterMoviesByCategory(selectedCategory);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                loadMovies();
            }
        });
    }

    private void loadMovies() {
        Log.d(TAG, "Loading movies...");
        db.collection("movies").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                movieList.clear();
                for (DocumentSnapshot document : task.getResult()) {
                    try {
                        Movie movie = document.toObject(Movie.class);
                        movieList.add(movie);
                    } catch (RuntimeException e) {
                        Log.e(TAG, "Error deserializing movie: " + document.getId(), e);
                    }
                }
                runOnUiThread(() -> {
                    movieAdapter.notifyDataSetChanged();
                    Log.d(TAG, "Movies loaded successfully. Count: " + movieList.size());
                });
            } else {
                Log.e(TAG, "Error getting documents: ", task.getException());
            }
        });
    }

    private void searchMovies(String query) {
        db.collection("movies").whereEqualTo("title", query).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                movieList.clear();
                for (DocumentSnapshot document : task.getResult()) {
                    try {
                        Movie movie = document.toObject(Movie.class);
                        movieList.add(movie);
                    } catch (RuntimeException e) {
                        Log.e(TAG, "Error deserializing movie: " + document.getId(), e);
                    }
                }
                movieAdapter.notifyDataSetChanged();
            } else {
                Log.e(TAG, "Error searching movies: ", task.getException());
            }
        });
    }

    private void filterMoviesByCategory(String category) {
        db.collection("movies").whereEqualTo("category", category).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                movieList.clear();
                for (DocumentSnapshot document : task.getResult()) {
                    try {
                        Movie movie = document.toObject(Movie.class);
                        movieList.add(movie);
                    } catch (RuntimeException e) {
                        Log.e(TAG, "Error deserializing movie: " + document.getId(), e);
                    }
                }
                movieAdapter.notifyDataSetChanged();
            } else {
                Log.e(TAG, "Error filtering movies: ", task.getException());
            }
        });
    }
}