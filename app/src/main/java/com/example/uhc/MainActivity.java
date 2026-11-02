
package com.example.uhc;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class MainActivity extends AppCompatActivity
        implements PatientListFragment.OnPatientSelectedListener {

    private boolean isTablet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        isTablet = findViewById(R.id.detail_container) != null;

        // Add Fragments only when creating the Activity for the first time.
        if (savedInstanceState == null) {

            FragmentManager fragmentManager = getSupportFragmentManager();

            if (isTablet) {

                fragmentManager.beginTransaction()
                        .setReorderingAllowed(true)
                        .replace(R.id.list_container, new PatientListFragment())
                        .commit();

                fragmentManager.beginTransaction()
                        .setReorderingAllowed(true)
                        .replace(
                                R.id.detail_container,
                                PatientDetailFragment.newEmptyInstance()
                        )
                        .commit();

            } else {

                fragmentManager.beginTransaction()
                        .setReorderingAllowed(true)
                        .replace(R.id.fragment_container, new PatientListFragment())
                        .commit();
            }
        }
    }

    @Override
    public void onPatientSelected(Patient patient) {

        FragmentManager fragmentManager = getSupportFragmentManager();

        // Avoid committing transactions after the FragmentManager saves its state.
        if (fragmentManager.isStateSaved()
                || isFinishing()
                || isDestroyed()) {
            return;
        }

        PatientDetailFragment details =
                PatientDetailFragment.newInstance(patient);

        FragmentTransaction transaction =
                fragmentManager.beginTransaction();

        transaction.setReorderingAllowed(true);

        if (isTablet) {

            transaction.replace(R.id.detail_container, details)
                    .commit();

        } else {

            transaction.replace(R.id.fragment_container, details)
                    .addToBackStack("patient-details")
                    .commit();
        }
    }

    public void returnToPatientList() {

        FragmentManager fragmentManager = getSupportFragmentManager();

        if (!isTablet
                && !fragmentManager.isStateSaved()
                && fragmentManager.getBackStackEntryCount() > 0) {

            fragmentManager.popBackStack();
        }
    }
}
