package android.support.design.animation;

import android.animation.TypeEvaluator;
import android.graphics.Matrix;

public class MatrixEvaluator implements TypeEvaluator<Matrix> {
    private final float[] tempEndValues = new float[9];
    private final Matrix tempMatrix = new Matrix();
    private final float[] tempStartValues = new float[9];

    public Matrix evaluate(float f, Matrix matrix, Matrix matrix2) {
        matrix.getValues(this.tempStartValues);
        matrix2.getValues(this.tempEndValues);
        for (matrix = null; matrix < 9; matrix++) {
            this.tempEndValues[matrix] = this.tempStartValues[matrix] + ((this.tempEndValues[matrix] - this.tempStartValues[matrix]) * f);
        }
        this.tempMatrix.setValues(this.tempEndValues);
        return this.tempMatrix;
    }
}
