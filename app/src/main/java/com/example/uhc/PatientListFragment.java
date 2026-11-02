

package com.example.uhc;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Button;
import android.widget.Toast;
import android.app.AlertDialog;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.Locale;

import java.util.ArrayList;
import java.util.List;


public class PatientListFragment extends Fragment {

    public interface OnPatientSelectedListener {
        void onPatientSelected(Patient patient);
    }

    private OnPatientSelectedListener listener;
    private final List<Patient> patients = new ArrayList<>();
    private boolean patientsLoaded = false;
    private RecyclerView recyclerView;
    private PatientAdapter adapter;
    private EditText searchInput;
    private TextWatcher searchWatcher;

    public PatientListFragment() {}

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if (context instanceof OnPatientSelectedListener) {
            listener = (OnPatientSelectedListener) context;
        } else {
            throw new IllegalStateException(
                    "MainActivity must implement OnPatientSelectedListener"
            );
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        return inflater.inflate(
                R.layout.fragment_patient_list,
                container,
                false
        );
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.patient_recycler_view);
        searchInput = view.findViewById(R.id.patient_search);
        Button addPatientButton =
                view.findViewById(R.id.add_patient_button);

        addPatientButton.setOnClickListener(v -> showAddPatientDialog());

        recyclerView.setLayoutManager(
                new LinearLayoutManager(requireContext())
        );

        if (!patientsLoaded) {
            loadSamplePatients();
            patientsLoaded = true;
        }

        adapter = new PatientAdapter(patients, patient -> {

            if (listener != null) {
                listener.onPatientSelected(patient);
            }
        });

        recyclerView.setAdapter(adapter);

        // Listen for changes in the search field.
        searchWatcher = new TextWatcher() {

            @Override
            public void beforeTextChanged(
                    CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(
                    CharSequence s, int start, int before, int count) {

                if (adapter != null) {
                    adapter.filterPatients(s.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        };

        searchInput.addTextChangedListener(searchWatcher);
    }

    private void showAddPatientDialog() {
        if (!isAdded() || getView() == null) {
            return;
        }

        View formView = LayoutInflater.from(requireContext())
                .inflate(R.layout.dialog_add_patient, null);

        EditText idInput = formView.findViewById(R.id.input_patient_id);
        EditText nameInput = formView.findViewById(R.id.input_patient_name);
        EditText ageInput = formView.findViewById(R.id.input_patient_age);
        EditText genderInput = formView.findViewById(R.id.input_patient_gender);
        EditText bloodInput = formView.findViewById(R.id.input_patient_blood_group);
        EditText bedInput = formView.findViewById(R.id.input_patient_bed);
        EditText historyInput = formView.findViewById(R.id.input_patient_history);
        EditText allergiesInput = formView.findViewById(R.id.input_patient_allergies);

        AlertDialog dialog = new AlertDialog.Builder(requireContext())
                .setTitle("Register New Patient")
                .setView(formView)
                .setPositiveButton("Register", null)
                .setNegativeButton("Cancel", null)
                .create();

        dialog.setOnShowListener(ignored -> {

            dialog.getButton(AlertDialog.BUTTON_POSITIVE)
                    .setOnClickListener(v -> {

                        String id = idInput.getText().toString().trim();
                        String name = nameInput.getText().toString().trim();
                        String ageText = ageInput.getText().toString().trim();
                        String gender = genderInput.getText().toString().trim();
                        String blood = bloodInput.getText().toString().trim();
                        String bed = bedInput.getText().toString().trim();
                        String history = historyInput.getText().toString().trim();
                        String allergies = allergiesInput.getText().toString().trim();

                        if (id.isEmpty() || name.isEmpty() || ageText.isEmpty()
                                || gender.isEmpty() || blood.isEmpty()
                                || bed.isEmpty() || history.isEmpty()
                                || allergies.isEmpty()) {

                            Toast.makeText(requireContext(),
                                    "Please complete all fields.",
                                    Toast.LENGTH_SHORT).show();
                            return;
                        }

                        int age;

                        try {
                            age = Integer.parseInt(ageText);
                        } catch (NumberFormatException e) {
                            Toast.makeText(requireContext(),
                                    "Enter a valid age.",
                                    Toast.LENGTH_SHORT).show();
                            return;
                        }

                        if (age < 1 || age > 120) {
                            Toast.makeText(requireContext(),
                                    "Age must be between 1 and 120.",
                                    Toast.LENGTH_SHORT).show();
                            return;
                        }

                        for (Patient existing : patients) {
                            if (existing.getId().equalsIgnoreCase(id)) {
                                Toast.makeText(requireContext(),
                                        "This patient ID already exists.",
                                        Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }

                        Patient newPatient = new Patient(
                                id,
                                name,
                                age,
                                gender,
                                blood,
                                history,
                                allergies,
                                bed,
                                "patient1"
                        );

                        // Clear any previous search so the new patient is visible.
                        searchInput.setText("");

                        patients.add(0, newPatient);
                        adapter.addPatient(newPatient);

                        recyclerView.scrollToPosition(0);

                        Toast.makeText(requireContext(),
                                "Patient registered successfully.",
                                Toast.LENGTH_SHORT).show();

                        dialog.dismiss();
                    });
        });

        dialog.show();
    }


    private void loadSamplePatients() {

        patients.clear();

        String[] genders = {"Male", "Female"};
        String[] bloodGroups = {"A+", "B+", "O+", "AB+", "O-", "A-"};

        for (int i = 1; i <= 500; i++) {

            String patientId = String.format(Locale.ROOT, "UHC%03d", i);
            String patientName = String.format(Locale.ROOT, "Demo Patient %03d", i);

            String bedNumber = "Ward " + ((i % 5) + 1)
                    + " - Bed " + String.format(Locale.ROOT, "%03d", i);
            int age = 18 + (i % 60);

            String gender = genders[i % genders.length];

            String bloodGroup = bloodGroups[i % bloodGroups.length];

            String history = "Sample medical history for testing patient "
                    + i + ". No real patient information.";

            String allergies = "Demo record - verify before clinical use.";


            String imageName = "patient" + (((i - 1) % 4) + 1);

            patients.add(new Patient(
                    patientId,
                    patientName,
                    age,
                    gender,
                    bloodGroup,
                    history,
                    allergies,
                    bedNumber,
                    imageName
            ));
        }
    }


    @Override
    public void onDestroyView() {

        // Remove the search listener before destroying the view.
        if (searchInput != null && searchWatcher != null) {
            searchInput.removeTextChangedListener(searchWatcher);
        }

        searchWatcher = null;

        if (recyclerView != null) {
            recyclerView.setAdapter(null);
        }

        recyclerView = null;
        searchInput = null;
        adapter = null;

        super.onDestroyView();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }
}
