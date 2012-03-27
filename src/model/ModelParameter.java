package model;

import util.Log;
import util.Log.LogType;


public class ModelParameter {

	private final double[] verticesWeight;
	private final double[] colorWeight;
	private final int modelCount;

	/** @return a new random ModelParameter with random values.
	 *  Both vertices weights and colors weights are normalized
	 *  (both sums are equal to one).
	 */
	public static ModelParameter getRandom(int modelCount) {
		ModelParameter param = new ModelParameter(modelCount);

		for(int x = 0; x < modelCount; x++) {
			param.verticesWeight[x] = Math.pow(Math.random(), 3.0);
			param.colorWeight[x] = Math.pow(Math.random(), 3.0);
		}

		param.normalize();

		return param;
	}

	/** Construct a new ModelParameter with the first coef set to 1, and all the others to 0.
	 *  @param modelCount the number of model in the morphable model
	 */
	public ModelParameter(int modelCount) {
		this.verticesWeight = new double[modelCount];
		this.colorWeight = new double[modelCount];
		this.modelCount = modelCount;

		verticesWeight[0] = 1.0;
		colorWeight[0] = 1.0;

		for(int x = 1; x < modelCount; x++) {
			verticesWeight[x] = 0.0;
			colorWeight[x] = 0.0;
		}
	}

	/** @return the number of parameter stored. */
	public int getModelCount() {
		return modelCount;
	}

	/** @return the weigth array for the vertices. */
	public double[] getVerticesWeight() {
		return verticesWeight;
	}

	/** @return the weigth array for the colors. */
	public double[] getColorWeight() {
		return colorWeight;
	}

	/** @return a linear application of this and another ModelParameter.
	 * return value = (1.0 - alpha) * this + alpha * targetParam
	 */
	public ModelParameter linearApplication(ModelParameter targetParam, double alpha) {
		if(this.modelCount != targetParam.modelCount)
			throw new IllegalArgumentException("Incoherent number of model count.");

		ModelParameter result = new ModelParameter(modelCount);

		for(int x = 0; x < modelCount; x++) {
			result.colorWeight[x] = (1.0 - alpha) * colorWeight[x] + alpha * targetParam.colorWeight[x];
			result.verticesWeight[x] = (1.0 - alpha) * verticesWeight[x] + alpha * targetParam.verticesWeight[x];
		}

		return result;
	}

	@Override
	public String toString() {
		String result = "ModelParameter: ";
		for(int x = 0; x < modelCount; x++) {
			result += "(" + verticesWeight[x] + "," + colorWeight[x] + ")";
		}
		return result;
	}

	/** Make sure that the sum of each weight array equal 1.0 */
	private void normalize() {
		double totalVertices = 0.0;
		double totalColor = 0.0;
		for(int x = 0; x < modelCount; x++) {
			totalVertices += verticesWeight[x];
			totalColor += colorWeight[x];
		}

		for(int x = 0; x < modelCount; x++) {
			verticesWeight[x] /= totalVertices;
			colorWeight[x] /= totalColor;
		}
		Log.debug(LogType.MODEL, "Normalizing by colors: " + 1/totalColor + " vertices: " + 1/totalVertices);
	}
}