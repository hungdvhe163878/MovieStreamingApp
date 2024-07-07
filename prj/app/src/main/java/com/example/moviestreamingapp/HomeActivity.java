package com.example.moviestreamingapp;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;



public class HomeActivity extends AppCompatActivity {
    private static final String TAG = "HomeActivity";

    private RecyclerView moviesRecyclerView;
    private MovieAdapter movieAdapter;
    private List<Movie> movieList;
    private DatabaseReference moviesDb;
    private DatabaseReference categoriesDb;
    private EditText searchEditText;
    private Spinner categorySpinner;
    private ArrayAdapter<String> categoryAdapter;
    private List<String> categoriesList;

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

        moviesDb = FirebaseDatabase.getInstance().getReference("movies");
        categoriesDb = FirebaseDatabase.getInstance().getReference("categories");

        setupSearchEditText();
        setupCategorySpinner();

        loadMovies();
        loadCategories();
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
        categoriesList = new ArrayList<>();
        categoryAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categoriesList);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(categoryAdapter);

        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedCategory = categoryAdapter.getItem(position);
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
        moviesDb.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                movieList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    try {
                        Movie movie = dataSnapshot.getValue(Movie.class);
                        if (movie != null) {
                            movieList.add(movie);
                        }
                    } catch (RuntimeException e) {
                        Log.e(TAG, "Error deserializing movie: " + dataSnapshot.getKey(), e);
                    }
                }
                runOnUiThread(() -> {
                    movieAdapter.notifyDataSetChanged();
                    Log.d(TAG, "Movies loaded successfully. Count: " + movieList.size());
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, "Error getting data: ", error.toException());
            }
        });
    }

    private void searchMovies(String query) {
        Log.d(TAG, "Searching movies with query: " + query);
        moviesDb.orderByChild("title").startAt(query).endAt(query + "\uf8ff").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                movieList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    try {
                        Movie movie = dataSnapshot.getValue(Movie.class);
                        if (movie != null) {
                            movieList.add(movie);
                        }
                    } catch (RuntimeException e) {
                        Log.e(TAG, "Error deserializing movie: " + dataSnapshot.getKey(), e);
                    }
                }
                Log.d(TAG, "Movies found: " + movieList.size());
                movieAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, "Error searching movies: ", error.toException());
            }
        });
    }

    private void filterMoviesByCategory(String category) {
        Log.d(TAG, "Filtering movies by category: " + category);
        if (category.equals("All")) {
            loadMovies();
            return;
        }

        moviesDb.orderByChild("categories").equalTo(category).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                movieList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    try {
                        Movie movie = dataSnapshot.getValue(Movie.class);
                        if (movie != null) {
                            movieList.add(movie);
                        }
                    } catch (RuntimeException e) {
                        Log.e(TAG, "Error deserializing movie: " + dataSnapshot.getKey(), e);
                    }
                }
                Log.d(TAG, "Movies in filtered list: " + movieList.size());
                movieAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, "Error filtering movies: ", error.toException());
            }
        });
    }

    private void loadCategories() {
        Log.d(TAG, "Loading categories...");
        categoriesDb.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                categoriesList.clear();
                categoriesList.add("All"); // Add default "All" category
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    String category = dataSnapshot.child("name").getValue(String.class);
                    if (category != null) {
                        categoriesList.add(category);
                    }
                }
                categoryAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, "Error loading categories: ", error.toException());
            }
        });
    }
}