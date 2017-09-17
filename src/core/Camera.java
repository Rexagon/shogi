package core;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector;
import org.lwjgl.util.vector.Vector3f;

public class Camera extends Transformable {
    private Matrix4f projection;
    private float fov = 60.0f;
    private float nearZ = 0.1f;
    private float farZ = 200.0f;

    public Camera() {
        updateProjection();
    }

    @Override
    public Matrix4f getTransformation() {
        if (positionChanged || rotationChanged || scaleChanged) {
            transformation.setIdentity();
            Matrix4f.mul(transformation, getScaleMatrix(), transformation);
            Matrix4f.mul(transformation, getRotationMatrix(), transformation);
            Matrix4f.mul(transformation, getTranslationMatrix(), transformation);
        }

        return transformation;
    }

    @Override
    public Matrix4f getTranslationMatrix() {
        if (positionChanged) {
            translationMatrix = new Matrix4f();
            translationMatrix.translate(new Vector3f(-position.x, -position.y, -position.z));
            positionChanged = false;
        }
        return translationMatrix;
    }

    @Override
    public Matrix4f getRotationMatrix() {
        if (rotationChanged) {
            rotationMatrix = new Matrix4f();
            rotationMatrix.rotate(-this.rotation.x, new Vector3f(1, 0, 0));
            rotationMatrix.rotate(-this.rotation.y, new Vector3f(0, 1, 0));
            rotationMatrix.rotate(-this.rotation.z, new Vector3f(0, 0, 1));
            rotationChanged = false;
        }
        return rotationMatrix;
    }

    public Matrix4f getProjection() {
        return projection;
    }

    public void setFov(float fov) {
        this.fov = fov;
        updateProjection();
    }

    public float getFov() {
        return fov;
    }

    public void setFrustrumRange(float nearZ, float farZ) {
        this.nearZ = nearZ;
        this.farZ = farZ;
        updateProjection();
    }

    public float getNearZ() {
        return nearZ;
    }

    public float getFarZ() {
        return farZ;
    }

    public void updateAspectRatio(int width, int height) {
        updateProjection();
    }

    private void updateProjection() {
        projection = new Matrix4f();
        float aspectRatio = (float)Display.getWidth()  / (float)Display.getHeight();

        float yScale = 1.0f / (float)Math.tan(Math.toRadians(fov / 2f));
        float xScale = yScale / aspectRatio;
        float frustumLength = farZ - nearZ;

        projection.m00 = xScale;
        projection.m11 = yScale;
        projection.m22 = -((farZ + nearZ) / frustumLength);
        projection.m23 = -1;
        projection.m32 = -((2 * nearZ * farZ) / frustumLength);
        projection.m33 = 0;
    }
}
