package com.example.loginsample;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.example.loginsample.databinding.ActivityMainBinding;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class LoginActivity extends AppCompatActivity {
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
 /*       EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        }); */

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        EditText edtUsername = binding.edtUsername;
        EditText edtPassword = binding.edtPassword;
        Button btnLogin = binding.btnLongin;
        Button btnAddAccount = binding.btnAddAccount;

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edtUsername.getText().toString().equals("admin") && edtPassword.getText().toString().equals("admin")) {
                    Toast.makeText(getApplicationContext(), "Bienvenido a mi app", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "Bienvenido a mi app");
                } else {
                    Toast.makeText(getApplicationContext(), "Error en la autenticacion", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "Error en la autenticacion");
                }

            }
        });

        ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult activityResult) {
                        Integer resultCode = activityResult.getResultCode();
                        if (resultCode == AccountActivity.ACCOUNT_ACEPTAR) {
                            Intent data = activityResult.getData();
                            String account_record = data.getStringExtra(AccountActivity.ACCOUNT_RECORD);

                            Gson gson = new Gson();
                            AccountEntity accountEntity = gson.fromJson(account_record, AccountEntity.class);

                            saveToFile(accountEntity, "cuentas.txt");

                            String firstname = accountEntity.getFirstnane();
                            Toast.makeText(getApplicationContext(), "Nombre: " + firstname, Toast.LENGTH_SHORT).show();
                            Log.d("LoginActivity", "Nombre: "+ firstname);
                            readFromFile("cuentas.txt");
                        } else if (resultCode == AccountActivity.ACCOUNT_CANCELAR) {
                            Toast.makeText(getApplicationContext(), "Cancelado", Toast.LENGTH_SHORT).show();
                            Log.d("LoginActivity", "Cancelado");
                        }
                    }
                }
        );

        btnAddAccount.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), AccountActivity.class);
            activityResultLauncher.launch(intent);
        });
    }

    private void saveToFile(AccountEntity accountEntity, String fileName) {
        FileOutputStream fos = null;
        try {
            Gson gson = new Gson();
            String jsonData = gson.toJson(accountEntity);
            fos = openFileOutput(fileName, MODE_APPEND);
            fos.write(jsonData.getBytes());
            fos.write("\n".getBytes());

        } catch (IOException e) {
            Log.e("LoginActivity", "Error al guardar el archivo", e);
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    Log.e("LoginActivity", "Error al cerrar el archivo", e);
                }
            }
        }
    }

    private void readFromFile(String filename) {
        try {
            FileInputStream fis = openFileInput(filename);
            InputStreamReader inputStreamReader = new InputStreamReader(fis);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            String line;
            Log.d(TAG, "Datos guardados:");
            while ((line = bufferedReader.readLine()) != null) {
                Log.d(TAG, line + "\n");
            }
            bufferedReader.close();
        } catch (Exception e) {
            Log.e(TAG, "Error al leer datos: " + e.getMessage());
            Toast.makeText(this, "Error al leer datos", Toast.LENGTH_SHORT).show();
        }
    }

}