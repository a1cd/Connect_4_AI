package MyAI;

import ConnectFour.Connect4Backend;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class AITrainer {
    AIStructure baseStructure;
    ArrayList<AIStructure> variations;
    ArrayList<Integer> wins;

    public AITrainer(AIStructure aiStructure) {
        baseStructure = aiStructure;
    }
    
    void run(Double tweakMultiplier, int variationCount) {
        variations = new ArrayList<>();
        wins = new ArrayList<>();
        System.out.println("creating ais");
        for (int i = 0; i < variationCount; i++) {
            var newAI = new AIStructure(baseStructure);
            newAI.tweak(Math.pow(Math.random(), 2.0)*tweakMultiplier);
            variations.add(newAI);
            wins.add(0);
        }
        System.out.println("AIs created");
        for (int x = 0; x < variations.size(); x++) {
            for (int y = x; y < variations.size(); y++) {
                System.out.println();
                Connect4Backend game = new Connect4Backend();
                AIStructure ai1 = variations.get(x);
                AIStructure ai2 = variations.get(y);
                if (ai1.equals(ai2)) {
                    System.err.println("ai1 == ai2");
                    continue;
                }
                System.out.println("running game: ("+x+", "+y+")");
                var winner = game.playAI(ai1, ai2);
                if (winner == 1) wins.set(x, wins.get(x)+1);
                else if (winner == 2) wins.set(y, wins.get(y)+1);
            }
        }
        int max = 0;
        for (int i = 0; i < variations.size(); i++) {
            if (wins.get(max) < wins.get(i)) {
                max = i;
            }
        }
        baseStructure = variations.get(max);
    }

    public static void main(String[] args) {
        var inputs = new ArrayList<Double>(6*7);
        for (int i = 0; i < 6*7; i++) inputs.add(0.0);
        var trainer = new AITrainer(new AIStructure(inputs, List.of(30,15,7), 7));
        System.out.println("starting training...");
        for (int i = 0; i < 50; i++) {
            trainer.run(0.05, 2);

        }
        System.out.println(trainer.baseStructure);
    }
}
