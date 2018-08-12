package com.mashjulal.android.financetracker.presentation.editoperation;

import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.mashjulal.android.financetracker.R;
import com.mashjulal.android.financetracker.presentation.root.MainActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class EditOperationActivityTest {
    @Rule
    public ActivityTestRule<MainActivity> activityTestRule =
            new ActivityTestRule<>(MainActivity.class);

    @Test
    public void addOperationTest() {
        onView(withId(R.id.menuSpinnerAccounts)).perform(click());
        onView(withText("Наличные рубли")).perform(click());
        onView(withId(R.id.rvMenu)).perform(
                RecyclerViewActions.actionOnItemAtPosition(1, RvViewAction.clickChildViewWithId(R.id.btnNewOperation)));
        onView(withId(R.id.etAmount))
                .perform(typeText("1111"));
        onView(withId(R.id.menuItemDone)).perform(click());

    }

}
