package com.mustafa.udacityprojects.bakingapp;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.mustafa.udacityprojects.bakingapp.activity.RecipeActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Instrumented test, which will execute on an Android device.
 */
@RunWith(AndroidJUnit4.class)
public class RecipeActivityInstrumentedTest {
    @Rule
    public ActivityTestRule<RecipeActivity> mActivityRule = new ActivityTestRule<>(
            RecipeActivity.class);

    @Test
    public void checkIfTheBrowniesRecipeIsAvailable() {
        // check if brownies recipe is displayed.
        onView(withText("Brownies")).check(matches(isDisplayed()));

        // click the brownies recipe.
        onView(withText("Brownies")).perform(click());

        // verify the title contains brownies.
        onView(withText("Brownies")).check(matches(isDisplayed()));

        // verify ingredients is visible.
        onView(withText("Recipe Ingredients")).check(matches(isDisplayed()));

    }
}
