package ConnectFour;

import MyAI.AIStructure;

import java.util.ArrayList;

public class Connect4Backend {
    short[][] grid;
    boolean isYellowsTurn = false;
    short winner = 0;

    public Connect4Backend() {
        grid = new short[7][6];
    }

    public short get(int column,int row) {
        return grid[column][row];
    }
    boolean isFull(int column) {
        for (int i = 0; i < 6; i++) if (get(column, i) == 0) return false;
        return true;
    }
    public boolean checkForWin(char player) {
        //check for 4 across
        for (short[] shorts : grid)
            for (int col = 0; col < grid[0].length - 3; col++)
                if (shorts[col] == player &&
                        shorts[col + 1] == player &&
                        shorts[col + 2] == player &&
                        shorts[col + 3] == player)
                    return true;
        //check for 4 up and down
        for(int row = 0; row < grid.length - 3; row++)
            for (int col = 0; col < grid[0].length; col++)
                if (grid[row][col] == player &&
                        grid[row + 1][col] == player &&
                        grid[row + 2][col] == player &&
                        grid[row + 3][col] == player)
                    return true;
        //check upward diagonal
        for(int row = 3; row < grid.length; row++)
            for (int col = 0; col < grid[0].length - 3; col++)
                if (grid[row][col] == player &&
                        grid[row - 1][col + 1] == player &&
                        grid[row - 2][col + 2] == player &&
                        grid[row - 3][col + 3] == player)
                    return true;
        //check downward diagonal
        for(int row = 0; row < grid.length - 3; row++)
            for(int col = 0; col < grid[0].length - 3; col++)
                if (grid[row][col] == player   &&
                        grid[row+1][col+1] == player &&
                        grid[row+2][col+2] == player &&
                        grid[row+3][col+3] == player)
                    return true;
        return false;
    }
    public boolean place(int column, boolean isYellow) {
        if (isYellow != isYellowsTurn) return false;
        if (isFull(column)) return false;
        short[] col = grid[column];
        int top = -1;
        for (short i : col) {
            if (i == 0) {
                top = i;
                break;
            }
        }
        if (top == -1) return false;
        grid[column][top] = (short) ((isYellow)?1:2);
        winner = (short) (checkForWin((char) ((isYellow)?1:2))? ((isYellow)?1:2): 0);
        isYellowsTurn=!isYellowsTurn;
        return true;
    }
    private ArrayList<Double> flatten() {
        var out = new ArrayList<Double>(7*6);
        for (short[] columns : grid) {
            for (short cell : columns) {
                out.add(cell/2.0);
            }
        }
        return out;
    }
    public int playAI(AIStructure ai1, AIStructure ai2) {
        int turns = 0;
        while (winner <= 0) {

            var currentAI = isYellowsTurn?ai1:ai2;
            currentAI.input(flatten());
            var res = currentAI.run();
            var max = 0;
            for (int i = 0; i < res.size(); i++) {
                if (res.get(max)<res.get(i) && !isFull(i)) max = i;
            }
            if (!place(max, isYellowsTurn)) {
                return (this.isYellowsTurn)?-1:1;
            };
            turns++;
            if (turns%16==0) {
                var b = false;
                for (int i = 0; i < grid.length; i++) {
                    if (!isFull(i)) {
                        b = true;
                        break;
                    }
                }
                if (!b) {
                    return 0;
                }
            } else if (turns>6*7) {
                return 0;
            }
            System.out.print("\t"+max);
        }
        if (winner == 1) return -1;
        return 1;
    }
}
