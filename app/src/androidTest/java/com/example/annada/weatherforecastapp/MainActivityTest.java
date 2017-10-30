package com.example.annada.weatherforecastapp;

import android.support.test.espresso.assertion.ViewAssertions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.example.annada.weatherforecastapp.view.activities.MainActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * Created by annada on 19/10/2017.
 */

@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    @Rule
    public ActivityTestRule<MainActivity> activityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void shouldBeAbleToMainScreen(){

        onView(withId(R.id.city_country)).check(ViewAssertions.matches(isDisplayed()));

        onView(withId(R.id.current_date)).check(ViewAssertions.matches(isDisplayed()));

        onView(withId(R.id.weather_icon)).check(ViewAssertions.matches(isDisplayed()));

        onView(withId(R.id.weather_result)).check(ViewAssertions.matches(isDisplayed()));

        onView(withId(R.id.wind_result)).check(ViewAssertions.matches(isDisplayed()));

        onView(withId(R.id.humidity_result)).check(ViewAssertions.matches(isDisplayed()));

        onView(withId(R.id.weather_daily_list)).check(ViewAssertions.matches(isDisplayed()));
    }
}