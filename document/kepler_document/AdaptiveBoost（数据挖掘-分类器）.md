#Adaptive Boosting
基于boosting思想的机器学习算法，属于迭代算法，弱学习算法

AdaBoosting方法大致有：Discrete Adaboost, Real AdaBoost, LogitBoost, 和Gentle AdaBoost

Adaptive Boosting核心思想：针对同一个训练集训练不同的分类器(弱分类器)，然后把这些弱分类器集合起来，构成一个更强的最终分类器（强分类器）

输入：训练数据集T={(x1,y1),(x2,y2),…,(xN,yN)}，其中xi∈X⊆Rn，表示输入数据，yi∈Y={-1,+1}，表示类别标签；（二维平面上点的坐标位置）

输出：最终分类器G(x)。（分类器输入一个点的数据，输出该点的类别标签）

>C++存在boost库

Matalb可以调用OpenCV的adaptive boosting借口

>OpenCV是基于BSD许可（开源）发行的跨平台计算机视觉库

OpenCV入门教程.pdf