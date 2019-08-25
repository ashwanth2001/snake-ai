import java.util.ArrayList;

public class NeuralNetwork {

	int iNodes, hNodes, oNodes, hLayers;
	Matrix[] weights;

	public NeuralNetwork(int input, int hidden, int output, int hiddenLayers) {
		iNodes = input;
		hNodes = hidden;
		oNodes = output;
		hLayers = hiddenLayers;

		weights = new Matrix[hLayers + 1];
		weights[0] = new Matrix(hNodes, iNodes + 1);
		for (int i = 1; i < hLayers; i++) {
			weights[i] = new Matrix(hNodes, hNodes + 1);
		}
		weights[weights.length - 1] = new Matrix(oNodes, hNodes + 1);

		for (Matrix w : weights) {
			w.randomize();
		}
	}

	public void mutate(float mr) {
		for (Matrix w : weights) {
			w.mutate(mr);
		}
	}

	public int output(float[] inputsArr) {
		Matrix inputs = weights[0].singleColumnMatrixFromArray(inputsArr);

		Matrix curr_bias = inputs.addBias();

		for (int i = 0; i < hLayers; i++) {
			Matrix hidden_ip = weights[i].dot(curr_bias);
			Matrix hidden_op = hidden_ip.activate();
			curr_bias = hidden_op.addBias();
		}

		Matrix output_ip = weights[weights.length - 1].dot(curr_bias);
		Matrix output = output_ip.activate();
		
		float max = -Integer.MAX_VALUE;
		int ret = 0;
		for(int i = 0; i<this.oNodes; i++) {
			if(output.toArray()[i]>max) {
				max = output.toArray()[i];
				ret = i;
			}
		}

		return ret;
	}

	public NeuralNetwork crossover(NeuralNetwork partner) {
		NeuralNetwork child = new NeuralNetwork(iNodes, hNodes, oNodes, hLayers);
		for (int i = 0; i < weights.length; i++) {
			child.weights[i] = weights[i].crossover(partner.weights[i]);
		}
		return child;
	}

	public NeuralNetwork clone() {
		NeuralNetwork clone = new NeuralNetwork(iNodes, hNodes, oNodes, hLayers);
		for (int i = 0; i < weights.length; i++) {
			clone.weights[i] = weights[i].clone();
		}

		return clone;
	}

	public void load(Matrix[] weight) {
		for (int i = 0; i < weights.length; i++) {
			weights[i] = weight[i];
		}
	}
	
	public String toString() {
		String ret = "";
		int row = 0;
		
		for(int i = 0; i<weights.length; i++) {
			Matrix m = weights[i];
			float[][] arr = m.getArray();
			for(int j = 0; j<arr.length; j++) {
				for(int k = 0; k<arr[0].length; k++) {
					ret+=arr[j][k]+" ";
				}
				ret+="\n";
			}
		}
		return ret;
	}

	public Matrix[] pull() {
		Matrix[] model = weights.clone();
		return model;
	}
}