package phcom.phlynk.lab5_and103;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;
import android.widget.Toast;

import phcom.phlynk.lab5_and103.model.Response;
import phcom.phlynk.lab5_and103.model.Distributor;
import phcom.phlynk.lab5_and103.databinding.ActivityMainBinding;
import phcom.phlynk.lab5_and103.adapter.DistributorAdapter;
import phcom.phlynk.lab5_and103.databinding.DialogAddBinding;
import phcom.phlynk.lab5_and103.services.HttpRequest;

import java.util.ArrayList;
import retrofit2.Call;
import retrofit2.Callback;

public class MainActivity extends AppCompatActivity implements DistributorAdapter.DistributorClick {
    private ActivityMainBinding binding;
    private HttpRequest httpRequest;
    private ArrayList<Distributor> list = new ArrayList<>();
    private DistributorAdapter adapter;
    private static final String TAG = "MainActivity";
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        httpRequest = new HttpRequest();

        fetchAPI();
        userListener();

        binding.edSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    String key = binding.edSearch.getText().toString().trim();
                    httpRequest.callAPI()
                            .searchDistributor(key)
                            .enqueue(getDistributorAPI);
                    Log.d(TAG, "onEditorAction: " + key);
                    return true;
                }
                return false;
            }
        });
    }

    private void fetchAPI() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Loading...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        httpRequest.callAPI()
                .getListDistributor()
                .enqueue(getDistributorAPI);
    }

    private void userListener() {
        binding.btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogAdd();
            }
        });
    }

    private void showDialogAdd() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add distributor");
        DialogAddBinding binding1 = DialogAddBinding.inflate(LayoutInflater.from(this));
        builder.setView(binding1.getRoot());
        AlertDialog alertDialog = builder.create();
        binding1.btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = binding1.etName.getText().toString().trim();
                if (name.isEmpty()) {
                    Toast.makeText(MainActivity.this, "You must enter a name", Toast.LENGTH_SHORT).show();
                } else {
                    Distributor distributor = new Distributor();
                    distributor.setName(name);
                    httpRequest.callAPI()
                            .addDistributor(distributor)
                            .enqueue(responseDistributorAPI);
                    alertDialog.dismiss();
                }
            }
        });
        alertDialog.show();
    }

    private void getData() {
        adapter = new DistributorAdapter(list, this, this);
        binding.rcvDistributor.setAdapter(adapter);
        progressDialog.dismiss();
    }

    Callback<Response<ArrayList<Distributor>>> getDistributorAPI = new Callback<Response<ArrayList<Distributor>>>() {
        @Override
        public void onResponse(Call<Response<ArrayList<Distributor>>> call, retrofit2.Response<Response<ArrayList<Distributor>>> response) {
            if (response.isSuccessful()) {
                if (response.body().getStatus() == 200) {
                    list = response.body().getData();
                    getData();
                    Log.d(TAG, "onResponse: " + list.size());
                }
            } else {
                Log.e(TAG, "onResponse: response not successful");
                progressDialog.dismiss();
            }
        }

        @Override
        public void onFailure(Call<Response<ArrayList<Distributor>>> call, Throwable t) {
            Log.e(TAG, "onFailure: " + t.getMessage());
            progressDialog.dismiss();
        }
    };

    Callback<Response<ArrayList<Distributor>>> responseDistributorAPI = new Callback<Response<ArrayList<Distributor>>>() {
        @Override
        public void onResponse(Call<Response<ArrayList<Distributor>>> call, retrofit2.Response<Response<ArrayList<Distributor>>> response) {
            if (response.isSuccessful()) {
                if (response.body().getStatus() == 200) {
                    list = response.body().getData();
                    getData();
                } else {
                    Toast.makeText(MainActivity.this, "Failed to update list", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(MainActivity.this, "Failed to update list", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onFailure(Call<Response<ArrayList<Distributor>>> call, Throwable t) {
            Log.e(TAG, "onFailure: " + t.getMessage());
        }
    };

    private void showDialogEdit(Distributor distributor) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Edit distributor");
        DialogAddBinding binding1 = DialogAddBinding.inflate(LayoutInflater.from(this));
        builder.setView(binding1.getRoot());
        AlertDialog alertDialog = builder.create();

        binding1.etName.setText(distributor.getName());

        binding1.btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = binding1.etName.getText().toString().trim();
                if (name.isEmpty()) {
                    Toast.makeText(MainActivity.this, "You must enter a name", Toast.LENGTH_SHORT).show();
                } else {
                    Distributor distributor1 = new Distributor();
                    distributor1.setName(name);
                    httpRequest.callAPI()
                            .updateDistributor(distributor.getId(), distributor1)
                            .enqueue(responseDistributorAPI);
                    alertDialog.dismiss();
                }
            }
        });
        alertDialog.show();
    }

    @Override
    public void delete(Distributor distributor) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirm delete");
        builder.setMessage("Are you sure you want to delete?");
        builder.setPositiveButton("Yes", (dialog, which) -> {
            httpRequest.callAPI()
                    .deleteDistributor(distributor.getId())
                    .enqueue(responseDistributorAPI);
        });
        builder.setNegativeButton("No", (dialog, which) -> dialog.dismiss());
        builder.show();
    }

    @Override
    public void edit(Distributor distributor) {
        showDialogEdit(distributor);
    }
}
