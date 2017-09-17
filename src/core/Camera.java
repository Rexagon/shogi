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

    /**
     * Default constructor.
     * Creates camera with FOV=60*, Z c [0.1, 200.0]
     */
    public Camera() {
        updateProjection();
    }

    /**
     * Camera transformation
     *
     * @return transformation matrix
     */
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

    /**
     * Camera translation
     *
     * @return translation matrix
     */
    @Override
    public Matrix4f getTranslationMatrix() {
        if (positionChanged) {
            translationMatrix = new Matrix4f();
            translationMatrix.translate(new Vector3f(-position.x, -position.y, -position.z));
            positionChanged = false;
        }
        return translationMatrix;
    }

    /**
     * Camera rotation
     *
     * @return rotation matrix
     */
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

    /**
     * Returns camera projection matrix
     *
     * @return projection matrix
     */
    public Matrix4f getProjection() {
        return projection;
    }

    /**
     * Set matrix field of view
     *
     * @param fov camera field of view
     */
    public void setFov(float fov) {
        this.fov = fov;
        updateProjection();
    }

    /**
     * Returns camera field of view
     *
     * @return camera field of view
     */
    public float getFov() {
        return fov;
    }

    /**
     * Set frustrum culling planes
     *
     * @param nearZ near culling plane
     * @param farZ far culling plane
     */
    public void setFrustrumRange(float nearZ, float farZ) {
        this.nearZ = nearZ;
        this.farZ = farZ;
        updateProjection();
    }

    /**
     * Returns near culling plane z
     *
     * @return near z
     */
    public float getNearZ() {
        return nearZ;
    }

    /**
     * Returns fat culling plane z
     *
     * @return far z
     */
    public float getFarZ() {
        return farZ;
    }

    /**
     * Updates camera aspect ratio data
     *
     * @param width new screen width
     * @param height new screen height
     */
    public void updateAspectRatio(int width, int height) {
        updateProjection();
    }

    /**
     * Updates camera projection matrix
     */
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
