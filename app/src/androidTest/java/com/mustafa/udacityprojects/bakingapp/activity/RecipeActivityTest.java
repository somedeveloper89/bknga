package com.mustafa.udacityprojects.bakingapp.activity;


import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.mustafa.udacityprojects.bakingapp.R;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.hamcrest.core.IsInstanceOf;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class RecipeActivityTest {

    @Rule
    public ActivityTestRule<RecipeActivity> mActivityTestRule = new ActivityTestRule<>(
            RecipeActivity.class);

    private static Matcher<View> childAtPosition(final Matcher<View> parentMatcher,
                                                 final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent) &&
                        view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }

    @Test
    public void recipeActivityTest() {
        onView(withText("Nutella Pie"));

        onView(withText("Baking Time"));

        ViewInteraction cardView = onView(allOf(withId(R.id.card_view),
                childAtPosition(childAtPosition(withId(R.id.recyclerview), 3), 0), isDisplayed()));
        cardView.perform(click());

        ViewInteraction recyclerView = onView(allOf(withId(R.id.recipestep_list), childAtPosition(
                allOf(withId(R.id.frameLayout),
                        childAtPosition(IsInstanceOf.<View>instanceOf(android.view.ViewGroup.class),
                                1)), 0), isDisplayed()));
        recyclerView.check(matches(isDisplayed()));

        ViewInteraction textView3 = onView(
                allOf(withId(R.id.header), withText("Prebake cookie crust. "),
                        childAtPosition(childAtPosition(withId(R.id.recipestep_list), 5), 0),
                        isDisplayed()));
        textView3.check(matches(withText("Prebake cookie crust. ")));

        ViewInteraction textView4 = onView(
                allOf(withId(R.id.header), withText("Final cooling and set."),
                        childAtPosition(childAtPosition(withId(R.id.recipestep_list), 13), 0),
                        isDisplayed()));
        textView4.check(matches(withText("Final cooling and set.")));

        ViewInteraction recyclerView2 = onView(
                allOf(withId(R.id.recipestep_list), childAtPosition(withId(R.id.frameLayout), 0)));
        recyclerView2.perform(actionOnItemAtPosition(7, click()));

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource
        // /index.html
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction appCompatImageButton = onView(
                allOf(withId(R.id.exo_play), withContentDescription("Play"), childAtPosition(
                        childAtPosition(withClassName(is("android.widget.LinearLayout")), 0), 4),
                        isDisplayed()));
        appCompatImageButton.perform(click());

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource
        // /index.html
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction nextStep = onView(withId(R.id.navigate_next_step));
        nextStep.check(matches(withText("Next step")));

        ViewInteraction prevStep = onView(withId(R.id.navigate_preivous_step));
        prevStep.check(matches(withText("Previous step")));


        onView(withText("Cheesecake"));
    }
}
