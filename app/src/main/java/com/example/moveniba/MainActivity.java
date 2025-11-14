package com.example.moveniba;

import android.app.AlertDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    TextView totalQuestionsTextView;
    TextView questionTextView;
    Button ansA, ansB, ansC, ansD;
    Button submitBtn;

    int score=0;
    int totalQuestions = QuestionAnswer.question.length;
    int currentQuestionIndex = 0;
    String selectedAnswer = "";

    private int playerId;
    private RankingDataSource dataSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        dataSource = new RankingDataSource(this);
        dataSource.open();

        playerId = getIntent().getIntExtra("PLAYER_ID", -1);
        if (playerId == -1) {
            finish(); // No player ID, can't proceed
            return;
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);

            totalQuestionsTextView = findViewById(R.id.total_question);
            questionTextView = findViewById(R.id.question);
            ansA = findViewById(R.id.ans_A);
            ansB = findViewById(R.id.ans_B);
            ansC = findViewById(R.id.ans_C);
            ansD = findViewById(R.id.ans_D);
            submitBtn = findViewById(R.id.submit_btn);

            ansA.setOnClickListener(this);
            ansB.setOnClickListener(this);
            ansC.setOnClickListener(this);
            ansD.setOnClickListener(this);
            submitBtn.setOnClickListener(this);

            totalQuestionsTextView.setText("Questões Totais -> "+totalQuestions);
            loadnewquestion();
            return insets;
        });
    }

    @Override
    public void onClick(View view) {

        ansA.setBackgroundColor(Color.WHITE);
        ansB.setBackgroundColor(Color.WHITE);
        ansC.setBackgroundColor(Color.WHITE);
        ansD.setBackgroundColor(Color.WHITE);

        Button clickedButton = (Button) view;
        if(clickedButton.getId()==R.id.submit_btn){
            if (selectedAnswer.equals(QuestionAnswer.correctAnswers[currentQuestionIndex])){
                score++;
            }
            currentQuestionIndex++;
            loadnewquestion();
        }else{
            //choices button clicked
            selectedAnswer = clickedButton.getText().toString();
            clickedButton.setBackgroundColor(Color.MAGENTA);
        }
    }

    void loadnewquestion(){
        if(currentQuestionIndex == totalQuestions){
            finishQuiz();
            return;
        }

        questionTextView.setText(QuestionAnswer.question[currentQuestionIndex]);
        ansA.setText(QuestionAnswer.choices[currentQuestionIndex][0]);
        ansB.setText(QuestionAnswer.choices[currentQuestionIndex][1]);
        ansC.setText(QuestionAnswer.choices[currentQuestionIndex][2]);
        ansD.setText(QuestionAnswer.choices[currentQuestionIndex][3]);
    }

    void finishQuiz(){
        dataSource.updateScoreIfHigher(playerId, score);

        String passStatus = score > totalQuestions * 0.60 ? "Passou" : "Falhou";

        new AlertDialog.Builder(this)
                .setTitle(passStatus)
                .setMessage("A pontuação é " + score + " de " + totalQuestions)
                .setPositiveButton("Voltar ao Ranking", (dialogInterface, i) -> finish())
                .setCancelable(false)
                .show();
    }

    @Override
    protected void onPause() {
        dataSource.close();
        super.onPause();
    }

    @Override
    protected void onResume() {
        dataSource.open();
        super.onResume();
    }
}
