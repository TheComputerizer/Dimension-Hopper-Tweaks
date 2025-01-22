package mods.thecomputerizer.dimhoppertweaks.client.shader.uniform;

import lombok.Getter;

public abstract class Uniform<U> {

    @Getter private final String name;
    private int uniformID;

    protected Uniform(String name) {
        this.name = name;
    }

    public int getID() {
        return this.uniformID;
    }
    
    public void setID(int id) {
        this.uniformID = id;
    }

    public abstract void upload(float partialTicks);
}