package amit.myapp.my_tictactoe;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private boolean player1Turn = true;
    private int rounds;
    private boolean gamePaused = true;
    private LinearLayout boardLayout;
    private ImageView currentPlayer;
    private ImageView table;
    private String[][] board;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.rounds = 0;
        boardLayout = (LinearLayout) findViewById(R.id.board_layout);
        currentPlayer = ((ImageView) findViewById(R.id.currentPlayer));
        table = (ImageView) findViewById(R.id.table);
        board = new String[3][3];

        for (int i = 0; i<3; i++){
            for (int j = 0; j<3; j++) {
                String buttonId = "button_" + i + j;
                int resID = getResources().getIdentifier(buttonId, "id", getPackageName());
                ImageButton btn = (ImageButton) findViewById(resID);

                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        onButtonClick(view);
                    }
                });
            }
        }
    }

    public void startGame(View view){
        Button startButton = (Button) view;
        startButton.setVisibility(View.GONE);
        resetBoard();
        this.gamePaused = false;
        this.player1Turn = true;
        setCurrentPlayer();
    }

    private void updatePlay(String buttonId, String tag){
        String[] parts = buttonId.split("_");
        String id = parts[1];
        int a =Integer.parseInt(String.valueOf(id.charAt(0)));
        int b =Integer.parseInt(String.valueOf(id.charAt(1)));
        board[a][b] = tag;
    }


    public void onButtonClick (View view){
        ImageButton b = (ImageButton) view;
        if ((b.getTag() != null || this.gamePaused == true)) {
            return;
        }
        String buttonId = getResources().getResourceEntryName(view.getId());
        String tag = "";
        if (this.player1Turn){
            b.setBackgroundResource(R.drawable.x);
            tag = "x";
            b.setTag(tag);
        }
        else{
            b.setBackgroundResource(R.drawable.o);
            tag = "o";
            b.setTag(tag);
        }

        updatePlay(buttonId, tag);
        this.rounds++;
        this.player1Turn = !this.player1Turn;
        setCurrentPlayer();
        checkGameStatus();
    }

    private void setCurrentPlayer(){
        if (this.player1Turn){
            this.currentPlayer.setImageResource(R.drawable.xplay);
        }
        else{
            this.currentPlayer.setImageResource(R.drawable.oplay);
        }
    }


    public void checkGameStatus() {
        boolean won = false;
        int markLine = -1;
        int winnerMark = -1 ;

        // Check Row Win
        if (!won) {
            for (int i = 0; i < 3; i++) {
                if (board[i][0] == board[i][1] && board[i][0] == board[i][2] && board[i][0] != null) {
                    won = true;
                    markLine = i == 0 ? R.drawable.mark3 : i == 1 ? R.drawable.mark4 : i == 2 ? R.drawable.mark5 : -1;
                    winnerMark = board[i][0] == "x" ? R.drawable.xwin : R.drawable.owin;
                    printGameResult(winnerMark, markLine, 90);
                }
            }
        }

        // Check Column Win
        if (!won) {
            for (int i = 0; i < 3; i++) {
                if (board[0][i] == board[1][i] && board[0][i] == board[2][i] && board[0][i] != null) {
                    won = true;
                    markLine = i == 0 ? R.drawable.mark3 : i == 1 ? R.drawable.mark4 : i == 2 ? R.drawable.mark5 : -1;
                    winnerMark = board[0][i] == "x" ? R.drawable.xwin : R.drawable.owin;
                    printGameResult(winnerMark, markLine, 0);
                }
            }
        }

        // Check Diagonal Win
        if (!won) {
            if (board[0][0] == board[1][1] && board[0][0] == board[2][2] && board[0][0] != null) {
                won = true;
                markLine = R.drawable.mark1;
                winnerMark = board[0][0] == "x" ? R.drawable.xwin : R.drawable.owin;
                printGameResult(winnerMark, markLine, 0);
            }
        }

        // Check Diagonal Win
        if (!won) {
            if (board[0][2] == board[1][1] && board[0][2] == board[2][0] && board[0][2] != null) {
                won = true;
                markLine = R.drawable.mark2;
                winnerMark = board[0][2] == "x" ? R.drawable.xwin : R.drawable.owin;
                printGameResult(winnerMark, markLine, 0);
            }
        }

        if (!won && rounds == 9){
            draw();
        }
    }


    public void printGameResult(int winnerMark, int markLine, int rotation){
        Drawable mark = getResources().getDrawable(markLine);
        this.table.setForeground(mark);
        this.table.setRotation(rotation);
        showGameOver(winnerMark);
        gameOver();

    }

    public void draw(){
        showGameOver(R.drawable.nowin);
        gameOver();
    }

    public void showGameOver(int mark){
        // show a dialog
        this.currentPlayer.setImageResource(mark);
    }

    public void gameOver(){
        Button startButton = (Button) findViewById(R.id.start_game_button);
        startButton.setVisibility(View.VISIBLE);
        gamePaused = true;

    }

    public void resetBoard() {
        for (int i = 0; i<3; i++){
            for (int j = 0; j<3; j++) {
                String buttonId = "button_" + i + j;
                int resID = getResources().getIdentifier(buttonId, "id", getPackageName());
                ImageButton currentButton =  ((ImageButton) findViewById(resID));
                currentButton.setBackgroundResource(R.drawable.empty);
                currentButton.setTag(null);
                board[i][j] = null;
            }
        }
        Drawable empty = getResources().getDrawable(R.drawable.empty);
        table.setForeground(empty);
        table.setRotation(0);
        rounds = 0;
    }
}