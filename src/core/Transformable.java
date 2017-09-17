package core;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

public class Transformable {
    protected Matrix4f transformation = new Matrix4f();
    protected Vector3f position = new Vector3f(0, 0, 0);
    protected Vector3f rotation = new Vector3f(0, 0, 0);
    protected Vector3f scale = new Vector3f(1, 1, 1);
    protected boolean wasChanged = true;

    public Matrix4f getTransformation() {
        if (wasChanged) {
            Matrix4f translation = (new Matrix4f()).translate(position);
            Matrix4f rotation = new Matrix4f();
            rotation.rotate(this.rotation.x, new Vector3f(1, 0 ,0));
            rotation.rotate(this.rotation.y, new Vector3f(0, 1 ,0));
            rotation.rotate(this.rotation.z, new Vector3f(0, 0 ,1));
            Matrix4f scale = (new Matrix4f()).scale(this.scale);

            transformation.setIdentity();
            Matrix4f.mul(transformation, scale, transformation);
            Matrix4f.mul(transformation, rotation, transformation);
            Matrix4f.mul(transformation, translation, transformation);
        }

        return transformation;
    }


    public void setPosition(float x, float y, float z) {
        this.position = new Vector3f(x, y, z);
        wasChanged = true;
    }

    public void setPosition(Vector3f position) {
        this.position = position;
        wasChanged = true;
    }

    public void move(float x, float y, float z) {
        setPosition(Vector3f.add(this.position, new Vector3f(x, y, z), null));
    }

    public void move(Vector3f vec) {
        setPosition(Vector3f.add(this.position, vec, null));
    }

    public Vector3f getPosition() {
        return position;
    }


    public void setRotation(float x, float y, float z) {
        this.rotation = new Vector3f(x, y, z);
        wasChanged = true;
    }

    public void setRotation(Vector3f eulerAngles) {
        this.rotation = eulerAngles;
        wasChanged = true;
    }

    public void rotate(float x, float y, float z) {
        setRotation(Vector3f.add(this.rotation, new Vector3f(x, y, z), null));
    }

    public void rotate(Vector3f eulerAngles) {
        setRotation(Vector3f.add(this.rotation, eulerAngles, null));
    }

    public Vector3f getRotation() {
        return rotation;
    }


    public void setScale(float s) {
        this.scale = new Vector3f(s, s, s);
        wasChanged = true;
    }

    public void setScale(float x, float y, float z) {
        this.scale = new Vector3f(x, y, z);
        wasChanged = true;
    }

    public void setScale(Vector3f scale) {
        this.scale = scale;
        wasChanged = true;
    }

    public void scale(float s) {
        this.scale.scale(s);
        wasChanged = true;
    }

    public void scale(float x, float y, float z) {
        this.scale.x *= x;
        this.scale.y *= y;
        this.scale.z *= z;
        wasChanged = true;
    }

    public void scale(Vector3f s) {
        this.scale.x *= s.x;
        this.scale.y *= s.y;
        this.scale.z *= s.z;
        wasChanged = true;
    }

    public Vector3f getScale() {
        return scale;
    }
}
