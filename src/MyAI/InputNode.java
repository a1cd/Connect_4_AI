package MyAI;

public class InputNode extends Node {
    private Double value;

    @Override
    public Double getOutput() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public InputNode(Double value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "" + value;
    }
}
