package com.ublox.BLE_med;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.ublox.BLE_med.activities.MainActivity;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.ublox.BLE_med.ConstantsForTests.TAB_CHAT;
import static com.ublox.BLE_med.ConstantsForTests.TAB_SERVICES;
import static com.ublox.BLE_med.ConstantsForTests.TAB_TEST;
import static com.ublox.BLE_med.EspressoExtensions.onEveryView;
import static com.ublox.BLE_med.Wait.waitFor;

@RunWith(AndroidJUnit4.class)
public class TestMainActivity {

    @Rule
    public ActivityTestRule<MainActivity> act = new MainWithBluetoothTestRule();

    @Before
    public void setup() {
        waitFor(500);
    }

    @Test
    public void tabsExists() {
        onEveryView(
            onView(withText(act.getActivity().getString(R.string.title_section1).toUpperCase())),
            onView(withText(TAB_SERVICES)),
            onView(withText(TAB_CHAT)),
            onView(withText(TAB_TEST))
        ).check(matches(isDisplayed()));
    }

    @Test
    public void disconnectFromDevice() {
        onView(withId(R.id.menu_disconnect)).perform(click());
        onView(withId(R.id.menu_connect)).check(matches(isDisplayed()));
    }

    @Test
    public void reconnectToDevice() {
        onView(withId(R.id.menu_disconnect)).perform(click());
        onView(withId(R.id.menu_connect)).perform(click());
        onView(withId(R.id.menu_disconnect)).check(matches(isDisplayed()));
    }
}
