package com.example.ev2;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ev2.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {

    private TextView mTextView;
    private ActivityMainBinding binding;
    private ListView listView;
    private ArrayList<Note> notes = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

//        mTextView = binding.text;
//        mTextView.setText("ola");

        listView = binding.lista;

        notes.add(0, new Note("", ""));
//        notes.add(1, new Note("titulo ejemplo2", "2"));
        listView.setAdapter(new ListViewAdapter(this, 0, notes));

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    vozAtexto();
                } else {
                    Note note = (Note) parent.getItemAtPosition(position);
                    AlertDialog.Builder dialog = new AlertDialog.Builder(view.getContext());
                    dialog.setTitle("Eliminar nota?")
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialoginterface, int i) {
                                    dialoginterface.cancel();
                                }
                            })
                            .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialoginterface, int i) {
                                    notes.remove(note);
                                    listView.setAdapter(new ListViewAdapter(view.getContext(), 0, notes));
//                                    Toast.makeText(MainActivity.this, "posicion: " + note, Toast.LENGTH_SHORT).show();
                                }
                            }).show();
                }
            }
        });

    }

    public void vozAtexto() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "cual es el titulo?");
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE, "es-MX");
        startActivityForResult(intent, 1001);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1001 && resultCode == RESULT_OK) {
            List<String> results = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            String message = results.get(0);
//            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
            Note nota = new Note(message, null);
            notes.add(nota);
            listView.setAdapter(new ListViewAdapter(this, 0, notes));
        }/* else {
            Toast.makeText(this, "Error al obtener texto", Toast.LENGTH_SHORT).show();
        }*/
    }

}