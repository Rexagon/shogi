package core;

import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

public class CameraController {
    private static Camera mainCamera = new Camera();
    private static Vector3f origin = new Vector3f(0, 0, 0);
    private static float distance = 30.0f;

    public static void init() {
        mainCamera.setPosition(origin.x, origin.y, origin.z + distance);
        rotateCamera(-1.0f, 0);
    }

    public static void setMainCamera(Camera mainCamera) {
        CameraController.mainCamera = mainCamera;
    }

    public static Camera getMainCamera() {
        return mainCamera;
    }

    public static void update(float dt) {
        if (mainCamera == null) {
            return;
        }

        int dwheel = Mouse.getDWheel();
        if (dwheel != 0) {
            Vector3f position = mainCamera.getPosition();
            Vector3f direction = Vector3f.sub(position, origin, null).normalise(null);

            float dw = 0.05f * (-dwheel);

            if (distance + dw < 20.0f) {
                dw = 20.0f - distance;
            }
            else if (distance + dw > 80.0f) {
                dw = 80.0f - distance;
            }

            distance += dw;

            position.x = origin.x + direction.x * distance;
            position.y = origin.y + direction.y * distance;
            position.z = origin.z + direction.z * distance;
            mainCamera.updatePosition();
        }

        if (Mouse.isButtonDown(1)) {
            float rotationX = Mouse.getDY() * dt;
            float rotationY = -Mouse.getDX() * dt;
            rotateCamera(rotationX, rotationY);
        }
    }

    public static void resize(int width, int height) {
        mainCamera.updateAspectRatio(width, height);
    }

    public static void rotateCamera(float rotationX, float rotationY) {
        float targetRoll = mainCamera.getRotation().x + rotationX;
        if (targetRoll > -0.05f) {
            rotationX = -0.05f - mainCamera.getRotation().x;
        }
        else if (targetRoll < -1.4f) {
            rotationX = -1.4f - mainCamera.getRotation().x;
        }

        Vector3f origin = new Vector3f(0, 1, 0);
        Vector3f position = mainCamera.getPosition();

        Vector3f direction = Vector3f.sub(origin, position, null);
        Vector3f up = new Vector3f(0, 1, 0);
        Vector3f right = Vector3f.cross(direction, up, null).normalise(null);

        Matrix4f rotateAround = new Matrix4f();
        rotateAround.translate(new Vector3f(-origin.x, -origin.y, -origin.z));
        rotateAround.rotate(rotationY, new Vector3f(0, 1, 0));
        rotateAround.rotate(rotationX, right);
        rotateAround.translate(new Vector3f(origin.x, origin.y, origin.z));

        Vector4f transform = Matrix4f.transform(rotateAround, new Vector4f(position.x, position.y, position.z, 1.0f), null);
        position.x = transform.x / transform.w;
        position.y = transform.y / transform.w;
        position.z = transform.z / transform.w;
        mainCamera.updatePosition();

        mainCamera.rotate(0, rotationY, 0);
        mainCamera.rotate(rotationX, 0, 0);
    }
}
