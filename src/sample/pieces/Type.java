package sample.pieces;

public enum Type {
    WHITE("white"),
    BLACK("black");

    private final String type;

    Type(String type) {
        this.type = type;
    }

    public String getType() {
        return this.type;
    }
}
