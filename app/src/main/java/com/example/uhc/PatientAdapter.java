


package com.example.uhc;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.DiffUtil;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class PatientAdapter extends RecyclerView.Adapter<PatientAdapter.PatientViewHolder> {

    public interface OnPatientClickListener {
        void onPatientClick(Patient patient);
    }

    private final List<Patient> patients;
    private final List<Patient> allPatients;
    private final OnPatientClickListener listener;

    private String currentQuery = "";

    public PatientAdapter(List<Patient> patients,
                          OnPatientClickListener listener) {

        this.patients = new ArrayList<>(patients);
        this.allPatients = new ArrayList<>(patients);
        this.listener = listener;
    }

    // Add a newly registered patient.
    public void addPatient(Patient patient) {
        allPatients.add(0, patient);
        filterPatients(currentQuery);
    }

    // Search patients by name or patient ID.
    public void filterPatients(String query) {

        currentQuery = query == null ? "" : query.trim();

        List<Patient> newPatients = new ArrayList<>();

        if (currentQuery.isEmpty()) {

            newPatients.addAll(allPatients);

        } else {

            String searchText = currentQuery.toLowerCase(Locale.ROOT);

            for (Patient patient : allPatients) {

                String patientName = patient.getName().toLowerCase(Locale.ROOT);
                String patientId = patient.getId().toLowerCase(Locale.ROOT);

                if (patientName.contains(searchText)
                        || patientId.contains(searchText)) {

                    newPatients.add(patient);
                }
            }
        }

        updatePatientList(newPatients);
    }

    // Update only the RecyclerView rows that have changed.
    private void updatePatientList(List<Patient> newPatients) {

        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(
                new DiffUtil.Callback() {

                    @Override
                    public int getOldListSize() {
                        return patients.size();
                    }

                    @Override
                    public int getNewListSize() {
                        return newPatients.size();
                    }

                    @Override
                    public boolean areItemsTheSame(int oldPosition, int newPosition) {
                        return patients.get(oldPosition).getId()
                                .equals(newPatients.get(newPosition).getId());
                    }

                    @Override
                    public boolean areContentsTheSame(int oldPosition, int newPosition) {
                        Patient oldPatient = patients.get(oldPosition);
                        Patient newPatient = newPatients.get(newPosition);

                        return oldPatient.getName().equals(newPatient.getName())
                                && oldPatient.getAge() == newPatient.getAge()
                                && oldPatient.getGender().equals(newPatient.getGender())
                                && oldPatient.getBloodGroup().equals(newPatient.getBloodGroup())
                                && oldPatient.getBedNumber().equals(newPatient.getBedNumber())
                                && oldPatient.getProfileImageUrl().equals(newPatient.getProfileImageUrl());
                    }
                }
        );

        patients.clear();
        patients.addAll(newPatients);

        diffResult.dispatchUpdatesTo(this);
    }

    @NonNull
    @Override
    public PatientViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_patient, parent, false);

        return new PatientViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull PatientViewHolder holder, int position) {

        Patient patient = patients.get(position);

        holder.name.setText(patient.getName());

        holder.meta.setText(
                "Patient ID: " + patient.getId()
                        + "   •   Age: " + patient.getAge()
                        + "   •   Bed: " + patient.getBedNumber()
        );

        int imageResource;

        switch (patient.getProfileImageUrl()) {
            case "patient1":
                imageResource = R.drawable.patient1;
                break;

            case "patient2":
                imageResource = R.drawable.patient2;
                break;

            case "patient3":
                imageResource = R.drawable.patient3;
                break;

            case "patient4":
                imageResource = R.drawable.patient4;
                break;

            default:
                imageResource = R.drawable.patient1;
                break;
        }

        Glide.with(holder.avatar.getContext())
                .load(imageResource)
                .override(200, 200)
                .centerCrop()
                .placeholder(android.R.drawable.ic_menu_gallery)
                .error(android.R.drawable.ic_menu_gallery)
                .into(holder.avatar);

        holder.itemView.setOnClickListener(v -> {

            if (listener != null) {
                listener.onPatientClick(patient);
            }
        });
    }

    @Override
    public int getItemCount() {
        return patients.size();
    }

    static class PatientViewHolder extends RecyclerView.ViewHolder {

        ImageView avatar;
        TextView name;
        TextView meta;

        PatientViewHolder(@NonNull View itemView) {
            super(itemView);

            avatar = itemView.findViewById(R.id.patient_avatar);
            name = itemView.findViewById(R.id.patient_name);
            meta = itemView.findViewById(R.id.patient_meta);
        }
    }
}
