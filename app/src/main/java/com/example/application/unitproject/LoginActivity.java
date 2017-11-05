package com.example.application.unitproject;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private EditText inputName, inputNumber;
    private TextInputLayout inputLayoutName, inputLayoutNumber;
    private Button signIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        signIn= findViewById(R.id.sign_in);
        inputLayoutName = findViewById(R.id.input_layout_name);
        inputLayoutNumber = findViewById(R.id.input_layout_number);
        inputName = findViewById(R.id.name_et);
        inputNumber = findViewById(R.id.number_et);

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });
    }

    public void signIn(){
        if (!validateName()) {
            return;
        }
        if (!validateNumber()) {
            return;
        }
        final String name = inputName.getText().toString();
        final String number = inputNumber.getText().toString();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.login_dialog,null);
        builder.setView(view);
        builder.setCancelable(true);
        final ProgressBar progressBar = view.findViewById(R.id.progress);
        final TextView logging = view.findViewById(R.id.logging_tv);
        final TextView responseTv = view.findViewById(R.id.response);
        final AlertDialog log_dialog = builder.create();
        log_dialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, "www.ikai.co.in/api/login.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);

                            if (!obj.getBoolean("error")) {

                                log_dialog.hide();
                                finish();
                                startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                            } else {
                                progressBar.setVisibility(View.INVISIBLE);
                                logging.setVisibility(View.INVISIBLE);
                                responseTv.setVisibility(View.VISIBLE);
                                responseTv.setText(obj.getString("message"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressBar.setVisibility(View.INVISIBLE);
                        logging.setVisibility(View.INVISIBLE);
                        responseTv.setVisibility(View.VISIBLE);
                        responseTv.setText(error.getMessage());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("name", name);
                params.put("number", number);
                return params;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }

    private boolean validateName() {
        if (inputName.getText().toString().trim().isEmpty()) {
            inputLayoutName.setError(getString(R.string.err_msg_name));
            requestFocus(inputName);
            return false;
        } else {
            inputLayoutName.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validateNumber() {
        String number = inputNumber.getText().toString().trim();

        if (number.isEmpty() || !TextUtils.isDigitsOnly(number)) {
            inputLayoutNumber.setError(getString(R.string.err_msg_num));
            requestFocus(inputNumber);
            return false;
        } else {
            inputLayoutNumber.setErrorEnabled(false);
        }
        return true;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }
}
