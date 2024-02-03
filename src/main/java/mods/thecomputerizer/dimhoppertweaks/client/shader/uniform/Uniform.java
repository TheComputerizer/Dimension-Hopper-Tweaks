package mods.thecomputerizer.dimhoppertweaks.client.shader.uniform;

public abstract class Uniform<U> {

    private final String name;
    private int uniformID;

    protected Uniform(String name) {
        this.name = name;
    }

    public int getID() {
        return this.uniformID;
    }

    public String getName() {
        return this.name;
    }

    public void setID(int id) {
        this.uniformID = id;
    }

    public abstract void upload(float partialTicks);
}
