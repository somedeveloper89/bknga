package com.mustafa.udacityprojects.bakingapp.service;

import com.mustafa.udacityprojects.bakingapp.model.Recipe;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * TODO: add description for this class.
 *
 * @author Mustafa Kabaktepe 21/01/2018, SNS Bank N.V.
 */

public interface BakingAPI {
    @GET("baking.json")
    Call<List<Recipe>> getRecipes();
}
