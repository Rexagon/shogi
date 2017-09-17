package core;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

public class Transformable {
    protected Matrix4f transformation = new Matrix4f();
    protected Matrix4f translationMatrix = new Matrix4f();
    protected Matrix4f rotationMatrix = new Matrix4f();
    protected Matrix4f scaleMatrix = new Matrix4f();

    protected Vector3f position = new Vector3f(0, 0, 0);
    protected Vector3f rotation = new Vector3f(0, 0, 0);
    protected Vector3f scale = new Vector3f(1, 1, 1);

    protected boolean positionChanged = true;
    protected boolean rotationChanged = true;
    protected boolean scaleChanged = true;

    public Matrix4f getTransformation() {
        if (positionChanged || rotationChanged || scaleChanged) {
            transformation.setIdentity();
            Matrix4f.mul(transformation, getScaleMatrix(), transformation);
            Matrix4f.mul(transformation, getRotationMatrix(), transformation);
            Matrix4f.mul(transformation, getTranslationMatrix(), transformation);
        }

        return transformation;
    }

    public Matrix4f getTranslationMatrix() {
        if (positionChanged) {
            translationMatrix.setIdentity();
            translationMatrix.translate(position);
            positionChanged = false;
        }
        return translationMatrix;
    }

    public Matrix4f getRotationMatrix() {
        if (rotationChanged) {
            rotationMatrix.setIdentity();
            rotationMatrix.rotate(this.rotation.x, new Vector3f(1, 0, 0));
            rotationMatrix.rotate(this.rotation.y, new Vector3f(0, 1, 0));
            rotationMatrix.rotate(this.rotation.z, new Vector3f(0, 0, 1));
            rotationChanged = false;
        }
        return rotationMatrix;
    }

    public Matrix4f getScaleMatrix() {
        if (scaleChanged) {
            scaleMatrix.setIdentity();
            scaleMatrix.scale(scale);
            scaleChanged = false;
        }
        return scaleMatrix;
    }

    public void updatePosition() {
        this.positionChanged = true;
    }

    public void updateRotation() {
        this.rotationChanged = true;
    }

    public void updateScale() {
        this.scaleChanged = true;
    }

    public void setPosition(float x, float y, float z) {
        this.position = new Vector3f(x, y, z);
        positionChanged = true;
    }

    public void setPosition(Vector3f position) {
        this.position = position;
        positionChanged = true;
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
        rotationChanged = true;
    }

    public void setRotation(Vector3f eulerAngles) {
        this.rotation = eulerAngles;
        rotationChanged = true;
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
        scaleChanged = true;
    }

    public void setScale(float x, float y, float z) {
        this.scale = new Vector3f(x, y, z);
        scaleChanged = true;
    }

    public void setScale(Vector3f scale) {
        this.scale = scale;
        scaleChanged = true;
    }

    public void scale(float s) {
        this.scale.scale(s);
        scaleChanged = true;
    }

    public void scale(float x, float y, float z) {
        this.scale.x *= x;
        this.scale.y *= y;
        this.scale.z *= z;
        scaleChanged = true;
    }

    public void scale(Vector3f s) {
        this.scale.x *= s.x;
        this.scale.y *= s.y;
        this.scale.z *= s.z;
        scaleChanged = true;
    }

    public Vector3f getScale() {
        return scale;
    }
}
