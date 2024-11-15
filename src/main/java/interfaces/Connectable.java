package interfaces;

public interface Connectable{
    <T extends Connectable> void setConnection(T connectable);
}

