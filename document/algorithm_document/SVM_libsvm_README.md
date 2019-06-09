Libsvm是一个简单的、易于使用的和高效的软件，对于支持向量机分类和回归。它解决了C-SVM分类、nu-SVM
分类、one-class-SVM epsilon-SVM回归,nu-SVM回归。它还提供了一个自动模型选择工具C-SVM分类。本文解释了libsvm的使用。

Libsvm可在http://www.csie.ntu.edu.tw/ ~ cjlin / libsvm，请在使用libsvm之前阅读版权文件。

##快速使用
如果你是第一次使用SVM并且数据并不大，请到"tools"目录，安装之后使用easy.py，它从数据缩放到参数选择都是自动的

使用方法：easy.py training_file [testing.file]

更多关于参数选择的信息可以再“tools/README”中找到

##安装和数据格式
在Unix系统，“make”类型去构建“svm-train”与“svm-predict”程序，无任何参数运行展示他们的使用方法

在其他系统，使用“Makefile”去构建他们或使用预构建的二进制文件

训练与测试的数据文件格式：

（label） （index1）：（value1） （index2）：（value2） ...

每一行包含一个实例和以字符“\n”为结束，对于分类器，（label）是一个由整数表示的类标签（支持的类多）。

对于回归,<label>是目标值，可以是任意的实数,对于one-class SVM，不能被任意实数使用。

（index1）：（value1）数据对，提供了“特征”“属性”值，<index1>是一个从1开始的整数，<value>是一个实数，唯一的例外是预先计算的内核,在(index1)从0开始，查看部分预先计算的内核。指数必须按升序。标签只使用在测试文件
计算精度或错误。如果他们是未知的,就填补以任何数字填补第一列。

一个简单的分类器数据包含在包里的是“heart_scala”。去检验如果你的数据是在正确的格式，使用“tools/checkdata.py”(细节在"tools/README"找到)

类型“svm-train heart_scale”,该程序将读取训练数据模型和输出文件“heart_scale.model”。如果你有一个测试
集称为heart_scale.t,然后输入“svm-predict heart_scale.t","heart_scale.model.output"去查看的预测精度。“输出”文件包含预测类标签。

包内有一些其他的有用的程序

svm_scala:衡量输入的数据文件工具

svm-toy:这是一个简单的图形界面,显示了支持向量机单独的数据在一个平面上。您可以单击窗口画数据点。使用“改变”按钮选择类1、2或3(支持三个类),“负载”按钮从文件加载数据,“保存”按钮,保存数据一个文件,“运行”按钮获取一个SVM模型,和“明确”按钮来清除窗口。

您可以输入选项窗口的底部,选项的语法与“svm-train”一样。

注意,“加载”和“保存”在考虑密集的数据格式在分类和回归中的分类,对于分类器,每个数据点都有一个标签(颜色),必须1、2,或3和两个属性(x轴和y轴值)(0,1)。对于回归,每个数据点都有一个目标的价值(轴)和一个属性(轴值)在[0,1)。

“make”类型在各自的目录中去构建他们

你需要Qt库去构建GTK版本（http://www.gtk.org）

预构建Windows二进制文件在“Windows”文件夹。我们使用Visual C++在32位机器，因此最大的隐藏大小为2GB

##svm-train使用
使用：svm-train [options] training_set_file [model_file]，具体使用查看README

##svm-predict使用
使用：svm-predict [options] test_file model_file output_file

model_file:由svm-train生成的模型文件

test_file:你想要预测的测试数据文件

output_file:svm-predict生成的文件

##svm_scala
使用：svm_scala [options] data_filename

##举例

	> svm-scale -l -1 -u 1 -s range train > train.scale
	> svm-scale -r range test > test.scale
	
	Scale each feature of the training data to be in [-1,1]. Scaling
	factors are stored in the file range and then used for scaling the
	test data.
	
	> svm-train -s 0 -c 5 -t 2 -g 0.5 -e 0.1 data_file 
	
	Train a classifier with RBF kernel exp(-0.5|u-v|^2), C=10, and
	stopping tolerance 0.1.
	
	> svm-train -s 3 -p 0.1 -t 0 data_file
	
	Solve SVM regression with linear kernel u'v and epsilon=0.1
	in the loss function.
	
	> svm-train -c 10 -w1 1 -w2 5 -w4 2 data_file
	
	Train a classifier with penalty 10 = 1 * 10 for class 1, penalty 50 =
	5 * 10 for class 2, and penalty 20 = 2 * 10 for class 4.
	
	> svm-train -s 0 -c 100 -g 0.1 -v 5 data_file
	
	Do five-fold cross validation for the classifier using
	the parameters C = 100 and gamma = 0.1
	
	> svm-train -s 0 -b 1 data_file
	> svm-predict -b 1 test_file data_file.model output_file
