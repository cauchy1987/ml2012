package ml2012.neuronnetwork.bp;
import ml2012.neuronnetwork.structure.*;
/**
 * 用BP算法训练多层神经网络<p>
 * 改进型，动量方法<p>
 * 由于输出层激活函数采用的是sigmoid函数，因此输出范围在[0,1]之间<p>
 * 因此被逼近的函数的值域应该在[0,1]     
 * */
public class BPTrain {
	private Net         net;
	private ReadSample  rs;
	double[] res;
	/**
	 * 误差反向传播训练
	 * @param net 待训练的多层网络
	 * */
	public void train(Net net){
		rs=new ReadSample(); 
		int    n      =1;
		double testErr=0;                        //神经网络的误差
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
	 * 测试网络
	 * @return 返回所有样本测试的均方差
	 * */
	private double testNet(){
		double testErr=0,e;
		double    n      =rs.getRow();           //样本数目
		for(int i=0;i<n;i++){    
			net.compute(rs.getInput()[i]);
			testErr+=Math.abs(computeOutErr(res[i]));
		}
		e=1/n;
		return 0.5*e*testErr;
	}
	/**
	 * 训练网络
	 * */
	private void trainNet(){
		for(int i=0;i<rs.getRow();i++){    
			net.compute(rs.getInput()[i]);
			backPropagateNet(res[i]);
			adjust(rs.getInput()[i]);
		}
	}
	/**
	 * 计算每个层的误差
	 * */
	private void backPropagateNet(double res){
		Parameter para=Parameter.getInstance();
		int       n   =para.nodeNum.length-1;    //取得输出层的位置
		for(int i=n-1;i>=0;i--){
			if(i==n-1)                           //如果是计算输出层的误差
				backPropagateLayer(net.layers[i],null,res);
			else
				backPropagateLayer(net.layers[i],net.layers[i+1],0);
		}
	}
	/**
	 * 计算某一层的误差
	 * @param layer     层
	 * @param nextLayer 下一层(输出层是隐层的下一层)
	 * */
	private void backPropagateLayer(Layer layer,Layer nextLayer,double res){
		double out;
		double err;
		double weight;                           //该层的一个神经元到下一层某个神经元的连接的权重
		if (nextLayer==null){                    //计算输出层的误差
			for(int i=0;i<layer.neurons.length;i++){
				out=layer.neurons[i].output;
				layer.neurons[i].err=(out-res)*out*(1-out);
			}
			return;
		}
		for(int i=0;i<layer.num;i++){            //计算其他层的误差
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
	 * 计算输入一个样本后的输出误差
	 * <p>可以是多输出
	 * */
	private double computeOutErr(double res){
		double   err     =0;
		int      n       =Parameter.getInstance()
		                 .nodeNum.length-1;      //输出层的位置
		Layer    outLayer=net.layers[n-1];
		for(int i=0;i<outLayer.neurons.length;i++){
			
			err+=0.5*sqr(outLayer.neurons[i].output-res);
		}
		return err;
	}
	/**
	 * 调整权值和偏置值
	 * @param in   当前的输入
	 * */
	private void adjust(double[] in){
	    Parameter para =Parameter.getInstance();
		double    eta  =para.eta;
		double    alpha=para.alpha;
		double    dw;
		Layer     layer,lastLayer;
		Neuron    neuron;
		double[]  lastLayerOut;                  //上一层的输出
		for(int i=0;i<net.layers.length;i++){
			layer=net.layers[i];
			if(i==0){                            //当上一层是输入层
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
