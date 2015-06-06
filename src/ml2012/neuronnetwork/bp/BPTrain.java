package ml2012.neuronnetwork.bp;
import ml2012.neuronnetwork.structure.*;
/**
 * ��BP�㷨ѵ�����������<p>
 * �Ľ��ͣ���������<p>
 * ��������㼤������õ���sigmoid��������������Χ��[0,1]֮��<p>
 * ��˱��ƽ��ĺ�����ֵ��Ӧ����[0,1]     
 * */
public class BPTrain {
	private Net         net;
	private ReadSample  rs;
	double[] res;
	/**
	 * ���򴫲�ѵ��
	 * @param net ��ѵ���Ķ������
	 * */
	public void train(Net net){
		rs=new ReadSample(); 
		int    n      =1;
		double testErr=0;                        //����������
		double err    =Parameter.getInstance().minErr;
		this.net      =net;
		//double[] res;
		rs.read();
		res=rs.getResult();
		do{
			testErr=testNet();
			if(n%10000==0)
			    System.out.println(n+"testErr:"+testErr);
			if (testErr<err||n>100000) {
				@SuppressWarnings("unused")
				Layer outLayer=net.layers[net.layers.length-1];
				for(int i=0;i<rs.getRow();i++){    
					net.compute(rs.getInput()[i]);
				}
				break;
			}
			n++;
			trainNet();
		}while(true);
	}
	/**
	 * ��������
	 * @return ���������������Եľ�����
	 * */
	private double testNet(){
		double testErr=0,e;
		double    n      =rs.getRow();           //������Ŀ
		for(int i=0;i<n;i++){    
			net.compute(rs.getInput()[i]);
			testErr+=Math.abs(computeOutErr(res[i]));
		}
		e=1/n;
		return 0.5*e*testErr;
	}
	/**
	 * ѵ������
	 * */
	private void trainNet(){
		for(int i=0;i<rs.getRow();i++){    
			net.compute(rs.getInput()[i]);
			backPropagateNet(res[i]);
			adjust(rs.getInput()[i]);
		}
	}
	/**
	 * ����ÿ��������
	 * */
	private void backPropagateNet(double res){
		Parameter para=Parameter.getInstance();
		int       n   =para.nodeNum.length-1;    //ȡ��������λ��
		for(int i=n-1;i>=0;i--){
			if(i==n-1)                           //����Ǽ������������
				backPropagateLayer(net.layers[i],null,res);
			else
				backPropagateLayer(net.layers[i],net.layers[i+1],0);
		}
	}
	/**
	 * ����ĳһ������
	 * @param layer     ��
	 * @param nextLayer ��һ��(��������������һ��)
	 * */
	private void backPropagateLayer(Layer layer,Layer nextLayer,double res){
		double out;
		double err;
		double weight;                           //�ò��һ����Ԫ����һ��ĳ����Ԫ�����ӵ�Ȩ��
		if (nextLayer==null){                    //�������������
			for(int i=0;i<layer.neurons.length;i++){
				out=layer.neurons[i].output;
				layer.neurons[i].err=(out-res)*out*(1-out);
			}
			return;
		}
		for(int i=0;i<layer.num;i++){            //��������������
			err=0;
			for(int j=0;j<nextLayer.num;j++){
				weight=nextLayer.neurons[j].weight[i];
				err+=nextLayer.neurons[j].err*weight;
			}
			out=layer.neurons[i].output;
			layer.neurons[i].err=out*(1-out)*err;
		}
	}
	/**
	 * ��������һ���������������
	 * <p>�����Ƕ����
	 * */
	private double computeOutErr(double res){
		double   err     =0;
		int      n       =Parameter.getInstance()
		                 .nodeNum.length-1;      //������λ��
		Layer    outLayer=net.layers[n-1];
		for(int i=0;i<outLayer.neurons.length;i++){
			
			err+=0.5*sqr(outLayer.neurons[i].output-res);
		}
		return err;
	}
	/**
	 * ����Ȩֵ��ƫ��ֵ
	 * @param in   ��ǰ������
	 * */
	private void adjust(double[] in){
	    Parameter para =Parameter.getInstance();
		double    eta  =para.eta;
		double    alpha=para.alpha;
		double    dw;
		Layer     layer,lastLayer;
		Neuron    neuron;
		double[]  lastLayerOut;                  //��һ������
		for(int i=0;i<net.layers.length;i++){
			layer=net.layers[i];
			if(i==0){                            //����һ���������
			    lastLayerOut=in;
			}else{
				lastLayer=net.layers[i-1];	
				lastLayerOut=new double[lastLayer.num];
				for(int j=0;j<lastLayer.num;j++){
					lastLayerOut[j]=lastLayer.neurons[j].output;
				}
			}
			for(int j=0;j<layer.neurons.length;j++){
				neuron=layer.neurons[j];
				for(int k=0;k<neuron.dweight.length;k++){
					dw=eta*neuron.err*lastLayerOut[k]+alpha*neuron.dweight[k];
					
					neuron.weight[k]+=-dw;
					neuron.dweight[k]=dw;
					neuron.bias-=eta*neuron.err;
				
				}
			}
		}
	}
	private double sqr(double n){
		return n*n;
	}
}
