package com.ublox.BLE_med;

import android.Manifest;
import android.app.Activity;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.IdlingRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.rule.GrantPermissionRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.runner.lifecycle.ActivityLifecycleMonitorRegistry;
import android.widget.ListView;

import com.ublox.BLE_med.activities.DevicesActivity;
import com.ublox.BLE_med.activities.MainActivity;

import org.junit.After;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Iterator;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.runner.lifecycle.Stage.RESUMED;
import static com.ublox.BLE_med.ConstantsForTests.DEVICE_ADDRESS;
import static com.ublox.BLE_med.ConstantsForTests.TIMEOUT;
import static com.ublox.BLE_med.EspressoExtensions.withDevice;

@RunWith(AndroidJUnit4.class)
public class TestDevicesActivity {
    DeviceDiscoverIdler waitForDevice = null;
    DeviceConnectionIdler waitForConnect = null;

    @Rule
    public ActivityTestRule<DevicesActivity> act = new ActivityTestRule<>(DevicesActivity.class);

    @Rule
    public GrantPermissionRule permissionRule = GrantPermissionRule.grant(Manifest.permission.ACCESS_COARSE_LOCATION);

    @Test
    public void findDeviceInList() {
        onView(withId(R.id.menu_scan)).perform(click());
        DeviceDiscoverIdler awaitData = new DeviceDiscoverIdler(
            DEVICE_ADDRESS,
            ((ListView) act.getActivity().findViewById(R.id.lvDevices)).getAdapter(),
            TIMEOUT
        );
        IdlingRegistry.getInstance().register(awaitData);
        onData(withDevice(DEVICE_ADDRESS)).check(matches(isDisplayed()));
        IdlingRegistry.getInstance().unregister(awaitData);
    }

    @Test
    public void connectToDevice() {
        onView(withId(R.id.menu_scan)).perform(click());
        waitForDevice = new DeviceDiscoverIdler(
            DEVICE_ADDRESS,
            ((ListView) act.getActivity().findViewById(R.id.lvDevices)).getAdapter(),
            TIMEOUT
        );
        IdlingRegistry.getInstance().register(waitForDevice);

        onData(withDevice(DEVICE_ADDRESS)).perform(click());

        waitForConnect = new DeviceConnectionIdler(getMainActivity(), TIMEOUT);
        IdlingRegistry.getInstance().register(waitForConnect);
        onView(withId(R.id.menu_disconnect)).check(matches(isDisplayed()));
    }

    @Test
    public void openAboutPage() {
        onView(withId(R.id.menu_about)).perform(click());
        onView(withId(R.id.bVisit)).check(matches(isDisplayed()));
    }

    @Test
    public void startScan() {
        onView(withId(R.id.menu_scan)).perform(click());
        onView(withId(R.id.menu_stop)).check(matches(isDisplayed()));
    }

    @Test
    public void stopScan() {
        onView(withId(R.id.menu_scan)).perform(click());
        onView(withId(R.id.menu_stop)).perform(click());
        onView(withId(R.id.menu_scan)).check(matches(isDisplayed()));
    }

    @After
    public void unregisterIdlers() {
        IdlingRegistry registry = IdlingRegistry.getInstance();
        if (waitForDevice != null) {
            registry.unregister(waitForDevice);
            waitForDevice = null;
        }
        if (waitForConnect != null) {
            registry.unregister(waitForConnect);
            waitForConnect = null;
        }
    }

    private MainActivity getMainActivity() {
        final MainActivity[] mainAct = new MainActivity[1];
        InstrumentationRegistry
            .getInstrumentation()
            .runOnMainSync(()->{
                Iterator < Activity > acts = ActivityLifecycleMonitorRegistry
                    .getInstance()
                    .getActivitiesInStage(RESUMED)
                    .iterator();
                if (acts.hasNext()) {
                    mainAct[0] = (MainActivity) acts.next();
                }
            });
        return mainAct[0];
    }
}
