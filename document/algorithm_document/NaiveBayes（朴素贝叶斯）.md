#NaiveBayes(朴素贝叶斯)

##贝叶斯定理
####P(B[i]|A)=P(B[i])P(A│B[i]) / {P(B[1])P(A│B[1]) +P(B[2])P(A│B[2])+…+P(B[n])P(A│B[n])}
####朴素贝叶斯：给定目标值时属性之间相互条件独立

##贝叶斯定理应用
####吸毒者检测(Baidu)

>假设一个常规的检测结果的敏感度与可靠度均为99%，也就是说，当被检者吸毒时，每次检测呈阳性（+）的概率为99%。而        被检者不吸毒时，每次检测呈阴性（-）的概率为99%。从检测结果的概率来看，检测结果是比较准确的，但是贝叶斯定理却可以揭示一个潜在的问题。

>假设某公司将对其全体雇员进行一次鸦片吸食情况的检测，已知0.5%的雇员吸毒。我们想知道，每位医学检测呈阳性的雇员吸毒的概率有多高？令“D”为雇员吸毒事件，“N”为雇员不吸毒事件，“+”为检测呈阳性事件。可得

>P(D)代表雇员吸毒的概率，不考虑其他情况，该值为0.005。因为公司的预先统计表明该公司的雇员中有0.5%的人吸食毒品，所以这个值就是D的先验概率。

>P(N)代表雇员不吸毒的概率，显然，该值为0.995，也就是1-P(D)。

>P(+|D)代表吸毒者阳性检出率，这是一个条件概率，由于阳性检测准确性是99%，因此该值为0.99。

>P(+|N)代表不吸毒者阳性检出率，也就是出错检测的概率，该值为0.01，因为对于不吸毒者，其检测为阴性的概率为99%，因此，其被误检测成阳性的概率为1-99%。

>P(+)代表不考虑其他因素的影响的阳性检出率。该值为0.0149或者1.49%。我们可以通过全概率公式计算得到：此概率 = 吸毒者阳性检出率（0.5% x 99% = 0.00495%)+ 不吸毒者阳性检出率（99.5% x 1% = 0.00995%)。P(+）=0.0149是检测呈阳性的先验概率。

>用数学公式描述为：根据上述描述，我们可以计算某人检测呈阳性时确实吸毒的条件概率P(D|+)：
>P(D|+) = P(+|D)P(D)/(P(+|D)P(D)+P(+|N)P(N))=0.99 *0.005/0.0149=0.332215

>尽管我们的检测结果可靠性很高，但是只能得出如下结论：如果某人检测呈阳性，那么此人是吸毒的概率只有大 约33%，也就是说此人不吸毒的可能性比较大。我们测试的条件（本例中指D，雇员吸毒）越难发生，发生误判的可能性越大。

>但如果让此人再次复检（相当于P(D)=33.2215%，为吸毒者概率，替换了原先的0.5%），再使用贝叶斯定理计算，将会得到此人吸毒的概率为98.01%。但这还不是贝叶斯定理最强的地方，如果让此人再次复检，再重复使用贝叶斯定理计算，会得到此人吸毒的概率为99.8%（99.9794951%）已经超过了检测的可靠度。
	
	public class Bayes {
		private double P_D = 99;
		private double P_X = 0.5;
		
		public double getBayes(double P_D, double P_X){
			double P_1_1 = P_X * P_D;
			double P_1_2 = (100 - P_X) * (100 - P_D);
			double P_2 = P_X * P_D;
			double P_3 = P_1_1 + P_1_2;
			double P = P_2 / P_3;
			return P;
		}
		
		@Test
		public void test(){
			Bayes bayes = new Bayes();
			double bayes2 = bayes.getBayes(P_D, P_X);
			System.out.println(bayes2);
		}
	}

##朴素贝叶斯算法

假设为了肃清电商平台上的恶意商户（刷单，非法交易，恶意竞争等），开发一个识别商家是否是恶意商户的模型M1。

目前平台上的恶意商户比率为0.2%，记为P(E)=0.2%

利用M1进行检测，发现在已经判定的恶意商户中，由M1判定阳性的人数占比为90%，表示为P(P|E)=90%，在非恶意商户中，M1判定为阳性的人数为8%，表示为P（P|~）=8%。乍看之下，会觉得M1精确度不够。

这时需要使用一个称为“全概率公式”的计算模型，计算在M1判别某个商户为恶意商户时，结果可信度有多高，记为P（E|P）

根据贝叶斯公式:P(E|P) = P(P|E)P(E) / P(E)P(P|E) + P(~E)P(P|~E) = 2.2%

被M1模型判断后，说明这家商户为恶意商户的概率为2.2%，相比原概率0.2%提高了11倍，有必要进行进一步检查，提高判断效率




