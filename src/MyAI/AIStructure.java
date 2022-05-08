package MyAI;

//import jdk.incubator.vector.DoubleVector;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class AIStructure {
    private ArrayList<ArrayList<HiddenNode>> hiddenLayers;
    private ArrayList<InputNode> inputLayer;

    AIStructure(List<Double> inputs, List<Integer> hiddenLayerSizes, int outputs) {
        this(new ArrayList<>(inputs), new ArrayList<>(hiddenLayerSizes), outputs);
    }

    AIStructure(ArrayList<ArrayList<HiddenNode>> hiddenLayers, ArrayList<InputNode> inputLayer) {
        this.hiddenLayers = hiddenLayers;
        this.inputLayer = inputLayer;
    }

    AIStructure(ArrayList<Double> inputs, ArrayList<Integer> hiddenLayerSizes, int outputs) {
        hiddenLayerSizes.add(outputs);
        for (Integer size : hiddenLayerSizes) {
            if (size == 0) throw new RuntimeException(""+hiddenLayerSizes+ " " + outputs);
        }
        inputLayer = new ArrayList<>(inputs.size());
        for (Double input : inputs) inputLayer.add(new InputNode(input));
        this.hiddenLayers = new ArrayList<>(hiddenLayerSizes.size());
        for (int j = 0; j < hiddenLayerSizes.size(); j++) {
            Integer hiddenLayer = hiddenLayerSizes.get(j);
            ArrayList<Node> previousLayer;
            if (j-1>=0) {
                previousLayer = new ArrayList<>(this.hiddenLayers.get(j-1));
            }
            else {
                previousLayer = new ArrayList<>(this.inputLayer);
            }
            var layer = new ArrayList<HiddenNode>(hiddenLayer);
            for (int i = 0; i < hiddenLayer; i++) {
                layer.add(new HiddenNode(previousLayer));
            }
            this.hiddenLayers.add(layer);
        }
    }

    AIStructure(ArrayList<ArrayList<HiddenNode>> hiddenLayers, ArrayList<InputNode> inputLayer, ArrayList<HiddenNode> outputLayer) {
        this.inputLayer = inputLayer;
        this.hiddenLayers = hiddenLayers;
        hiddenLayers.add(outputLayer);
    }

    HiddenNode get(int layer, int index) {
        return hiddenLayers.get(layer).get(index);
    }
    HiddenNode set(int layer, int index, HiddenNode node) {
        return this.hiddenLayers.get(layer).set(index, node);
    }
    public AIStructure(AIStructure o) {
        this(o.hiddenLayers.stream().map(
                        hiddenLayer -> new ArrayList<HiddenNode>(List.copyOf(hiddenLayer))
                ).collect(Collectors.toCollection(ArrayList::new)), new ArrayList<>(List.copyOf(o.inputLayer)));
    }
    void tweak(Double amount) {
        for (int layer = 0; layer < this.hiddenLayers.size(); layer++) {
            for (int i = 0; i < this.hiddenLayers.get(layer).size(); i++) {
                HiddenNode node = get(layer, i);
                for (int j = 0; j < node.getWeights().size(); j++) {
                    Double weight = node.getWeights().get(j);
                    node.setWeight(j, weight + (Math.random()* 2 - 1) * amount);
                    node.setBias(node.getBias() + (Math.random()* 2 - 1) * amount);
                }
            }
        }
    }
    public boolean input(ArrayList<Double> inputData) {
        if (inputData.size() != inputLayer.size()) return false;
        for (int i = 0; i < inputLayer.size(); i++) {
            inputLayer.get(i).setValue(inputData.get(i));
        }
        return true;
    };
    public ArrayList<Double> run() {
        var output = hiddenLayers.get(hiddenLayers.size()-1);
        ArrayList<Double> outputs = new ArrayList<>(output.size());
        for (Node node : output) {
            outputs.add(node.getOutput());
        }
        return outputs;
    }

    public static void main(String[] args) {
        var ai = new AIStructure(List.of(2.0, 1.5), List.of(1), 1);
        var node = (HiddenNode) ai.get(0,0);
        node.setWeight(0, 1.0);
        ai.set(0,0, node);


        var out = (HiddenNode) ai.get(1,0);
        out.setWeight(0, 1.0);
        ai.set(1,0, out);


        System.out.println(ai.hiddenLayers);
        System.out.println(ai.run());
    }

    @Override
    public String toString() {
        return "AIStructure{" +
                "hiddenLayers=" + hiddenLayers +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AIStructure)) return false;
        AIStructure that = (AIStructure) o;
        return Objects.equals(hiddenLayers, that.hiddenLayers) && Objects.equals(inputLayer, that.inputLayer);
    }

    @Override
    public int hashCode() {
        return Objects.hash(hiddenLayers, inputLayer);
    }
}
