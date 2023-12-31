package com.ublox.BLE_med;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.ublox.BLE_med.activities.MainActivity;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.ublox.BLE_med.ConstantsForTests.MAP_KEY_NAME;
import static com.ublox.BLE_med.ConstantsForTests.TAB_SERVICES;
import static com.ublox.BLE_med.EspressoExtensions.withMapping;
import static com.ublox.BLE_med.Wait.waitFor;
import static org.hamcrest.Matchers.containsString;

@RunWith(AndroidJUnit4.class)
public class TestServicesFragment {

    @Rule
    public ActivityTestRule<MainActivity> act = new MainWithBluetoothTestRule();

    @Before
    public void setup() {
        waitFor(500);
        onView(withText(TAB_SERVICES)).perform(click());
    }

    @Test
    public void readModel() {
        assertService("Device ID","Model Number", "TEST-T0");
    }

    @Test
    public void readManufacturer() {
        assertService("Device ID","Manufacture Name", "u-blox");
    }

    @Test
    public void readDeviceName() {
        assertService("Generic Access","Device Name", "TEST-T0-012345");
    }

    private void assertService(String service, String gatt, String expected) {
        onData(withMapping(MAP_KEY_NAME, service))
            .inAdapterView(withId(R.id.gatt_services_list))
            .perform(click());

        onData(withMapping(MAP_KEY_NAME, gatt))
            .inAdapterView(withId(R.id.gatt_services_list))
            .perform(click());

        onView(withId(R.id.tvValue)).check(matches(withText(containsString(expected))));
    }
}
