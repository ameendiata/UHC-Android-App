


package com.example.uhc;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;

public class PatientDetailFragment extends Fragment {
    private ImageView avatar;

    public PatientDetailFragment() {
    }

    public static PatientDetailFragment newInstance(Patient patient) {
        PatientDetailFragment fragment = new PatientDetailFragment();

        Bundle args = new Bundle();

        args.putString("id", patient.getId());
        args.putString("name", patient.getName());
        args.putInt("age", patient.getAge());
        args.putString("gender", patient.getGender());
        args.putString("bloodGroup", patient.getBloodGroup());
        args.putString("history", patient.getMedicalHistory());
        args.putString("allergies", patient.getAllergies());
        args.putString("bedNumber", patient.getBedNumber());
        args.putString("profileImageUrl", patient.getProfileImageUrl());

        fragment.setArguments(args);

        return fragment;
    }

    public static PatientDetailFragment newEmptyInstance() {
        return new PatientDetailFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        return inflater.inflate(
                R.layout.fragment_patient_detail,
                container,
                false
        );
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView name = view.findViewById(R.id.detail_name);
        TextView id = view.findViewById(R.id.detail_id);
        TextView age = view.findViewById(R.id.detail_age);
        TextView gender = view.findViewById(R.id.detail_gender);
        TextView blood = view.findViewById(R.id.detail_blood_group);
        TextView history = view.findViewById(R.id.detail_history);
        TextView allergies = view.findViewById(R.id.detail_allergies);
        TextView bedNumberView = view.findViewById(R.id.detail_bed_number);

        avatar = view.findViewById(R.id.detail_avatar);
        Button back = view.findViewById(R.id.back_button);

        Bundle args = getArguments();
        boolean hasPatient = args != null && args.containsKey("id");

        boolean tablet = getActivity() != null
                && getActivity().findViewById(R.id.detail_container) != null;

        if (hasPatient) {
            String patientName = args.getString("name", "Patient");
            String bedNumber = args.getString("bedNumber", "");

            name.setText(patientName);
            id.setText("Patient ID: " + args.getString("id", ""));
            age.setText("Age: " + args.getInt("age") + " years");
            gender.setText("Gender: " + args.getString("gender", ""));
            blood.setText("Blood group: " + args.getString("bloodGroup", ""));
            bedNumberView.setText("Ward and Bed: " + bedNumber);

            history.setText(
                    args.getString("history", "No history recorded.")
            );

            allergies.setText(
                    args.getString("allergies", "No information available.")
            );

            // Load the patient's local drawable photograph.
            String imageUrl = args.getString("profileImageUrl");

            int imageResource;

            if ("patient2".equals(imageUrl)) {
                imageResource = R.drawable.patient2;
            } else if ("patient3".equals(imageUrl)) {
                imageResource = R.drawable.patient3;
            } else if ("patient4".equals(imageUrl)) {
                imageResource = R.drawable.patient4;
            } else {
                imageResource = R.drawable.patient1;
            }

            Glide.with(this)
                    .load(imageResource)
                    .override(600, 600)
                    .centerCrop()
                    .placeholder(android.R.drawable.ic_menu_gallery)
                    .error(android.R.drawable.ic_menu_gallery)
                    .into(avatar);

        } else {
            name.setText("Select a patient");
            bedNumberView.setText("");

            Glide.with(this).clear(avatar);

            avatar.setImageResource(android.R.drawable.ic_menu_gallery);

            id.setText("Patient details will appear here.");
            age.setText("");
            gender.setText("");
            blood.setText("");

            history.setText(
                    "Choose a patient from the list to view their medical history."
            );

            allergies.setText("No patient selected.");
        }

        // Hide the back button on tablets.
        back.setVisibility(tablet ? View.GONE : View.VISIBLE);

        back.setOnClickListener(v -> {
            if (getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity()).returnToPatientList();
            }
        });

    }
    @Override
    public void onDestroyView() {
        if (avatar != null) {
            Glide.with(this).clear(avatar);
            avatar = null;
        }

        super.onDestroyView();
    }
}
