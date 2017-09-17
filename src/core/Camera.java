package core;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector;
import org.lwjgl.util.vector.Vector3f;

public class Camera extends Transformable {
    private Matrix4f projection;
    private float fov = 60.0f;
    private float nearZ = 0.1f;
    private float farZ = 100.0f;

    public Camera() {
        updateProjection();
    }

    @Override
    public Matrix4f getTransformation() {
        if (wasChanged) {
            Vector3f position = new Vector3f();
            this.position.negate(position);

            Matrix4f translation = (new Matrix4f()).translate(position);
            Matrix4f rotation = new Matrix4f();
            rotation.rotate(-this.rotation.x, new Vector3f(1, 0 ,0));
            rotation.rotate(-this.rotation.y, new Vector3f(0, 1 ,0));
            rotation.rotate(-this.rotation.z, new Vector3f(0, 0 ,1));
            Matrix4f scale = (new Matrix4f()).scale(this.scale);

            transformation.setIdentity();
            Matrix4f.mul(transformation, scale, transformation);
            Matrix4f.mul(transformation, rotation, transformation);
            Matrix4f.mul(transformation, translation, transformation);
        }

        return transformation;
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
