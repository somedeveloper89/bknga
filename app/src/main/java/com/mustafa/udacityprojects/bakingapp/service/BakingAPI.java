/**
 * Copyright (C) 2018 Mustafa Kabaktepe
 */

package com.mustafa.udacityprojects.bakingapp.service;

import com.mustafa.udacityprojects.bakingapp.model.Recipe;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Retrofit interface for request callbacks.
 */

public interface BakingAPI {
    @GET("baking.json")
    Call<List<Recipe>> getRecipes();
}
