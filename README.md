# SmartStock

#描述
因为在15年年初被朋(sun)友忽悠了进股市,在经历了15年大跌和几次熔断之后站在了高高的山顶吹风(手动微笑).
因为我遇到喜欢的事物我就想着能不能在这方面写个app来方便下自己使用,于是就出现了这个选股app了,这里面的数据全是来自于网上,仅供参考.
#app概述
这个app主要是利用爬虫技术把网上已有的数据进行集合,说智能其实却不是很智能.
#重要
这个app用到了Android 的DataBinding 但是里面好多地方我没有使用 Observable 来控制view的刷新,而是直接调用刷新,这样是不好的,会导致性能问题
但是我又懒得改,只有股票详情那里是用了.
#智能选股Sdk
https://github.com/momo145/StockSdk
#通讯类
https://github.com/momo145/TransportModule
#服务端
https://github.com/momo145/SmartStockServer
MinaConstant 这个类修改连接ip,你也可以改成配置文件
#screenshot
![](https://github.com/momo145/SmartStock/blob/master/screenshot/S61120-112355.jpg)
![](https://github.com/momo145/SmartStock/blob/master/screenshot/S61120-112401.jpg)
![](https://github.com/momo145/SmartStock/blob/master/screenshot/S61120-112407.jpg)
![](https://github.com/momo145/SmartStock/blob/master/screenshot/S61120-112416.jpg)
![](https://github.com/momo145/SmartStock/blob/master/screenshot/S61120-112422.jpg)
![](https://github.com/momo145/SmartStock/blob/master/screenshot/S61120-112426.jpg)
![](https://github.com/momo145/SmartStock/blob/master/screenshot/S61120-112434.jpg)
![](https://github.com/momo145/SmartStock/blob/master/screenshot/S61120-112440.jpg)
![](https://github.com/momo145/SmartStock/blob/master/screenshot/S70115-142719.jpg)




# Change Log
## 1.1.0
- 添加股票盘口详情
- bug fix
