package com.example.moveniba;

import android.os.Bundle;
import android.text.InputType;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class UserManagementActivity extends AppCompatActivity implements RankingAdapter.OnItemClickListener {

    private RankingDataSource dataSource;
    private RecyclerView recyclerView;
    private RankingAdapter adapter;
    private List<Ranking> userList;
    private EditText editTextName;
    private Button buttonSave;
    private Button buttonBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_management);

        dataSource = new RankingDataSource(this);
        dataSource.open();

        recyclerView = findViewById(R.id.recycler_view_users);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        editTextName = findViewById(R.id.edit_text_name);
        buttonSave = findViewById(R.id.button_save);
        buttonBack = findViewById(R.id.button_back);

        buttonSave.setOnClickListener(v -> {
            String name = editTextName.getText().toString().trim();
            if (!name.isEmpty()) {
                long id = dataSource.addRanking(name);
                if (id != -1) {
                    updateUserList();
                    editTextName.setText("");
                } else {
                    Toast.makeText(UserManagementActivity.this, "Erro ao salvar usuário", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(UserManagementActivity.this, "Digite um nome", Toast.LENGTH_SHORT).show();
            }
        });
        
        buttonBack.setOnClickListener(v -> finish()); // Lógica para o botão Voltar

        updateUserList();
    }

    private void updateUserList() {
        userList = dataSource.getAllRankings();
        adapter = new RankingAdapter(this, userList);
        adapter.setOnItemClickListener(this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onEditClick(int position) {
        Ranking selectedUser = userList.get(position);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Editar Nome");

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        input.setText(selectedUser.getName());
        builder.setView(input);

        builder.setPositiveButton("Salvar", (dialog, which) -> {
            String newName = input.getText().toString().trim();
            if (!newName.isEmpty()) {
                dataSource.updateName(selectedUser.getId(), newName);
                updateUserList();
            }
        });
        builder.setNegativeButton("Cancelar", (dialog, which) -> dialog.cancel());

        builder.show();
    }

    @Override
    public void onDeleteClick(int position) {
        dataSource.deleteRanking(userList.get(position).getId());
        userList.remove(position);
        adapter.notifyItemRemoved(position);
        adapter.notifyItemRangeChanged(position, userList.size());
    }

    @Override
    protected void onResume() {
        dataSource.open();
        super.onResume();
        updateUserList();
    }

    @Override
    protected void onPause() {
        dataSource.close();
        super.onPause();
    }
}
